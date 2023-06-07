package com.team5.api;

import com.team5.sql.SQL;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;

public class Spots extends Fee{
    class spot{
        private int id;
        private String status;
        private String no;
        private Timestamp time;
        public spot(int id){
            this.id=id;
            getData();
        }
        void getData(){
            SQL sql = new SQL();
            sql.getConnect();
            try {
                PreparedStatement ps = sql.conn.prepareStatement("SELECT status,no,time FROM spots WHERE id=?");
                ps.setInt(1,this.id);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    this.status= rs.getString("status");
                    this.no= rs.getString("no");
                    this.time=rs.getTimestamp("time");
                    rs.close();
                    ps.close();
                    sql.disConnect();
                }
                rs.close();
            }catch (SQLException e){
                System.out.println(e.getMessage());
            }
            sql.disConnect();
        }
        public int getId(){
            return this.id;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            SQL sql = new SQL();
            sql.getConnect();
            try {
                PreparedStatement ps = sql.conn.prepareStatement("update spots set status=? WHERE id=?;");
                ps.setInt(2, this.id);
                ps.setString(1, status);
                int rows = ps.executeUpdate();
                this.status = status;
                System.out.println("更新了" + rows + "条数据");
                ps.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            sql.disConnect();
        }

        public String getNo() {
            return no;
        }

        public void setNo(String no) {
            SQL sql = new SQL();
            sql.getConnect();
            try {
                PreparedStatement ps = sql.conn.prepareStatement("update spots set no=? WHERE id=?;");
                ps.setInt(2, this.id);
                ps.setString(1, no);
                int rows = ps.executeUpdate();
                this.no = no;
                System.out.println("更新了" + rows + "条数据");
                ps.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            sql.disConnect();
        }

        public Timestamp getTime() {
            return time;
        }

        public void setTime(Timestamp time) {
            SQL sql = new SQL();
            sql.getConnect();
            try {
                PreparedStatement ps = sql.conn.prepareStatement("update spots set time=? WHERE id=?;");
                ps.setInt(2, this.id);
                ps.setTimestamp(1, time);
                int rows = ps.executeUpdate();
                this.time = time;
                System.out.println("更新了" + rows + "条数据");
                ps.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            sql.disConnect();
        }
    }

    public spot spot1 = new spot(1);
    public spot spot2 = new spot(2);
    public spot spot3 = new spot(3);
    public spot spot4 = new spot(4);
    public spot spot5 = new spot(5);
    public spot spot6 = new spot(6);
    public spot spot7 = new spot(7);
    public spot spot8 = new spot(8);
    public spot spot9 = new spot(9);
    public spot spot10 = new spot(10);
    public Spots(){
        super();
    }
    public static String timestamp_toString(Timestamp timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(timestamp);
    }
    public static String modifyMoney(double money) {
        BigDecimal bd = new BigDecimal(money).setScale(2, RoundingMode.UP);
        return bd.toString();
    }
    String getHTML_single(spot spot){
        String result="";
        result+="<tr>\n\r";
        result+="<td>车位"+spot.getId()+"</td>\n\r";
        result+="<td>"+ spot.getStatus() +"</td>\n\r";
        if(spot.getStatus().equals("空闲")){
            result += "<td></td>\n\r";
            result += "<td></td>\n\r";
            result += "<td></td>\n\r";
        }
        else {
            result += "<td>" + spot.getNo() + "</td>\n\r";
            result += "<td>" + timestamp_toString(spot.getTime()) + "</td>\n\r";
            result += "<td>"+modifyMoney(calculateFee(spot.getTime()))+"</td>\n\r";
        }

        result += "</tr>\n\r";
        return result;
    }
    public double calculateFee(Timestamp time){
        int minutes=getMinutes(time);
        minutes -=super.getFreetime();
        if(minutes<=0){
            return 0;
        }
        if (minutes<=getFirsttime()){
            return minutes*getFirstfee();
        }
        minutes-=getFirsttime();
        if(minutes<=getSecondtime()){
            return getFirsttime()*getFirstfee()+minutes*getSecondfee();
        }
        return getFirsttime()*getFirstfee()+getSecondtime()*getSecondfee();
    }

    int getMinutes(Timestamp time) {
        Instant instant = time.toInstant();
        Instant now = Instant.now();
        long minutes = Duration.between(instant, now).toMinutes();
        return (int) minutes;
    }

    public String getHTML(){
        String result="";
        result+=getHTML_single(spot1);
        result+=getHTML_single(spot2);
        result+=getHTML_single(spot3);
        result+=getHTML_single(spot4);
        result+=getHTML_single(spot5);
        result+=getHTML_single(spot6);
        result+=getHTML_single(spot7);
        result+=getHTML_single(spot8);
        result+=getHTML_single(spot9);
        result+=getHTML_single(spot10);
        return result;
    }
}
