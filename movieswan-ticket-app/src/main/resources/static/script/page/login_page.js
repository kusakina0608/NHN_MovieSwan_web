'use strict';

(function() {
    const visibility = document.querySelector("#visibility");
    const visibility_off = document.querySelector("#visibility_off");
    const password = document.querySelector("#password");

    visibility.addEventListener("click", ()=>{
        visibility.style.display = "none";
        visibility_off.style.display = "inline";
        password.type = "password";
    }, false);
    
    visibility_off.addEventListener("click", ()=>{
        visibility.style.display = "inline";
        visibility_off.style.display = "none";
        password.type = "text";
    }, false);

}());