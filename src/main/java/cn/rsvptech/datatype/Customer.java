/*
 * Copyright (c) 2018 RSVP Technologies Inc. All rights reserved.
 */

package cn.rsvptech.datatype;

import cn.rsvptech.db.annotation.FieldName;
import cn.rsvptech.db.annotation.TableName;

@TableName("t_account")
public class Customer {
  @FieldName("userId") private int userId;
  @FieldName("balance") private int balance;
  @FieldName("status") private int status;

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public int getBalance() {
    return balance;
  }

  public void setBalance(int balance) {
    this.balance = balance;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }
}
