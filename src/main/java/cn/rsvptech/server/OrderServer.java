package cn.rsvptech.server;

import akka.actor.ActorRef;
import akka.pattern.Patterns;
import cn.rsvptech.actor.ActorFactory;
import cn.rsvptech.datatype.Order;
import cn.rsvptech.datatype.SalesRequest;
import cn.rsvptech.datatype.SalesResponse;
import cn.rsvptech.db.table.InfoOrderDB;
import cn.rsvptech.util.GlobalUtil;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author Dai.Liangzhi (dlz@rsvptech.cn)
 * @since 2018/10/23
 */
public class OrderServer {
  private static final int Clean_Task_Time = 10 * 1000;

  private static OrderServer mInstance;

  public static OrderServer getInstance() {
    if (mInstance == null) {
      synchronized (OrderServer.class) {
        if (mInstance == null) {
          mInstance = new OrderServer();
        }
      }
    }
    return mInstance;
  }

  private OrderServer() {
    Timer timer = new Timer();
    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        System.out.println("-----addOrder2DB----");
        ActorRef orderActor = ActorFactory.getInstance().getOrderActor();
        if (orderActor != null) {
          orderActor.tell("batch", ActorRef.noSender());
        }
      }
    }, Clean_Task_Time, Clean_Task_Time);
  }

  /**
   * 购买商品
   *
   * @param userid
   * @param goodsid
   * @param number
   * @return
   */
  public String processOrder(String userid, String goodsid, int number) {
    String response = null;

    int cid = Integer.parseInt(userid);
    int gid = Integer.parseInt(goodsid);

    SalesRequest request = new SalesRequest();
    request.setCustomId(cid);
    request.setGoodsId(gid);
    request.setNumber(number);
    boolean success = createOrder(request);
    if (success) {
      response = GlobalUtil.getSuccessResponse();
    } else {
      response = GlobalUtil.getFailResponse("purchase failure");
    }

    return response;
  }

  /**
   * 统计订单总量
   *
   * @return
   */
  public String getOrderStatistics() {
    String response = InfoOrderDB.queryOrder();
    return response;
  }

  /**
   * 订单是否创建成功
   *
   * @param request
   * @return
   */
  private Boolean createOrder(SalesRequest request) {
    int customId = request.getCustomId();
    int gId = request.getGoodsId();
    ActorRef gActorRef = ActorFactory.getInstance().getGoodActor(gId);
    if (gActorRef == null) {
      return false;
    }
    ActorRef cActorRef = ActorFactory.getInstance().getCustomActor(customId);
    if (cActorRef == null) {
      return false;
    }

    try {
      Future<Object> future1 = Patterns.ask(gActorRef, request, 1000);
      SalesResponse response1 = (SalesResponse) Await.result(future1, Duration.create(1000, TimeUnit.MILLISECONDS));
      if (response1 == null || !response1.isSaled()) {
        return false;
      }
      request.setCoin(response1.getCoin());

      Future<Object> future2 = Patterns.ask(cActorRef, request, 1000);
      SalesResponse response2 = (SalesResponse) Await.result(future2, Duration.create(1000, TimeUnit.MILLISECONDS));
      if (response2 == null || !response2.isSaled()) {
        gActorRef.tell(request.getNumber(), ActorRef.noSender());
        return false;
      }

      ActorRef orderActor = ActorFactory.getInstance().getOrderActor();
      Order order = new Order(request.getGoodsId(), customId, request.getNumber(), request.getCoin());
      orderActor.tell(order, ActorRef.noSender());
      return true;
    } catch (Exception e) {
      e.printStackTrace();
    }

    return false;
  }
}
