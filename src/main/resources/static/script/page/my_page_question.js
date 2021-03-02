'use strict';

function trClicked(value) {
    location.href = "/mypage/question/post?questionId=" + value;
}

(function() {
    const menu = document.getElementById("menu_question");
    menu.className = "active";
} ());