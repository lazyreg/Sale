/*
 * Copyright (c) 2018 RSVP Technologies Inc. All rights reserved.
 */

package cn.rsvptech.db.table;

import cn.rsvptech.datatype.Customer;
import cn.rsvptech.db.DBCPConnectionFactory;
import cn.rsvptech.db.DataBaseHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public class InfoCustomerDB {

  public static List<Customer> queryCustomerList(int limit) {
    String sql = "SELECT userId,balance,status FROM t_account";
    if (limit > 0) {
      sql += "LIMIT " + limit;
    }
    return DataBaseHelper.queryEntityList(Customer.class, sql, null);
  }

  public static Customer queryCustomerById(int userId) {
    return DataBaseHelper.queryObject(Customer.class, "userId=" + userId);
  }

  public static void minusCustomerCoin(int cid, int coin) {
    String sql = "UPDATE t_account SET balance=balance-? where userId=?";
    DataBaseHelper.executeUpdate(sql, coin, cid);
  }

  public static void minusCustomerCoin(HashMap customMap) {
    String sql = "UPDATE t_account SET balance=balance-? where userId=?";

    Connection conn = DBCPConnectionFactory.getInstance().getConn();
    PreparedStatement stmt = null;
    try {
      stmt = conn.prepareStatement(sql);
      conn.setAutoCommit(false);

      Iterator map1it = customMap.entrySet().iterator();
      while (map1it.hasNext()) {
        Entry<Integer, Integer> entry = (Entry<Integer, Integer>) map1it.next();
        stmt.setInt(1, entry.getValue());
        stmt.setInt(2, entry.getKey());
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
