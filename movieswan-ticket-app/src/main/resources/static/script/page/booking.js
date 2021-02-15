'use strict';

(function() {
    const movieList = document.querySelector("#movie-list");
    const theaterList = document.querySelector("#theater-list");
    const dayList = document.querySelector("#day-list");
    const timeList = document.querySelector("#time-list");

    const movieLinkList = movieList.querySelectorAll(".list-element-link");
    const theaterLinkList = theaterList.querySelectorAll(".list-element-link");
    const dayLinkList = dayList.querySelectorAll(".list-element-link");
    const timeLinkList = timeList.querySelectorAll(".list-element-link");

    const nextButton = document.querySelector(".next-button");
    console.log(nextButton);

    const requestTicketAPI = axios.create({
        // baseURL: "http://10.161.106.78"
        baseURL: "http://127.0.0.1:8080"
    });

    const scheduleAPI = {
        getSchedules: (movie) => {
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

    nextButton.addEventListener("click", e => {
        e.preventDefault();

        let form = document.createElement("form");

        form.setAttribute("charset", "UTF-8");
        form.setAttribute("method", "Post");
        form.setAttribute("action", "/booking/seat");
        
        let selectedMovie = movieList.querySelector(".selected");
        let selectedDay = dayList.querySelector(".selected");
        let selectedTime = timeList.querySelector(".selected");

        if(selectedMovie && selectedDay && selectedTime){
            console.log("OK");
            let movieInput = document.createElement("input");
            movieInput.setAttribute("type", "hidden");
            movieInput.setAttribute("name", "mid");
            movieInput.setAttribute("value", selectedMovie.querySelector("div").innerHTML);
            form.appendChild(movieInput);

            let dateInput = document.createElement("input");
            dateInput.setAttribute("type", "hidden");
            dateInput.setAttribute("name", "date");
            dateInput.setAttribute("value", selectedDay.innerHTML);
            form.appendChild(dateInput);

            let timeInput = document.createElement("input");
            timeInput.setAttribute("type", "hidden");
            timeInput.setAttribute("name", "time");
            timeInput.setAttribute("value", selectedTime.innerHTML);
            form.appendChild(timeInput);
            document.body.appendChild(form);
            console.log(form);
            form.submit();
        }
        else {
            console.log("NO");
        }
    })

    selectElement(movieLinkList, scheduleAPI.getSchedules);
    selectElement(theaterLinkList);
    selectElement(dayLinkList);
    selectElement(timeLinkList);
}());