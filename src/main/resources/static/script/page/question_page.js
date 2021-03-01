(function() {
    const writer = document.querySelector("#memberId");
    const memberId = document.querySelector("#memberId_hidden");

    writer.innerHTML = localStorage.getItem("memberId");
    memberId.value = localStorage.getItem("memberId");
} ());

(function() {
    const menu = document.getElementById("menu_question");
    menu.className = "active";
} ());