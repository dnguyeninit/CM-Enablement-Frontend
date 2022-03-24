package com.coremedia.blueprint.studio.rest.taxonomies;

import com.coremedia.blueprint.taxonomies.TaxonomyConfiguration;
import com.coremedia.blueprint.taxonomies.TaxonomyResolver;
import com.coremedia.blueprint.taxonomies.semantic.SemanticStrategy;
import com.coremedia.rest.cap.CapRestServiceConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.List;

@Configuration(proxyBeanMethods = false)
@Import({
        CapRestServiceConfiguration.class,
        TaxonomyConfiguration.class,
})
public class TaxonomyStudioConfiguration {

  @Bean
  TaxonomyResource taxonomyResource(TaxonomyResolver taxonomyResolver,
                                    List<SemanticStrategy> semanticServiceStrategies) {
    return new TaxonomyResource(taxonomyResolver, semanticServiceStrategies);
  }
}
