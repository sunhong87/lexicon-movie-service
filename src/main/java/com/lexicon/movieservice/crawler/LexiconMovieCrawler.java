package com.lexicon.movieservice.crawler;

import com.lexicon.movieservice.config.LexiconMovieProviderConfig;
import com.lexicon.movieservice.converter.MovieCrawlResponseConverter;
import com.lexicon.movieservice.crawler.model.MoviesCrawlResponse;
import com.lexicon.movieservice.share.model.MovieModel;
import java.util.Locale;
import java.util.Optional;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@RequiredArgsConstructor
public class LexiconMovieCrawler implements MovieCrawler {

  private final LexiconMovieProviderConfig config;
  private final MovieCrawlResponseConverter movieCrawlResponseConverter;
  private WebClient webClient;

  @PostConstruct
  public void init() {
    webClient = WebClient.builder()
        .baseUrl(config.getBaseUrl())
        .build();
  }

  @Override
  public Flux<MovieModel> crawlMovies() {
    return Flux.fromIterable(config.getCinemas())
        .subscribeOn(Schedulers.boundedElastic())
        .map(cinema -> crawlMovie(cinema))
        .flatMap(monoJson -> monoJson)
        .filter(optionalCrawlResp -> optionalCrawlResp.isPresent())
        .map(optionalCrawlResp -> optionalCrawlResp.get())
        .doOnNext(crawlMovies -> crawlMovies.getMovies().forEach(movie -> {
          movie.setProvider(crawlMovies.getProvider());
          movie.setCurrency(config.getCurrency());
        }))
        .flatMap(crawlMovie -> Flux.fromIterable(crawlMovie.getMovies()));
  }

  private Mono<Optional<MoviesCrawlResponse>> crawlMovie(String cinema) {
    return webClient.get()
        .uri(uriBuilder -> uriBuilder
            .path("/api/v2/{cinema}/movies")
            .build(cinema))
        .header("x-api-key", config.getApiKey())
        .accept(MediaType.APPLICATION_JSON)
        .exchangeToMono(clientResponse -> {
          HttpStatus respCode = clientResponse.statusCode();
          if (!respCode.is2xxSuccessful()) {
            log.error("[{}] Receive response ::{}", cinema, respCode);
            return Mono.just(StringUtils.EMPTY);
          }
          return clientResponse
              .bodyToMono(String.class)
              .doOnNext(resp -> log.debug("Receive response :: {} {}", respCode, resp));
        })
        .map(json -> movieCrawlResponseConverter.convertFromJson(json));
  }
}
