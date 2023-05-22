package com.lexicon.movieservice.service.model;

import com.lexicon.movieservice.share.model.MovieModel;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class MovieComparisonModel {
  private String title;
  private String poster;
  private List<MovieModel> movies;
}
