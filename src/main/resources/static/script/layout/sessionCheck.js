function getCookie(cname) {
    var name = cname + "=";
    var decodedCookie = decodeURIComponent(document.cookie);
    var ca = decodedCookie.split(';');
    for(var i = 0; i <ca.length; i++) {
      var c = ca[i];
      while (c.charAt(0) == ' ') {
        c = c.substring(1);
      }
      if (c.indexOf(name) == 0) {
        return c.substring(name.length, c.length);
      }
    }
    return "";
}

(function checkCookie() {
    const auth = getCookie("SWANAUTH");
    const loggedin = document.getElementById("loggedin");
    const notloggedin = document.getElementById("notloggedin");

    if (auth == "") {
        loggedin.style.display = "none";
        notloggedin.style.display = "block";

        localStorage.removeItem("memberId");
        localStorage.removeItem("name");
    } else {
        loggedin.style.display = "block";
        notloggedin.style.display = "none";

        document.getElementById("member_name").innerText = localStorage.getItem("name")
    }
})();