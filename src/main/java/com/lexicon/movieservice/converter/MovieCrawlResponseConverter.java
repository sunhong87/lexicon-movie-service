package com.lexicon.movieservice.converter;

import com.lexicon.movieservice.crawler.model.MoviesCrawlResponse;
import java.util.Optional;

public interface MovieCrawlResponseConverter {
  Optional<MoviesCrawlResponse> convertFromJson(String json);
}
