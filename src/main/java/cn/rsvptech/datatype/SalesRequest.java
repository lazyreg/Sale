package cn.rsvptech.datatype;

/**
 * @author Dai.Liangzhi (dlz@rsvptech.cn)
 * @since 2018/10/22
 */
public class SalesRequest {
  private int customId;
  private int coin;

  private int goodsId;
  private int number;

  public int getCustomId() {
    return customId;
  }

  public void setCustomId(int customId) {
    this.customId = customId;
  }

  public int getCoin() {
    return coin;
  }

  public void setCoin(int coin) {
    this.coin = coin;
  }

  public int getGoodsId() {
    return goodsId;
  }

  public void setGoodsId(int goodsId) {
    this.goodsId = goodsId;
  }

  public int getNumber() {
    return number;
  }

  public void setNumber(int number) {
    this.number = number;
  }
}
