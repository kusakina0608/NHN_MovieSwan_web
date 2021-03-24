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
        form.setAttribute("action", "/reserve/result");
        form.submit();
    });
}());

history.pushState(null, null, location.href);  // push
window.onpopstate = function(event) {    //  뒤로가기 이벤트 등록
    // 특정 페이지로 가고싶다면 location.href = '';
   history.go(-1);
};