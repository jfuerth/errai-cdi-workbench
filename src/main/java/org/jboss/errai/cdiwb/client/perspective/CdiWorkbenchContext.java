package org.jboss.errai.cdiwb.client.perspective;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;

import org.uberfire.client.annotations.WorkbenchContext;
import org.uberfire.client.annotations.WorkbenchPartTitle;
import org.uberfire.lifecycle.OnContextAttach;
import org.uberfire.workbench.model.PanelDefinition;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;

@Dependent
@WorkbenchContext(identifier = "CdiWorkbenchContextPlaceRequest")
public class CdiWorkbenchContext extends FlowPanel {

  private PanelDefinition panel;

  @PostConstruct
  public void init() {
    System.out.println("CdiWorkbenchContext created");
    add(new Label("This is a CdiWorkbenchContext!"));
  }

  @OnContextAttach
  public void onAttach( final PanelDefinition panel ) {
    this.panel = panel;
    System.out.println("CdiWorkbenchContext attached. Panel = " + panel);
  }

  @Override
  @WorkbenchPartTitle
  public String getTitle() {
    return "CDI Workbenck Context";
  }

}