package com.team5.websocket;

public class Request extends Header{
    public String method;//Allowed method: GET and Post
    public String data;
    public String httpversion;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        if(method.equals("GET") || method.equals("POST"))
            this.method = method;
        else{
            throw new RuntimeException("This request is setting a method which is not allowed!!!");
        }

    }

    public Request() {
        super();
    }

}
