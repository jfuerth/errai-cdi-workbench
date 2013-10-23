package org.jboss.errai.cdiwb.client.panel;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import org.uberfire.client.annotations.WorkbenchPartTitle;
import org.uberfire.client.annotations.WorkbenchPartView;
import org.uberfire.client.annotations.WorkbenchScreen;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

@Dependent
@WorkbenchScreen(identifier="BusStatus")
public class BusStatusPanel {

  @Inject private Widget busStatusWidget;

  @WorkbenchPartTitle
  public String getName() {
    return "Errai Bus Status";
  }

  @WorkbenchPartView
  public IsWidget getView() {
    return busStatusWidget;
  }


}
