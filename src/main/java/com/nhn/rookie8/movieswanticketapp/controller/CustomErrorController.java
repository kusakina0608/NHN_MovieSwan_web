package com.nhn.rookie8.movieswanticketapp.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            if (statusCode == 405) { return "/error/405"; }
            else if (statusCode >= 400 && statusCode < 500) { return "/error/404"; }
            else if (statusCode >= 500) { return "/error/500"; }
        }
        return null;
    }
    @Override
    public String getErrorPath() {
        return null;
    }
}
