package com.team5.websocket;

public class Manager {
    public Request request=new Request();
    private Response response=new Response();
    public static final String br ="\r\n";
    public Response getResponse() {
        if(!request.method.equals("GET")){
            response.setData("<html><body><h1>Methon not allowed!</h1></body></html>");
            response.httpStatus="403 AccessDenied";
            return this.response;

        }
        defReturn(response);
        return this.response;
    }
    private void defReturn(Response response){
        response.setData("<html><body><h1>Hello, world!</h1></body></html>");

    }
}
