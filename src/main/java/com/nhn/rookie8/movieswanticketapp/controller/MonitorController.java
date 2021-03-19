package com.nhn.rookie8.movieswanticketapp.controller;

import com.nhn.rookie8.movieswanticketapp.service.MonitorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
@Log4j2
@RequestMapping("/monitor")
@RequiredArgsConstructor
public class MonitorController {

    private final MonitorService monitorService;

    @GetMapping("/l7check")
    public ResponseEntity l7check(){
        if(monitorService.l7isEnable()){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/l7check/enable")
    public String enablel7(HttpServletRequest request){
        monitorService.enableL7();
        return "redirect:/monitor/l7check";

    }

    @GetMapping("/l7check/disable")
    public String disablel7(HttpServletRequest request){
        monitorService.disableL7();
        return "redirect:/monitor/l7check";
    }

}
