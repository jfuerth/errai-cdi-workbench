package org.jboss.errai.cdiwb.shared;

import org.jboss.errai.common.client.api.annotations.Portable;
import org.jboss.errai.enterprise.client.cdi.api.Conversational;
import org.jboss.errai.marshalling.client.api.annotations.MapsTo;

/**
 * A conversational event that can be used for testing CDI messaging between client and server.
 *
 * @author Jonathan Fuerth <jfuerth@redhat.com>
 */
@Portable @Conversational
public class ConversationalEvent {

  private static long nextId;

  private final long id;

  public ConversationalEvent() {
    synchronized (ConversationalEvent.class) {
      this.id = nextId++;
    }
  }

  ConversationalEvent(@MapsTo("id") long id) {
    this.id = id;
  }

  public long getId() {
    return id;
  }

  @Override
  public String toString() {
    return "ConversationalEvent " + id;
  }
}
