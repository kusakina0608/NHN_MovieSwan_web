"use strict"

var req = ""

function movieSelectorOnChange() {
    const request = axios.create({
        baseURL: "http://movieswan.nhnent.com/movie"
    });

    req = request.get(`/api/schedule/get?mid=5`);
};

(movieSelectorOnChange());