'use strict';

(function() {
    //찜 버튼 클릭 동작 관련
    const requestTicketAPI = axios.create({
        baseURL: location.origin
    });

    const favoriteAPI = {
        registerFavorite: (memberId, movieId) => {
            return requestTicketAPI.post(`/api/favorite/register?memberId=${memberId}&movieId=${movieId}`);
        },
        deleteFavorite: (memberId, movieId) => {
            return requestTicketAPI.delete(`/api/favorite/delete?memberId=${memberId}&movieId=${movieId}`);
        },
        isFavorite: (memberId, movieId) => {
            return requestTicketAPI.get(`/api/favorite/isFavorite?memberId=${memberId}&movieId=${movieId}`);
        }
    }

    const reviewAPI = {
        calculateRating: (movieId) => {
            return requestTicketAPI.get(`/api/review/getRating?movieId=${movieId}`)
        }
    }

    var favBtns = document.querySelectorAll(".favorite");
    var uidInput = localStorage.getItem("uid");
    favBtns.forEach(btn => {
        let movieId = btn.querySelector("input").value;

        //찜한 영화라면 꽉찬 하트로 바꾸기
        favoriteAPI.isFavorite(memberIdInput, movieId).then(response => {
            if(response.data) {
                btn.classList.add("clicked");
                btn.querySelector(".material-icons").innerText = "favorite";
            }
        });

        btn.addEventListener("click", async (e) => {
            if(memberIdInput != "") {
                // 찜 영화 등록 과정
                if(!btn.classList.contains("clicked")){
                    await favoriteAPI.registerFavorite(memberIdInput, movieId);
                    btn.classList.add("clicked");
                    btn.querySelector(".material-icons").innerText = "favorite";
                }
                //찜 영화 삭제 과정
                else {
                    await favoriteAPI.deleteFavorite(memberIdInput, movieId);
                    btn.classList.remove("clicked");
                    btn.querySelector(".material-icons").innerText = "favorite_border";
                }
            }
        })
    });

    //평균 평점 계산
    var ratings = document.querySelectorAll(".rating");
    ratings.forEach(rating => {
        let movieId = rating.querySelector("input").value;

        reviewAPI.calculateRating(movieId).then(response => {
            rating.querySelector(".rating-num").innerText = (response.data).toFixed(1);
        })
    })

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