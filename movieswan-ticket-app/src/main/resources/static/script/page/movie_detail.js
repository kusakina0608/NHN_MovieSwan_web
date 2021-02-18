'use strict';

(function() {
    var submit = document.querySelector(".review_register");

    if(submit != null) {
        submit.addEventListener("click", function(e) {
            e.preventDefault();

            let form = document.createElement("form");

            form.setAttribute("charset", "UTF-8");
            form.setAttribute("method", "Post");
            form.setAttribute("action", "/api/review/register");

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
    }

    var modify_review = document.querySelector(".review-modify");
    if(modify_review != null) {
        modify_review.addEventListener('click', function() {
            let form = document.createElement("form");

            form.setAttribute("charset", "UTF-8");
            form.setAttribute("method", "Put");
            form.setAttribute("action", "/api/review/register");
        })
    }
})();