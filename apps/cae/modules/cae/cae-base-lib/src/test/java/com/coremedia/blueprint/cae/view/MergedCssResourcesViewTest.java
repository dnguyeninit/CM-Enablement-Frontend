package com.coremedia.blueprint.cae.view;

import com.coremedia.blueprint.base.tree.TreeRelation;
import com.coremedia.blueprint.base.tree.TreeRelationServicesConfiguration;
import com.coremedia.blueprint.cae.contentbeans.MergeableResourcesImpl;
import com.coremedia.blueprint.coderesources.CodeResources;
import com.coremedia.blueprint.coderesources.CodeResourcesCacheKey;
import com.coremedia.blueprint.common.contentbeans.CMNavigation;
import com.coremedia.blueprint.common.contentbeans.MergeableResources;
import com.coremedia.blueprint.testing.ContentTestConfiguration;
import com.coremedia.blueprint.testing.ContentTestHelper;
import com.coremedia.cache.config.CacheConfiguration;
import com.coremedia.cap.content.Content;
import com.coremedia.cap.test.xmlrepo.XmlRepoConfiguration;
import com.coremedia.cap.test.xmlrepo.XmlUapiConfig;
import com.coremedia.cms.delivery.configuration.DeliveryConfigurationProperties;
import com.coremedia.objectserver.beans.ContentBeanFactory;
import com.coremedia.springframework.xml.ResourceAwareXmlBeanDefinitionReader;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;
import javax.inject.Inject;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import static com.coremedia.blueprint.cae.view.MergedCssResourcesViewTest.LocalConfig.PROFILE;
import static com.coremedia.cap.test.xmlrepo.XmlRepoResources.CONTENT_BEAN_FACTORY;
import static org.junit.Assert.assertEquals;
import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_SINGLETON;

/**
 * Tests {@link MergeableResourcesView}
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = MergedCssResourcesViewTest.LocalConfig.class)
@ActiveProfiles(PROFILE)
public class MergedCssResourcesViewTest {
  @Configuration(proxyBeanMethods = false)
  @EnableConfigurationProperties({
          DeliveryConfigurationProperties.class
  })
  @ImportResource(
          value = {
                  CONTENT_BEAN_FACTORY,
                  "classpath:/framework/spring/blueprint-contentbeans.xml",
                  "classpath:spring/test/dummy-views.xml",
          },
          reader = ResourceAwareXmlBeanDefinitionReader.class
  )
  @Import({
          CacheConfiguration.class,
          ContentTestConfiguration.class,
          TreeRelationServicesConfiguration.class,
          XmlRepoConfiguration.class,
  })
  @Profile(PROFILE)
  public static class LocalConfig {
    public static final String PROFILE = "MergedCssResourcesViewTest";
    private static final String CONTENT_REPOSITORY = "classpath:/com/coremedia/blueprint/cae/view/mergedcodeview/content.xml";

    @Bean
    @Scope(SCOPE_SINGLETON)
    public XmlUapiConfig xmlUapiConfig() {
      return new XmlUapiConfig(CONTENT_REPOSITORY);
    }
  }

  private static final String NAVIGATION_ID = "4";
  private CodeResources codeResources;
  @Inject
  private MockHttpServletRequest request;
  @Inject
  private MockHttpServletResponse response;
  @Inject
  private ContentTestHelper contentTestHelper;
  @Inject
  private MergeableResourcesView testling;
  @Inject
  private ContentBeanFactory contentBeanFactory;
  @Resource(name="navigationTreeRelation")
  private TreeRelation<Content> treeRelation;

  @Before
  public void setup() {
    CMNavigation navigation = contentTestHelper.getContentBean(NAVIGATION_ID);
    codeResources = new CodeResourcesCacheKey(navigation.getContent(), "css", false, treeRelation, null).evaluate(null);
  }

  @Test
  public void testMergedResources() throws UnsupportedEncodingException, IOException {
    MergeableResources mergeableResources = new MergeableResourcesImpl(codeResources.getModel("body"), contentBeanFactory, null);
    testling.render(mergeableResources, null, request, response);
    String expected = IOUtils.toString(getClass().getResourceAsStream("mergedcodeview/mergedCss.css"), StandardCharsets.UTF_8);
    assertEquals("Output does not match", expected, response.getContentAsString());
  }
}
