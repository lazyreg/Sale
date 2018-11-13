/*
 * Copyright (c) 2018 RSVP Technologies Inc. All rights reserved.
 */

package cn.rsvptech.db.table;

import cn.rsvptech.datatype.Order;
import cn.rsvptech.db.DBCPConnectionFactory;
import cn.rsvptech.db.DataBaseHelper;
import com.alibaba.fastjson.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class InfoOrderDB {

  public static String queryOrder() {
    String result = null;
    int amountCount = 0;
    int orderCount = 0;
    int userCount = 0;
    int salesCount = 0;

    String sql = "select SUM(amount) as amountCount, count(*) as orderCount, count(DISTINCT userId) as userCount, SUM(Number) as salesCount from t_orders";
    Connection conn = DBCPConnectionFactory.getInstance().getConn();
    PreparedStatement stmt = null;
    ResultSet set = null;
    try {
      stmt = conn.prepareStatement(sql);
      set = stmt.executeQuery();
      if (set.next()) {
        JSONObject json = new JSONObject();
        amountCount = set.getInt("amountCount");
        orderCount = set.getInt("orderCount");
        userCount = set.getInt("userCount");
        salesCount = set.getInt("salesCount");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      DBCPConnectionFactory.closeCon(set, stmt, conn);
    }

    JSONObject json = new JSONObject();
    json.put("amountCount", amountCount);
    json.put("orderCount", orderCount);
    json.put("userCount", userCount);
    json.put("salesCount", salesCount);
    result = json.toString();

    return result;
  }

  public static void addOrder2DB(int gid, int cid, int number, int amount) {
    String sql = "INSERT INTO t_orders (userId,goodsId,Number,amount) VALUES (?,?,?,?)";
    DataBaseHelper.insertObject(sql, cid, gid, number, amount);
  }

  public static void addOrder2DB(List<Order> orders) {
    String sql = "INSERT INTO t_orders (userId,goodsId,Number,amount) VALUES (?,?,?,?)";

    Connection conn = DBCPConnectionFactory.getInstance().getConn();
    PreparedStatement stmt = null;
    try {
      stmt = conn.prepareStatement(sql);
      conn.setAutoCommit(false);
      for (Order order : orders) {
        stmt.setInt(1, order.getCid());
        stmt.setInt(2, order.getGid());
        stmt.setInt(3, order.getNumber());
        stmt.setInt(4, order.getAmount());
        stmt.addBatch();
      }
      stmt.executeBatch();
      conn.commit();
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      DBCPConnectionFactory.closeCon(null, stmt, conn);
    }
  }
}
