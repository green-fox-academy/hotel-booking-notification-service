package com.greenfox.notification.controller;

import com.greenfox.notification.model.classes.Data;
import com.greenfox.notification.model.classes.UnsubscriptionResponse;
import com.greenfox.notification.service.UnsubscriptionResponseService;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UnsubscriptionController {

  private final UnsubscriptionResponseService unsubscriptionResponseService;

  @Autowired
  public UnsubscriptionController(UnsubscriptionResponseService unsubscriptionResponseService) {
    this.unsubscriptionResponseService = unsubscriptionResponseService;
  }

  @PostMapping(value = "/unsubscription")
  public UnsubscriptionResponse giveResponse(@RequestBody Data data,
      HttpServletRequest httpServletRequest) throws Exception{

//    System.out.println(unsubscriptionResponse.getData().getType());
//    System.out.println(unsubscriptionResponse.getData().getAttributes().getEmail());
//    System.out.println(unsubscriptionResponse.getData().getAttributes().getCreated_at());
    return unsubscriptionResponseService.giveResponse(data);
  }

}
