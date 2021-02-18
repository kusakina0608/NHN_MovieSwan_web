'use strict';

(function() {
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
            rid.setAttribute("value", myReview.substr(myReview.lastIndexOf("rid")+4, 14));
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
        uid.setAttribute("value", "testuser");
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