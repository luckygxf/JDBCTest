package com.gxf.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by 58 on 2017/12/12.
 */
public class StudentDao {

    /**
     * 带有事务的更新插入 | 先更新在插入
     * 更新一条已有记录，插入一条已有记录
     * 主键相同事务回滚
     * */
    public static void insertWithTransaction(Student insertStudent, Student updateStudent) throws SQLException {
        String insertSql = "insert into student(NO, name) values(?, ?);";
        Connection connection = DBUtils.getConnection();
        try{
            //设置手动提交
            connection.setAutoCommit(false);
            //更新一条记录
            String upateSql = "update student set name = ? where NO = ?;";
            PreparedStatement updatePs = connection.prepareStatement(upateSql);
            updatePs.setString(1, updateStudent.getName());
            updatePs.setString(2, updateStudent.getNo());
            updatePs.executeUpdate();

            //插入一条记录
            PreparedStatement ps = connection.prepareStatement(insertSql);
            ps.setString(1, insertStudent.getNo());
            ps.setString(2, insertStudent.getName());
            System.out.println("insert sql: " + ps.toString());
            ps.executeUpdate();
            //提交事务
            connection.commit();
            ps.close();
        }catch (Exception e){
            e.printStackTrace();
            //事务回滚
            connection.rollback();
        } finally {
            connection.close();
        }
    }

    /**
     * 不带事务的更新插入 | 先更新在插入
     * 更新一条已有记录，插入一条已有记录
     * 主键相同事务依然可以更新
     * */
    public static void insertWithouotTransaction(Student insertStudent, Student updateStudent) throws SQLException {
        String insertSql = "insert into student(NO, name) values(?, ?);";
        Connection connection = DBUtils.getConnection();
        try{
            //更新一条记录
            String upateSql = "update student set name = ? where NO = ?;";
            PreparedStatement updatePs = connection.prepareStatement(upateSql);
            updatePs.setString(1, updateStudent.getName());
            updatePs.setString(2, updateStudent.getNo());
            updatePs.executeUpdate();

            //插入一条记录
            PreparedStatement ps = connection.prepareStatement(insertSql);
            ps.setString(1, insertStudent.getNo());
            ps.setString(2, insertStudent.getName());
            System.out.println("insert sql: " + ps.toString());
            ps.executeUpdate();
            ps.close();
        }catch (Exception e){
            e.printStackTrace();
            //事务回滚
        } finally {
            connection.close();
        }
    }
}
