'use strict';

function trClicked(value) {
    location.href = "/movie/mypage/question/post?qid=" + value;
}

(function() {
    const menu = document.getElementById("menu_question");
    menu.className = "active";
} ());