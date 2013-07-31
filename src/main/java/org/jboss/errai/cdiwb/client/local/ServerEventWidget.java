package org.jboss.errai.cdiwb.client.local;

import javax.inject.Inject;

import org.jboss.errai.bus.client.api.BusErrorCallback;
import org.jboss.errai.bus.client.api.messaging.Message;
import org.jboss.errai.cdiwb.shared.JobDescriptor;
import org.jboss.errai.cdiwb.shared.ServerJobService;
import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.common.client.api.RemoteCallback;
import org.jboss.errai.ioc.client.container.ClientBeanManager;
import org.jboss.errai.ui.client.widget.HasModel;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.InlineLabel;

@Templated
public class ServerEventWidget extends Composite implements HasModel<JobDescriptor> {

  @Inject private Caller<ServerJobService> jobService;
  @Inject private ClientBeanManager bm;

  private JobDescriptor runningJobDescriptor;

  @Inject private @DataField InlineLabel jobId;
  @Inject private @DataField InlineLabel jobDescription;
  @Inject private @DataField Button stopButton;


  @Override
  public JobDescriptor getModel() {
    return runningJobDescriptor;
  }

  @Override
  public void setModel(JobDescriptor model) {
    this.runningJobDescriptor = model;
    jobId.setText(String.valueOf(model.getId()));
    jobDescription.setText(model.getDescription());
  }

  @EventHandler("stopButton")
  private void stop(ClickEvent e) {
    jobService.call(new RemoteCallback<Void>() {
      @Override
      public void callback(Void response) {
        jobDescription.setText("Stopped");
      }
    }, new BusErrorCallback() {
      @Override
      public boolean error(Message message, Throwable throwable) {
        jobDescription.setText(throwable.toString());
        return false;
      }
    }).stop(runningJobDescriptor);
    stopButton.setEnabled(false);
  }
}
