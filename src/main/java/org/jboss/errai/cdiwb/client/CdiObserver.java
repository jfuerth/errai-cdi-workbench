package org.jboss.errai.cdiwb.client;

import org.jboss.errai.bus.client.ErraiBus;
import org.jboss.errai.bus.client.api.Subscription;
import org.jboss.errai.enterprise.client.cdi.AbstractCDIEventCallback;
import org.jboss.errai.enterprise.client.cdi.api.CDI;

/**
 * Model type for the local CDI observer widget.
 *
 * @author Jonathan Fuerth <jfuerth@redhat.com>
 */
public class CdiObserver {

  private final Subscription subscription;
  private String eventType;

  private Runnable onEvent;

  public CdiObserver(final String eventType) {
    this.eventType = eventType;
    CDI.subscribe(eventType, new AbstractCDIEventCallback<Object>() {
      @Override
      public void fireEvent(final Object event) {
        if (onEvent != null) onEvent.run();
      }
      @Override
      public String toString() {
        return "Observer: " + eventType + " []";
      }
    });

    subscription = ErraiBus.get().subscribe("cdi.event:" + eventType, CDI.ROUTING_CALLBACK);
  }

  public String getEventType() {
    return eventType;
  }

  public Subscription getSubscription() {
    return subscription;
  }

  public void setEventCallback(Runnable onEvent) {
    this.onEvent = onEvent;
  }
}
