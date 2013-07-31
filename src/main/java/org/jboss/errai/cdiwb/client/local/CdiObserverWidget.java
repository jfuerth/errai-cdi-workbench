package org.jboss.errai.cdiwb.client.local;

import javax.inject.Inject;

import org.jboss.errai.ui.client.widget.HasModel;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.InlineLabel;

@Templated
public class CdiObserverWidget extends Composite implements HasModel<CdiObserver> {

  private CdiObserver observer;

  @Inject private @DataField InlineLabel eventType;
  @Inject private @DataField InlineLabel eventCount;
  private int ec;
  @Inject private @DataField Button stopButton;

  @Override
  public CdiObserver getModel() {
    return observer;
  }

  @Override
  public void setModel(CdiObserver model) {
    if (observer != null) {
      observer.setEventCallback(null);
      eventType.setText("");
    }

    observer = model;
    ec = 0;
    eventCount.setText(String.valueOf(ec));

    if (model != null) {
      eventType.setText(model.getEventType());
      model.setEventCallback(new Runnable() {
        @Override
        public void run() {
          ec++;
          eventCount.setText(String.valueOf(ec));
        }
      });
    }
  }

  @EventHandler("stopButton")
  private void stopListening(ClickEvent e) {
    observer.getSubscription().remove();
    eventType.setText("Stopped");
    stopButton.setEnabled(false);
  }
}
