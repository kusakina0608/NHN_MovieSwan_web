'use strict';

function trClicked(value) {
    location.href = "/admin/answer?questionId=" + value;
}

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

            console.log(answered);

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