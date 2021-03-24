'use strict';

(function() {
    const menu = document.getElementById("menu_question");
    menu.className = "active";
} ());


(function() {
    const requestTicketAPI = axios.create({
        baseURL: location.origin
    });

    const answerAPI = {
        getAnswer: (questionId) => {
            return requestTicketAPI.get(`/api/question/getAnswer?questionId=${questionId}`);
        }
    }

    const questionId = document.getElementsByName("questionId")[0].value;
    
    answerAPI.getAnswer(questionId).then(res => {
        const answer = res.data;
        if (answer.questionId == null) {
            const waitform = document.getElementsByClassName("form_group_wait")[0];
            waitform.style.display = "flex";
        } else {
            const completeform = document.querySelectorAll(".form_group_answered");
            completeform.forEach(e => {
                e.style.display = "flex";
            })

            completeform[0].querySelector("input").value =
                "[답변] RE : " + document.getElementsByName("title")[0].value;

            const time = answer.regDate;
            const datetime = time[0] + "/" + time[1] + "/" + time[2]
                + " " + time[3] + ":" + time[4] + ":" + time[5];

            completeform[1].querySelector("input").value = datetime;
            completeform[2].querySelector("textarea").innerText = answer.content;
        }
    })
} ());