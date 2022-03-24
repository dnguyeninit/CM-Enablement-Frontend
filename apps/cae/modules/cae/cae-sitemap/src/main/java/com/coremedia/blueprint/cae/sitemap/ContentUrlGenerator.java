package com.coremedia.blueprint.cae.sitemap;

import com.coremedia.blueprint.common.services.validation.ValidationService;
import com.coremedia.cap.content.Content;
import com.coremedia.cap.multisite.Site;
import com.coremedia.objectserver.beans.ContentBean;
import com.coremedia.objectserver.beans.ContentBeanFactory;
import com.coremedia.objectserver.web.links.LinkFormatter;
import edu.umd.cs.findbugs.annotations.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import static com.coremedia.blueprint.base.links.UriConstants.Links.ABSOLUTE_URI_KEY;
import static java.lang.invoke.MethodHandles.lookup;

public class ContentUrlGenerator implements SitemapUrlGenerator {
  private static final Logger LOG = LoggerFactory.getLogger(lookup().lookupClass());

  private final LinkFormatter linkFormatter;
  private final ContentBeanFactory contentBeanFactory;
  private final ValidationService validationService;

  private final List<String> exclusionPaths;
  private final List<Predicate<Content>> predicates;

  /**
   * The predicates are combined, i.e. if a predicate is not fulfilled for
   * a content, the URL is not generated.
   *
   * @param exclusionPaths paths relative to site root folder
   */
  public ContentUrlGenerator(LinkFormatter linkFormatter,
                             ContentBeanFactory contentBeanFactory,
                             ValidationService validationService,
                             List<String> exclusionPaths,
                             List<Predicate<Content>> predicates) {
    this.linkFormatter = linkFormatter;
    this.contentBeanFactory = contentBeanFactory;
    this.validationService = validationService;
    this.exclusionPaths = exclusionPaths;
    this.predicates = predicates;
  }

  // --- SitemapUrlGenerator ----------------------------------------

  /**
   * Generate content URLs
   */
  @Override
  public void generateUrls(@NonNull HttpServletRequest request,
                           @NonNull HttpServletResponse response,
                           Site site,
                           boolean absoluteUrls,
                           String protocol,
                           UrlCollector sitemapRenderer) {
    String folderNamesToExcludeParam = request.getParameter(SitemapRequestParams.PARAM_EXCLUDE_FOLDERS);
    List<String> folderNamesToExclude = StringUtils.isNotEmpty(folderNamesToExcludeParam) ? Arrays.asList(folderNamesToExcludeParam.split(",")) : new ArrayList<String>();
    Content sitemapRoot = site.getSiteRootFolder();
    buildUrls(sitemapRenderer, sitemapRoot, sitemapRoot, request, response, absoluteUrls, protocol, folderNamesToExclude);
  }

  // --- internal ---------------------------------------------------

  /**
   * Recursive call through the repository.
   *
   * @param builder  The string builder the urls are stored into.
   * @param folder   The current folder.
   * @param request  The active request.
   * @param response The active response.
   */
  private void buildUrls(UrlCollector builder,
                         Content folder,
                         Content siteRoot,
                         @NonNull HttpServletRequest request,
                         @NonNull HttpServletResponse response,
                         boolean absoluteUrls,
                         String protocol,
                         List<String> folderNamesToExclude) {
    if (!isPathExcluded(siteRoot, folder) && !isFolderNameExcluded(folder, folderNamesToExclude)) {
      Set<Content> children = folder.getChildren();
      for (Content child : children) {
        if (child.isFolder()) {
          buildUrls(builder, child, siteRoot, request, response, absoluteUrls, protocol, folderNamesToExclude);
        } else {
          buildUrl(builder, child, request, response, absoluteUrls, protocol);
        }
      }
    }
  }

  /**
   * Append the URL for the content.
   *
   * @param builder  The string builder the urls are stored into.
   * @param content  The linkable.
   * @param request  The active request.
   * @param response The active response.
   */
  private void buildUrl(UrlCollector builder, Content content, @NonNull HttpServletRequest request,
                        @NonNull HttpServletResponse response, boolean absoluteUrls, String protocol) {
    try {
      if (isValid(content)) {
        String link = createLink(content, request, response, absoluteUrls);
        if (link != null) {
          // Make absolutely absolute
          if (link.startsWith("//")) {
            link = protocol + ":" + link;
          }
          builder.appendUrl(link);
        }
      }
    } catch (Exception e) {
      LOG.warn("Cannot handle \"" + content + "\". Omit and continue.", e);
    }
  }

  private boolean isFolderNameExcluded(Content child, List<String> folderNamesToExclude) {
    String path = child.getPath();
    // check all excluded folder names
    for (String folderName : folderNamesToExclude) {
      if (path.endsWith("/" + folderName)) {
        LOG.info("Found excluded folder name {} for content {} with path {}", folderName, child, path);
        return true;
      }
    }
    return false;
  }

  private boolean isValid(Content content) {
    return predicates.stream().allMatch(predicate -> predicate.test(content));
  }

  /**
   * Creates the URL for the given content.
   *
   * @return The URL for the given content.
   */
  protected String createLink(Content content, @NonNull HttpServletRequest request,
                              @NonNull HttpServletResponse response, boolean absoluteUrls) {
    try {
      ContentBean bean = contentBeanFactory.createBeanFor(content, ContentBean.class);
      if (validationService.validate(bean)) {
        request.setAttribute(ABSOLUTE_URI_KEY, absoluteUrls);
        return linkFormatter.formatLink(bean, null, request, response, false);
      }
    } catch (Exception e) {
      LOG.warn("Cannot not create link for " + content + ": " + e.getMessage());
    }
    return null;
  }

  /**
   * Checks if the given folder should not be analyzed for link resolving.
   *
   * @param folder The folder to check.
   * @return True if the path's documents should not be resolved.
   */
  private boolean isPathExcluded(Content siteRoot, Content folder) {
    for (String path : exclusionPaths) {
      Content folderInSite = siteRoot.getChild(path);
      if (folderInSite == null) {
        LOG.warn("Path {} is excluded from sitemap creation, but does not exist in {} anyway.  You should clean up the configuration.", path, siteRoot);
      }
      if (folder.equals(folderInSite)) {
        return true;
      }
    }
    return false;
  }
}
