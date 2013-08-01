package org.jboss.errai.cdiwb.server;

import javax.servlet.http.HttpSession;

import org.jboss.errai.bus.client.api.QueueSession;
import org.jboss.errai.bus.server.annotations.Service;
import org.jboss.errai.bus.server.api.RpcContext;
import org.jboss.errai.cdiwb.shared.SessionService;

@Service
public class SessionServiceImpl implements SessionService {

  @Override
  public void killQueueSession() {
    QueueSession queueSession = RpcContext.getQueueSession();
    System.out.println("Killing QueueSession " + queueSession.getSessionId());
    queueSession.endSession();
  }

  @Override
  public void killServletSession() {
    HttpSession httpSession = RpcContext.getHttpSession();
    System.out.println("Killing HttpSession " + httpSession.getId());
    httpSession.invalidate();
  }

}
