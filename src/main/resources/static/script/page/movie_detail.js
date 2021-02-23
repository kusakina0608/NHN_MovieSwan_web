'use strict';

(function() {
    //찜 버튼 클릭 동작 관련
    const requestTicketAPI = axios.create({
        baseURL: location.origin
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

    var favBtn = document.querySelector(".favorite");
    let mid = favBtn.querySelector("input").value;

    //찜한 영화라면 꽉찬 하트로 바꾸기
    favoriteAPI.isFavorite(uidInput, mid).then(response => {
        if(response.data) {
            favBtn.classList.add("clicked");
            favBtn.querySelector(".material-icons").innerText = "favorite";
        }
    });

    favBtn.addEventListener("click", async (e) => {
        if(uidInput != "") {
            // 찜 영화 등록 과정
            if(!favBtn.classList.contains("clicked")){
                await favoriteAPI.registerFavorite(uidInput, mid);
                favBtn.classList.add("clicked");   
                favBtn.querySelector(".material-icons").innerText = "favorite";
            }
            //찜 영화 삭제 과정
            else {
                await favoriteAPI.deleteFavorite(uidInput, mid);
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

            let rid = document.createElement("input");
            rid.setAttribute("type", "hidden");
            rid.setAttribute("name", "rid");
            rid.setAttribute("value", myReview.substring(myReview.lastIndexOf("rid=")+4, myReview.lastIndexOf("mid=")-2));
            form.appendChild(rid);
        }

        let gradeInput = document.querySelector(".myreview").querySelector(".grade").querySelector("input:checked");
        let contentInput = document.querySelector(".myreview").querySelector(".content");

        let mid = document.createElement("input");
        mid.setAttribute("type", "hidden");
        mid.setAttribute("name", "mid");
        mid.setAttribute("value", midInput);
        form.appendChild(mid);

        let uid = document.createElement("input");
        uid.setAttribute("type", "hidden");
        uid.setAttribute("name", "uid");
        uid.setAttribute("value", uidInput);
        form.appendChild(uid);

        let grade = document.createElement("input");
        grade.setAttribute("type", "hidden");
        grade.setAttribute("name", "grade");
        grade.setAttribute("value", gradeInput.value);
        form.appendChild(grade);

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