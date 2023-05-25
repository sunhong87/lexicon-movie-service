package com.lexicon.movieservice.service;

import com.lexicon.movieservice.crawler.MovieCrawler;
import com.lexicon.movieservice.service.model.MovieComparisonModel;
import com.lexicon.movieservice.share.model.MovieModel;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class MovieComparisonService {
  private final MovieCrawler movieCrawler;
  public Mono<List<MovieComparisonModel>> getComparisonMoviesList() {
    return movieCrawler.crawlMovies()
        .collectMultimap(movieModel -> movieModel.getTitle().toLowerCase())
        .map(movieMap ->
            movieMap.entrySet().stream()
                .map(entry -> mapToComparisonModel(entry))
                .sorted(Comparator.comparing(MovieComparisonModel::getTitle))
                .collect(Collectors.toList())
        );
  }

  private MovieComparisonModel mapToComparisonModel(
      Collection<MovieModel> entry) {
    MovieComparisonModel comparisonModel = new MovieComparisonModel();
    entry.stream().findFirst().ifPresent(firstMovie -> {
      comparisonModel.setTitle(firstMovie.getTitle());
      comparisonModel.setPoster(firstMovie.getPoster());
    });
    comparisonModel.setMovies(entry.stream().collect(Collectors.toList()));
    markCheapestMovie(comparisonModel.getMovies());
    comparisonModel.getMovies().sort(Comparator.comparing(MovieModel::getPrice));
    return comparisonModel;
  }

  private MovieComparisonModel mapToComparisonModel(
      Entry<String, Collection<MovieModel>> entry) {
    MovieComparisonModel comparisonModel = new MovieComparisonModel();
    entry.getValue().stream().findFirst().ifPresent(firstMovie -> {
      comparisonModel.setTitle(firstMovie.getTitle());
      comparisonModel.setPoster(firstMovie.getPoster());
    });
    comparisonModel.setMovies(entry.getValue().stream().collect(Collectors.toList()));
    markCheapestMovie(comparisonModel.getMovies());
    comparisonModel.getMovies().sort(Comparator.comparing(MovieModel::getPrice));
    return comparisonModel;
  }

  private void markCheapestMovie(List<MovieModel> movies) {
    movies.stream()
        .sorted(Comparator.comparing(MovieModel::getPrice))
        .findFirst()
        .ifPresent(movie -> movie.setCheapest(true));
  }
}
