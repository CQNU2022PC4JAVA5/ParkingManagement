package com.team5.websocket;

import com.team5.sql.SQL;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
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
        String result="";
        do{
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 32; i++) {
                int index = RANDOM.nextInt(36);
                sb.append(CHARS[index]);
            }
            result=sb.toString();
            //System.out.println(result+isNewToken(result));
        }while(!isNewToken(result));
        return result;
    }
    public Date getExpireDate(){
        SQL sql=new SQL();
        sql.getConnect();
        sql.getStatement();
        ResultSet rs = sql.getResultSet("SELECT expire FROM token WHERE token='"+this.tokenkey+"'");
        try {
            while (rs.next()) {
                java.sql.Timestamp timestamp = rs.getTimestamp("expire");
                rs.close();
                sql.delStatement();
                sql.disConnect();
                return toDate(timestamp);
            }
            rs.close();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        sql.delStatement();
        sql.disConnect();
        return new Date(0);
    }
    public boolean isRightAccount(String account){
        if(account==null || account.equals("")){
            return false;
        }
        SQL sql=new SQL();
        sql.getConnect();

        try {
            PreparedStatement ps = sql.conn.prepareStatement("SELECT account FROM token WHERE account=?;");
            ps.setString(1,account);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                boolean result=false;
                if(rs.getString("account").equals(account)){
                    result=true;
                }
                rs.close();
                ps.close();
                sql.disConnect();
                return result;
            }
            rs.close();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        sql.disConnect();
        return false;
    }
    public boolean isNewToken(String tokenkey){
        if(tokenkey==null || tokenkey.equals("")){
            return false;
        }
        SQL sql=new SQL();
        sql.getConnect();
        try {
            PreparedStatement ps = sql.conn.prepareStatement("SELECT token FROM token WHERE token=?;");
            ps.setString(1,tokenkey);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                System.out.println(rs.getString("token"));
                boolean result=true;
                if(rs.getString("token").equals(tokenkey)){
                    result=false;
                }
                rs.close();
                ps.close();
                sql.disConnect();
                return result;
            }
            rs.close();
            return true;
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        sql.disConnect();
        return false;
    }
    public static java.sql.Timestamp toTimestamp(java.util.Date utilDate) {
        java.sql.Timestamp ts = new java.sql.Timestamp(utilDate.getTime());
        return ts;
    }
    public static java.util.Date toDate(java.sql.Timestamp ts) {
        java.util.Date utilDate = new java.util.Date(ts.getTime());
        return utilDate;
    }
    public void setExpireDate(String account,String tokenkey,Date expire){
        if(!isRightAccount(account)){
            return;
        }
        if(tokenkey.equals("")){
            return;
        }
        SQL sql = new SQL();
        sql.getConnect();
        try {
            if(!getTokenkey(account).equals("")){
                PreparedStatement ps = sql.conn.prepareStatement("update token set token=?,expire = ? where account = ?;");
                ps.setTimestamp(2, toTimestamp(expire));
                ps.setString(3, account);
                ps.setString(1, tokenkey);
                int rows = ps.executeUpdate();
                System.out.println("更新了" + rows + "条数据");
                ps.close();
            }else{
                PreparedStatement ps = sql.conn.prepareStatement("INSERT INTO token (account, token,expire) VALUES (?, ?,?);");
                ps.setString(1, account);
                ps.setString(2, tokenkey);
                ps.setTimestamp(3,toTimestamp(expire));
                int rows = ps.executeUpdate();
                System.out.println("插入了" + rows + "条数据");
                ps.close();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        sql.disConnect();
    }
    public void setExpireDate(String account,Date expire){
        if(!isRightAccount(account)){
            return;
        }
        if(tokenkey.equals("")){
            return;
        }
        SQL sql = new SQL();
        sql.getConnect();
        try {
            if(!getTokenkey(account).equals("")){
                PreparedStatement ps = sql.conn.prepareStatement("update token set token=?,expire = ? where account = ?;");
                ps.setTimestamp(2, toTimestamp(expire));
                ps.setString(3, account);
                ps.setString(1, this.tokenkey);
                int rows = ps.executeUpdate();
                System.out.println("更新了" + rows + "条数据");
                ps.close();
            }else{
                PreparedStatement ps = sql.conn.prepareStatement("INSERT INTO token (account, token,expire) VALUES (?, ?,?);");
                ps.setString(1, account);
                ps.setString(2, this.tokenkey);
                ps.setTimestamp(3,toTimestamp(expire));
                int rows = ps.executeUpdate();
                System.out.println("插入了" + rows + "条数据");
                ps.close();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        sql.disConnect();
    }

    public Date getNewExpireDate(){//token过期时间为30分钟，更新时间为超过10分钟
        Date now = new Date();
        return new Date(now.getTime() + 30 * 60 * 1000);
    }
    public Date getExpireDate(String tokenkey){
        SQL sql=new SQL();
        sql.getConnect();
        sql.getStatement();
        ResultSet rs = sql.getResultSet("SELECT expire FROM token WHERE token='"+tokenkey+"'");
        try {
            while (rs.next()) {
                Date expire = rs.getTimestamp("expire");
                rs.close();
                sql.delStatement();
                sql.disConnect();
                return expire;
            }
            rs.close();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        sql.delStatement();
        sql.disConnect();
        return new Date(0);
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
    public boolean isNeedUpdate(String tokenkey) {
        Date now = new Date();
        Date after20Minutes = new Date(now.getTime() + 20 * 60 * 1000);
        Date expire = getExpireDate(tokenkey);
        boolean isNeedUpdate = after20Minutes.after(expire);
        return isNeedUpdate;
    }
    public boolean isNeedUpdate() {
        Date now = new Date();
        Date after20Minutes = new Date(now.getTime() + 20 * 60 * 1000);
        if(this.tokenkey.equals("")){
            return true;
        }
        if(getAccount(this.tokenkey).equals("")){
            return true;
        }
        Date expire = getExpireDate();

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
                    String account=rs.getString("account");
                    rs.close();
                    ps.close();
                    sql.disConnect();
                    return account;
                }
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        sql.disConnect();
        return "";
    }
    public void update(){
        setExpireDate(getAccount(this.tokenkey),this.tokenkey,getNewExpireDate());
    }
    public void update(String tokenkey){
        setExpireDate(getAccount(tokenkey),tokenkey,getNewExpireDate());
    }
    public void init(String account,String tokenkey){
        setExpireDate(account,tokenkey,getNewExpireDate());
    }
    public void init(String account){
        setExpireDate(account,this.tokenkey,getNewExpireDate());
    }
    public String getTokenkey(String account){
        SQL sql = new SQL();
        sql.getConnect();
        try {
            PreparedStatement ps = sql.conn.prepareStatement("select token from token where account=?;");
            ps.setString(1, account);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if(!rs.getString("token").equals("") && rs.getString("token")!=null){
                    String token=rs.getString("token");
                    rs.close();
                    ps.close();
                    sql.disConnect();
                    return token;
                }
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        sql.disConnect();
        return "";
    }
    public boolean haveToken(String account){
        return getTokenkey(account).equals("");
    }
}
