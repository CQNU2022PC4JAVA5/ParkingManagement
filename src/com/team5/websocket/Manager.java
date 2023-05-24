package com.team5.websocket;
import static com.team5.api.login.*;
import static com.team5.other.text.*;


public class Manager {
    public Request request=new Request();
    private Response response=new Response();
    public static final String br ="\r\n";
    public Response getResponse() {
        if(!request.method.equals("GET")&&!request.method.equals("POST")){
            System.out.println("checked method which not allowed:"+request.method);
            response.setData("<html><body><h1>Methon not allowed!</h1></body></html>");
            response.httpStatus="403 AccessDenied";
            return this.response;
        }
        System.out.println("request:"+request.data);
        String tokenkey = getRightString(request.getValue("Cookie"),"token=");
        Token token = new Token(getRightString(request.getValue("Cookie"),"token="));
        if(request.data.equals("/")) {
            toLogin(response);
        }
        if(request.data.equals("/login")) {
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
        else{
            defReturn(response);
        }
        return this.response;
    }
    private void getLogin_success(Response response,String account){
        FileReader reader = new FileReader();
        response.setData(reader.readFile("src/websites/login_success.html"));
        Token token = new Token();
        token.tokenkey= token.getNewTokenKey();
        System.out.println(account);
        token.init(account);
        response.addHearder("Set-Cookie: token="+token.tokenkey);
    }
    private void getFunctions(Response response){
        FileReader reader = new FileReader();
        response.setData(reader.readFile("src/websites/functions.html"));
    }
    private void getLogin(Response response){
        FileReader reader = new FileReader();
        response.setData(reader.readFile("src/websites/login.html"));
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
    private void defReturn(Response response){
        response.setData("<html><body><h1>Hello, world!</h1></body></html>");
    }
}
