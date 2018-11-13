package cn.rsvptech.db;

import cn.rsvptech.db.annotation.FieldName;
import cn.rsvptech.db.annotation.TableName;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wuzhiyong on 2016/7/28
 */
public final class DataBaseHelper {
  private static final QueryRunner QUERY_RUNNER = new QueryRunner();

  public static String getTableName(Class entityClass) {
    try {
      TableName obj = (TableName) entityClass.getAnnotation(TableName.class);
      return obj.value();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return entityClass.getName();
  }

  public static String[][] getFieldNames(Class entityClass) {
    Field[] fields = entityClass.getDeclaredFields();
    String[][] resultArray = new String[fields.length][2];
    for (int i = 0, len = fields.length; i < len; i++) {
      Field field = fields[i];
      String[] arr = new String[2];
      arr[0] = field.getName();
      if (field.isAnnotationPresent(FieldName.class)) {
        FieldName fieldName = field.getAnnotation(FieldName.class);
        boolean ignore = fieldName.ignore();
        if (ignore) {
          continue;
        }
        if ("NONE".equals(fieldName.value())) {
          continue;
        } else if ("IGNORE".equals(fieldName.value())) {
          continue;
        }

        arr[1] = fieldName.value();
      } else {
        arr[1] = field.getName();
      }
      resultArray[i] = arr;
    }
    return resultArray;
  }

  public static <T> List<T> queryEntityList(Class<T> entityClass, String sql, Object... params) {
    List<T> entityList = null;
    Connection conn = null;
    try {
      conn = DBCPConnectionFactory.getInstance().getConn();
      entityList = QUERY_RUNNER.query(conn, sql, new BeanListHandler<T>(entityClass), params);
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      closeConn(conn);
    }
    return entityList;
  }

  public static <T> T queryObject(Class<T> objectClass, String queryCondition, Object... params) {
    return queryEntity(objectClass, generateSql(objectClass, queryCondition), params);
  }

  public static <T> List<T> queryObjectList(Class<T> objectClass, String queryCondition, Object... params) {
    return queryEntityList(objectClass, generateSql(objectClass, queryCondition), params);
  }

  private static String generateSql(Class objectClass, String queryCondition) {
    String[][] fieldNames = getFieldNames(objectClass);
    StringBuilder sql = new StringBuilder("select ");
    for (String[] arr : fieldNames) {
      if (arr[0] == null || arr[1] == null) {
        continue;
      }
      sql.append(arr[1]).append(" as ").append(arr[0]).append(",");
    }
    if (sql.lastIndexOf(",") == sql.length() - 1) {
      sql.replace(sql.lastIndexOf(","), sql.length(), "");
    }
    sql.append(" from ").append(getTableName(objectClass));
    if (queryCondition == null || queryCondition.isEmpty()) {
      sql.append(" where 1 = 1 ");
    } else {
      sql.append(" where ").append(queryCondition);
    }
    return sql.toString();
  }

  public static <T> T queryEntity(Class<T> entityClass, String sql, Object... params) {
    T entity = null;
    Connection conn = null;
    try {
      conn = DBCPConnectionFactory.getInstance().getConn();
      entity = QUERY_RUNNER.query(conn, sql, new BeanHandler<T>(entityClass), params);
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      closeConn(conn);
    }
    return entity;
  }

  public static int insertObject(String sql, Object... params) {
    Connection conn = null;
    int rows = 0;
    try {
      conn = DBCPConnectionFactory.getInstance().getConn();
      conn.setAutoCommit(false);
      rows = QUERY_RUNNER.update(conn, sql, params);
      conn.commit();
    } catch (SQLException e) {
      e.printStackTrace();
      try {
        conn.rollback();
      } catch (SQLException e1) {
        e1.printStackTrace();
      }
    } finally {
      closeConn(conn);
    }
    return rows;
  }

  public static <T> T queryObject(String sql, Object... params) {
    Connection conn = null;
    T result = null;
    try {
      conn = DBCPConnectionFactory.getInstance().getConn();
      result = QUERY_RUNNER.query(conn, sql, new ScalarHandler<T>(), params);
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      closeConn(conn);
    }
    return result;
  }

  /**
   * 执行更新语句
   *
   * @param sql
   * @param params
   * @return
   */
  public static int executeUpdate(String sql, Object... params) {
    int rows = 0;
    Connection conn = null;
    try {
      conn = DBCPConnectionFactory.getInstance().getConn();
      rows = QUERY_RUNNER.update(conn, sql, params);
    } catch (SQLException e) {
//            LOGGER.error("execute update failure", e);
      e.printStackTrace();
      if ("23000".equals(e.getSQLState())) {
        return 23000;
      }
    } finally {
      closeConn(conn);
    }
    return rows;
  }

  private static void closeConn(Connection conn) {
    if (conn != null) {
      try {
        conn.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }

  public static <T> T beanMapper(ResultSet rs, Class<T> clazz) {
    T t = null;
    try {
      t = clazz.newInstance();
    } catch (InstantiationException | IllegalAccessException e) {
      e.printStackTrace();
    }

    String[][] fieldNames = getFieldNames(clazz);

    Map<String, Object> values = new HashMap<>();
    for (String[] field : fieldNames) {
      try {
        values.put(field[0], rs.getObject(field[1]));
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    try {
      BeanUtils.populate(t, values);
    } catch (IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
    }
    return t;
  }
}
