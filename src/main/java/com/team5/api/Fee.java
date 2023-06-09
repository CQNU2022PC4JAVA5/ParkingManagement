package com.team5.api;

import com.team5.other.text;
import com.team5.sql.SQL;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Fee {
    private int freetime;
    private int firsttime;
    private double firstfee;
    private int secondtime;
    private double secondfee;
    public Fee(){
        getData();
    }

    public void getData(){
        SQL sql = new SQL();
        sql.getConnect();
        try {
            PreparedStatement ps = sql.conn.prepareStatement("SELECT freetime,firsttime,firstfee,secondtime,secondfee FROM fee WHERE id=1");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                this.freetime=rs.getInt("freetime");
                this.firsttime=rs.getInt("firsttime");
                this.firstfee=rs.getDouble("firstfee");
                this.secondtime=rs.getInt("secondtime");
                this.secondfee=rs.getDouble("secondfee");
            }
            rs.close();
            ps.close();
            sql.disConnect();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
    public int getFreetime() {
        return freetime;
    }

    public int getFirsttime() {
        return firsttime;
    }

    public double getFirstfee() {
        return firstfee;
    }

    public int getSecondtime() {
        return secondtime;
    }

    public double getSecondfee() {
        return secondfee;
    }

    public void setFreetime(int freetime) {
        SQL sql = new SQL();
        sql.getConnect();
        try {
            PreparedStatement ps = sql.conn.prepareStatement("update fee set freetime=? WHERE id=1;");
            ps.setInt(1, freetime);
            int rows = ps.executeUpdate();
            this.freetime=freetime;
            System.out.println("更新了" + rows + "条数据");
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        sql.disConnect();
    }
    public void setFirsttime(int firsttime) {
        SQL sql = new SQL();
        sql.getConnect();
        try {
            PreparedStatement ps = sql.conn.prepareStatement("update fee set firsttime=? WHERE id=1;");
            ps.setInt(1, firsttime);
            int rows = ps.executeUpdate();
            this.firsttime=firsttime;
            System.out.println("更新了" + rows + "条数据");
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        sql.disConnect();
    }

    public void setFirstfee(double firstfee) {
        SQL sql = new SQL();
        sql.getConnect();
        try {
            PreparedStatement ps = sql.conn.prepareStatement("update fee set firstfee=? WHERE id=1;");
            ps.setDouble(1, firstfee);
            int rows = ps.executeUpdate();
            this.firstfee=firstfee;
            System.out.println("更新了" + rows + "条数据");
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        sql.disConnect();
    }

    public void setSecondtime(int secondtime) {
        SQL sql = new SQL();
        sql.getConnect();
        try {
            PreparedStatement ps = sql.conn.prepareStatement("update fee set secondtime=? WHERE id=1;");
            ps.setInt(1, secondtime);
            int rows = ps.executeUpdate();
            this.secondtime=secondtime;
            System.out.println("更新了" + rows + "条数据");
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        sql.disConnect();
    }

    public void setSecondfee(double secondfee) {
        SQL sql = new SQL();
        sql.getConnect();
        try {
            PreparedStatement ps = sql.conn.prepareStatement("update fee set secondfee=? WHERE id=1;");
            ps.setDouble(1, secondfee);
            int rows = ps.executeUpdate();
            this.secondfee=secondfee;
            System.out.println("更新了" + rows + "条数据");
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        sql.disConnect();
    }
    public String changeHTML(String HTML){
        String result = HTML;
        result= text.getLeftString(result,"id=\"freetime\"")+"id=\"freetime\" value=\""+getFreetime()+"\""+text.getRightString(result,"id=\"freetime\"");
        result= text.getLeftString(result,"id=\"firsttime\"")+"id=\"firsttime\" value=\""+getFirsttime()+"\""+text.getRightString(result,"id=\"firsttime\"");
        result= text.getLeftString(result,"id=\"firstfee\"")+"id=\"firstfee\" value=\""+getFirstfee()+"\""+text.getRightString(result,"id=\"firstfee\"");
        result= text.getLeftString(result,"id=\"secondtime\"")+"id=\"secondtime\" value=\""+getSecondtime()+"\""+text.getRightString(result,"id=\"secondtime\"");
        result= text.getLeftString(result,"id=\"secondfee\"")+"id=\"secondfee\" value=\""+getSecondfee()+"\""+text.getRightString(result,"id=\"secondfee\"");
        return result;
    }

}
