package com.lexicon.movieservice.controller;

import com.lexicon.movieservice.service.MovieComparisonService;
import com.lexicon.movieservice.service.model.MovieComparisonModel;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
public class MoviesWebController {

  private final MovieComparisonService movieComparisonService;

  @RequestMapping("/movies")
  public String index(Model model) {
    model.addAttribute("moviesComparisonList", movieComparisonService.getComparisonMoviesList());
    return "movies";
  }

}
