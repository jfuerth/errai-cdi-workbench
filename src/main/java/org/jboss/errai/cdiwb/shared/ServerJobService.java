package org.jboss.errai.cdiwb.shared;

import java.util.List;

import org.jboss.errai.bus.server.annotations.Remote;

@Remote
public interface ServerJobService {

  /**
   * Starts firing unqualified events from the server at the given interval.
   *
   * @param intervalMillis
   *          The interval between event firings.
   * @param eventType
   *          The type of event to fire
   * @return a job descriptor that can be passed to {@link #stop(JobDescriptor)}
   *         to stop the periodic firing of the event.
   */
  public JobDescriptor startFiringEvents(long intervalMillis, String eventType);

  public void stop(JobDescriptor jd);
  public List<JobDescriptor> getRunningJobs();

}
