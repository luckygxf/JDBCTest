package com.gxf.jdbc;

import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by 58 on 2017/12/12.
 */
public class TestAPI {
    private StudentDao studentDao;

    @Before
    public void init(){
        studentDao = new StudentDao();
    }

    //测试有事务的
    @Test
    public void testInsertWithTransaction() throws SQLException {
        Student updateStudent = new Student("20171212", "guanxiangfei111");
        Student insertStudent = new Student("20171212", "guanxiangfei");
        studentDao.insertWithTransaction(insertStudent, updateStudent, Connection.TRANSACTION_READ_UNCOMMITTED);
    }

    //测试不带事务的
    @Test
    public void testInsertWithoutTransaction() throws SQLException {
        Student updateStudent = new Student("20171212", "guanxiangfei111");
        Student insertStudent = new Student("20171212", "guanxiangfei");
        studentDao.insertWithouotTransaction(insertStudent, updateStudent);
    }

    //测试插入一条记录
    @Test
    public void testInsert() throws SQLException {
        Student insertStudent = new Student("20171212", "guanxiangfei");
        studentDao.insert(insertStudent);
    }

    //测试查询
    @Test
    public void testGet() throws SQLException {
        String name = "guanxiangfei";
        Student student = studentDao.getByName(name);
        System.out.println(student);
    }

    //测试数据库隔离级别
    @Test
    public void testTransactionLevel() throws Exception {
        //1. 读未提交
        //第一个事务 更新后10秒 执行插入
        //第二个sleep 1s后读取
        Thread t = new Thread(new Runnable() {
            public void run() {
                Student updateStudent = new Student("20171212", "guanxiangfei111");
                Student insertStudent = new Student("20171212", "guanxiangfei");
                try {
                    studentDao.insertWithTransaction(insertStudent, updateStudent, Connection.TRANSACTION_READ_UNCOMMITTED);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();

        Thread.sleep(1000);
        Student student = studentDao.getByNo("20171212");
        System.out.println(student);
        Thread.sleep(6000);
        student = studentDao.getByNo("20171212");
        System.out.println(student);
    }

    //使用callablestatment访问数据库
    @Test
    public void testCallable() throws SQLException {
        studentDao.callProducer();
    }

    //使用preparementstatment访问数据库
    @Test
    public void testUsePreparementCallProducer(){
        studentDao.usePreparementCallProducer();
    }

}
