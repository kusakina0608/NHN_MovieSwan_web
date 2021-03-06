'use strict';

(function() {
    
    let submittable = false;
    

    const enableSubmit = () =>{
        const check = val => val == 1;
        const list = document.querySelectorAll(".div");
        var listValue = [];
        list.forEach(i=>{
            listValue.push(i.value);
        })
        
        
        if(listValue.every(check)){
            document.querySelector("#submit").disabled = false;
            submittable = true;
        }
        else{
            document.querySelector("#submit").disabled = true;
            submittable = false;
        }
    };


    const isExistId = axios.create({
        baseURL: location.origin.replace("8082","8081") + "/account/"
    });

    const isValid = (memberId) => {
        return isExistId.get(`/api/isExistId?memberId=${memberId}`);
    }

    const memberId = document.querySelector("#memberId");
    const ok_id = document.querySelectorAll(".ok_id");
    const no_id = document.querySelectorAll(".no_id");

    var memberId_reg = /^[a-z0-9._-]{6,20}$/

    memberId.addEventListener("keyup",(event)=>{
        var handle = false;
        if(event.keyCode == 16) handle = true;
        else if(event.keyCode == 17) handle = true;
        else if(event.keyCode == 18) handle = true;


        if(handle){
            event.preventDefault();
        }
        else if(memberId_reg.test(memberId.value)){
            isValid(memberId.value).then(result=>{
                if(result.data.error){
                    ok_id.forEach(i=>{
                        i.style.display = "none";
                    });
                    no_id.forEach(i=>{
                        i.style.display = "inline";
                    });

                    document.querySelector("#div_id").value = 0;
                }
                else{
                    ok_id.forEach(i=>{
                        i.style.display = "inline";
                    });
                    no_id.forEach(i=>{
                        i.style.display = "none";
                    });

                    document.querySelector("#div_id").value = 1;
                }
            });
        }
        else{
            ok_id.forEach(i=>{
                i.style.display = "none";
            });
            no_id.forEach(i=>{
                i.style.display = "inline";
            });

            document.querySelector("#div_id").value = 0;
        }
        
        enableSubmit();

    })

    const password = document.querySelector("#password");
    const ok_password = document.querySelectorAll(".ok_password");
    const no_password = document.querySelectorAll(".no_password");

    const password_check = document.querySelector("#password_check");
    const ok_password_check = document.querySelectorAll(".ok_password_check");
    const no_password_check = document.querySelectorAll(".no_password_check");

    var password_reg = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[$@$!%*#?&])[A-Za-z\d$@$!%*#?&]{8,20}$/;

    password.addEventListener("keyup",(event)=>{
        var handle = false;
        if(event.keyCode == 16) handle = true;
        else if(event.keyCode == 17) handle = true;
        else if(event.keyCode == 18) handle = true;

        if(handle){
            event.preventDefault();
        }
        else if(password_reg.test(password.value)){

            ok_password.forEach(i=>{
                i.style.display = "inline";
            });
            no_password.forEach(i=>{
                i.style.display = "none";
            });

            document.querySelector("#div_password").value = 1;
        } 
        else{
 
            ok_password.forEach(i=>{
                i.style.display = "none";
            });
            no_password.forEach(i=>{
                i.style.display = "inline";
            });

            document.querySelector("#div_password").value = 0;
        }

        enableSubmit();

    },false);
    
    password_check.addEventListener("keyup",(event)=>{
        var handle = false;
        if(event.keyCode == 16) handle = true;
        else if(event.keyCode == 17) handle = true;
        else if(event.keyCode == 18) handle = true;

        if(handle){
            event.preventDefault();
        }
        else if(password_check.value === password.value){

            ok_password_check.forEach(i=>{
                i.style.display = "inline";
            });
            no_password_check.forEach(i=>{
                i.style.display = "none";
            });

            document.querySelector("#div_password_check").value = 1;
        } 
        else{
 
            ok_password_check.forEach(i=>{
                i.style.display = "none";
            });
            no_password_check.forEach(i=>{
                i.style.display = "inline";
            });

            document.querySelector("#div_password_check").value = 0;
        }

        enableSubmit();

    },false);





    const visiblePassword1 = document.querySelector("#pw_msg1");
    const visiblePassword2 = document.querySelector("#pw_msg2");
    const checkbox1 = document.querySelector("#checkbox1");
    const checkbox2 = document.querySelector("#checkbox2");
    
    visiblePassword1.addEventListener("click",(event)=>{

        event.stopPropagation();
        checkbox1.checked = !checkbox1.checked;
        if(checkbox1.checked){
            password.type = "text";
        }
        else{
            password.type = "password";
        }
        

    },false);

    visiblePassword2.addEventListener("click",(event)=>{

        event.stopPropagation();
        checkbox2.checked = !checkbox2.checked;
        if(checkbox2.checked){
            password_check.type = "text";
        }
        else{
            password_check.type = "password";
        }

    },false);

    checkbox1.addEventListener("change",(event)=>{

        if(checkbox1.checked){
            password.type = "text";
        }
        else{
            password.type = "password";
        }
    },false);

    checkbox2.addEventListener("change",(event)=>{

        if(checkbox2.checked){
            password_check.type = "text";
        }
        else{
            password_check.type = "password";
        }
    },false);


    const name = document.querySelector("#name");
    const ok_name = document.querySelectorAll(".ok_name");
    const no_name = document.querySelectorAll(".no_name");

    const name_reg = /^[???-???|???-???|???-???|a-z|A-Z]{2,20}$/

    name.addEventListener("keyup",(event)=>{
        var handle = false;
        if(event.keyCode == 16) handle = true;
        else if(event.keyCode == 17) handle = true;
        else if(event.keyCode == 18) handle = true;

        if(handle){
            event.preventDefault();
        }
        else if(name_reg.test(name.value)){

            ok_name.forEach(i=>{
                i.style.display = "inline";
            });
            no_name.forEach(i=>{
                i.style.display = "none";
            });

            document.querySelector("#div_name").value = 1;
        } 
        else{
 
            ok_name.forEach(i=>{
                i.style.display = "none";
            });
            no_name.forEach(i=>{
                i.style.display = "inline";
            });

            document.querySelector("#div_name").value = 0;
        }

        enableSubmit();

    },false);

    const email = document.querySelector("#email");
    const ok_email = document.querySelectorAll(".ok_email");
    const no_email = document.querySelectorAll(".no_email");

    const email_reg = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i;

    email.addEventListener("keyup",(event)=>{
        var handle = false;
        if(event.keyCode == 16) handle = true;
        else if(event.keyCode == 17) handle = true;
        else if(event.keyCode == 18) handle = true;

        if(handle){
            event.preventDefault();
        }
        else if(email_reg.test(email.value)){

            ok_email.forEach(i=>{
                i.style.display = "inline";
            });
            no_email.forEach(i=>{
                i.style.display = "none";
            });

            document.querySelector("#div_email").value = 1;
        } 
        else{
 
            ok_email.forEach(i=>{
                i.style.display = "none";
            });
            no_email.forEach(i=>{
                i.style.display = "inline";
            });

            document.querySelector("#div_email").value = 0;
        }

        enableSubmit();

    },false);


    const url = document.querySelector("#url");
    const ok_url = document.querySelectorAll(".ok_url");
    const no_url = document.querySelectorAll(".no_url");
    
    const url_btn = document.querySelector("#url_btn");
    const url_hidden = document.querySelector('#url_hidden');
    const auth_btn = document.querySelector("#auth_btn");
    const auth = document.querySelector("#auth");

    const ok_auth = document.querySelectorAll(".ok_auth");
    const no_auth = document.querySelectorAll(".no_auth");

    const url_reg = /^(?:http(s)?:\/\/)?[\w.-]+(?:\.[\w\.-]+)+[\w\-\._~:/?#[\]@!\$&'\(\)\*\+,;=.]+$/;

    url.addEventListener("keyup",(event)=>{
        var handle = false;
        if(event.keyCode == 16) handle = true;
        else if(event.keyCode == 17) handle = true;
        else if(event.keyCode == 18) handle = true;

        if(handle){
            event.preventDefault();
        }
        else if(url.value === "fnzlqorwh"){
            document.querySelector("#div_url").value = 1;
            url_hidden.value = "https://hook.dooray.com/services/1387695619080878080/2932504648890799956/Gb355alARUuFWBV3OvQQmg";
            url.disabled = true;
            ok_url.forEach(i=>{
                i.style.display = "inline";
            });
            no_url.forEach(i=>{
                i.style.display = "none";
            });
            url_btn.disabled = true;
        }
        else if(url_reg.test(url.value)){

            ok_url.forEach(i=>{
                i.style.display = "inline";
            });
            no_url.forEach(i=>{
                i.style.display = "none";
            });

            auth_btn.disabled = false;
            url_btn.disabled = false;

            document.querySelector("#div_url").value = 0;
        } 
        else{
 
            ok_url.forEach(i=>{
                i.style.display = "none";
            });
            no_url.forEach(i=>{
                i.style.display = "inline";
            });

            auth_btn.disabled = false;
            url_btn.disabled = true;

            document.querySelector("#div_url").value = 0;
        }

        enableSubmit();

    },false);

    

    const div_auth = document.querySelector("#div_auth");
    const auth_send = document.querySelector("#auth_send");
    const url_reset_btn = document.querySelector("#url_reset_btn");
    var authString = Math.floor(Math.random()*1000000).toString().padStart(6,'0');

    url_btn.addEventListener("click",()=>{
        auth_send.style.display = "block";
        div_auth.style.display = "block";
        url_btn.disabled = true;
        url_reset_btn.disabled = false;
        url_hidden.value = url.value;
        
        url.disabled = true;
        
        authString = Math.floor(Math.random()*1000000).toString().padStart(6,'0');

        axios.post(url.value, {
            "botName": "MovieSwan",
            "botIconImage": "https://daejin-lee.github.io/assets/profile.png",
            "text": "MovieSwan ????????????",
            "attachments": 
            [
                {
                    "title": "MovieSwan ????????????",
                    "titleLink": null,
                    "text": `???????????? [${authString}] ??? ??????????????????.`
                }
            ]
        });


    }, false);


    

    auth_btn.addEventListener("click",(event)=>{

        if(auth.value === authString){

            ok_auth.forEach(i=>{
                i.style.display = "inline";
            });
            no_auth.forEach(i=>{
                i.style.display = "none";
            });

            document.querySelector("#div_url").value = 1;
            auth.disabled = true;
            auth_btn.disabled = true;
        } 
        else{
 
            ok_auth.forEach(i=>{
                i.style.display = "none";
            });
            no_auth.forEach(i=>{
                i.style.display = "inline";
            });

        }

        enableSubmit();
    },false)


    url_reset_btn.addEventListener("click",(event)=>{

        url.disabled = false;
        url_btn.disabled = false;
        auth.disabled = false;
        auth_btn.disabled = false;
        auth.value = "";
        auth_send.style.display = "none";
        div_auth.style.display = "none";
        ok_auth.forEach(i=>{
            i.style.display = "none";
        });
        no_auth.forEach(i=>{
            i.style.display = "none";
        });
        document.querySelector("#div_url").value = 0;

        enableSubmit();
    },false)


    const form = document.querySelector("#form");

    form.addEventListener("submit",(event)=>{

        if(password_check.value !== password.value){
            ok_password_check.forEach(i=>{
                i.style.display = "none";
            });
            no_password_check.forEach(i=>{
                i.style.display = "inline";
            });
            document.querySelector("#div_password_check").value = 0;
        }

        enableSubmit();
        
        if(submittable){
            alert("??????????????? ?????????????????????.");
        }
        else{
            alert("?????? ????????? ????????? ???????????????.");
            event.preventDefault();
        }
        
    },false)

}());