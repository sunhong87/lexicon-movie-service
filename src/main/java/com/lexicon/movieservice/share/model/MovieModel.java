package com.lexicon.movieservice.share.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class MovieModel {
  @SerializedName("ID")
  private String ID;
  private String title;
  private String type;
  private String poster;
  private String actors;
  private Double price;
  private String provider;
  private boolean isCheapest;
  private String currency;
}
