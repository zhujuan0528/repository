package com.briup.environment.gui;

import com.briup.environment.bean.UserBean;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserImpl implements User {
    private static String loginSql="SELECT * FROM TB_USER WHERE username=? and password=?";
    private static String searchByNameSql="SELECT* FROM TB_USER WHERE username=?";
    private static String registerSql="insert into TB_USER values(user_seq.nextval,?,?)";
    static {

    }
    @Override
    public boolean login(String username, String pwd) {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:XE", "envir", "envir");
            PreparedStatement ps = connection.prepareStatement(loginSql);
            ps.setString(1,username);
            ps.setString(2,pwd);
            ResultSet resultSet = ps.executeQuery();
            if(resultSet.next()){
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean searchByName(String username) {
        return false;
    }

    @Override
    public boolean register(String username, String pwd) {
        return false;
    }

}
