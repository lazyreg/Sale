/*
 * Copyright (c) 2018 RSVP Technologies Inc. All rights reserved.
 */

package cn.rsvptech.datatype;

import cn.rsvptech.db.annotation.FieldName;
import cn.rsvptech.db.annotation.TableName;

@TableName("t_goods")
public class Goods {
  @FieldName("goodsId") private int goodsId;
  @FieldName("goodsSn") private String goodsSn;
  @FieldName("goodsName") private String goodsName;
  @FieldName("shopPrice") private int shopPrice;
  @FieldName("goodsStock") private int goodsStock;

  public int getGoodsId() {
    return goodsId;
  }

  public void setGoodsId(int goodsId) {
    this.goodsId = goodsId;
  }

  public String getGoodsSn() {
    return goodsSn;
  }

  public void setGoodsSn(String goodsSn) {
    this.goodsSn = goodsSn;
  }

  public String getGoodsName() {
    return goodsName;
  }

  public void setGoodsName(String goodsName) {
    this.goodsName = goodsName;
  }

  public int getShopPrice() {
    return shopPrice;
  }

  public void setShopPrice(int shopPrice) {
    this.shopPrice = shopPrice;
  }

  public int getGoodsStock() {
    return goodsStock;
  }

  public void setGoodsStock(int goodsStock) {
    this.goodsStock = goodsStock;
  }

  @Override
  public String toString() {
    return "Goods{" + "goodsId=" + goodsId + ", goodsSn='" + goodsSn + '\'' + ", goodsName='" + goodsName + '\'' + ", shopPrice=" + shopPrice + ", goodsStock="
            + goodsStock + '}';
  }
}
