package com.lexicon.movieservice.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.ViewResolverRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.thymeleaf.spring5.ISpringWebFluxTemplateEngine;
import org.thymeleaf.spring5.view.reactive.ThymeleafReactiveViewResolver;

@EnableWebFlux
@Configuration
@AutoConfiguration(before = ThymeleafAutoConfiguration.class)
public class WebfluxConfig implements WebFluxConfigurer {

  private final ISpringWebFluxTemplateEngine templateEngine;

  public WebfluxConfig(ISpringWebFluxTemplateEngine templateEngine) {
    this.templateEngine = templateEngine;
  }

  @Override
  public void configureViewResolvers(ViewResolverRegistry registry) {
    registry.viewResolver(thymeleafViewResolver());
  }

  @Bean
  public ThymeleafReactiveViewResolver thymeleafViewResolver() {
    final ThymeleafReactiveViewResolver viewResolver = new
        ThymeleafReactiveViewResolver();
    viewResolver.setTemplateEngine(templateEngine);
    return viewResolver;
  }
}
