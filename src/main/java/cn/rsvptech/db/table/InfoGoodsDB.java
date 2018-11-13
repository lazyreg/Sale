/*
 * Copyright (c) 2018 RSVP Technologies Inc. All rights reserved.
 */

package cn.rsvptech.db.table;

import cn.rsvptech.datatype.Goods;
import cn.rsvptech.db.DBCPConnectionFactory;
import cn.rsvptech.db.DataBaseHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public class InfoGoodsDB {
  public static List<Goods> queryGoodsList(int limit) {
    String sql = "SELECT goodsId,goodsSn,goodsName,shopPrice,goodsStock FROM t_goods";
    if (limit > 0) {
      sql += "LIMIT " + limit;
    }
    return DataBaseHelper.queryEntityList(Goods.class, sql, null);
  }

  public static Goods queryGoodsById(int gid) {
    return DataBaseHelper.queryObject(Goods.class, "goodsId=" + gid);
  }

  public static void minusGoodsStock(int gid, int number) {
    String sql = "UPDATE t_goods SET goodsStock=goodsStock-? where goodsId=?";
    DataBaseHelper.executeUpdate(sql, number, gid);
  }

  public static void minusGoodsStock(HashMap goodMap) {
    String sql = "UPDATE t_goods SET goodsStock=goodsStock-? where goodsId=?";

    Connection conn = DBCPConnectionFactory.getInstance().getConn();
    PreparedStatement stmt = null;
    try {
      stmt = conn.prepareStatement(sql);
      conn.setAutoCommit(false);

      Iterator map1it = goodMap.entrySet().iterator();
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
