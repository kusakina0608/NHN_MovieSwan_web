'use strict';

(function () {
    // 상영 중, 상영 예정 탭 동작 관련
    var btn = document.querySelector(".currentMovie");
    var list = document.querySelector(".movieList")

    btn.addEventListener('click', function(ev) {
        if(ev.target.id == "current") {
            list.className = "movieList currentMovieList"
            ev.target.className = "selected"
            document.querySelector("#expected").className = "";
        }

        else {
            list.className = "movieList expectedMovieList"
            ev.target.className = "selected"
            document.querySelector("#current").className = "";
        }
    })
}) ();