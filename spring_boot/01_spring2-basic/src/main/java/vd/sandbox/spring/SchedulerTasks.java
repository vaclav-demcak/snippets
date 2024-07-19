package vd.sandbox.spring;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class SchedulerTasks {

  private static final Logger LOG = LoggerFactory.getLogger(SchedulerTasks.class);

  private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

  /* Every fixed rate and doesn't check previous one */
  @Scheduled(fixedRate = 5000)
  public void reportCurrentTimeFixRate() {
    LOG.info("FIXED RATE :> The time is now {}", dateFormat.format(new Date()));
  }

  /* Only one execution in one time */
  @Scheduled(fixedDelay = 5000)
  public void reportCurrentTimeFixDelay() {
    LOG.info("FIXED DELAY :> The time is now {}", dateFormat.format(new Date()));
  }

  @Scheduled(fixedDelay = 4000, initialDelay = 1000)
  public void scheduleFixedRateWithInitialDelayTask() {
    LOG.info("FIXED DELAY + INIT DELAY :> The time is now {}", dateFormat.format(new Date()));
  }

  /* CRON approach for scheduling */
  @Scheduled(cron = "0 15 10 15 * ?")
  public void scheduleTaskUsingCronExpression() {
    /* we're scheduling a task to be executed at 10:15 AM on the 15th day of every month. */
    LOG.info("CRON :> The time is now {}", dateFormat.format(new Date()));
  }

  /* Parallel behavior */
  @Async
  @Scheduled(fixedRate = 3000)
  public void scheduleFixedRateTaskAsync() throws InterruptedException {
    LOG.info("ASYNC FIXED RATE :> The time is now {}", dateFormat.format(new Date()));
  }
}
