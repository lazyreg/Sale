/**
 * Copyright 2014 RSVP Technologies Inc. All rights reserved. DBCPConnectionFactory.java
 */
package cn.rsvptech.db;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Dai Liangzhi (dlz@cn.rsvptech.cn)
 * @date 2015-3-12
 */
public class DBCPConnectionFactory {
  private String driverName = "com.mysql.jdbc.Driver";

  private String url = "jdbc:mysql://192.168.1.116:3306/mytestshop?useUnicode=true&characterEncoding=utf8";

  private String userName = "t_user";

  private String password = "rsvptuser";

  private int initialSize = 20;

  private int maxActive = 100;

  private int maxIdle = 50;

  private int maxWait = 1000;

  private BasicDataSource dataSource = null;

  public static DBCPConnectionFactory mInstance = null;

  public static DBCPConnectionFactory getInstance() {
    if (mInstance == null) {
      synchronized (DBCPConnectionFactory.class) {
        if (mInstance == null) {
          mInstance = new DBCPConnectionFactory();
        }
      }
    }

    return mInstance;
  }

  private DBCPConnectionFactory() {
    BasicDataSource ds = new BasicDataSource();
    ds.setDriverClassName(driverName);
    ds.setUrl(url);
    ds.setUsername(userName);
    ds.setPassword(password);

    ds.setInitialSize(initialSize); // 初始的连接数；
    ds.setMaxTotal(maxActive);
    //ds.set(maxActive);
    ds.setMaxIdle(maxIdle);
    ds.setMaxWaitMillis(maxWait);

    ds.setRemoveAbandonedTimeout(180); // 超过时间(s)限制，回收没有用(废弃)的连接
    ds.setRemoveAbandonedOnBorrow(true);
    // ds.setRemoveAbandoned(true); // 超过removeAbandonedTimeout时间后，是否进 行没用连接（废弃）的回收
    ds.setTestOnBorrow(true);// 调取连接时检查有效性
    ds.setTestOnReturn(true);
    ds.setTestWhileIdle(true);
    ds.setValidationQuery("select 1");// 验证连接有效性的方式，这步不能省
    ds.setTimeBetweenEvictionRunsMillis(1000 * 60 * 30); // 检查无效连接的时间间隔 设为30分钟

    dataSource = ds;
  }

  /**
   * 获得数据库连接
   *
   * @return
   */
  public Connection getConn() {
    try {
      return dataSource.getConnection();
    } catch (SQLException e) {
      int active = dataSource.getNumActive();
      int idle = dataSource.getNumIdle();
      System.out.println("---------------------------------------------------");
      System.out.println("------Active=" + active + ", Idle=" + idle + "-----");
      System.out.println("-----------------获得连接出错！---------------------");
      System.out.println("---------------------------------------------------");
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 关闭连接
   */
  public static void closeCon(ResultSet rs, PreparedStatement ps, Connection con) {
    if (rs != null) {
      try {
        rs.close();
      } catch (Exception e) {
        System.out.println("关闭结果集ResultSet异常！" + e.getMessage());
      }
    }
    if (ps != null) {
      try {
        ps.close();
      } catch (Exception e) {
        System.out.println("预编译SQL语句对象PreparedStatement关闭异常！" + e.getMessage());
      }
    }
    if (con != null) {
      try {
        con.close();
      } catch (Exception e) {
        System.out.println("关闭连接对象Connection异常！" + e.getMessage());
      }
    }
  }

  /**
   * 关闭数据库
   */
  public void shutdownDataSource() throws SQLException {
    BasicDataSource bds = (BasicDataSource) dataSource;
    bds.close();
  }
}
