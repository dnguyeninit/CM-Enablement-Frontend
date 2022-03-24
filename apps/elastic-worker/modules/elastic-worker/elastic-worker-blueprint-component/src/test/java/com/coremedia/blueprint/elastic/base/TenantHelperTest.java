package com.coremedia.blueprint.elastic.base;

import com.coremedia.blueprint.base.settings.SettingsService;
import com.coremedia.cap.multisite.SitesService;
import com.coremedia.cap.test.xmlrepo.XmlRepoConfiguration;
import com.coremedia.cap.test.xmlrepo.XmlUapiConfig;
import com.coremedia.springframework.xml.ResourceAwareXmlBeanDefinitionReader;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TenantInitializerTest.LocalConfig.class,
        com.coremedia.elastic.core.impl.tenant.TenantConfiguration.class,
        XmlRepoConfiguration.class})
public class TenantHelperTest {

  @Autowired
  private SettingsService settingsService;

  @Autowired
  private SitesService sitesService;

  @Test
  public void testReadTenantsFromContent() throws Exception {
    final TenantHelper tenantHelper = new TenantHelper(sitesService, settingsService);
    final Collection<String> strings = tenantHelper.readTenantsFromContent();
    assertEquals(2, strings.size());
    assertThat(strings, CoreMatchers.hasItems("tenant", "testTenant"));
  }

  @Configuration(proxyBeanMethods = false)
  @ImportResource(value = {"classpath:META-INF/coremedia/component-elastic-worker.xml",
          "classpath:/com/coremedia/blueprint/base/settings/impl/bpbase-settings-services.xml"},
          reader = ResourceAwareXmlBeanDefinitionReader.class)
  public static class LocalConfig {
    @Bean
    public XmlUapiConfig xmlUapiConfig() {
      return new XmlUapiConfig("classpath:/com/coremedia/testing/contenttest.xml");
    }
  }
}
