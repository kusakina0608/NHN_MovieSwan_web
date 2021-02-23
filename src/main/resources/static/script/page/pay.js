'use strict';

(function() {
    const nextButton = document.querySelector(".next-button");
    const prevButton = document.querySelector(".prev-button");

    prevButton.addEventListener("click", e => {
        e.preventDefault();
        window.history.back(1);
    });
    nextButton.addEventListener("click", e => {
        e.preventDefault();

        let form = document.querySelector("form");

        form.setAttribute("charset", "UTF-8");
        form.setAttribute("method", "Post");
        form.setAttribute("action", "/booking/result");
        form.submit();
    });
}());