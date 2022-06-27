package com.example.shop.dao;


import com.example.shop.utils.JdbcUtilsByDruid;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class BasicDao<T> {

    protected QueryRunner qr = new QueryRunner();

    public int update(String sql, Object...params){
        Connection connection = null;
        try {
            connection = JdbcUtilsByDruid.getConnection();
            int update = qr.update(connection, sql, params);
            return update;
        } catch (SQLException e) {
            throw new RuntimeException();
        }finally {
            JdbcUtilsByDruid.close(null, null, connection);
        }
    }

    public List<T> queryMultis(String sql, Class<T> cls, Object...params){
        Connection connection = null;
        try {
            connection = JdbcUtilsByDruid.getConnection();
            return qr.query(connection, sql, new BeanListHandler<T>(cls), params);
        } catch (SQLException e) {
            throw new RuntimeException();
        }finally {
            JdbcUtilsByDruid.close(null, null, connection);
        }
    }

    public T querySingle(String sql, Class<T> clazz, Object... parameters) {
        Connection connection = null;
        try {
            connection = JdbcUtilsByDruid.getConnection();
            return qr.query(connection, sql, new BeanHandler<T>(clazz), parameters);
        } catch (SQLException e) {
            throw new RuntimeException(e); //将编译异常->运行异常 ,抛出
        } finally {
            JdbcUtilsByDruid.close(null, null, connection);
        }
    }
    //查询单行单列的方法,即返回单值的方法
    public Object queryScalar(String sql, Object... parameters) {
        Connection connection = null;
        try{
            connection = JdbcUtilsByDruid.getConnection();
            return qr.query(connection, sql, new ScalarHandler(), parameters);
        } catch (SQLException e) {
            throw new RuntimeException(e); //将编译异常->运行异常 ,抛出
        } finally {
            JdbcUtilsByDruid.close(null, null, connection);
        }
    }

    //获取单列多行
    public List<Object> queryTypes(String sql, String columnName, Object... params){
        Connection connection = null;
        try {
            connection = JdbcUtilsByDruid.getConnection();
            return qr.query(connection, sql, new ColumnListHandler(columnName), params);
        } catch (SQLException e) {
            throw new RuntimeException();
        }finally {
            JdbcUtilsByDruid.close(null, null, connection);
        }
    }
}
