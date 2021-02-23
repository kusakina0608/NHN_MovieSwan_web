'use strict';

function trClicked(value) {
    location.href = "/movie/mypage/ticket/detail?rid=" + value;
}

(function() {
    const menu = document.getElementById("menu_ticket");
    menu.className = "active";
} ());