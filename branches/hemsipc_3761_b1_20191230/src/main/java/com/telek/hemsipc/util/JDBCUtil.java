package com.telek.hemsipc.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * @Auther: wll
 * @Date: 2018/9/18 12:50
 * @Description:
 */
public class JDBCUtil {
    public static void main(String[] args) throws Exception {
        String JDBC_DRIVER = "com.mysql.jdbc.Driver";
        String DB_URL = "jdbc:h2:file:./h2/test;AUTO_SERVER=TRUE;DB_CLOSE_DELAY=-1";
        String User = "root";
        String Passwd = "telek001";
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found " + e);
        }
        Connection con = DriverManager.getConnection(DB_URL, User, Passwd);
        Statement stmt = con.createStatement();
        String query = "CREATE TABLE employees2(id INTEGER PRIMARY KEY, first_name CHAR(50), last_name CHAR(75))";

        stmt.execute(query);
        System.out.println("Employee2 table created");
        String query1 = "aLTER TABLE employees2 ADD address CHAR(100) ";
        String query2 = "ALTER TABLE employees2 DROP COLUMN last_name";

        stmt.execute(query1);
        stmt.execute(query2);
        System.out.println("Address column added to the table & last_name column removed from the table");

//        String query3 = "drop table employees2";
//        stmt.execute(query3);
//        System.out.println("Employees table removed");
    }
}
