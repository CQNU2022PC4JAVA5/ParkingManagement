package com.team5.api;

import com.team5.sql.SQL;
import com.team5.websocket.Token;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import static com.team5.other.text.*;

public class User {
    public User(){
    }
    public boolean login(String account,String password) {
        SQL sql = new SQL();
        sql.getConnect();
        sql.getStatement();
        ResultSet rs = sql.getResultSet("SELECT account, password FROM account");

        try {
            while (rs.next()) {
                String sql_account = rs.getString("account");
                String sql_password = rs.getString("password");
                if (sql_account.equals(account)) {
                    if (sql_password.equals(password)) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    continue;
                }
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        sql.delStatement();
        sql.disConnect();
        return false;
    }
    public boolean create(String account,String password){
        if(isExist(account)){
            return false;
        }
        SQL sql = new SQL();
        sql.getConnect();
        try {
            PreparedStatement ps = sql.conn.prepareStatement("INSERT INTO account (account,password) VALUES (?, ?);");
            ps.setString(1, account);
            ps.setString(2, password);
            int rows = ps.executeUpdate();
            System.out.println("插入了" + rows + "条数据");
            ps.close();
            sql.disConnect();
            return rows > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public boolean changePassword(String account,String password){
        SQL sql = new SQL();
        sql.getConnect();
        try {
            PreparedStatement ps = sql.conn.prepareStatement("UPDATE account set password = ? WHERE account = ?;");
            ps.setString(2, account);
            ps.setString(1, password);
            int rows = ps.executeUpdate();
            System.out.println("更新了" + rows + "条数据");
            ps.close();
            sql.disConnect();
            Token token = new Token();
            token.delToken(account);
            return rows > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public boolean delect(String account){
        ArrayList<String> list = list();
        if (list.size()<2){
            return false;
        }
        if(!list.contains(account)){
            return false;
        }
        Token token = new Token();
        token.delToken(account);
        SQL sql = new SQL();
        sql.getConnect();
        try {
            PreparedStatement ps = sql.conn.prepareStatement("DELETE FROM account WHERE account = ?;");
            ps.setString(1, account);
            int rows = ps.executeUpdate();
            System.out.println("删除了" + rows + "条数据");
            ps.close();
            sql.disConnect();
            return rows > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<String> list(){
        ArrayList<String> list = new ArrayList<String>();
        SQL sql = new SQL();
        sql.getConnect();
        try {
            PreparedStatement ps = sql.conn.prepareStatement("SELECT * FROM account;");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(rs.getString("account"));
            }
            rs.close();
            ps.close();
            sql.disConnect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
    private boolean isExist(String account){
        ArrayList<String> list=list();
        return list.contains(account);
    }
    private String generateHTML_single(String account){
        String result = "";
        result+="<tr>\n\r";
        result+="<td>"+account+"</td>\n\r";
        result += "<td>"+"<button onclick=\"window.location.href='changepassword?account="+account+"'\" type=\"button\" id=\"add\">更改密码</button>"+"</td>\n\r";
        result += "<td>"+"<button onclick=\"window.location.href='delect?account="+ account+"'\" type=\"button\" id=\"add\">删除账号</button>"+"</td>\n\r";
        result += "</tr>\n\r";
        return result;
    }

    public String generateHTML(String HTML){
        String result = "";
        ArrayList<String> list = list();
        String[] users = list.toArray(new String[list.size()]);
        for (String user : users){
            result +=generateHTML_single(user);
        }
        result = getLeftString(HTML,"<users>")+result+getRightString(HTML,"<users>");
        return result;
    }




}
