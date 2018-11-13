/*
 * Copyright (c) 2018 RSVP Technologies Inc. All rights reserved.
 */

package cn.rsvptech.actor;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import cn.rsvptech.datatype.Customer;
import cn.rsvptech.datatype.Goods;
import cn.rsvptech.db.table.InfoCustomerDB;
import cn.rsvptech.db.table.InfoGoodsDB;
import cn.rsvptech.redis.RsvpRchatCache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Kun Xiong (kun@rsvptech.ca)
 * @since 25/05/18
 */
public class ActorFactory {
  private Map<Integer, ActorRef> staticGoods;
  private Map<Integer, ActorRef> staticCustoms;
  private ActorRef orderActor;
  private ActorSystem system;

  private static ActorFactory mInstance;

  public static ActorFactory getInstance() {
    if (mInstance == null) {
      synchronized (ActorFactory.class) {
        if (mInstance == null) {
          mInstance = new ActorFactory();
        }
      }
    }
    return mInstance;
  }

  private ActorFactory() {
    try {
      system = ActorSystem.create("bigbang");

      staticGoods = new HashMap<>();
      List<Goods> goodsList = InfoGoodsDB.queryGoodsList(-1);
      for (Goods goods : goodsList) {
        int gid = goods.getGoodsId();
        ActorRef actorRef = system.actorOf(Props.create(GoodsActor.class, new Object[] {
                gid, goods.getGoodsName(), goods.getShopPrice(), goods.getGoodsStock()
        }), "lazy_good_" + gid);
        staticGoods.put(goods.getGoodsId(), actorRef);
      }

      staticCustoms = new HashMap<>();
      List<Customer> customerList = InfoCustomerDB.queryCustomerList(-1);
      for (Customer customer : customerList) {
        int cid = customer.getUserId();
        ActorRef actorRef = system.actorOf(Props.create(CustomActor.class, new Object[] { cid, customer.getBalance() }), "lazy_custom_" + cid);
        staticCustoms.put(customer.getUserId(), actorRef);
      }

      orderActor = system.actorOf(Props.create(OrderActor.class), "order");

      RsvpRchatCache.getInstance();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public ActorRef getGoodActor(int goodId) {
    return staticGoods.get(goodId);
  }

  public ActorRef getCustomActor(int customId) {
    return staticCustoms.get(customId);
  }

  public ActorRef getOrderActor() {
    return orderActor;
  }
}
