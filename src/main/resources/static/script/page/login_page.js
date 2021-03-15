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

(function() {
    const id_domain = document.querySelector("#idDomain");
    ["백조", "사슴", "기린", "여우", "판다", "하마", "오리", "사자", "토끼", "새콤", "달콤"]
        .forEach(n => {
            const child = document.createElement("option");
            child.value = child.innerText = n;
            id_domain.appendChild(child);
        });
} ()); 