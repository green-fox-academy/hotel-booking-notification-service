package com.greenfox.notification.service;

import com.greenfox.notification.model.classes.Data;
import com.greenfox.notification.model.classes.Link;
import com.greenfox.notification.model.classes.Unsubscription;
import com.greenfox.notification.model.classes.UnsubscriptionResponse;
import com.greenfox.notification.repository.UnsubcriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UnsubscriptionResponseService {
  private final UnsubcriptionRepository unsubcriptionRepository;


  @Autowired
  public UnsubscriptionResponseService(UnsubcriptionRepository unsubcriptionRepository) {
    this.unsubcriptionRepository = unsubcriptionRepository;
  }

  public UnsubscriptionResponse giveResponse(Data data) {
    UnsubscriptionResponse unsubscriptionResponse = new UnsubscriptionResponse();
    unsubscriptionResponse.setData(data);
    Link link = new Link("helloka");
    unsubscriptionResponse.setLinks(link);
    Unsubscription unsubscription = new Unsubscription(unsubscriptionResponse.getData().getAttributes().getEmail());
    unsubcriptionRepository.save(unsubscription);
    unsubcriptionRepository.findOne((long) 1).setEmail(unsubscriptionResponse.getData().getAttributes().getEmail());
    return unsubscriptionResponse;
  }

}
