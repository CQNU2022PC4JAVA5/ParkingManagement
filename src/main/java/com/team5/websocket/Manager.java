package com.team5.websocket;
import com.team5.api.Fee;
import com.team5.api.Spots;
import com.team5.api.User;
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
        else if(request.data.equals("/favicon.ico")){
            response.httpStatus="302 moved";
            response.addHearder("Location: http://cqnu.baiyun.work/d/%E6%9C%AC%E5%9C%B0%E5%AD%98%E5%82%A8/%E7%A8%8B%E5%BA%8F%E8%AE%BE%E8%AE%A1/java/favicon.ico");
        }
        else if(request.data.equals("/crypto-js.min.js")){
            getJS(response);
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
            User user = new User();
            if(user.login(account,password)) {
                getLogin_success(response,account);
            }else{
                generateBAD(response,"登录失败","请检查您输入的数据是否正确，2秒后将自动跳转到登录页面。","login");
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
        else if(request.data.equals("/users")){
            getUsers(response);
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
        else if(request.data.startsWith("/changepassword?account=")){
            String account;
            account= getRightString(request.data,"account=");
            if(request.data.contains("&password")){
                String password = getRightString(account,"&password=");
                account = getLeftString(account,"&");
                User user = new User();
                if(user.changePassword(account,password)){
                    generateSUCCESS(response,"更改密码","更改密码成功，2秒后跳回账号列表，如当前账户被更改，请重新登录!","users");
                }else {
                    generateBAD(response,"更改密码","很抱歉，更改密码失败，2秒后跳回账号列表","users");
                }
            }else{
                getChangePassword(response,account);
            }
        }
        else if(request.data.equals("/create")){
            getCreate(response);
        }
        else if(request.data.startsWith("/create?account=")&&request.data.contains("&password=")){
            String account,password;
            account = getSubString(request.data,"account=","&");
            password = getRightString(request.data,"password=");
            getCreate(response,account,password);
        }

        else if(request.data.startsWith("/delect?account=")){
            delAccount(response,getRightString(request.data,"account="));
        }
        else if(request.data.equals("/logout")){
            getlogout(response,tokenkey);
        }
        else{
            defReturn(response);
        }
        return this.response;
    }
    private void getCreate(Response response){
        FileReader reader = new FileReader();
        String html = reader.readFile("src/main/java/websites/create.html");
        response.setData(html);
    }
    private void getCreate(Response response,String account,String password){
        User user = new User();
        boolean result = user.create(account,password);
        if(result){
            generateSUCCESS(response,"创建成功","创建账号成功，2秒后将自动跳转回账号管理界面。","users");
        }else{
            generateBAD(response,"创建失败","该账号已存在，2秒后将自动跳转回账号管理界面。","users");
        }
    }
    private void delAccount(Response response,String account){
        boolean result;
        User user = new User();
        result = user.delect(account);
        if(result){
            generateSUCCESS(response,"删除成功","该账号已成功删除，2秒后将自动跳转回账号管理界面。","users");
        }else{
            generateBAD(response,"删除失败","2秒后将自动跳转回账号管理界面。","users");
        }
    }
    private void getJS(Response response){
        FileReader reader = new FileReader();
        response.setData(reader.readFile("src/main/java/websites/crypto-js.min.js"));
        response.contentType="application/javascript; charset=utf-8";
    }
    private void generateBAD(Response response,String title,String content,String target){
        FileReader reader = new FileReader();
        String html = reader.readFile("src/main/java/websites/bad.html");
        html=html.replace("标题",title);
        html=html.replace("提示",content);
        html=html.replace("/跳转","/"+target);
        response.setData(html);
    }
    private void generateSUCCESS(Response response,String title,String content,String target){
        FileReader reader = new FileReader();
        String html = reader.readFile("src/main/java/websites/success.html");
        html=html.replace("标题",title);
        html=html.replace("提示",content);
        html=html.replace("/跳转","/"+target);
        response.setData(html);
    }
    private void getChangePassword(Response response,String account){
        FileReader reader = new FileReader();
        String html = reader.readFile("src/main/java/websites/changepassword.html");
        html = getLeftString(html,"value")+"value=\""+account+"\""+getRightString(html,"value");
        response.setData(html);
    }
    private void getlogout(Response response,String tokenkey){
        Token token = new Token();
        token.delToken(tokenkey);
        response.addHearder("Set-Cookie: token=blank");
        toLogin(response);
    }
    private void inSpot(Response response,String spot,String no){
        Spots spots = new Spots();
        boolean result = false;
        result=spots.inSpot(Integer.parseInt(spot),no);
        if(result){
            generateSUCCESS(response,"入场成功","该车辆已成功入场，2秒后将自动跳转回入场界面。","in");
        }else{
            generateBAD(response,"入场失败","车位可能被占用，2秒后将自动跳转回入场界面。","in");
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
            generateSUCCESS(response,"出场成功","该车辆已成功出场，2秒后将自动跳转回出场界面。","out");
        }else{
            generateBAD(response,"出场失败","没有找到此车，2秒后将自动跳转回出场界面。","out");
        }
    }
    private void outSpot(Response response,int wno){
        Spots spots = new Spots();
        spots.outSpot(wno);
        toSpots(response);
    }

    private void getLogin_success(Response response,String account){
        generateSUCCESS(response,"登录成功","您已经成功登录，2秒后将自动跳转到功能页面。","functions");
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
    private void getUsers(Response response){
        FileReader reader = new FileReader();
        String html=reader.readFile("src/main/java/websites/user.html");
        User user = new User();
        response.setData(user.generateHTML(html));
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
