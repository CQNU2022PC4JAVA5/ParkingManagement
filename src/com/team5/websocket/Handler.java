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
                System.out.println(e.getMessage());
                // TODO: handle exception
            }
        } catch (Exception e) {
            try {
                this.socket.close();
            } catch (IOException e1) {
                System.out.println(e.getMessage());
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
        Manager manager = new Manager();
        for(;;){
            String header=reader.readLine();
            if (header.isEmpty()) {
                break;
            }
            manager.request.addHearder(header);
        }
        String data=manager.getResponse().getData();
        writer.write(data);
        writer.flush();
    }
}

