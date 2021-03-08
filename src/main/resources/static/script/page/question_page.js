(function() {
    const writer = document.querySelector("#memberId");
    const uid = document.querySelector("#memberId_hidden");

    writer.innerHTML = localStorage.getItem("memberId");
    uid.value = localStorage.getItem("memberId");
} ());

(function() {
    const menu = document.getElementById("menu_question");
    menu.className = "active";
} ());