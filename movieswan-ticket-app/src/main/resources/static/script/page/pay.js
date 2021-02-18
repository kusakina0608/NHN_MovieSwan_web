'use strict';

(function() {
    const nextButton = document.querySelector(".next-button");

    nextButton.addEventListener("click", e => {
        e.preventDefault();

        let form = document.querySelector("form");

        form.setAttribute("charset", "UTF-8");
        form.setAttribute("method", "Post");
        form.setAttribute("action", "/booking/result");
        form.submit();
    });
}());