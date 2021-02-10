'use strict';

(function() {
    //찜 버튼 클릭 동작 관련
    var favBtns = document.querySelectorAll(".favorite");

    favBtns.forEach(favBtn => {
        favBtn.addEventListener('click', function(ev) {
            ev.stopPropagation();
            if(favBtn.className == "favorite")
                favBtn.className = "favorite clicked";
            else
                favBtn.className = "favorite";
        }); 
    });
}) ();