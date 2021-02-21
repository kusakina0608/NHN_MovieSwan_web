(function() {
    const writer = document.querySelector("#uid");
    const val = document.querySelector("#uid_hidden")
    writer.innerHTML = localStorage.getItem("uid");
    val.value = localStorage.getItem("uid");
} ());