package com.team5.websocket;
import com.team5.api.Spots;

import static com.team5.api.Login.*;
import static com.team5.other.text.*;


public class Manager {
    public Request request=new Request();
    private Response response=new Response();
    public static final String br ="\r\n";
    public Response getResponse() {
        if(!request.method.equals("GET")&&!request.method.equals("POST")){
            System.out.println("checked method which not allowed:"+request.method);
            response.setData("<html><body><h1>Method not allowed!</h1></body></html>");
            response.httpStatus="403 AccessDenied";
            return this.response;
        }
        System.out.println("request:"+request.data);
        String tokenkey = getRightString(request.getValue("Cookie"),"token=");
        Token token = new Token(getRightString(request.getValue("Cookie"),"token="));
        if(request.data.equals("/")) {
            toLogin(response);
        }
        else if(request.data.equals("/login")) {
            if(token.isExpire()){
                getLogin(response);
            }else{
                toFunctions(response);
            }
        }
        else if(request.data.startsWith("/login?username=")&&request.data.contains("&password=")){
            String account,password;
            account= getSubString(request.data,"username=","&");
            password=getRightString(request.data,"password=");
            if(login(account,password)) {
                getLogin_success(response,account);
            }else{
                toLogin(response);
            }
        }
        else if(token.isExpire()){
            toLogin(response);
        }
        else if(request.data.equals("/functions")){
            getFunctions(response);
        }
        else if(request.data.equals("/spots")){
            getSpots(response);
        }
        else if(request.data.equals("/fee")){
            getFee(response);
        }
        else if(request.data.equals("/in")){
            getIn(response);
        }
        else if(request.data.equals("/out")){
            getOut(response);
        }
        else{
            defReturn(response);
        }
        return this.response;
    }
    private void getLogin_success(Response response,String account){
        FileReader reader = new FileReader();
        response.setData(reader.readFile("src/main/java/websites/login_success.html"));
        Token token = new Token();
        token.tokenkey= token.getNewTokenKey();
        System.out.println(account);
        token.init(account);
        response.addHearder("Set-Cookie: token="+token.tokenkey);
    }
    private void getFunctions(Response response){
        FileReader reader = new FileReader();
        response.setData(reader.readFile("src/main/java/websites/functions.html"));
    }
    private void getLogin(Response response){
        FileReader reader = new FileReader();
        response.setData(reader.readFile("src/main/java/websites/login.html"));
    }
    private void getSpots(Response response){
        FileReader reader = new FileReader();
        String html=reader.readFile("src/main/java/websites/spots.html");
        String head=getLeftString(html,"<spots>");
        String foot=getLeftString(html,"<spots>");
        String data="";
        data+=head;
        Spots spots = new Spots();
        data+=spots.getHTML();
        data+=foot;
        response.setData(data);
    }
    private void getFee(Response response){
        FileReader reader = new FileReader();
        response.setData(reader.readFile("src/main/java/websites/fee.html"));
    }
    private void getIn(Response response){
        FileReader reader = new FileReader();
        response.setData(reader.readFile("src/main/java/websites/in.html"));
    }
    private void getOut(Response response){
        FileReader reader = new FileReader();
        response.setData(reader.readFile("src/main/java/websites/out.html"));
    }
    private void toLogin(Response response){
        response.httpStatus="302 moved";
        response.addHearder("Location: \\login");
        //response.setData("<html><body><h1>Hello, world!</h1></body></html>");
    }
    private void toFunctions(Response response){
        response.httpStatus="302 moved";
        response.addHearder("Location: \\functions");
        //response.setData("<html><body><h1>Hello, world!</h1></body></html>");
    }
    private void toSpots(Response response){
        response.httpStatus="302 moved";
        response.addHearder("Location: \\spots");
    }
    private void toFee(Response response){
        response.httpStatus="302 moved";
        response.addHearder("Location: \\fee");
    }
    private void toIn(Response response){
        response.httpStatus="302 moved";
        response.addHearder("Location: \\in");
    }
    private void toOut(Response response){
        response.httpStatus="302 moved";
        response.addHearder("Location: \\out");
    }
    private void defReturn(Response response){
        response.setData("<html><body><h1>Hello, world!</h1></body></html>");
    }
}
