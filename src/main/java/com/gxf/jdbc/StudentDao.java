package com.gxf.jdbc;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    /**
     * 调用存储过程
     * */
    public static void callProducer() throws SQLException {
        Connection connection = DBUtils.getConnection();
        try{
            String sql = "call pr_add(?, ?);";
            CallableStatement callableStatement = connection.prepareCall(sql);
            //设置存储过程参数 | 参数名 --> 参数
            callableStatement.setInt("a", 10);
            callableStatement.setInt("b", 13);
            ResultSet rs = callableStatement.executeQuery();
            while(rs.next()){
                System.out.println(rs.getInt("sum"));
            }
            callableStatement.close();
            connection.close();
        }catch (Exception e){
            e.printStackTrace();
        } finally {
            connection.close();
        }
    }

    /**
     * 使用Preparmenstatement访问存储过程
     * */
    public void usePreparementCallProducer(){
        Connection connection = DBUtils.getConnection();
        try{
            String sql = "call pr_add(?, ?);";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, 13);
            ps.setInt(2, 3);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                System.out.println("sum = " + rs.getInt("sum"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 测试resultmetadata
     * */
    public void userResultMetaData(){
        Connection connection = DBUtils.getConnection();
        try{
            String sql = "select * from student";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            ResultSetMetaData rsm = rs.getMetaData();
            System.out.println("rms.clomun num: " + rsm.getColumnCount());
            List<String> columNames = new ArrayList<String>();
            for(int i = 1; i <= rsm.getColumnCount(); i++){
                System.out.println(rsm.getColumnName(i));
                columNames.add(rsm.getColumnName(i));
            }
            while(rs.next()){
                for(int i = 0; i < columNames.size(); i++){
                    System.out.println(rs.getObject(columNames.get(i)));
                }
            }
            System.out.println(rsm.getColumnClassName(1));
            System.out.println(rsm.getTableName(1));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
