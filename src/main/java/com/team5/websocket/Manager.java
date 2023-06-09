package com.team5.websocket;
import com.team5.api.Fee;
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
        else if(token.isNeedUpdate(token.tokenkey)){
            token.update();
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
        else if(request.data.startsWith("/fee?freetime=")&&request.data.contains("&firsttime=")&&request.data.contains("&firstfee=")&&request.data.contains("&secondtime=")&&request.data.contains("&secondfee=")){
            String freetime,firsttime,firstfee,secondtime,secondfee;

            freetime= getSubString(request.data,"freetime=","&firsttime=");
            firsttime= getSubString(request.data,"firsttime=","&firstfee=");
            firstfee= getSubString(request.data,"firstfee=","&secondtime=");
            secondtime= getSubString(request.data,"secondtime=","&secondfee=");
            secondfee= getRightString(request.data,"secondfee=");

            Fee fee = new Fee();
            fee.setFreetime(Integer.parseInt(freetime));
            fee.setFirsttime(Integer.parseInt(firsttime));
            fee.setFirstfee(Double.parseDouble(firstfee));
            fee.setSecondtime(Integer.parseInt(secondtime));
            fee.setSecondfee(Double.parseDouble(secondfee));

            toFunctions(response);

        }
        else if(request.data.equals("/in")){
            getIn(response);
        }
        else if(request.data.startsWith("/in?pno=")&&request.data.contains("&wno=")){
            String pno,wno;
            pno= getSubString(request.data,"pno=","&");
            wno= getRightString(request.data,"wno=");
            pno= getURLDecoderString(pno);
            inSpot(response,wno,pno);
        }
        else if(request.data.startsWith("/fast-in?wno=")){
            getIn_fast(response,getRightString(request.data,"?wno="));
        }
        else if(request.data.startsWith("/fast-in?pno=")&&request.data.contains("&wno=")){
            String pno,wno;
            pno= getSubString(request.data,"pno=","&");
            wno= getRightString(request.data,"wno=");
            pno= getURLDecoderString(pno);
            inSpot_fast(response,wno,pno);
        }
        else if(request.data.equals("/out")){
            getOut(response);
        }
        else if(request.data.startsWith("/out?pno=")){
            String pno;
            pno= getRightString(request.data,"pno=");
            pno= getURLDecoderString(pno);
            outSpot(response,pno);
        }
        else if(request.data.startsWith("/fast-out?wno=")){
            String wno;
            wno= getRightString(request.data,"wno=");
            outSpot(response,Integer.parseInt(wno));
        }
        else{
            defReturn(response);
        }
        return this.response;
    }
    private void inSpot(Response response,String spot,String no){
        Spots spots = new Spots();
        boolean result = false;
        result=spots.inSpot(Integer.parseInt(spot),no);
        if(result){
            getin_success(response);
        }else{
            getin_bad(response);
        }
    }
    private void inSpot_fast(Response response,String spot,String no){
        Spots spots = new Spots();
        boolean result = false;
        result=spots.inSpot(Integer.parseInt(spot),no);
        toSpots(response);
    }

    private void getIn_fast(Response response,String wno){
        FileReader reader = new FileReader();
        String html=reader.readFile("src/main/java/websites/in.html");
        html = getLeftString(html,"name=\"wno\"")+"name=\"wno\""+" value=\""+wno+"\" style=\"display:none\""+getRightString(html,"name=\"wno\"");
        html = getLeftString(html,"functions")+"spots"+getRightString(html,"functions");
        html = getLeftString(html,"/in")+"/fast-in"+getRightString(html,"/in");
        html = getLeftString(html,"for=\"textfield2\"")+"for=\"textfield2\" style=\"display:none\""+getRightString(html,"for=\"textfield2\"");
        response.setData(html);
    }
    private void outSpot(Response response,String pno){
        Spots spots = new Spots();
        boolean result = spots.outSpot(pno);
        if(result){
            getout_success(response);
        }else{
            getout_bad(response);
        }
    }
    private void outSpot(Response response,int wno){
        Spots spots = new Spots();
        spots.outSpot(wno);
        toSpots(response);
    }
    private void getin_success(Response response){
        FileReader reader = new FileReader();
        response.setData(reader.readFile("src/main/java/websites/in_success.html"));
    }
    private void getin_bad(Response response){
        FileReader reader = new FileReader();
        response.setData(reader.readFile("src/main/java/websites/in_bad.html"));
    }
    private void getout_success(Response response){
        FileReader reader = new FileReader();
        response.setData(reader.readFile("src/main/java/websites/out_success.html"));
    }
    private void getout_bad(Response response){
        FileReader reader = new FileReader();
        response.setData(reader.readFile("src/main/java/websites/out_bad.html"));
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
        String foot=getRightString(html,"<spots>");
        String data="";
        data+=head;
        Spots spots = new Spots();
        data+=spots.getHTML();
        data+=foot;
        response.setData(data);
    }
    private void getFee(Response response){
        FileReader reader = new FileReader();
        Fee fee = new Fee();
        String data="";
        data=reader.readFile("src/main/java/websites/fee.html");
        data= fee.changeHTML(data);
        response.setData(data);
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
