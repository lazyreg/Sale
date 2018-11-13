/*
 * Copyright (c) 2018 RSVP Technologies Inc. All rights reserved.
 */

package cn.rsvptech.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisDB {

  private JedisPool pool;

  public JedisDB(String ip, int port, int timeout) {
    pool = new JedisPool(createPoolConfig(), ip, port, timeout);
  }

  public JedisDB(String ip, int port, int timeout, String password) {
    pool = new JedisPool(createPoolConfig(), ip, port, timeout, password);
  }

  public static JedisPoolConfig createPoolConfig() {
    JedisPoolConfig config = new JedisPoolConfig();
    config.setMaxTotal(500);
    config.setMaxIdle(10);
    config.setMaxWaitMillis(10000L);
    config.setTestOnBorrow(true);

    return config;
  }

  public synchronized boolean set(String key, String value, int expireTime) {
    if (value == null || key == null) {
      return false;
    }
    Jedis jedis = null;

    try {
      jedis = (Jedis) pool.getResource();
      jedis.set(key, value);
      if (expireTime > 0) {
        jedis.expire(key, expireTime);
      }
      return true;
    } catch (Exception e) {
      System.err.println("Redis SET error : " + e.toString());
    } finally {
      if (jedis != null) {
        jedis.close();
      }
    }
    return false;
  }

  public String get(String key) {
    String value = null;
    if (key == null) {
      return null;
    }
    Jedis jedis = null;
    try {
      jedis = (Jedis) pool.getResource();
      value = jedis.get(key);
    } catch (Exception e) {
      System.err.println("Redis GET error : " + e.toString());
    } finally {
      if (jedis != null) {
        jedis.close();
      }
    }
    return value;
  }
}
