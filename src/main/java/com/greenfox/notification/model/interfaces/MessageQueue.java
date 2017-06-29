package com.greenfox.notification.model.interfaces;

import java.io.IOException;

public interface MessageQueue<T, U> {

  void push(String request, T queue, U message) throws IOException;

}
