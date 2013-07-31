package org.jboss.errai.cdiwb.server;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

import org.jboss.errai.bus.server.annotations.Service;
import org.jboss.errai.cdiwb.shared.JobDescriptor;
import org.jboss.errai.cdiwb.shared.ServerJobService;

@ApplicationScoped @Service
public class ServerJobServiceImpl implements ServerJobService {

  private final ScheduledExecutorService jobExecutor = Executors.newScheduledThreadPool(2);
  private final ConcurrentMap<JobDescriptor, ScheduledFuture<?>> runningJobs =
          new ConcurrentHashMap<JobDescriptor, ScheduledFuture<?>>();

  @Inject BeanManager bm;

  @Override
  public JobDescriptor startFiringEvents(
          final long intervalMillis, final String eventType) {

    final JobDescriptor jd = JobDescriptor.create(
            "Fire " + eventType + " every " + intervalMillis + "ms");

    Runnable j = new Runnable() {
      @Override
      public void run() {
        try {
          Object event = Class.forName(eventType).newInstance();
          System.out.println(jd + " is about to fire " + event);
          bm.fireEvent(event);
        } catch (Throwable t) {
          System.out.println(jd + " failed. Exiting.");
          t.printStackTrace(System.out);
          stop(jd);
        }
      }
    };

    ScheduledFuture<?> future = jobExecutor.scheduleAtFixedRate(j, 0, intervalMillis, TimeUnit.MILLISECONDS);
    runningJobs.put(jd, future);
    return jd;
  }

  @Override
  public void stop(JobDescriptor jd) {
    ScheduledFuture<?> future = runningJobs.remove(jd);
    if (future != null) {
      future.cancel(true);
    }
  }

  @Override
  public List<JobDescriptor> getRunningJobs() {
    return new ArrayList<JobDescriptor>(runningJobs.keySet());
  }

  @PreDestroy
  private void shutdown() {
    jobExecutor.shutdownNow();
  }
}
