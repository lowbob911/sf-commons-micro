package eu.sportsfusion.common.jobcontrol.control;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Vetoed;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;

import java.time.Duration;
import java.time.Instant;

import eu.sportsfusion.common.jobcontrol.entity.JobLock;

/**
 * @author Anatoli Pranovich
 */
@Vetoed
@ApplicationScoped
public class JobControl {

    @PersistenceContext
    EntityManager em;

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public boolean lock(String jobName) {
        return lock(jobName, 2);
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public boolean lock(String jobName, int maxLockMinutes) {
        JobLock jobLock = em.find(JobLock.class, jobName, LockModeType.PESSIMISTIC_WRITE);

        if (jobLock == null) {
            jobLock = new JobLock();
            jobLock.setJobName(jobName);

            try {
                em.persist(jobLock);
                em.flush();
                return true;
            } catch (PersistenceException ex) {
                // already locked
                return false;
            }
        } else {
            // check lock time
            if (jobLock.getLockTime().isBefore(Instant.now().minus(Duration.ofMinutes(maxLockMinutes)))) {
                jobLock.setLockTime(Instant.now());
                em.merge(jobLock);
                return true;
            }
            return false;
        }
    }

    @Transactional
    public void unlock(String jobName) {
        JobLock jobLock = em.find(JobLock.class, jobName);

        if (jobLock != null) {
            em.remove(jobLock);
        }
    }

    public void runWithLock(String jobName, Runnable job) {
        if (lock(jobName)) {
            try {
                job.run();
            } finally {
                // release lock
                unlock(jobName);
            }
        }
    }
}
