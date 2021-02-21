(function() {
    const writer = document.querySelector("#uid");
    const val = document.querySelector("#uid_hidden")
    writer.innerHTML = localStorage.getItem("uid");
    val.value = localStorage.getItem("uid");
} ());

(function() {
    const menu = document.getElementById("menu_question");
    menu.className = "active";
} ());