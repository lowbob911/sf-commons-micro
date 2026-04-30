package eu.sportsfusion.common.jobcontrol.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.Objects;

/**
 * @author Anatoli Pranovich
 */
@Entity
@Table(name = "job_locks")
public class JobLock {

    @Id
    private String jobName;

    private Instant lockTime;

    @PrePersist
    protected void onCreate() {
        lockTime = Instant.now();
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public Instant getLockTime() {
        return lockTime;
    }

    public void setLockTime(Instant lockTime) {
        this.lockTime = lockTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JobLock)) return false;
        JobLock jobLock = (JobLock) o;
        return Objects.equals(jobName, jobLock.jobName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(jobName);
    }
}
