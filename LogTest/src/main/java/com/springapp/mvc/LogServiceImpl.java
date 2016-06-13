package com.springapp.mvc;

/**
 * Created by yimilan on 2016/2/29.
 */
public class LogServiceImpl implements LogService {

    public void add(Log log){
        System.out.println(log.getDescription());
    }
}
