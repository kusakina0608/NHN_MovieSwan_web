'use strict';

(function() {
    const menu = document.getElementById("menu_review");
    menu.className = "active";

    const requestTicketAPI = axios.create({
        baseURL: location.origin
    });

    const movieAPI = {
        getTitle: (mid) => {
            return requestTicketAPI.get(`/api/movie/getTitle?mid=${mid}`);
        }
    }

    var reviews = document.querySelectorAll(".review_title");
    reviews.forEach(review => {
        var mid = review.querySelector("input");
        movieAPI.getTitle(mid).then(response => {
            review.querySelector(".title").innerText = response.data.name;
        })
    })
} ());