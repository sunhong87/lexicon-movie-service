package com.lexicon.movieservice.crawler.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lexicon.movieservice.share.model.MovieModel;
import java.util.List;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class MoviesCrawlResponse {
  private String provider;
  private List<MovieModel> movies;
}
