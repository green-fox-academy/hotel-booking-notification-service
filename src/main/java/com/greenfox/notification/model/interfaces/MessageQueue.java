package com.greenfox.notification.model.interfaces;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public interface MessageQueue<T, U> {
  void push(HttpServletRequest request, T queue, U message) throws IOException;

  //Closeable consume(Function<T, Void> processor);

  //T consumeNext();
}
