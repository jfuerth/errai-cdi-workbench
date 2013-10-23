/*
 * Copyright 2012 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jboss.errai.cdiwb.server;

import static org.uberfire.backend.server.repositories.SystemRepository.SYSTEM_REPO;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.errai.common.client.api.Assert;
import org.uberfire.backend.repositories.Repository;
import org.uberfire.commons.cluster.ClusterServiceFactory;
import org.uberfire.io.IOService;
import org.uberfire.io.impl.IOServiceDotFileImpl;
import org.uberfire.io.impl.cluster.IOServiceClusterImpl;

@ApplicationScoped
public class ApplicationScopedProducer {

    @Inject
    @Named("clusterServiceFactory")
    private ClusterServiceFactory clusterServiceFactory;

    private IOService ioService;

    @PostConstruct
    public void setup() {
        if ( clusterServiceFactory == null ) {
            ioService = new IOServiceDotFileImpl();
        } else {
            ioService = new IOServiceClusterImpl( new IOServiceDotFileImpl(), clusterServiceFactory );
        }
    }

    @PreDestroy
    public void onShutdown() {
        ioService.dispose();
    }

    @Produces
    @Named("ioStrategy")
    public IOService ioService() {
        return Assert.notNull(ioService);
    }

    @Produces
    @Named("system")
    public Repository systemRepository() {
        return Assert.notNull(SYSTEM_REPO);
    }

}
