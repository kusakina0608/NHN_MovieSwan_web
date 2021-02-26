'use strict';

function trClicked(value) {
    location.href = "/mypage/ticket/detail?rid=" + value;
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
        getMid: (tid) => {
            return requestTicketAPI.get(`/api/schedule/get?tid=${tid}`);
        }
    }

    const movieAPI = {
        getMovieInfo: (mid) => {
            return requestTicketAPI.get(`/movie/getMovieInfo?mid=${mid}`);
        }
    }

    const tableRow = document.querySelectorAll("tr");

    tableRow.forEach(tr => {
        const row = tr.querySelector(".tid");
        if (!!row) {
            const tid = row.innerText;
            const td = tr.querySelectorAll("td");

            scheduleAPI.getMid(tid).then(res => {
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

                movieAPI.getMovieInfo(res.data.mid).then(res => {
                    td[1].innerText = res.data.name;
                })
            })
        }
    })
}());