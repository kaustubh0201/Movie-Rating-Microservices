package com.practice.springboot.ratingsdataservice.resources;

import com.practice.springboot.ratingsdataservice.models.Rating;
import com.practice.springboot.ratingsdataservice.models.UserRating;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/ratingsdata")
public class RatingsResources {
    @RequestMapping("/{movieId}")
    public Rating getRating(@PathVariable("movieId") String movieId) {
        return new Rating(movieId, 4);
    }

    @RequestMapping("/users/{userId}")
    public UserRating getRatingByUser(@PathVariable("userId") String userId) {

        UserRating userRating = new UserRating();
        userRating.initData(userId);

        return userRating;
    }
}
