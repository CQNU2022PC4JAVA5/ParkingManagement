package com.team5.sql;
import java.sql.*;
public class SQL {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private String DB_URL = "jdbc:mysql://127.0.0.1/parkingmanagement?useSSL=false&characterEncoding=utf8";
    private String USER = "root";
    private String PASS = "root";
    public Connection conn = null;
    public Statement stmt = null;

    public SQL() {
    }

    public SQL(String DB_URL, String USER, String PASS) {
        this.DB_URL = DB_URL;
        this.USER = USER;
        this.PASS = PASS;
    }

    public boolean getConnect(){
        System.out.println("连接数据库...");
        try {
            this.conn = DriverManager.getConnection(DB_URL, USER, PASS);
        }
        catch (SQLException e){
            e.printStackTrace();
            System.out.println("连接数据库[失败]");
            return false;
        }
        System.out.println("连接数据库[成功]");
        return true;
    }
    public boolean disConnect(){
        try {
            System.out.println("断开数据库...");
            conn.close();
        }
        catch (Exception e){
            System.out.println("断开数据库[失败]");
            return false;
        }
        System.out.println("断开数据库[成功]");
        return true;
    }
    public boolean getStatement() {
        try{
            System.out.println("实例化Statement对象...");
            this.stmt = conn.createStatement();
        }catch (SQLException e){
            e.printStackTrace();
            System.out.println("实例化Statement对象[失败]");
            return false;
        }
        System.out.println("实例化Statement对象[成功]");
        return true;
    }
    public boolean delStatement() {
        try{
            System.out.println("销毁Statement对象...");
            stmt.close();
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("销毁Statement对象[失败]");
            return false;
        }
        System.out.println("销毁Statement对象[成功]");
        return true;
    }
    public ResultSet getResultSet(String SQLCommand){
        ResultSet rs=null;
        try {
            rs = stmt.executeQuery(SQLCommand);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return rs;
    }
    public void delResultSet(ResultSet rs){
        try {
            rs.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
