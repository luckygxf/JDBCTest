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

    @Test
    public void testInsert() throws SQLException {
        Student student = new Student("20171212", "guanxiangfei");
        studentDao.insert(student);
    }
}
