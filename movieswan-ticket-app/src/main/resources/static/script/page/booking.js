'use strict';

(function() {
    const movieList = document.querySelector("#movie-list");
    const theaterList = document.querySelector("#theater-list");
    const dayList = document.querySelector("#day-list");
    const timeList = document.querySelector("#time-list");

    const movieListElementLink = movieList.querySelectorAll(".list-element-link");
    const theaterListElementLink = theaterList.querySelectorAll(".list-element-link");
    const dayListElementLink = dayList.querySelectorAll(".list-element-link");
    const timeListElementLink = timeList.querySelectorAll(".list-element-link");

    const requestTicketAPI = axios.create({
        baseURL: "http://10.161.106.72:13306"
    });
    
    const bookingAPI = {
        getShowingDate: (movie) => {
            return requestTicketAPI.get(`/api/schedule/${movie.mid}`);
        }
    }

    const clearSelectedElement = function (links){
        links.forEach(lell => {
            lell.querySelector("li").classList.remove("selected");
        })
    }

    const selectElement = function (links, handler){
        links.forEach(lell => {
            lell.addEventListener("click", e => {
                e.preventDefault();
                if(handler !== undefined){
                    console.log(e.target.querySelector("div").innerHTML);
                    handler(e.target.querySelector("div").innerHTML);
                }
                clearSelectedElement(links);
                e.target.classList.add("selected");
            })
        })
    };

    selectElement(movieListElementLink, bookingAPI.getShowingDate);
    selectElement(theaterListElementLink);
    selectElement(dayListElementLink);
    selectElement(timeListElementLink);
}());