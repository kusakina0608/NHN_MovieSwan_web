'use strict';

(function() {
    emailjs.init('user_HWJSjiu6Yqh1TaX3CYQHR');
    var templateParams = {
        movie_title,
        movie_date,
        movie_time,
        movie_theater,
        user_name,
        user_url
    }
    emailjs.send('service_x131zev','template_21e1e7g',templateParams)
        .then(res=>{
            console.log(res.text);
        },err=>{
            console.log("failed"+error);
        });
}());