'use strict';

(function() {
    const movieList = document.querySelector("#movie-list");
    const theaterList = document.querySelector("#theater-list");
    const dayList = document.querySelector("#day-list");
    const timeList = document.querySelector("#time-list");

    var movieLinkList = movieList.querySelectorAll(".list-element-link");
    var theaterLinkList = theaterList.querySelectorAll(".list-element-link");
    var dayLinkList = dayList.querySelectorAll(".list-element-link");
    var timeLinkList = timeList.querySelectorAll(".list-element-link");

    // 선택한 영화의 상영시간표를 저장하고 있는 변수
    var movieSchedule;

    const nextButton = document.querySelector(".next-button");

    const requestTicketAPI = axios.create({
        baseURL: location.origin
    });

    // 상영시간표 API에 요청
    const scheduleAPI = {
        getSchedules: (movieId) => {
            return requestTicketAPI.get(`/api/schedule/getall?movieId=${movieId}`);
        }
    }

    // links 요소들의 selected 속성을 제거해주는 함수
    const clearSelectedElement = function (links){
        links.forEach(lell => {
            lell.querySelector("li").classList.remove("selected");
        })
    }

    // 시간 클릭 이벤트의 콜백 함수를 지정해주는 함수
    const onTimeClicked = function(links) {
        links.forEach(el => {
            el.addEventListener("click", e => {
                e.preventDefault();
                clearSelectedElement(links);
                e.target.classList.add("selected");
            });
        });
    }

    // 일자 클릭 이벤트의 콜백 함수를 지정해주는 함수
    const onDateClicked = function(links) {
        links.forEach(el => {
            el.addEventListener("click", e => {
                e.preventDefault();

                // 일자가 선택되었으므로, 시간을 초기화
                timeList.innerHTML = '';
                movieSchedule[e.target.innerHTML].forEach(item => {
                    let timetableIdElement = document.createElement("div");
                    timetableIdElement.style.display = "none";
                    timetableIdElement.innerHTML = item.timeTableId;
                    let newListElement = document.createElement("li");
                    newListElement.classList.add("list-element");
                    newListElement.innerHTML = item.time;
                    newListElement.appendChild(timetableIdElement);
                    let newLink = document.createElement("a");
                    newLink.appendChild(newListElement);
                    newLink.classList.add("list-element-link");
                    newLink.href = "/reserve/";
                    timeList.appendChild(newLink);
                })
                // 추가된 시간 요소들을 갱신
                timeLinkList = timeList.querySelectorAll(".list-element-link");
                // 추가된 시간 요소들의 콜백 함수를 지정
                onTimeClicked(timeLinkList);
                // 일자 선택이 변경되었으므로, 기존 선택을 취소
                clearSelectedElement(links);
                // 새롭게 선택된 요소에 selected 클래스 추가
                e.target.classList.add("selected");
            });
        });
    }

    const onTheaterClicked = function(links) {
        links.forEach(el => {
            el.addEventListener("click", e => {
                e.preventDefault();
            })
        })
    }

    // 영화 클릭 이벤트의 콜백 함수를 지정해주는 함수
    const onMovieClicked = function (links, handler){
        links.forEach(el => {
            el.addEventListener("click", async (e) => {
                e.preventDefault();
                let res = await handler(e.target.querySelector("div").innerHTML);
                loadSchedule(res);
                // 영화 선택이 변경되었으므로, 기존 선택을 취소
                clearSelectedElement(links);
                e.target.classList.add("selected");
            })
        })
    };

    function loadSchedule(res) {
        // 영화가 선택되었으므로 일자, 시간을 초기화
        dayList.innerHTML = '';
        timeList.innerHTML = '';
        movieSchedule = {};
        res.data.forEach(item=>{
            movieSchedule[item.date] = item.detail;
        });
        // API로부터 전달받은 영화의 상영시간표를 저장
        // 일자를 추가
        for(var key in movieSchedule){
            let newListElement = document.createElement("li");
            newListElement.classList.add("list-element");
            newListElement.innerHTML = key;
            let newLink = document.createElement("a");
            newLink.appendChild(newListElement);
            newLink.classList.add("list-element-link");
            newLink.href = "/reserve/";
            dayList.appendChild(newLink);
        }
        // 추가된 일자 요소들을 갱신
        dayLinkList = dayList.querySelectorAll(".list-element-link");
        // 추가된 일자 요소들의 콜백 함수를 지정
        onDateClicked(dayLinkList);
    }

    nextButton.addEventListener("click", e => {
        e.preventDefault();

        let form = document.createElement("form");

        form.setAttribute("charset", "UTF-8");
        form.setAttribute("method", "Post");
        form.setAttribute("action", "/reserve/seat");
        
        let selectedMovie = movieList.querySelector(".selected");
        let selectedDay = dayList.querySelector(".selected");
        let selectedTime = timeList.querySelector(".selected");

        if(selectedMovie && selectedDay && selectedTime){
            
            let timetableIdInput = document.createElement("input");
            timetableIdInput.setAttribute("type", "hidden");
            timetableIdInput.setAttribute("name", "timetableId");
            timetableIdInput.setAttribute("value", selectedTime.children[0].innerText);
            form.appendChild(timetableIdInput);

            document.body.appendChild(form);
            form.submit();
        }
        else {
            alert("영화, 극장, 시간, 날짜를 모두 선택해 주세요.");
        }
    })

    let selectedMovie = movieList.querySelector(".selected");
    if(selectedMovie !== null) {
        scheduleAPI.getSchedules(selectedMovie.querySelector("div").innerHTML).then(response => {
            loadSchedule(response);
        });
    }

    onMovieClicked(movieLinkList, scheduleAPI.getSchedules);
    onTheaterClicked(theaterLinkList);
    onDateClicked(dayLinkList);
    onTimeClicked(timeLinkList);
}());
