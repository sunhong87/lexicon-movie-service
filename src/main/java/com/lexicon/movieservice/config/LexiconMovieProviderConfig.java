package com.lexicon.movieservice.config;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "lexicon-provider")
@Getter
@Setter
public class LexiconMovieProviderConfig {
  private String baseUrl;
  private String path;
  private List<String> cinemas;
  private String apiKey;
  private String currency;
}
