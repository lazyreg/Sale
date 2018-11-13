package cn.rsvptech.datatype;

/**
 * @author Dai.Liangzhi (dlz@rsvptech.cn)
 * @since 2018/10/22
 */
public class SalesResponse {
  private boolean saled = false;

  private int coin;

  public void setSaled(boolean saled) {
    this.saled = saled;
  }

  public boolean isSaled() {
    return saled;
  }

  public int getCoin() {
    return coin;
  }

  public void setCoin(int coin) {
    this.coin = coin;
  }
}
