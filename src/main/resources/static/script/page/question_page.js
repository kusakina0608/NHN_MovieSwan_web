(function() {
    const writer = document.querySelector("#member_id");
    const uid = document.querySelector("#member_id_hidden");

    writer.innerHTML = localStorage.getItem("member_id");
    uid.value = localStorage.getItem("member_id");
} ());

(function() {
    const menu = document.getElementById("menu_question");
    menu.className = "active";
} ());