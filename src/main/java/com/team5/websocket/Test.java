package com.team5.websocket;

public class Test {
    public static void main(String[] args) {
        Request h=new Request();
        h.addHearder("Host: www.baidu.com");
        h.setMethod("GET");
        System.out.println(h.getMethod());
    }
}
