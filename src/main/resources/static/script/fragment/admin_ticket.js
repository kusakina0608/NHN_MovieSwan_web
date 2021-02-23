"use strict"

var req = ""

function movieSelectorOnChange() {
    const request = axios.create({
        baseURL: location.origin
    });

    req = request.get(`/api/schedule/get?mid=5`);
};

(movieSelectorOnChange());