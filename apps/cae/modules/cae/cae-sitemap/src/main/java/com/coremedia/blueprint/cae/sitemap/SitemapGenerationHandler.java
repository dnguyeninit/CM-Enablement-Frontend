package com.coremedia.blueprint.cae.sitemap;

import com.coremedia.blueprint.base.multisite.cae.SiteResolver;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.coremedia.blueprint.base.links.UriConstants.Segments.SEGMENT_ROOT;
import static com.coremedia.blueprint.links.BlueprintUriConstants.Prefixes.PREFIX_INTERNAL;

@RequestMapping
public class SitemapGenerationHandler extends SitemapGenerationController {

  public static final String URI_PATTERN =
          '/' + PREFIX_INTERNAL +
          "/{" + SEGMENT_ROOT + '}' +
          '/' + SitemapHelper.SITEMAP_ORG;

  public SitemapGenerationHandler(SiteResolver siteResolver, SitemapSetupFactory sitemapSetupFactory) {
    super(siteResolver, sitemapSetupFactory);
  }

  @GetMapping(URI_PATTERN)
  public ModelAndView handleRequest(@PathVariable(SEGMENT_ROOT) String rootSegment, HttpServletRequest request, HttpServletResponse response) {
    return handleRequestInternal(rootSegment, request, response);
  }
}
