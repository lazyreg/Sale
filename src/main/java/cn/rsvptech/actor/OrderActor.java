package cn.rsvptech.actor;

import akka.actor.AbstractActor;
import cn.rsvptech.datatype.Order;
import cn.rsvptech.db.table.InfoCustomerDB;
import cn.rsvptech.db.table.InfoGoodsDB;
import cn.rsvptech.db.table.InfoOrderDB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Dai.Liangzhi (dlz@rsvptech.cn)
 * @since 2018/10/22
 */
public class OrderActor extends AbstractActor {
  private ConcurrentLinkedQueue queue = new ConcurrentLinkedQueue();

  @Override
  public Receive createReceive() {
    return receiveBuilder().match(Order.class, msg -> {
      queue.offer(msg);
    }).match(String.class, msg -> {
      addOrder2DB();
    }).build();
  }

  private synchronized void addOrder2DB() {
    int length = queue.size();
    if (length == 0) {
      return;
    }

    HashMap<Integer, Integer> goodMap = new HashMap<>();
    HashMap<Integer, Integer> customMap = new HashMap<>();
    while (length > 0) {
      List<Order> list = new ArrayList<>();
      int number = length < 1000 ? length : 1000;
      for (int i = 0; i < number; i++) {
        Order order = (Order) queue.poll();
        list.add(order);

        int gid = order.getGid();
        if (goodMap.containsKey(gid)) {
          int saled = goodMap.get(gid);
          goodMap.put(gid, saled + order.getNumber());
        } else {
          goodMap.put(gid, order.getNumber());
        }

        int cid = order.getCid();
        if (customMap.containsKey(cid)) {
          int amount = customMap.get(cid);
          customMap.put(cid, amount + order.getAmount());
        } else {
          customMap.put(cid, order.getAmount());
        }
      }
      InfoOrderDB.addOrder2DB(list);

      length -= number;
    }

    if (goodMap.size() > 0) {
      InfoGoodsDB.minusGoodsStock(goodMap);
    }
    if (customMap.size() > 0) {
      InfoCustomerDB.minusCustomerCoin(customMap);
    }
  }
}
