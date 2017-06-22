package com.greenfox.notification.model.interfaces;

import java.io.IOException;

public interface MessageQueue<T, U> {
  void push(T queue, U message) throws IOException;

  //Closeable consume(Function<T, Void> processor);

  //T consumeNext();
}
