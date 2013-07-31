package org.jboss.errai.cdiwb.shared;

import org.jboss.errai.common.client.api.Assert;
import org.jboss.errai.common.client.api.annotations.Portable;
import org.jboss.errai.marshalling.client.api.annotations.MapsTo;

@Portable
public final class JobDescriptor {

  private static long nextId;

  private long id;
  private String description;

  private JobDescriptor(@MapsTo("id") long id, @MapsTo("description") String description) {
    this.id = id;
    this.description = Assert.notNull(description);
  }

  public static synchronized JobDescriptor create(String description) {
    return new JobDescriptor(nextId++, description);
  }

  public long getId() {
    return id;
  }

  public String getDescription() {
    return description;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (int) (id ^ (id >>> 32));
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    JobDescriptor other = (JobDescriptor) obj;
    if (id != other.id)
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "Job " + id + ": " + description;
  }
}
