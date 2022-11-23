package com.practice.springboot.moviecatalogservice.services;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.practice.springboot.moviecatalogservice.models.CatalogItem;
import com.practice.springboot.moviecatalogservice.models.Movie;
import com.practice.springboot.moviecatalogservice.models.Rating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MovieInfo {

    @Autowired
    private RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "getFallbackCatalogItem")
    public CatalogItem getCatalogItem(Rating rating) {
        // restTemplate is for synchronous programming
        // for each movie ID, call movie info service and get details
        Movie movie = restTemplate.getForObject(
                "http://movie-info-service/movies/" + rating.getMovieId(),
                Movie.class
        );

        // put them all together
        return new CatalogItem(
                movie.getName(),
                movie.getDescription(),
                rating.getRating()
        );
    }

    public CatalogItem getFallbackCatalogItem(Rating rating) {
        return new CatalogItem("Movie name not found", "", rating.getRating());
    }

}
