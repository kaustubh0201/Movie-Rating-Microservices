package com.practice.springboot.moviecatalogservice.resources;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.practice.springboot.moviecatalogservice.models.CatalogItem;
import com.practice.springboot.moviecatalogservice.models.Movie;
import com.practice.springboot.moviecatalogservice.models.Rating;
import com.practice.springboot.moviecatalogservice.models.UserRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResources {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @RequestMapping("/{userId}")
    @HystrixCommand(fallbackMethod = "getFallbackCatalog")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {

        UserRating ratings = getUserRating(userId);

        return ratings.getRatings()
                .stream()
                .map(this::getCatalogItem)
                .collect(Collectors.toList());
    }

    @HystrixCommand(fallbackMethod = "getFallbackUserRating")
    private UserRating getUserRating(String userId) {
        return restTemplate.getForObject(
                "http://ratings-data-service/ratingsdata/users/" + userId,
                UserRating.class
        );
    }

    @HystrixCommand(fallbackMethod = "getFallbackCatalogItem")
    private CatalogItem getCatalogItem(Rating rating) {
        // restTemplate is for synchronous programming
        // for each movie ID, call movie info service and get details
        Movie movie = restTemplate.getForObject(
                "http://movie-info-service/movies/" + rating.getMovieId(),
                Movie.class
        );

        // web client is for asynchronous programming

            /*

            Movie movie = webClientBuilder.build()
                    .get()
                    .uri("http://localhost:8081/movies/" + rating.getMovieId())
                    .retrieve()
                    .bodyToMono(Movie.class)
                    .block();
            */

        // put them all together
        return new CatalogItem(
                movie.getName(),
                movie.getDescription(),
                rating.getRating()
        );
    }

    public List<CatalogItem> getFallbackCatalog(@PathVariable("userId") String userId) {
        return Arrays.asList(new CatalogItem(
                "No movie",
                "",
                0
        ));
    }

    private UserRating getFallbackUserRating(String userId) {
        UserRating userRating = new UserRating();
        userRating.setUserId(userId);
        userRating.setRatings(Arrays.asList(
                new Rating("0", 0)
        ));

        return userRating;
    }

    private CatalogItem getFallbackCatalogItem(Rating rating) {
        return new CatalogItem("Movie name not found", "", rating.getRating());
    }

}
