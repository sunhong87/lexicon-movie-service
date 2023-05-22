package com.lexicon.movieservice.crawler;

import com.lexicon.movieservice.share.model.MovieModel;
import reactor.core.publisher.Flux;

public interface MovieCrawler {
  Flux<MovieModel> crawlMovies();
}
