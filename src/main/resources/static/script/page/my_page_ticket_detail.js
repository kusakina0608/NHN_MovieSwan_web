'use strict';

// const onButtonClick = function(rid) {
//     location.href = "/mypage/ticket/delete?rid=" + rid;
// }

(function () {
    const menu = document.getElementById("menu_ticket");
    menu.className = "active";

    const my_contents = document.getElementById("my_contents");
    my_contents.style.backgroundColor = "grey";
}());

(function () {
    const requestTicketAPI = axios.create({
        baseURL: location.origin
    });

    const scheduleAPI = {
        getScheduleInfo: (tid) => {
            return requestTicketAPI.get(`/api/schedule/get?tid=${tid}`);
        }
    }

    const movieAPI = {
        getMovieInfo: (mid) => {
            return requestTicketAPI.get(`/api/movie/getMovieInfo?mid=${mid}`);
        }
    }

    const tid = document.querySelector("#tid");

    if (!!tid) {
        scheduleAPI.getScheduleInfo(tid.innerText).then(res => {
            const date = new Date(res.data.time);

            var yy = date.getFullYear();
            var MM = date.getMonth() + 1;
            var dd = date.getDate();
            var HH = date.getHours();
            var mm = date.getMinutes();

            HH = HH < 10 ? '0' + HH : HH;
            mm = mm < 10 ? '0' + mm : mm;

            const ticketMovieTime = document.querySelector("#ticket_movie_time");
            ticketMovieTime.innerText = [yy, "년 ", MM, "월 ", dd, "일, ", HH, ':', mm].join('');

            movieAPI.getMovieInfo(res.data.mid).then(res => {
                const ticketPoster = document.querySelector("#ticket_movie_poster");
                const ticketMovieId = document.querySelector("#ticket_movie_id");

                ticketPoster.src = res.data.poster;
                ticketMovieId.innerText = res.data.mid;
            })
        })
    }

}());