package org.jboss.errai.cdiwb.client.perspective;

import javax.enterprise.context.Dependent;

import org.uberfire.client.annotations.Perspective;
import org.uberfire.client.annotations.WorkbenchPerspective;
import org.uberfire.lifecycle.OnClose;
import org.uberfire.lifecycle.OnOpen;
import org.uberfire.lifecycle.OnStartup;
import org.uberfire.mvp.impl.DefaultPlaceRequest;
import org.uberfire.workbench.model.PerspectiveDefinition;
import org.uberfire.workbench.model.impl.ContextDefinitionImpl;
import org.uberfire.workbench.model.impl.PartDefinitionImpl;
import org.uberfire.workbench.model.impl.PerspectiveDefinitionImpl;

@Dependent
@WorkbenchPerspective(identifier = "CdiWorkbenchPerspective", isDefault = true)
public class CdiWorkbenchPerspective {

  public CdiWorkbenchPerspective() {
    System.out.println("CdiWorkbenchPerspective created");
  }

  @Perspective
  public PerspectiveDefinition getDefinition15() {
    PerspectiveDefinitionImpl p = new PerspectiveDefinitionImpl();
    p.setName("CDI Workbench");

    p.setContextDefinition( new ContextDefinitionImpl( new DefaultPlaceRequest( "CdiWorkbenchContextPlaceRequest" ) ) );

    p.getRoot().addPart( new PartDefinitionImpl( new DefaultPlaceRequest( "CdiWorkbenchRootPlaceRequest" ) ) );

    System.out.println("CdiWorkbenchPerspective returning perspective definition: " + p);
    return p;
  }

  @OnStartup
  public void onStartup() {
    System.out.println(this + " starting");
  }

  @OnOpen
  public void onOpen() {
    System.out.println(this + " opening");
    new Exception().printStackTrace(System.out);
  }

  @OnClose
  public void onClose() {
    System.out.println(this + " closing");
  }

}
