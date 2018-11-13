/**
 * Copyright 2014 RSVP Technologies Inc. All rights reserved. GlobalUtil.java
 */
package cn.rsvptech.util;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

/**
 * @author Dai Liangzhi (dlz@cn.rsvptech.cn)
 * @date 2018-8-21
 */
public class GlobalUtil {

  /**
   * 处理web请求，返回默认回答
   */
  public static String getFailResponse() {
    JSONObject jsonObject = new JSONObject();
    int status = 0;

    try {
      jsonObject.put("status", status);
      jsonObject.put("errmsg", "Purchase failure");
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return jsonObject.toString();
  }

  public static String getFailResponse(String errmsg) {
    JSONObject jsonObject = new JSONObject();
    int status = 0;

    try {
      jsonObject.put("status", status);
      jsonObject.put("errmsg", errmsg);
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return jsonObject.toString();
  }

  public static String getSuccessResponse() {
    JSONObject jsonObject = new JSONObject();
    int status = 1;

    try {
      jsonObject.put("status", status);
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return jsonObject.toString();
  }
}
