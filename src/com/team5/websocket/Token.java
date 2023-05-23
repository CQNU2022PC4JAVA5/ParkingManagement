package com.team5.websocket;

import com.team5.sql.SQL;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Random;

public class Token {
    String tokenkey ="";
    public Token(){}
    public Token(String tokenkey){
        this.tokenkey=tokenkey;
    }
    public String getNewTokenKey(){
        final char[] CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        final Random RANDOM = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 32; i++) {
            int index = RANDOM.nextInt(36);
            sb.append(CHARS[index]);
        }
        return sb.toString();
    }
    public Date getExpireDate(){
        SQL sql=new SQL();
        sql.getConnect();
        sql.getStatement();
        ResultSet rs = sql.getResultSet("SELECT expire FROM token WHERE token='"+this.tokenkey+"'");
        try {
            while (rs.next()) {
                Date expire = rs.getDate("expire");
            }
            rs.close();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        sql.delStatement();
        sql.disConnect();
        return new Date();
    }
    public void setExpireDate(String tokenkey,Date expire){
        SQL sql = new SQL();
        sql.getConnect();
        try {
            PreparedStatement ps = sql.conn.prepareStatement("update token set expire = ? where token = ?;");
            ps.setDate(1, (java.sql.Date) expire);
            ps.setString(2, tokenkey);
            int rows = ps.executeUpdate();
            System.out.println("插入了" + rows + "条数据");
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void setExpireDate(Date expire){
        SQL sql = new SQL();
        sql.getConnect();
        try {
            PreparedStatement ps = sql.conn.prepareStatement("update token set expire = ? where token = ?;");
            ps.setDate(1, (java.sql.Date) expire);
            ps.setString(2, this.tokenkey);
            int rows = ps.executeUpdate();
            System.out.println("插入了" + rows + "条数据");
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public Date getNewExpireDate(){//token过期时间为30分钟，更新时间为超过10分钟
        Date now = new Date();
        return new Date(now.getTime() + 5 * 60 * 1000);
    }
    public Date getExpireDate(String tokenkey){
        SQL sql=new SQL();
        sql.getConnect();
        sql.getStatement();
        ResultSet rs = sql.getResultSet("SELECT expire FROM token WHERE token='"+tokenkey+"'");
        try {
            while (rs.next()) {
                Date expire = rs.getDate("expire");
            }
            rs.close();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        sql.delStatement();
        sql.disConnect();
        return new Date();
    }
    public boolean isExpire(String tokenkey){
        Date now = new Date();
        return !now.before(getExpireDate(tokenkey));
    }
    public boolean isExpire(){
        Date now = new Date();
        return !now.before(getExpireDate());
    }
    public boolean isNeedUpdate(Date expire) {
        Date now = new Date();
        Date after20Minutes = new Date(now.getTime() + 20 * 60 * 1000);
        boolean isNeedUpdate = after20Minutes.after(expire);
        return isNeedUpdate;
    }
    public String getAccount(String tokenkey){
        SQL sql = new SQL();
        sql.getConnect();
        try {
            PreparedStatement ps = sql.conn.prepareStatement("select account from token where token=?;");
            ps.setString(1, tokenkey);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if(!rs.getString("account").equals("") && rs.getString("account")!=null){
                    return rs.getString("account");
                }
            }
            rs.close();
            int rows = ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return "";
    }
}
