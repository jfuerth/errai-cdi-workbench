package org.jboss.errai.cdiwb.client.perspective;

import javax.enterprise.context.Dependent;

import org.uberfire.client.annotations.Perspective;
import org.uberfire.client.annotations.WorkbenchPerspective;
import org.uberfire.lifecycle.OnClose;
import org.uberfire.lifecycle.OnOpen;
import org.uberfire.lifecycle.OnStartup;
import org.uberfire.workbench.model.PerspectiveDefinition;
import org.uberfire.workbench.model.impl.PerspectiveDefinitionImpl;

@Dependent
@WorkbenchPerspective(identifier = "CdiWorkbenchPerspective")
public class CdiWorkbenchPerspective {

  @Perspective
  public PerspectiveDefinition getDefinition() {
    PerspectiveDefinitionImpl p = new PerspectiveDefinitionImpl();
    p.setName("CDI Workbench");
    return p;
  }


  @OnStartup
  public void onStartup() {
    System.out.println(this + " starting");
  }

  @OnOpen
  public void onOpen() {
    System.out.println(this + " opening");
  }

  @OnClose
  public void onClose() {
    System.out.println(this + " closing");
  }

}
