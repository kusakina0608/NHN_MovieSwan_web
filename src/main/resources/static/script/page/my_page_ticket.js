'use strict';

function trClicked(value) {
    location.href = "/mypage/ticket/detail?reservationId=" + value;
}

(function () {
    const menu = document.getElementById("menu_ticket");
    menu.className = "active";
}());

(function () {
    const requestTicketAPI = axios.create({
        baseURL: location.origin
    });

    const scheduleAPI = {
        getMovieId: (timetableId) => {
            return requestTicketAPI.get(`/api/schedule/get?timetableId=${timetableId}`);
        }
    }

    const movieAPI = {
        getMovieInfo: (movieId) => {
            return requestTicketAPI.get(`/api/movie/getMovieInfo?movieId=${movieId}`);
        }
    }

    const tableRow = document.querySelectorAll("tr");

    tableRow.forEach(tr => {
        const row = tr.querySelector(".timetableId");
        if (!!row) {
            const timetableId = row.innerText;
            const td = tr.querySelectorAll("td");

            scheduleAPI.getMovieId(timetableId).then(res => {
                const date = new Date(res.data.time);
                var MM = date.getMonth() + 1;
                var dd = date.getDate();
                var HH = date.getHours();
                var mm = date.getMinutes();

                MM = MM < 10 ? '0' + MM : MM;
                dd = dd < 10 ? '0' + dd : dd;
                HH = HH < 10 ? '0' + HH : HH;
                mm = mm < 10 ? '0' + mm : mm;

                td[2].innerText = [MM, '/', dd, ' ', HH, ':', mm].join('');

                movieAPI.getMovieInfo(res.data.movieId).then(res => {
                    td[1].innerText = res.data.name;
                })
            })
        }
    })
}());