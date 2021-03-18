package com.nhn.rookie8.movieswanticketapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MonitorServiceImpl implements MonitorService{

    private static boolean l7State;

    @Override
    public void disableL7() {
        l7State = false;
    }

    @Override
    public void enableL7() {
        l7State = true;
    }

    @Override
    public boolean l7isEnable() {
        return l7State;
    }
}
