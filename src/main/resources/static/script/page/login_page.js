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
    [["백조", "swan"], ["사슴", "deer"], ["기린", "giraffe"], ["여우", "fox"],
    ["판다", "panda"], ["하마", "hippo"], ["오리", "duck"], ["사자", "lion"],
    ["토끼", "rabbit"], ["새콤", "sour"], ["달콤", "sweet"]]
        .forEach(n => {
            const child = document.createElement("option");
            child.innerText = n[0];
            child.value = n[1];
            id_domain.appendChild(child);
        });
} ()); 