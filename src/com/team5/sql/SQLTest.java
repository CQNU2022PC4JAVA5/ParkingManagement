package com.team5.sql;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLTest {
    public static void main(String[] args) throws SQLException {
        SQL sql=new SQL();
        sql.getConnect();
        sql.getStatement();
        ResultSet rs = sql.getResultSet("SELECT id, name, url FROM websites");

        while(rs.next()){
            // 通过字段检索
            int id  = rs.getInt("id");
            String name = rs.getString("name");
            String url = rs.getString("url");

            // 输出数据
            System.out.print("ID: " + id);
            System.out.print(", 站点名称: " + name);
            System.out.print(", 站点 URL: " + url);
            System.out.print("\n");
        }
        rs.close();
        sql.delStatement();
        sql.disConnect();
    }
}
