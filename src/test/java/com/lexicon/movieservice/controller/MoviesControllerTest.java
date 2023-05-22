package com.lexicon.movieservice.controller;

import com.lexicon.movieservice.config.LexiconMovieProviderConfig;
import com.lexicon.movieservice.converter.DefaultMovieCrawlRespConverter;
import com.lexicon.movieservice.converter.MovieCrawlResponseConverter;
import com.lexicon.movieservice.crawler.LexiconMovieCrawler;
import com.lexicon.movieservice.service.MovieComparisonService;
import com.lexicon.movieservice.service.model.MovieComparisonModel;
import com.lexicon.movieservice.share.model.MovieModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class MoviesControllerTest {
	public static String cinemaworld_MockData = "{ \"Provider\": \"Cinema World\", \"Movies\": [ { \"ID\": \"cw2488496\", \"Title\": \"Star Wars: Episode VII - The Force Awakens\", \"Type\": \"movie\", \"Poster\": \"https://m.media-amazon.com/images/M/MV5BOTAzODEzNDAzMl5BMl5BanBnXkFtZTgwMDU1MTgzNzE@._V1_SX300.jpg\", \"Actors\": \"Harrison Ford, Mark Hamill, Carrie Fisher, Adam Driver\", \"Price\": 24.7 }, { \"ID\": \"cw2527336\", \"Title\": \"Star Wars: Episode VIII - The Last Jedi\", \"Type\": \"movie\", \"Poster\": \"https://m.media-amazon.com/images/M/MV5BMjQ1MzcxNjg4N15BMl5BanBnXkFtZTgwNzgwMjY4MzI@._V1_SX300.jpg\", \"Actors\": \"Mark Hamill, Carrie Fisher, Adam Driver, Daisy Ridley\", \"Price\": 24 }, { \"ID\": \"cw2527338\", \"Title\": \"Star Wars: Episode IX - The Rise of Skywalker\", \"Type\": \"movie\", \"Poster\": \"https://m.media-amazon.com/images/M/MV5BMDljNTQ5ODItZmQwMy00M2ExLTljOTQtZTVjNGE2NTg0NGIxXkEyXkFqcGdeQXVyODkzNTgxMDg@._V1_SX300.jpg\", \"Actors\": \"Carrie Fisher, Mark Hamill, Adam Driver, Daisy Ridley\", \"Price\": 23 }, { \"ID\": \"cw3748528\", \"Title\": \"Rogue One: A Star Wars Story\", \"Type\": \"movie\", \"Poster\": \"https://m.media-amazon.com/images/M/MV5BMjEwMzMxODIzOV5BMl5BanBnXkFtZTgwNzg3OTAzMDI@._V1_SX300.jpg\", \"Actors\": \"Felicity Jones, Diego Luna, Alan Tudyk, Donnie Yen\", \"Price\": 25 }, { \"ID\": \"cw3778644\", \"Title\": \"Solo: A Star Wars Story\", \"Type\": \"movie\", \"Poster\": \"https://m.media-amazon.com/images/M/MV5BOTM2NTI3NTc3Nl5BMl5BanBnXkFtZTgwNzM1OTQyNTM@._V1_SX300.jpg\", \"Actors\": \"Alden Ehrenreich, Joonas Suotamo, Woody Harrelson, Emilia Clarke\", \"Price\": 24.5 }, { \"ID\": \"cw0076759\", \"Title\": \"Star Wars: Episode IV - A New Hope\", \"Type\": \"movie\", \"Poster\": \"https://m.media-amazon.com/images/M/MV5BNzg4MjQxNTQtZmI5My00YjMwLWJlMjUtMmJlY2U2ZWFlNzY1XkEyXkFqcGdeQXVyODk4OTc3MTY@._V1_SX300.jpg\", \"Actors\": \"Mark Hamill, Harrison Ford, Carrie Fisher, Peter Cushing\", \"Price\": 25.5 }, { \"ID\": \"cw0080684\", \"Title\": \"Star Wars: Episode V - The Empire Strikes Back\", \"Type\": \"movie\", \"Poster\": \"https://m.media-amazon.com/images/M/MV5BYmU1NDRjNDgtMzhiMi00NjZmLTg5NGItZDNiZjU5NTU4OTE0XkEyXkFqcGdeQXVyNzkwMjQ5NzM@._V1_SX300.jpg\", \"Actors\": \"Mark Hamill, Harrison Ford, Carrie Fisher, Billy Dee Williams\", \"Price\": 23 }, { \"ID\": \"cw0086190\", \"Title\": \"Star Wars: Episode VI - Return of the Jedi\", \"Type\": \"movie\", \"Poster\": \"https://m.media-amazon.com/images/M/MV5BOWZlMjFiYzgtMTUzNC00Y2IzLTk1NTMtZmNhMTczNTk0ODk1XkEyXkFqcGdeQXVyNTAyODkwOQ@@._V1_SX300.jpg\", \"Actors\": \"Mark Hamill, Harrison Ford, Carrie Fisher, Billy Dee Williams\", \"Price\": 24.2 }, { \"ID\": \"cw0120915\", \"Title\": \"Star Wars: Episode I - The Phantom Menace\", \"Type\": \"movie\", \"Poster\": \"https://m.media-amazon.com/images/M/MV5BYTRhNjcwNWQtMGJmMi00NmQyLWE2YzItODVmMTdjNWI0ZDA2XkEyXkFqcGdeQXVyNTAyODkwOQ@@._V1_SX300.jpg\", \"Actors\": \"Liam Neeson, Ewan McGregor, Natalie Portman, Jake Lloyd\", \"Price\": 26.4 }, { \"ID\": \"cw0121765\", \"Title\": \"Star Wars: Episode II - Attack of the Clones\", \"Type\": \"movie\", \"Poster\": \"https://m.media-amazon.com/images/M/MV5BMDAzM2M0Y2UtZjRmZi00MzVlLTg4MjEtOTE3NzU5ZDVlMTU5XkEyXkFqcGdeQXVyNDUyOTg3Njg@._V1_SX300.jpg\", \"Actors\": \"Ewan McGregor, Natalie Portman, Hayden Christensen, Christopher Lee\", \"Price\": 20.5 }, { \"ID\": \"cw0121766\", \"Title\": \"Star Wars: Episode III - Revenge of the Sith\", \"Type\": \"movie\", \"Poster\": \"https://m.media-amazon.com/images/M/MV5BNTc4MTc3NTQ5OF5BMl5BanBnXkFtZTcwOTg0NjI4NA@@._V1_SX300.jpg\", \"Actors\": \"Ewan McGregor, Natalie Portman, Hayden Christensen, Ian McDiarmid\", \"Price\": 23 } ] }";
	public static String filmworld_MockData = "{ \"Provider\": \"Film World\", \"Movies\": [ { \"ID\": \"fw2488496\", \"Title\": \"Star Wars: Episode VII - The Force Awakens\", \"Type\": \"movie\", \"Poster\": \"https://m.media-amazon.com/images/M/MV5BOTAzODEzNDAzMl5BMl5BanBnXkFtZTgwMDU1MTgzNzE@._V1_SX300.jpg\", \"Actors\": \"Harrison Ford, Mark Hamill, Carrie Fisher, Adam Driver\", \"Price\": 25 }, { \"ID\": \"fw2527336\", \"Title\": \"Star Wars: Episode VIII - The Last Jedi\", \"Type\": \"movie\", \"Poster\": \"https://m.media-amazon.com/images/M/MV5BMjQ1MzcxNjg4N15BMl5BanBnXkFtZTgwNzgwMjY4MzI@._V1_SX300.jpg\", \"Actors\": \"Mark Hamill, Carrie Fisher, Adam Driver, Daisy Ridley\", \"Price\": 24.5 }, { \"ID\": \"fw2527338\", \"Title\": \"Star Wars: Episode IX - The Rise of Skywalker\", \"Type\": \"movie\", \"Poster\": \"https://m.media-amazon.com/images/M/MV5BMDljNTQ5ODItZmQwMy00M2ExLTljOTQtZTVjNGE2NTg0NGIxXkEyXkFqcGdeQXVyODkzNTgxMDg@._V1_SX300.jpg\", \"Actors\": \"Carrie Fisher, Mark Hamill, Adam Driver, Daisy Ridley\", \"Price\": 23.5 }, { \"ID\": \"fw3748528\", \"Title\": \"Rogue One: A Star Wars Story\", \"Type\": \"movie\", \"Poster\": \"https://m.media-amazon.com/images/M/MV5BMjEwMzMxODIzOV5BMl5BanBnXkFtZTgwNzg3OTAzMDI@._V1_SX300.jpg\", \"Actors\": \"Felicity Jones, Diego Luna, Alan Tudyk, Donnie Yen\", \"Price\": 28 }, { \"ID\": \"fw3778644\", \"Title\": \"Solo: A Star Wars Story\", \"Type\": \"movie\", \"Poster\": \"https://m.media-amazon.com/images/M/MV5BOTM2NTI3NTc3Nl5BMl5BanBnXkFtZTgwNzM1OTQyNTM@._V1_SX300.jpg\", \"Actors\": \"Alden Ehrenreich, Joonas Suotamo, Woody Harrelson, Emilia Clarke\", \"Price\": 24 }, { \"ID\": \"fw0076759\", \"Title\": \"Star Wars: Episode IV - A New Hope\", \"Type\": \"movie\", \"Poster\": \"https://m.media-amazon.com/images/M/MV5BNzg4MjQxNTQtZmI5My00YjMwLWJlMjUtMmJlY2U2ZWFlNzY1XkEyXkFqcGdeQXVyODk4OTc3MTY@._V1_SX300.jpg\", \"Actors\": \"Mark Hamill, Harrison Ford, Carrie Fisher, Peter Cushing\", \"Price\": 22.9 }, { \"ID\": \"fw0080684\", \"Title\": \"Star Wars: Episode V - The Empire Strikes Back\", \"Type\": \"movie\", \"Poster\": \"https://m.media-amazon.com/images/M/MV5BYmU1NDRjNDgtMzhiMi00NjZmLTg5NGItZDNiZjU5NTU4OTE0XkEyXkFqcGdeQXVyNzkwMjQ5NzM@._V1_SX300.jpg\", \"Actors\": \"Mark Hamill, Harrison Ford, Carrie Fisher, Billy Dee Williams\", \"Price\": 23.7 }, { \"ID\": \"fw0086190\", \"Title\": \"Star Wars: Episode VI - Return of the Jedi\", \"Type\": \"movie\", \"Poster\": \"https://m.media-amazon.com/images/M/MV5BOWZlMjFiYzgtMTUzNC00Y2IzLTk1NTMtZmNhMTczNTk0ODk1XkEyXkFqcGdeQXVyNTAyODkwOQ@@._V1_SX300.jpg\", \"Actors\": \"Mark Hamill, Harrison Ford, Carrie Fisher, Billy Dee Williams\", \"Price\": 22 }, { \"ID\": \"fw0120915\", \"Title\": \"Star Wars: Episode I - The Phantom Menace\", \"Type\": \"movie\", \"Poster\": \"https://m.media-amazon.com/images/M/MV5BYTRhNjcwNWQtMGJmMi00NmQyLWE2YzItODVmMTdjNWI0ZDA2XkEyXkFqcGdeQXVyNTAyODkwOQ@@._V1_SX300.jpg\", \"Actors\": \"Liam Neeson, Ewan McGregor, Natalie Portman, Jake Lloyd\", \"Price\": 27.2 }, { \"ID\": \"fw0121765\", \"Title\": \"Star Wars: Episode II - Attack of the Clones\", \"Type\": \"movie\", \"Poster\": \"https://m.media-amazon.com/images/M/MV5BMDAzM2M0Y2UtZjRmZi00MzVlLTg4MjEtOTE3NzU5ZDVlMTU5XkEyXkFqcGdeQXVyNDUyOTg3Njg@._V1_SX300.jpg\", \"Actors\": \"Ewan McGregor, Natalie Portman, Hayden Christensen, Christopher Lee\", \"Price\": 19.9 }, { \"ID\": \"fw0121766\", \"Title\": \"Star Wars: Episode III - Revenge of the Sith\", \"Type\": \"movie\", \"Poster\": \"https://m.media-amazon.com/images/M/MV5BNTc4MTc3NTQ5OF5BMl5BanBnXkFtZTcwOTg0NjI4NA@@._V1_SX300.jpg\", \"Actors\": \"Ewan McGregor, Natalie Portman, Hayden Christensen, Ian McDiarmid\", \"Price\": 22.4 } ] }";
	private static LexiconMovieProviderConfig mockConfig;
	private static MovieCrawlResponseConverter converter;
	@BeforeAll
	public static void setup() {
		mockConfig = new LexiconMovieProviderConfig();
		mockConfig.setBaseUrl("http://localhost:9989");
		mockConfig.setPath("/api/v2/{cinema}/movies");
		mockConfig.setCinemas(Arrays.asList("cinemaworld", "filmworld"));
		converter = new DefaultMovieCrawlRespConverter();
	}

	@Test
	public void test_get_lexicon_movies_when_all_is_ok_should_return_correct_list() {
		WebClient webClientMock = WebClient.builder()
				.baseUrl(mockConfig.getBaseUrl())
				.exchangeFunction(clientRequest -> {
					ClientResponse.Builder builder = ClientResponse.create(HttpStatus.OK)
							.header("content-type", "application/json");
					if (clientRequest.url().getPath().contains("cinemaworld")) {
						builder.body(cinemaworld_MockData);
					} else {
						builder.body(filmworld_MockData);
					}
					return Mono.just(builder.build());
				})
				.build();
		LexiconMovieCrawler crawler = new LexiconMovieCrawler(mockConfig, converter);

		ReflectionTestUtils.setField(crawler, "webClient", webClientMock);

		MovieComparisonService service = new MovieComparisonService(crawler);
		MoviesController controller = new MoviesController(service);

		WebTestClient
				.bindToController(controller)
				.build()
				.get()
				.uri("/movies/compare")
				.exchange()
				.expectHeader()
				.contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
				.expectStatus().is2xxSuccessful()
				.returnResult(MovieComparisonModel.class)
				.getResponseBody()
				.as(StepVerifier::create)
				.recordWith(ArrayList::new)
				.thenConsumeWhile(movie -> movie != null)
				//expect return 11 items
				.expectRecordedMatches(allMovies -> allMovies.size() == 11)
				//expect return 2 movies in each item
				.expectRecordedMatches(allMovies -> allMovies.stream().flatMap(movieComparisonModel -> movieComparisonModel.getMovies().stream()).count() == 22)
				//expect 1 cheapest movie in each item
				.expectRecordedMatches(allMovies -> assertCheapestPriceCount(allMovies, 11))
				//expect all movies should have provider name
				.expectRecordedMatches(allMovies -> assertProviderInEachMovie(allMovies))
				.expectComplete()
				.verify();
	}

	@Test
	public void test_get_lexicon_movies_when_one_cinema_is_error_should_still_return_correct_list() {
		WebClient webClientMock = WebClient.builder()
				.baseUrl(mockConfig.getBaseUrl())
				.exchangeFunction(clientRequest -> {
					if (clientRequest.url().getPath().contains("cinemaworld")) {
						return Mono.just(ClientResponse.create(HttpStatus.OK)
								.header("content-type", "application/json")
								.body(cinemaworld_MockData)
								.build());
					} else {
						return Mono.just(ClientResponse.create(HttpStatus.INTERNAL_SERVER_ERROR)
								.header("content-type", "application/json")
								.body("{ \"message\": \"Server Error\" }")
								.build());
					}
				})
				.build();
		LexiconMovieCrawler crawler = new LexiconMovieCrawler(mockConfig, converter);

		ReflectionTestUtils.setField(crawler, "webClient", webClientMock);

		MovieComparisonService service = new MovieComparisonService(crawler);
		MoviesController controller = new MoviesController(service);

		WebTestClient
				.bindToController(controller)
				.build()
				.get()
				.uri("/movies/compare")
				.exchange()
				.expectHeader()
				.contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
				.expectStatus().is2xxSuccessful()
				.returnResult(MovieComparisonModel.class)
				.getResponseBody()
				.as(StepVerifier::create)
				.recordWith(ArrayList::new)
				.thenConsumeWhile(movie -> movie != null)
				//expect return 11 items
				.expectRecordedMatches(allMovies -> allMovies.size() == 11)
				//expect return 1 movie in each item
				.expectRecordedMatches(allMovies -> allMovies.stream().flatMap(movieComparisonModel -> movieComparisonModel.getMovies().stream()).count() == 11)
				//expect 1 cheapest movie in each item
				.expectRecordedMatches(allMovies -> assertCheapestPriceCount(allMovies, 11))
				//expect all movies should have provider name
				.expectRecordedMatches(allMovies -> assertProviderInEachMovie(allMovies))
				.expectComplete()
				.verify();
	}

	@Test
	public void test_get_lexicon_movies_when_all_cinemas_are_error_should_still_return_empty_list() {
		WebClient webClientMock = WebClient.builder()
				.baseUrl(mockConfig.getBaseUrl())
				.exchangeFunction(clientRequest -> Mono.just(ClientResponse.create(HttpStatus.INTERNAL_SERVER_ERROR)
						.header("content-type", "application/json")
						.body("{ \"message\": \"Server Error\" }")
						.build()))
				.build();
		LexiconMovieCrawler crawler = new LexiconMovieCrawler(mockConfig, converter);

		ReflectionTestUtils.setField(crawler, "webClient", webClientMock);

		MovieComparisonService service = new MovieComparisonService(crawler);
		MoviesController controller = new MoviesController(service);

		WebTestClient
				.bindToController(controller)
				.build()
				.get()
				.uri("/movies/compare")
				.exchange()
				.expectHeader()
				.contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
				.expectStatus().is2xxSuccessful()
				.returnResult(MovieComparisonModel.class)
				.getResponseBody()
				.as(StepVerifier::create)
				.recordWith(ArrayList::new)
				.thenConsumeWhile(movie -> movie != null)
				//expect return 0 items
				.expectRecordedMatches(allMovies -> allMovies.isEmpty())
				.expectComplete()
				.verify();
	}

	private boolean assertCheapestPriceCount(Collection<MovieComparisonModel> comparisonModels, int expectTotal) {
		List<MovieModel> movieModels = comparisonModels.stream()
				.flatMap(movieComparisonModel -> movieComparisonModel.getMovies().stream())
				.collect(Collectors.toList());
		return expectTotal == movieModels.stream().filter(m -> m.isCheapest()).count();
	}

	private boolean assertProviderInEachMovie(Collection<MovieComparisonModel> comparisonModels) {
		List<MovieModel> movieModels = comparisonModels.stream()
				.flatMap(movieComparisonModel -> movieComparisonModel.getMovies().stream())
				.collect(Collectors.toList());
		return movieModels.stream().allMatch(m -> StringUtils.isNotBlank(m.getProvider()));
	}
}
