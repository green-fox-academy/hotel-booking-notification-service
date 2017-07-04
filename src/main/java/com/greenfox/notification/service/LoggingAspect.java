package com.greenfox.notification.service;

import com.greenfox.notification.model.classes.heartbeat.Data;
import com.greenfox.notification.model.interfaces.Response;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
  private final Log log;

  @Autowired
  public LoggingAspect(Log log) {
    this.log = log;
  }

  @AfterReturning(
          pointcut = "execution(* com.greenfox.notification.controller.HeartbeatController.getHeartbeats(..))",
          returning = "retVal")
  public void logInfoHeartbeat(JoinPoint joinPoint, Response retVal) {
    log.info("/heartbeat " + joinPoint.getSignature().getName() + " method", retVal.toString());
  }

  @AfterThrowing(
          pointcut = "execution(* com.greenfox.notification.controller.HeartbeatController.getHeartbeats(..))",
          throwing = "error")
  public void logErrorHeartbeat(JoinPoint joinPoint, Exception error) {
    log.error("/heartbeat " + joinPoint.getSignature().getName() + " method", error.getMessage());
  }

  @AfterReturning(
          pointcut = "execution(* com.greenfox.notification.controller.RegistrationConfirmationController.registration(..))",
          returning = "retVal")
  public void logInfoRegistration(JoinPoint joinPoint, Data retVal) {
    log.info("/email/registration", joinPoint.getSignature().getName() + " method " + retVal.toString());
  }

  @AfterThrowing(
          pointcut = "execution(* com.greenfox.notification.controller.HeartbeatController.getHeartbeats(..))",
          throwing = "error")
  public void logErrorRegistration(JoinPoint joinPoint, Exception error) {
    log.error("/email/registration " + joinPoint.getSignature().getName() + " method", error.getMessage());
  }

  @AfterReturning(
      pointcut = "execution(* com.greenfox.notification.controller.UnsubscribeController.letUsersUnsubscribe(..))",
      returning = "retVal")
  public void logInfoUnsubscription(JoinPoint joinPoint, Response retVal) {
    log.info("/subscriptions " + joinPoint.getSignature().getName() + " method", retVal.toString());
  }

  @AfterThrowing(
      pointcut = "execution(* com.greenfox.notification.controller.UnsubscribeController.letUsersUnsubscribe(..))",
      throwing = "error")
  public void logErrorUnsubscription(JoinPoint joinPoint, Exception error) {
    log.error("/subscriptions " + joinPoint.getSignature().getName() + " method", error.getMessage());
  }
}
