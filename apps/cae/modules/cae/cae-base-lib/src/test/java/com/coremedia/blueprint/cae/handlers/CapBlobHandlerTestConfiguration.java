package com.coremedia.blueprint.cae.handlers;

import com.coremedia.blueprint.base.links.ContentLinkBuilder;
import com.coremedia.blueprint.base.links.UrlPathFormattingHelper;
import com.coremedia.blueprint.coderesources.ThemeService;
import com.coremedia.blueprint.common.services.validation.ValidationService;
import com.coremedia.cap.test.xmlrepo.XmlUapiConfig;
import com.coremedia.mimetype.MimeTypeService;
import com.coremedia.objectserver.beans.ContentBean;
import com.coremedia.objectserver.beans.ContentBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
class CapBlobHandlerTestConfiguration extends AbstractHandlerTestConfiguration {

  @Bean
  public XmlUapiConfig xmlUapiConfig() {
    return new XmlUapiConfig(
            "classpath:/com/coremedia/blueprint/cae/handlers/blob-test-content.xml",
            "classpath:/com/coremedia/blueprint/cae/handlers/blob-test-users.xml"
    );
  }

  @Bean
  CapBlobHandler capBlobHandler(MimeTypeService mimeTypeService,
                                UrlPathFormattingHelper urlPathFormattingHelper,
                                ValidationService<ContentBean> validationService,
                                ThemeService themeService,
                                ContentLinkBuilder contentLinkBuilder,
                                ContentBeanFactory contentBeanFactory) {
    CapBlobHandler testling = new CapBlobHandler();
    testling.setMimeTypeService(mimeTypeService);
    testling.setUrlPathFormattingHelper(urlPathFormattingHelper);
    testling.setValidationService(validationService);
    testling.setThemeService(themeService);
    testling.setContentLinkBuilder(contentLinkBuilder);
    testling.setContentBeanFactory(contentBeanFactory);
    return testling;
  }

}
