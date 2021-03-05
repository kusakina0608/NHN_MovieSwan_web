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
    let movieIdInput = favBtn.querySelector("input").value;
    let memberIdInput = localStorage.getItem("memberId");

    //찜한 영화라면 꽉찬 하트로 바꾸기
    favoriteAPI.isFavorite(memberIdInput, movieIdInput).then(response => {
        if(response.data) {
            favBtn.classList.add("clicked");
            favBtn.querySelector(".material-icons").innerText = "favorite";
        }
    });

    favBtn.addEventListener("click", async (e) => {
        if(memberIdInput != null) {
            // 찜 영화 등록 과정
            if(!favBtn.classList.contains("clicked")){
                await favoriteAPI.registerFavorite(memberIdInput, movieIdInput);
                favBtn.classList.add("clicked");   
                favBtn.querySelector(".material-icons").innerText = "favorite";
            }
            //찜 영화 삭제 과정
            else {
                await favoriteAPI.deleteFavorite(memberIdInput, movieIdInput);
                favBtn.classList.remove("clicked");
                favBtn.querySelector(".material-icons").innerText = "favorite_border";
            }
        }
    });

    const reviewAPI = {
        getMyReview: (memberId, movieId) => {
            return requestTicketAPI.get(`/api/review/getMyReview?memberId=${memberId}&movieId=${movieId}`);
        }
    }

    reviewAPI.getMyReview(memberIdInput, movieIdInput).then(response => {
        if(response.data == "") {
            var review = document.querySelector(".myreview-view");
            review.className = "myreview-view hide";
            submitAction(response.data);
        }
        else {
            var review = document.querySelector(".myreview");
            review.className = "myreview hide";
            var myReview = document.querySelector(".myreview-view");
            myReview.querySelector(".content").innerText = response.data.content;
            var ratingHTML = "";
            for(var i=0; i<5; i++) {
                if(i < response.data.rating)
                    ratingHTML += `<span class="material-icons">star</span>`
                else
                    ratingHTML += `<span class="material-icons">star_outline</span>`
            }
            myReview.querySelector(".rating").innerHTML = ratingHTML;
        }
    })

    var modify_review = document.querySelector(".review-modify");
    
    modify_review.addEventListener('click', function() {
        document.querySelector(".myreview-view").className = "myreview-view hide";
        document.querySelector(".myreview").className = "myreview";
        reviewAPI.getMyReview(memberIdInput, movieIdInput).then(response => {
            submitAction(response.data);
        })
    })

    reviewAPI.getMyReview(memberIdInput, movieIdInput).then(response => {
        let form = document.querySelector(".review-delete");
        let reviewId = document.createElement("input");
        reviewId.setAttribute("type", "hidden");
        reviewId.setAttribute("name", "reviewId");
        reviewId.setAttribute("value", response.data.reviewId);
        form.appendChild(reviewId);
    })
})();

function submitAction(myReview) {
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
            reviewId.setAttribute("value", myReview.reviewId);
            form.appendChild(reviewId);
        }

        let movieIdInput = document.querySelector(".favorite").querySelector("input").value;
        let memberIdInput = localStorage.getItem("memberId") === null ? "" : localStorage.getItem("memberId");
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
        memberId.setAttribute("value", memberIdInput);
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
}