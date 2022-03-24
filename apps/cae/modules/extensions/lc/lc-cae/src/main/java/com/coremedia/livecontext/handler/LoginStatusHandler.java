package com.coremedia.livecontext.handler;

import com.coremedia.blueprint.base.livecontext.ecommerce.common.CommerceConnectionSupplier;
import com.coremedia.blueprint.base.livecontext.ecommerce.common.CurrentStoreContext;
import com.coremedia.livecontext.ecommerce.common.CommerceConnection;
import com.coremedia.livecontext.handler.util.LiveContextSiteResolver;
import com.coremedia.objectserver.web.links.Link;
import edu.umd.cs.findbugs.annotations.DefaultAnnotation;
import edu.umd.cs.findbugs.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import static com.coremedia.blueprint.base.links.UriConstants.Segments.PREFIX_DYNAMIC;
import static java.util.Collections.singletonMap;
import static java.util.Objects.requireNonNull;

@Link
@Controller
@DefaultAnnotation(NonNull.class)
public class LoginStatusHandler {

  private static final Logger LOG = LoggerFactory.getLogger(LoginStatusHandler.class);

  private static final String STATUS = '/' + PREFIX_DYNAMIC + "/loginstatus";

  private final LiveContextSiteResolver liveContextSiteResolver;
  private final CommerceConnectionSupplier commerceConnectionSupplier;

  @SuppressWarnings("WeakerAccess") // used in Spring XML
  public LoginStatusHandler(LiveContextSiteResolver liveContextSiteResolver,
                            CommerceConnectionSupplier commerceConnectionSupplier) {
    this.liveContextSiteResolver = requireNonNull(liveContextSiteResolver);
    this.commerceConnectionSupplier = requireNonNull(commerceConnectionSupplier);
  }

  @GetMapping(value = STATUS, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Map<String, Object>> handleStatus(@RequestParam String storeId,
                                                          @RequestParam Locale locale,
                                                          HttpServletRequest request) {
    CommerceConnection connection = findConnection(storeId, locale).orElse(null);
    if (connection == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    try {
      boolean loggedIn = connection.getUserContextProvider().createContext(request).isLoggedIn();
      //either the new implementation login status can be resolved via the generic client
      // or the legacy implementation will be used.
      Map<String, Object> body = singletonMap("loggedIn", loggedIn);
      return new ResponseEntity<>(body, HttpStatus.OK);
    } finally {
      CurrentStoreContext.remove();
    }
  }

  private Optional<CommerceConnection> findConnection(String storeId, Locale locale) {
    return liveContextSiteResolver.findSiteFor(storeId, locale)
            .flatMap(commerceConnectionSupplier::findConnection);
  }

  // --- Link building ----------------------------------------------------------------------

  @Link(type = LinkType.class, uri = STATUS)
  public UriComponents buildLink(LinkType linkType, UriTemplate uriTemplate, HttpServletRequest request) {
    UriComponentsBuilder builder = UriComponentsBuilder.fromPath(uriTemplate.toString());
    var storeContext = CurrentStoreContext.get(request);
    var storeId = storeContext.getStoreId();
    var locale = storeContext.getLocale();
    builder.queryParam("storeId", storeId);
    builder.queryParam("locale", locale.toLanguageTag());
    return builder.build();
  }

  public enum LinkType {
    STATUS
  }
}
