package com.greenfox.notification.model.interfaces;

import java.io.IOException;

public interface ConsumerQueue<T, U> {

  void push(String request, T queue, U message) throws IOException;

}
