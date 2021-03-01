(function() {
    const writer = document.querySelector("#uid");
    const uid = document.querySelector("#uid_hidden");

    writer.innerHTML = localStorage.getItem("uid");
    uid.value = localStorage.getItem("uid");
} ());

(function() {
    const menu = document.getElementById("menu_question");
    menu.className = "active";
} ());