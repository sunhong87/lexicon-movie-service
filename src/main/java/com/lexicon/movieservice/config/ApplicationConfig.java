package com.lexicon.movieservice.config;

import com.lexicon.movieservice.converter.DefaultMovieCrawlRespConverter;
import com.lexicon.movieservice.converter.MovieCrawlResponseConverter;
import com.lexicon.movieservice.crawler.LexiconMovieCrawler;
import com.lexicon.movieservice.crawler.MovieCrawler;
import com.lexicon.movieservice.service.MovieComparisonService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(LexiconMovieProviderConfig.class)
public class ApplicationConfig {

  @Bean
  public MovieCrawlResponseConverter movieCrawlResponseConverter() {
    return new DefaultMovieCrawlRespConverter();
  }

  @Bean
  public MovieCrawler movieCrawler(LexiconMovieProviderConfig config, MovieCrawlResponseConverter converter) {
    return new LexiconMovieCrawler(config, converter);
  }

  @Bean
  public MovieComparisonService movieComparisonService(MovieCrawler movieCrawler) {
    return new MovieComparisonService(movieCrawler);
  }
}
