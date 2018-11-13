/*
 * Copyright (c) 2018 RSVP Technologies Inc. All rights reserved.
 */

package cn.rsvptech.datatype;

public class Order {
  private int gid;
  private int cid;
  private int number;
  private int amount;

  public Order(int gid, int cid, int number, int amount) {
    this.gid = gid;
    this.cid = cid;
    this.number = number;
    this.amount = amount;
  }

  public int getGid() {
    return gid;
  }

  public int getCid() {
    return cid;
  }

  public int getNumber() {
    return number;
  }

  public int getAmount() {
    return amount;
  }
}
