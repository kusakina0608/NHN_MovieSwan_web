window.onscroll = function () { scrollFunction() };

function scrollFunction() {
    scrolltotopbutton = document.getElementById("scroll_to_top")
    if (document.body.scrollTop > 20 || document.documentElement.scrollTop > 20) {
        scrolltotopbutton.style.display = "flex";
    } else {
        scrolltotopbutton.style.display = "none";
    }
}

function scrollToTop() {
    document.body.scrollTop = 0; // For Safari
    document.documentElement.scrollTop = 0; // For Chrome, Firefox, IE and Opera
}