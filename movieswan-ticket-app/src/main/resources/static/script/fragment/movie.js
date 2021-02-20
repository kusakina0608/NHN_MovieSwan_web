'use strict';

(function() {
    //찜 버튼 클릭 동작 관련
    const requestTicketAPI = axios.create({
        baseURL: "http://movieswan.nhnent.com:8080"
    });

    const favoriteAPI = {
        registerFav: (uid, mid) => {
            return requestTicketAPI.post(`/api/favorite/register?uid=${uid}&mid=${mid}`);
        },
        deleteFav: (uid, mid) => {
            return requestTicketAPI.delete(`/api/favorite/delete?uid=${uid}&mid=${mid}`);
        }
    }

    var favBtns = document.querySelectorAll(".favorite");
    favBtns.forEach(btn => {
        btn.addEventListener("click", async (e) => {
            if(uidInput != "") {
                let mid = btn.querySelector("input").value;

                // 찜 영화 등록 과정
                if(!btn.classList.contains("clicked")){
                    await favoriteAPI.registerFav(uidInput, mid);
                    btn.classList.add("clicked");   
                }
                //찜 영화 삭제 과정
                else {
                    await favoriteAPI.deleteFav(uidInput, mid);
                    btn.classList.remove("clicked");
                }
            }
        })
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