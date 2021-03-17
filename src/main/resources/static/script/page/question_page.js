function alertFunction() {
    if (confirm("문의를 등록하시겠습니까?")) {
        document.querySelector("#question_contents").submit();
        alert("문의가 등록되었습니다.");
    } else { }
}

(function() {
    const writer = document.querySelector("#memberId");
    const uid = document.querySelector("#memberId_hidden");

    writer.innerHTML = localStorage.getItem("memberId");
    uid.value = localStorage.getItem("memberId");
} ());

(function() {
    const menu = document.getElementById("menu_question");
    menu.className = "active";

    const cancel = document.querySelector("#cancel_button");
    cancel.addEventListener("click", function(event) {
        event.preventDefault();
        window.history.back();
    }, false);

    const question = document.querySelector("#question_button");
    question.addEventListener("click", function(event) {
        event.preventDefault();
    }, false)
} ());