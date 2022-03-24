package com.coremedia.blueprint.boot.autoconfigure;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@Configuration(proxyBeanMethods = false)
@Profile("dev")
@PropertySource("classpath:/com/coremedia/blueprint/boot/autoconfigure/default-dev.properties")
public class DevPropertiesAutoConfiguration {
}
