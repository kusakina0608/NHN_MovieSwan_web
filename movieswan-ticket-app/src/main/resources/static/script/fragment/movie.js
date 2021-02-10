'use strict';

(function() {
    //찜 버튼 클릭 동작 관련
    var favBtns = document.querySelectorAll(".favorite");

    favBtns.forEach(favBtn => {
        favBtn.addEventListener('click', function(ev) {
            ev.stopPropagation();
            if(favBtn.className == "favorite")
                favBtn.className = "favorite clicked";
            else
                favBtn.className = "favorite";
        }); 
    });

    //영화 이미지에 마우스 호버 동작 관련
    var movies = document.querySelectorAll(".movieLink");

    movies.forEach(movie => {
        movie.addEventListener('mouseover',function(ev) {
            ev.stopPropagation();
            movie.querySelector(".story").style.visibility = "visible";

        });

        movie.addEventListener('mouseout',function(ev) {
            ev.stopPropagation();
            movie.querySelector(".story").style.visibility = "hidden";

        });
    })
}) ();