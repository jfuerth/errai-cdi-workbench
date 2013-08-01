/**
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.errai.cdiwb.client.local;

import javax.inject.Inject;

import org.jboss.errai.bus.client.ErraiBus;
import org.jboss.errai.bus.client.api.BusLifecycleEvent;
import org.jboss.errai.bus.client.api.BusLifecycleListener;
import org.jboss.errai.bus.client.api.ClientMessageBus;
import org.jboss.errai.bus.client.framework.ClientMessageBusImpl;
import org.jboss.errai.cdiwb.shared.SessionService;
import org.jboss.errai.common.client.api.Caller;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;

public class BusStatusWidget extends Composite implements BusLifecycleListener {

    interface Resources extends ClientBundle {
        @Source("white.png")
        ImageResource white();

        @Source("red.png")
        ImageResource red();

        @Source("yellow.png")
        ImageResource yellow();

        @Source("green.png")
        ImageResource green();
    }

    @Inject Caller<SessionService> sessionService;

    private final HorizontalPanel me = new HorizontalPanel();
    private final Resources resources = GWT.create(Resources.class);
    private final Image statusImage = new Image(resources.white());
    private final Label statusLabel = new Label("Unknown");
    private final Button stopTrueButton = new Button("bus.stop(true)");
    private final Button stopFalseButton = new Button("bus.stop(false)");
    private final Button initButton = new Button("bus.init()");
    private final Button killHttpSessionButton = new Button("Kill HTTP Session");
    private final Button killQueueSessionButton = new Button("Kill Queue Session");

    public BusStatusWidget() {
        initWidget(me);

        statusLabel.setWordWrap(false);
        statusLabel.setWidth("6em");

        stopTrueButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                ((ClientMessageBusImpl) ErraiBus.get()).stop(true);
            }
        });

        stopFalseButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                ((ClientMessageBusImpl) ErraiBus.get()).stop(false);
            }
        });

        initButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                ((ClientMessageBus) ErraiBus.get()).init();
            }
        });

        killHttpSessionButton.addClickHandler(new ClickHandler() {

          @Override
          public void onClick(ClickEvent event) {
            sessionService.call().killServletSession();
          }
        });

        killQueueSessionButton.addClickHandler(new ClickHandler() {

          @Override
          public void onClick(ClickEvent event) {
            sessionService.call().killQueueSession();
          }
        });

        me.add(statusImage);
        me.add(statusLabel);
        me.add(stopFalseButton);
        me.add(stopTrueButton);
        me.add(initButton);
        me.add(killHttpSessionButton);
        me.add(killQueueSessionButton);

        ClientMessageBusImpl bus = (ClientMessageBusImpl) ErraiBus.get();
        bus.addLifecycleListener(this);
        switch (bus.getState()) {
        case CONNECTED:
          busOnline(null);
          break;
        case CONNECTING:
          busAssociating(null);
          break;
        case CONNECTION_INTERRUPTED:
          busDisassociating(null);
          break;
        case LOCAL_ONLY:
          busOffline(null);
          break;
        case UNINITIALIZED:
        default:
          break;
        }
    }

    @Override
    public void busAssociating(BusLifecycleEvent e) {
        statusLabel.setText("Connecting");
        statusImage.setResource(resources.yellow());
    }

    @Override
    public void busDisassociating(BusLifecycleEvent e) {
        statusLabel.setText("Local Only");
        statusImage.setResource(resources.red());
    }

    @Override
    public void busOnline(BusLifecycleEvent e) {
        statusLabel.setText("Connected");
        statusImage.setResource(resources.green());
        killHttpSessionButton.setEnabled(true);
        killQueueSessionButton.setEnabled(true);
    }

    @Override
    public void busOffline(BusLifecycleEvent e) {
        statusLabel.setText("Connecting");
        statusImage.setResource(resources.yellow());
        killHttpSessionButton.setEnabled(false);
        killQueueSessionButton.setEnabled(false);
    }
}
