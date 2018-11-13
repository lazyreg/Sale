/**
 * Copyright 2018 RSVP Technologies Inc. All rights reserved.
 */
package cn.rsvptech.util;

import cn.rsvptech.actor.ActorFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * @author Dai.Liangzhi (dlz@cn.rsvptech.cn)
 * @since 2018/8/21
 */
public class TomcatListener implements ServletContextListener {
  private int limit = -1;

  @Override
  public void contextInitialized(ServletContextEvent servletContextEvent) {
    ActorFactory.getInstance();
  }

  @Override
  public void contextDestroyed(ServletContextEvent servletContextEvent) {

  }
}
