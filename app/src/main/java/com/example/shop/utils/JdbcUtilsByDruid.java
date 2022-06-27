package com.example.shop.utils;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.sql.DataSource;

public class JdbcUtilsByDruid {

    private static DataSource ds;

    //在静态代码块完成 ds初始化
    static {

        try {
            Properties pro = new Properties();
            pro.setProperty("driverClassName","com.mysql.cj.jdbc.Driver");
            pro.setProperty("url","jdbc:mysql:0// 192.168.198.1:3306/e-shop?serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true&autoReconnect=true&useUnicode=true&characterEncoding=utf8");
            pro.setProperty("username","root");
            pro.setProperty("password","origin");
            pro.setProperty("initialSize","5");
            pro.setProperty("initialSize","5");
            pro.setProperty("maxActive","10");
            pro.setProperty("maxWait","3000");
            ds = DruidDataSourceFactory.createDataSource(pro);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //编写getConnection方法
    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    //关闭连接, 老师再次强调： 在数据库连接池技术中，close 不是真的断掉连接
    //而是把使用的Connection对象放回连接池
    public static void close(ResultSet resultSet, Statement statement, Connection connection) {

        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
