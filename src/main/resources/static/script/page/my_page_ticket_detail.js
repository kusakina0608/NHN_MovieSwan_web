'use strict';

(function() {
    const menu = document.getElementById("menu_ticket");
    menu.className = "active";

    const my_contents = document.getElementById("my_contents");
    my_contents.style.backgroundColor = "grey";
} ());

const onButtonClick = function(rid) {
    location.href = "/movie/mypage/ticket/delete?rid=" + rid;
}
