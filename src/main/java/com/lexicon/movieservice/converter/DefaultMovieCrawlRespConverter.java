package com.lexicon.movieservice.converter;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lexicon.movieservice.crawler.model.MoviesCrawlResponse;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class DefaultMovieCrawlRespConverter implements MovieCrawlResponseConverter {
  private Gson gson;
  public DefaultMovieCrawlRespConverter() {
    gson = new GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
        .setPrettyPrinting()
        .create();
  }
  @Override
  public Optional<MoviesCrawlResponse> convertFromJson(String json) {
    if (StringUtils.isBlank(json)) {
      return  Optional.empty();
    }

    try {
      return Optional.ofNullable(gson.fromJson(json, MoviesCrawlResponse.class));
    } catch (Exception exception) {
      log.error("Fail to parse MoviesListResponse", exception);
      return Optional.empty();
    }
  }
}
