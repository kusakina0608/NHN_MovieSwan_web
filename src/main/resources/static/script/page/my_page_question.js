'use strict';

function trClicked(value) {
    location.href = "/mypage/question/post?questionId=" + value;
}

(function() {
    const menu = document.getElementById("menu_question");
    menu.className = "active";
} ());

(function() {
    const requestTicketAPI = axios.create({
        baseURL: location.origin
    });

    const answerAPI = {
        isAnswered: (questionId) => {
            return requestTicketAPI.get(`/api/question/isAnswered?questionId=${questionId}`);
        }
    }

    const table = document.querySelectorAll("tr");

    for (let i = 1; i < table.length; i++) {
        const questionId = table[i].querySelector("td").innerText;

        answerAPI.isAnswered(questionId).then(res => {
            const answered = res.data;

            if (answered) {
                const complete = table[i].querySelector("#yes");
                complete.style.display = "block";
            } else {
                const waiting = table[i].querySelector("#no");
                waiting.style.display = "block";
            }
        })
    }
} ());