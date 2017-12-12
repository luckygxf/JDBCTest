package com.gxf.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by 58 on 2017/12/12.
 */
public class StudentDao {

    public static void insert(Student student)  {
        String insertSql = "insert into student(NO, name) values(?, ?);";
        Connection connection = DBUtils.getConnection();
        try{
            //设置手动提交
            connection.setAutoCommit(false);
            //更新一条记录


            //插入一条记录
            PreparedStatement ps = connection.prepareStatement(insertSql);
            ps.setString(1, student.getNo());
            ps.setString(2, student.getName());
            System.out.println("insert sql: " + ps.toString());
            ps.executeUpdate();
            //提交事务
            connection.commit();
            ps.close();
            connection.close();
        }catch (Exception e){

        }
    }

    public static Student findByName(String name){
        return null;
    }
}
