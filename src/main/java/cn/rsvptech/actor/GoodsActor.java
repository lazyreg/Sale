package cn.rsvptech.actor;

import akka.actor.AbstractActor;
import cn.rsvptech.datatype.SalesRequest;
import cn.rsvptech.datatype.SalesResponse;
import cn.rsvptech.redis.RedisUtil;

/**
 * @author Dai.Liangzhi (dlz@rsvptech.cn)
 * @since 2018/10/22
 */
public class GoodsActor extends AbstractActor {
  private int goodid;
  private String name;
  private int price;
  private int stock;

  public GoodsActor(int goodid, String name, int price, int stock) {
    this.goodid = goodid;
    this.name = name;
    this.price = price;
    setStock(stock);
    //System.out.println("Good name=" + name + "  stock=" + stock);
  }

  @Override
  public Receive createReceive() {
    return receiveBuilder().match(SalesRequest.class, msg -> {
      SalesResponse response = new SalesResponse();

      int stock = getStock();
      int numbers = msg.getNumber();
      int coin = numbers * price;
      if (numbers <= stock && msg.getCoin() >= coin) {
        stock -= numbers;
        setStock(stock);
        //System.out.println("Good name=" + name + " stock=" + stock);

        response.setSaled(true);
        response.setCoin(coin);
      }

      sender().tell(response, getSelf());
    }).build();
  }

  private int getStock() {
    int stock = this.stock;
    String stockStr = RedisUtil.getRedis("gooid_" + goodid + "_lazy");
    if (stockStr != null) {
      stock = Integer.parseInt(stockStr);
      this.stock = stock;
    }
    return stock;
  }

  private void setStock(int stock) {
    this.stock = stock;
    RedisUtil.setRedis("gooid_" + goodid + "_lazy", stock + "");
  }
}
