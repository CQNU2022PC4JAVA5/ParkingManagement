package com.team5.websocket;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Handler extends Thread{
    protected Socket socket;
    public  Handler(Socket socket) {
        this.socket=socket;
    }

    @Override
    public void run() {
        try(InputStream input=this.socket.getInputStream()){
            try(OutputStream output=this.socket.getOutputStream()) {
                handle(input,output);
            } catch (Exception e) {
                // TODO: handle exception
            }
        } catch (Exception e) {
            try {
                this.socket.close();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
    }

    private void handle(InputStream input, OutputStream output) throws IOException {
        // TODO Auto-generated method stub
        BufferedReader reader=new BufferedReader(new InputStreamReader(input,StandardCharsets.UTF_8)) ;
        BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(output,StandardCharsets.UTF_8));
        // 读取http请求
        boolean requestOk=false;
        String first=reader.readLine();
        if (first.startsWith("GET / HTTP/")) {
            requestOk=true;
        }
        for(;;){
            String header=reader.readLine();
            // 读取头部信息为空时,HTTP Header读取完毕
            if (header.isEmpty()) {
                break;
            }
            System.out.println(header);
        }

        System.out.println(requestOk ? "Response OK" : "Response Error");
        // 请求失败
        if (!requestOk) {
            // 发送错误响应:
            writer.write("HTTP/1.0 404 Not Found\r\n");
            writer.write("Content-Length: 0\r\n");
            writer.write("\r\n");
            writer.flush();
        }else {
            // 请求成功
            // 发送成功响应:
            String data = "<html><body><h1>Hello, world!</h1></body></html>";
            int length = data.getBytes(StandardCharsets.UTF_8).length;
            writer.write("HTTP/1.1 200 OK\r\n");
            writer.write("Connection: close\r\n");
            writer.write("Content-Type: text/html\r\n");
            writer.write("Content-Length: " + length + "\r\n");
            writer.write("\r\n"); // 空行标识Header和Body的分隔
            writer.write(data);
            writer.flush();
        }
    }
}

