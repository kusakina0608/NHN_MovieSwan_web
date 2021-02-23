'use strict';

(function() {
    //찜 버튼 클릭 동작 관련
    const requestTicketAPI = axios.create({
        baseURL: "http://dev-movieswan.nhn.com/movie"
    });

    const favoriteAPI = {
        registerFavorite: (uid, mid) => {
            return requestTicketAPI.post(`/api/favorite/register?uid=${uid}&mid=${mid}`);
        },
        deleteFavorite: (uid, mid) => {
            return requestTicketAPI.delete(`/api/favorite/delete?uid=${uid}&mid=${mid}`);
        },
        isFavorite: (uid, mid) => {
            return requestTicketAPI.get(`/api/favorite/isFavorite?uid=${uid}&mid=${mid}`);
        }
    }

    var favBtns = document.querySelectorAll(".favorite");
    favBtns.forEach(btn => {
        let mid = btn.querySelector("input").value;

        //찜한 영화라면 꽉찬 하트로 바꾸기
        favoriteAPI.isFavorite(uidInput, mid).then(response => {
            if(response.data) {
                btn.classList.add("clicked");
                btn.querySelector(".material-icons").innerText = "favorite";
            }
        });

        btn.addEventListener("click", async (e) => {
            if(uidInput != "") {
                // 찜 영화 등록 과정
                if(!btn.classList.contains("clicked")){
                    await favoriteAPI.registerFavorite(uidInput, mid);
                    btn.classList.add("clicked");
                    btn.querySelector(".material-icons").innerText = "favorite";
                }
                //찜 영화 삭제 과정
                else {
                    await favoriteAPI.deleteFavorite(uidInput, mid);
                    btn.classList.remove("clicked");
                    btn.querySelector(".material-icons").innerText = "favorite_border";
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