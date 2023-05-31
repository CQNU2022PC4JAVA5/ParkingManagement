package com.team5.api;

import com.team5.sql.SQL;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Login {
    public static boolean login(String account,String password){
        SQL sql=new SQL();
        sql.getConnect();
        sql.getStatement();
        ResultSet rs = sql.getResultSet("SELECT account, password FROM account");

        try {
            while (rs.next()) {
                String sql_account = rs.getString("account");
                String sql_password = rs.getString("password");

                //System.out.print(", 账户: " + sql_account);
                //System.out.print(", 密码（MD5）: " + sql_password);
                if(sql_account.equals(account)){
                    if(sql_password.equals(password)){
                        return true;
                    }else{
                        return false;
                    }
                }else{
                    continue;
                }
                //System.out.print("\n");
            }
            rs.close();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        sql.delStatement();
        sql.disConnect();
        return false;
    }
}
