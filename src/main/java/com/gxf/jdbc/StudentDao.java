package com.gxf.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by 58 on 2017/12/12.
 */
public class StudentDao {

    /**
     * 插入记录
     * */
    public static void insert(Student student) throws SQLException {
        String insertSql = "insert into student(NO, name) values(?, ?);";
        Connection connection = DBUtils.getConnection();
        try{
            //插入一条记录
            PreparedStatement ps = connection.prepareStatement(insertSql);
            ps.setString(1, student.getNo());
            ps.setString(2, student.getName());
            System.out.println("insert sql: " + ps.toString());
            ps.executeUpdate();
            ps.close();
        }catch (Exception e){
            e.printStackTrace();
        } finally {
            connection.close();
        }
    }

    /**
     * 带有事务的更新插入 | 先更新在插入
     * 更新一条已有记录，插入一条已有记录
     * 主键相同事务回滚
     * */
    public static void insertWithTransaction(Student insertStudent, Student updateStudent, int level) throws SQLException {
        String insertSql = "insert into student(NO, name) values(?, ?);";
        Connection connection = DBUtils.getConnection();
        try{
            //设置手动提交
            connection.setAutoCommit(false);
            //设置数据库隔离级别 | 读未提交
            connection.setTransactionIsolation(level);
            //更新一条记录
            String upateSql = "update student set name = ? where NO = ?;";
            PreparedStatement updatePs = connection.prepareStatement(upateSql);
            updatePs.setString(1, updateStudent.getName());
            updatePs.setString(2, updateStudent.getNo());
            updatePs.executeUpdate();

            //睡眠5s
            Thread.sleep(5000);

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

    /**
     * 根据名字查询
     */
    public static Student getByName(String name) throws SQLException {
        String querySql = "select * from student where name = ?";
        Connection connection = DBUtils.getConnection();
        Student student = new Student();
        try{
            PreparedStatement updatePs = connection.prepareStatement(querySql);
            updatePs.setString(1, name);
            ResultSet rs = updatePs.executeQuery();
            while(rs.next()){
                student.setNo(rs.getString(1));
                student.setName(rs.getString(2));
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            connection.close();
        }
        return student;
    }

    /**
     * 根据no查询
     */
    public static Student getByNo(String no) throws SQLException {
        String querySql = "select * from student where NO = ?";
        Connection connection = DBUtils.getConnection();
        //设置读未提交
        connection.setAutoCommit(false);
        connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        Student student = new Student();
        try{
            PreparedStatement updatePs = connection.prepareStatement(querySql);
            updatePs.setString(1, no);
            ResultSet rs = updatePs.executeQuery();
            connection.commit();
            while(rs.next()){
                student.setNo(rs.getString(1));
                student.setName(rs.getString(2));
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            connection.close();
        }
        return student;
    }
}
