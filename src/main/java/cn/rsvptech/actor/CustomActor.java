package cn.rsvptech.actor;

import akka.actor.AbstractActor;
import cn.rsvptech.datatype.SalesRequest;
import cn.rsvptech.datatype.SalesResponse;
import cn.rsvptech.redis.RedisUtil;

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
      SalesResponse response = new SalesResponse();

      int coin = getCoin();
      int saleCoin = request.getCoin();
      if (saleCoin <= coin) {
        coin -= saleCoin;
        setCoin(coin);
        response.setSaled(true);
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
