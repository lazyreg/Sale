/*
 * Copyright (c) 2018 RSVP Technologies Inc. All rights reserved.
 */
package cn.rsvptech.redis;

import java.util.UUID;

public class RsvpRchatCache {
  private JedisDB cacheDB;
  private String url;
  private int port;
  private int timeout;
  private String password;

  private static RsvpRchatCache cache = new RsvpRchatCache();

  public RsvpRchatCache(String url, int port, int timeout, String password) {
    init(url, port, timeout, password);
    checkStatus();
  }

  private RsvpRchatCache() {
    url = "192.168.1.116";//CacheConfig.getInstance().getProperty(CacheConfig.RSVP_CACHE_URL);
    port = 6379;//CacheConfig.getInstance().getInt(CacheConfig.RSVP_CACHE_PORT, 6379);
    timeout = 200;//CacheConfig.getInstance().getInt(CacheConfig.RSVP_CACHE_TIMEOUT, 2000);
    password = null;//CacheConfig.getInstance().getProperty(CacheConfig.RSVP_CACHE_PASSWORD);
    init(url, port, timeout, password);
  }

  private void init(String url, int port, int timeout, String password) {
    try {
      if (password == null) {
        cacheDB = new JedisDB(url, port, timeout);
      } else {
        cacheDB = new JedisDB(url, port, timeout, password);
      }
    } catch (Throwable e) {
      e.printStackTrace();
    }
    checkStatus();
  }

  private void checkStatus() {
    System.out.println("\n------------- Redis Cache Status ----------");

    System.out.println("Url    :\t" + url);
    System.out.println("port   :\t" + port);
    System.out.println("Timeout:\t" + timeout);

    String key = UUID.randomUUID().toString();
    String value = " reign over the world";
    set(key, value, 60);
    String actual = get(key);

    String result = "Fail";
    if (value.equals(actual)) {
      result = "OK";
    }
    System.out.println("Status :\t" + result);
    System.out.println("-------------------------------------\n");
    System.out.println();
  }

  public boolean set(String key, String value, int expireTimeInSeconds) {
    return cacheDB.set(key, value, expireTimeInSeconds);
  }

  public String get(String key) {
    return cacheDB.get(key);
  }

  public static RsvpRchatCache getInstance() {
    return cache;
  }

  public static void main(String[] args) {
    System.out.println("\n------------- Redis Cache Status ----------");

    String key = UUID.randomUUID().toString();
    String value = " reign over the world!!";
    getInstance().set(key, value, 60);
    String actual = getInstance().get(key);

    String result = "Fail";
    if (value.equals(actual)) {
      result = "OK";
    }
    System.out.println("Status :\t" + result);
    System.out.println("-------------------------------------\n");
    System.out.println();
  }
}
