(function () {
    const loggedin = document.getElementById("loggedin");
    const notloggedin = document.getElementById("notloggedin");

    const name = localStorage.getItem("name");

    if (name == undefined || name == null) {
        loggedin.style.display = "none";
        notloggedin.style.display = "block";
    } else {
        loggedin.style.display = "block";
        notloggedin.style.display = "none";

        const membername = document.getElementById("member_name");
        membername.innerText = localStorage.getItem("name");
    }
})();

// const logoutOnClick = function () {
//     localStorage.setItem("memberId", "null");
//     localStorage.setItem("name", "null");

// //    (function (name) {
// //        name = new RegExp(name + '=([^;]*)');
// //        return name.test(document.cookie) ? unescape(RegExp.$1) : '';
// //    });

// //    const setCookie = function (name, value) {
// //        document.cookie = name + '=' + escape(value);
// //    }
// //
// //    setCookie("JSESSIONID", '');
// }   