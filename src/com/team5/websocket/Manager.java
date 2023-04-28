package com.team5.websocket;

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

        if(request.data.equals("/")){
            toLogin(response);
        }

        if(request.data.equals("/login")) {
            getLogin(response);
        }

        else {
            defReturn(response);
        }

        return this.response;
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
    private void defReturn(Response response){
        response.setData("<html><body><h1>Hello, world!</h1></body></html>");

    }
}
