/*
 * Copyright (c) 2018 RSVP Technologies Inc. All rights reserved.
 */
package cn.rsvptech.redis;

/**
 * @author Dai Liangzhi (dlz@rsvptech.cn)
 * @date 2015-3-12
 */
public class RedisUtil {
  public static final int REDIS_TIME = 60 * 60 * 1;

  public static boolean setRedis(String key, String redis) {
    return RsvpRchatCache.getInstance().set(key, redis, REDIS_TIME);
  }

  public static boolean setRedis(String key, String redis, int expire) {
    return RsvpRchatCache.getInstance().set(key, redis, expire);
  }

  public static String getRedis(String key) {
    return RsvpRchatCache.getInstance().get(key);
  }
}