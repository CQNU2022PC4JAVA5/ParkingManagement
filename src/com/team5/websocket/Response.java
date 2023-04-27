package com.team5.websocket;

import java.nio.charset.StandardCharsets;

public class Response extends Header{
    private String data;
    public String httpVersion="HTTP/1.1";
    public String httpStatus="200 OK";
    public String contentType="text/html";

    public String getData() {
        String result="";
        result+=getHeaders();
        result+="Content-Length: "+getDatalength()+br;
        result+=br;
        result+=this.data;
        return result;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getDatalength() {
        return data.getBytes(StandardCharsets.UTF_8).length;
    }

    public Response(){
        super();
    }
    public String getHeaders(){
        if(!isInclude("Content-Type"))
            super.addHearder("Content-Type: "+this.contentType);
        if(!isInclude("Connection"))
                super.addHearder("Connection: close");
        String result="";
        result+=httpVersion+" "+httpStatus+br;
        for(String eachheader:super.headers){
            result+=eachheader+br;
        }
        return result;
    }


}
