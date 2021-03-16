'use strict';

(function () {
    const menu = document.getElementById("menu_ticket");
    menu.className = "active";

    const my_contents = document.getElementById("my_contents");
    my_contents.style.backgroundColor = "grey";
}());

const cancelReservation = function (reservationId) {
    location.href = "/mypage/ticket/delete?reservationId=" + reservationId;
}

function alertFunction () {
    const reservationId = document.querySelector("#reservationId").innerText;
    console.log(reservationId);

    if (confirm("정말로 예약을 취소하시겠습니까?")) {
        cancelReservation(reservationId);
        alert("예약이 취소되었습니다.");
    } else { }
}