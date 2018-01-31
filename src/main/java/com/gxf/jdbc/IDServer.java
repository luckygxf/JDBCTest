package com.gxf.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by 58 on 2018/1/31.
 */
public class IDServer {

    /**
     * 获取自增id
     * 通过使用mysql 自增属性
     * replace_into 放到一个事务里面
     * */
    public long getId(){
        String replaceIntoSql = "replace into tickets64(stub) values('a')";
        String selectIdSql = "SELECT LAST_INSERT_ID() as id";
        Connection connection = DBUtils.getConnection();
        long ret = 0;
        try{
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            //开始执行replace into
            PreparedStatement replacePS = connection.prepareStatement(replaceIntoSql);
            replacePS.executeUpdate();
            //开始执行select
            PreparedStatement selectPS = connection.prepareStatement(selectIdSql);
            ResultSet rs = selectPS.executeQuery();
            //获取集合
            while(rs.next()){
                ret = rs.getLong("id");
            } //while
            connection.commit();
            connection.close();
        }catch (Exception e){
            //回滚事务
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return ret;
    }

    /***
     * 获取当前数据库id
     * */
    public long getCurId(){
        String selectIdSql = "SELECT id from tickets64 order by id desc limit 1";
        Connection connection = DBUtils.getConnection();
        long ret = 0;
        try{
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
            //开始执行select
            PreparedStatement selectPS = connection.prepareStatement(selectIdSql);
            ResultSet rs = selectPS.executeQuery();
            //获取集合
            while(rs.next()){
                ret = rs.getLong("id");
            } //while
            connection.commit();
            connection.close();
        }catch (Exception e){
            //回滚事务
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return ret;
    }
}
