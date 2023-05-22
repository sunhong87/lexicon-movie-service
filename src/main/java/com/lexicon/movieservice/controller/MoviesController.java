package com.lexicon.movieservice.controller;

import com.lexicon.movieservice.service.MovieComparisonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/movies")
@Slf4j
public class MoviesController {
  private final MovieComparisonService movieComparisonService;

  @GetMapping("/compare")
  public Mono getComparisonMovies() {
    return movieComparisonService.getComparisonMoviesList();
  }
}
