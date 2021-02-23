"use strict"

var req = ""

function movieSelectorOnChange() {
    const request = axios.create({
        baseURL: "http://dev-movieswan.nhn.com/movie"
    });

    req = request.get(`/api/schedule/get?mid=5`);
};

(movieSelectorOnChange());