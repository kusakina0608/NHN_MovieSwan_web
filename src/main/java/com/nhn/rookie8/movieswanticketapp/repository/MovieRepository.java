package com.nhn.rookie8.movieswanticketapp.repository;

import com.nhn.rookie8.movieswanticketapp.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface MovieRepository extends JpaRepository<Movie, String>, QuerydslPredicateExecutor<Movie> {
}
