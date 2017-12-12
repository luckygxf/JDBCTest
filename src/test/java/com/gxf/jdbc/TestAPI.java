package com.gxf.jdbc;

import org.junit.Before;
import org.junit.Test;

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
        studentDao.insertWithTransaction(insertStudent, updateStudent);
    }

    //测试不带事务的
    @Test
    public void testInsertWithoutTransaction() throws SQLException {
        Student updateStudent = new Student("20171212", "guanxiangfei111");
        Student insertStudent = new Student("20171212", "guanxiangfei");
        studentDao.insertWithouotTransaction(insertStudent, updateStudent);
    }
}
