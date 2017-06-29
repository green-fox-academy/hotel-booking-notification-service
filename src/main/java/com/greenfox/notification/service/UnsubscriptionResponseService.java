package com.greenfox.notification.service;

import com.greenfox.notification.model.classes.unsubscription.Link;
import com.greenfox.notification.model.classes.unsubscription.UnsubscribeAttribute;
import com.greenfox.notification.model.classes.unsubscription.UnsubscribeData;
import com.greenfox.notification.model.classes.unsubscription.UnsubscribeInput;
import com.greenfox.notification.model.classes.unsubscription.Unsubscription;
import com.greenfox.notification.repository.UnsubscribeAttributeRepository;
import com.greenfox.notification.repository.UnsubscribeDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UnsubscriptionResponseService {
  private final SimpleDateService simpleDateService;
  private final UnsubscribeAttributeRepository unsubscribeAttributeRepository;
  private final UnsubscribeDataRepository unsubscribeDataRepository;
  private Link link;
  private Unsubscription unsubscription;

  @Autowired
  public UnsubscriptionResponseService(SimpleDateService simpleDateService,
      UnsubscribeAttributeRepository unsubscribeAttributeRepository,
      UnsubscribeDataRepository unsubscribeDataRepository) {
    this.simpleDateService = simpleDateService;
    this.unsubscribeAttributeRepository = unsubscribeAttributeRepository;
    this.unsubscribeDataRepository = unsubscribeDataRepository;
    this.link = new Link();
  }

  public Unsubscription letUsersUnsubscribe(UnsubscribeInput unsubscribeInput) {
    UnsubscribeData unsubscribeData = unsubscribeInput.getData();
    UnsubscribeAttribute unsubscribeAttribute = unsubscribeData.getAttributes();
    if (unsubscribeAttribute.getCreatedAt() == null) {
      unsubscribeAttribute.setCreatedAt(simpleDateService.getSimpleDateFormat());
    }
    unsubscribeAttributeRepository.save(unsubscribeAttribute);
    unsubscribeDataRepository.save(unsubscribeData);
    link.setSelf(System.getenv("HOSTNAME") + "/unsubscriptions/" + unsubscribeData.getId());
    unsubscription = new Unsubscription(link, unsubscribeData);
    return unsubscription;
  }
}
