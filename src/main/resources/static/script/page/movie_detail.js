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

    var favBtn = document.querySelector(".favorite");
    let movieId = favBtn.querySelector("input").value;
    let memberIdInput = localStorage.getItem("memberId");

    //찜한 영화라면 꽉찬 하트로 바꾸기
    favoriteAPI.isFavorite(memberIdInput, movieId).then(response => {
        if(response.data) {
            favBtn.classList.add("clicked");
            favBtn.querySelector(".material-icons").innerText = "favorite";
        }
    });

    favBtn.addEventListener("click", async (e) => {
        if(memberIdInput != "") {
            // 찜 영화 등록 과정
            if(!favBtn.classList.contains("clicked")){
                await favoriteAPI.registerFavorite(memberIdInput, movieId);
                favBtn.classList.add("clicked");   
                favBtn.querySelector(".material-icons").innerText = "favorite";
            }
            //찜 영화 삭제 과정
            else {
                await favoriteAPI.deleteFavorite(memberIdInput, movieId);
                favBtn.classList.remove("clicked");
                favBtn.querySelector(".material-icons").innerText = "favorite_border";
            }
        }
    });

    if(myReview == "") {
        var review = document.querySelector(".myreview-view");
        review.className = "myreview-view hide";
    }
    else {
        var review = document.querySelector(".myreview");
        review.className = "myreview hide";
    }


    var submit = document.querySelector(".review_register");
    submit.addEventListener("click", function(e) {
        e.preventDefault();

        let form = document.createElement("form");

        form.setAttribute("charset", "UTF-8");
        form.setAttribute("method", "Post");
        if(myReview == "")
            form.setAttribute("action", "/api/review/register");
        else {
            form.setAttribute("action", "/api/review/modify");

            let reviewId = document.createElement("input");
            reviewId.setAttribute("type", "hidden");
            reviewId.setAttribute("name", "reviewId");
            reviewId.setAttribute("value", myReview.substring(myReview.lastIndexOf("reviewId=")+4, myReview.lastIndexOf("movieId=")-2));
            form.appendChild(reviewId);
        }

        let ratingInput = document.querySelector(".myreview").querySelector(".rating").querySelector("input:checked");
        let contentInput = document.querySelector(".myreview").querySelector(".content");

        let movieId = document.createElement("input");
        movieId.setAttribute("type", "hidden");
        movieId.setAttribute("name", "movieId");
        movieId.setAttribute("value", movieIdInput);
        form.appendChild(movieId);

        let memberId = document.createElement("input");
        memberId.setAttribute("type", "hidden");
        memberId.setAttribute("name", "memberId");
        memberId.setAttribute("value", localStorage.getItem("memberId"));
        form.appendChild(memberId);

        let rating = document.createElement("input");
        rating.setAttribute("type", "hidden");
        rating.setAttribute("name", "rating");
        rating.setAttribute("value", ratingInput.value);
        form.appendChild(rating);

        let content = document.createElement("input");
        content.setAttribute("type", "hidden");
        content.setAttribute("name", "content");
        content.setAttribute("value", contentInput.value);
        form.appendChild(content);

        document.body.appendChild(form);
        form.submit();
    })

    var modify_review = document.querySelector(".review-modify");
    
    modify_review.addEventListener('click', function() {
        document.querySelector(".myreview-view").className = "myreview-view hide";
        document.querySelector(".myreview").className = "myreview";
    })
})();