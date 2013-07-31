Errai To-Do List Demo
=====================

This demo app allows you to experiment with dynamic creation and observation of CDI events within
Errai.


Try It Out
==========

This demo is designed to work with a full Java EE 6 server such as JBoss EAP 6 or AS 7. Although
it should be possible to craft a deployment of this demo to a simpler web server, it's much simpler
to deploy to an EE 6 capable app server.


Prerequisites
-------------

 * Maven 3 (run `mvn --version` on the command line to check)
 * JBoss AS 7 or EAP 6 running on the local machine


Build and Deploy
----------------

To build a .war file and deploy it to the local running JBoss EAP 6 or AS 7 instance:

    % mvn clean package jboss-as:deploy

Once the above command has completed, you should be able to access the app at the following URL:

    http://localhost:8080/errai-cdi-workbench/


Code-and-Refresh
----------------

To try it in GWT's dev mode, after completing the above steps, do this:

    % mvn gwt:run

Then click "Launch in Default Browser" on the GWT Dev Mode window that appears.
