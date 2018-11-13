package cn.rsvptech.actor;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.pattern.Patterns;
import cn.rsvptech.datatype.SalesRequest;
import cn.rsvptech.datatype.SalesResponse;
import cn.rsvptech.redis.RedisUtil;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

/**
 * @author Dai.Liangzhi (dlz@rsvptech.cn)
 * @since 2018/10/22
 */
public class CustomActor extends AbstractActor {
  private int customid;
  private int coin;

  public CustomActor(int customid, int coin) {
    this.customid = customid;
    setCoin(coin);
    //System.out.println("Custom coin=" + coin);
  }

  @Override
  public Receive createReceive() {
    return receiveBuilder().match(SalesRequest.class, request -> {
      SalesResponse response = null;

      int coin = getCoin();
      int goodId = request.getGoodsId();
      ActorRef actorRef = ActorFactory.getInstance().getGoodActor(goodId);
      if (actorRef != null) {
        request.setCoin(coin);
        Future<Object> future = Patterns.ask(actorRef, request, 1000);
        response = (SalesResponse) Await.result(future, (Duration) Duration.create(1000, TimeUnit.MILLISECONDS));
      }
      if (response == null) {
        response = new SalesResponse();
      }
      if (response.isSaled()) {
        coin -= response.getCoin();
        setCoin(coin);
        //System.out.println("CustomActor custom=" + request.getCustomId() + " coin=" + coin);
      }

      sender().tell(response, getSelf());
    }).build();
  }

  private int getCoin() {
    int coin = this.coin;
    String coinStr = RedisUtil.getRedis("custom_" + customid + "_lazy");
    if (coinStr != null) {
      coin = Integer.parseInt(coinStr);
      this.coin = coin;
    }
    return coin;
  }

  private void setCoin(int coin) {
    this.coin = coin;
    RedisUtil.setRedis("custom_" + customid + "_lazy", coin + "");
  }
}
