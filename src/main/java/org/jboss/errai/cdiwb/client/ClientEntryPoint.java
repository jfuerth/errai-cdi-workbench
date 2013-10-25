package org.jboss.errai.cdiwb.client;

import static org.uberfire.workbench.model.menu.MenuFactory.newTopLevelMenu;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jboss.errai.ioc.client.api.EntryPoint;
import org.jboss.errai.ioc.client.container.IOC;
import org.jboss.errai.ioc.client.container.IOCBeanDef;
import org.jboss.errai.ioc.client.container.SyncBeanManager;
import org.uberfire.client.JSWorkbenchScreenActivity;
import org.uberfire.client.mvp.ActivityManager;
import org.uberfire.client.mvp.PerspectiveActivity;
import org.uberfire.client.mvp.PlaceManager;
import org.uberfire.client.mvp.WorkbenchScreenActivity;
import org.uberfire.client.workbench.widgets.menu.WorkbenchMenuBar;
import org.uberfire.mvp.Command;
import org.uberfire.mvp.impl.DefaultPlaceRequest;
import org.uberfire.workbench.events.ApplicationReadyEvent;
import org.uberfire.workbench.model.menu.MenuFactory;
import org.uberfire.workbench.model.menu.MenuItem;
import org.uberfire.workbench.model.menu.MenuPosition;
import org.uberfire.workbench.model.menu.Menus;

import com.google.gwt.user.client.Window;

@EntryPoint
public class ClientEntryPoint {

  @Inject
  private SyncBeanManager manager;

  @Inject
  private WorkbenchMenuBar menubar;

  @Inject
  private PlaceManager placeManager;

  @Inject
  private ActivityManager activityManager;

  private void setupMenu(@Observes final ApplicationReadyEvent event) {
    final PerspectiveActivity defaultPerspective = getDefaultPerspectiveActivity();

    final Menus menus =
    newTopLevelMenu("Home")
      .respondsWith(new Command() {
              @Override
              public void execute() {
                if (defaultPerspective != null) {
                  placeManager.goTo(new DefaultPlaceRequest(defaultPerspective.getIdentifier()));
                }
                else {
                  Window.alert("Default perspective not found.");
                }
              }
            })
      .endMenu()
    .newTopLevelMenu("Perspectives")
      .withItems(getPerspectives())
      .endMenu()
    .newTopLevelMenu("Screens")
      .withItems(getScreens())
      .endMenu()
    .newTopLevelMenu("Logout")
      .position(MenuPosition.RIGHT)
      .respondsWith(new Command() {
              @Override
              public void execute() {
                Window.alert("Not implemented! You're here to stay!");
//                redirect(GWT.getModuleBaseURL() + "uf_logout");
              }
            })
      .endMenu()
    .build();

    menubar.addMenus(menus);
  }

  private List<MenuItem> getScreens() {
    final List<MenuItem> screens = new ArrayList<MenuItem>();
    final List<String> names = new ArrayList<String>();

    for (final IOCBeanDef<WorkbenchScreenActivity> _menuItem : IOC.getBeanManager().lookupBeans(
            WorkbenchScreenActivity.class)) {
      final String name;
      if (_menuItem.getBeanClass().equals(JSWorkbenchScreenActivity.class)) {
        name = _menuItem.getName();
      }
      else {
        name = IOC.getBeanManager().lookupBean(_menuItem.getBeanClass()).getName();
      }

//      if (!menuItemsToRemove.contains(name)) {
        names.add(name);
//      }
    }

    Collections.sort(names);

    for (final String name : names) {
      final MenuItem item = MenuFactory.newSimpleItem(name).respondsWith(new Command() {
        @Override
        public void execute() {
          placeManager.goTo(new DefaultPlaceRequest(name));
        }
      }).endMenu().build().getItems().get(0);
      screens.add(item);
    }

    return screens;
  }

  private List<MenuItem> getPerspectives() {
    final List<MenuItem> perspectives = new ArrayList<MenuItem>();
    for (final PerspectiveActivity perspective : getPerspectiveActivities()) {
      final String name = perspective.getPerspective().getName();
      final Command cmd = new Command() {

        @Override
        public void execute() {
          placeManager.goTo(new DefaultPlaceRequest(perspective.getIdentifier()));
        }

      };
      final MenuItem item = MenuFactory.newSimpleItem(name).respondsWith(cmd).endMenu().build().getItems().get(0);
      perspectives.add(item);
    }

    return perspectives;
  }

  private PerspectiveActivity getDefaultPerspectiveActivity() {
    PerspectiveActivity defaultPerspective = null;
    final Collection<IOCBeanDef<PerspectiveActivity>> perspectives = manager.lookupBeans(PerspectiveActivity.class);
    final Iterator<IOCBeanDef<PerspectiveActivity>> perspectivesIterator = perspectives.iterator();

    while (perspectivesIterator.hasNext()) {
      final IOCBeanDef<PerspectiveActivity> perspective = perspectivesIterator.next();
      final PerspectiveActivity instance = perspective.getInstance();
      if (instance.isDefault()) {
        defaultPerspective = instance;
        break;
      }
      else {
        manager.destroyBean(instance);
      }
    }
    return defaultPerspective;
  }

  private List<PerspectiveActivity> getPerspectiveActivities() {

    // Get Perspective Providers
    final Set<PerspectiveActivity> activities = activityManager.getActivities(PerspectiveActivity.class);

    // Sort Perspective Providers so they're always in the same sequence!
    List<PerspectiveActivity> sortedActivities = new ArrayList<PerspectiveActivity>(activities);
    Collections.sort(sortedActivities, new Comparator<PerspectiveActivity>() {

      @Override
      public int compare(PerspectiveActivity o1, PerspectiveActivity o2) {
        return o1.getPerspective().getName().compareTo(o2.getPerspective().getName());
      }

    });

    return sortedActivities;
  }

  private Collection<WorkbenchScreenActivity> getScreenActivities() {

    // Get Perspective Providers
    return activityManager.getActivities(WorkbenchScreenActivity.class);
  }

}
