'use strict';

(function() {
    const menu = document.getElementById("menu_review");
    menu.className = "active";

    const requestTicketAPI = axios.create({
        baseURL: location.origin
    });

    const movieAPI = {
        getTitle: (movieId) => {
            return requestTicketAPI.get(`/movie/getMovieInfo?movieId=${movieId}`);
        }
    }

    var reviews = document.querySelectorAll(".review_title");
    reviews.forEach(review => {
        var movieId = review.querySelector("input").value;
        movieAPI.getTitle(movieId).then(response => {
            console.log(response.data);
            review.querySelector(".title").innerText = response.data.title;
        })
    })
} ());
