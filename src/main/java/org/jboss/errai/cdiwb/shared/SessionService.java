package org.jboss.errai.cdiwb.shared;

import org.jboss.errai.bus.server.annotations.Remote;

@Remote
public interface SessionService {

  void killQueueSession();
  void killServletSession();

}
