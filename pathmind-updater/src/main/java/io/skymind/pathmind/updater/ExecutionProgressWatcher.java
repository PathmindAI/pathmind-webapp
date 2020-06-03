package io.skymind.pathmind.updater;

import io.skymind.pathmind.services.notificationservice.EmailNotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ExecutionProgressWatcher implements ApplicationListener<ContextRefreshedEvent>, DisposableBean
{
    @Autowired
    private EmailNotificationService emailNotificationService;

    private Runner runner = null;

    public void destroy(){
        if (runner != null) {
            log.info("Stopping...");
            runner.stop();
        }
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info("Restarting with new context...");
        ExecutionProgressUpdater updater = event.getApplicationContext().getBean(ExecutionProgressUpdater.class);
        runner = new Runner(updater);
        Thread thread = new Thread(runner);
        thread.setName("ExecutionProgressWatcher");
        thread.setDaemon(true);
        thread.start();
    }

    private class Runner implements Runnable {
        private final long UPDATE_INTERVAL = 60 * 1000;

        private boolean stop = false;

        private final ExecutionProgressUpdater updater;

        Runner(ExecutionProgressUpdater updater) {
            this.updater = updater;
        }

        void stop() {
            stop = true;
        }

        @Override
        public void run() {
            long lastRun = 0;
            while (!stop) {
                // todo get rid of the below temporary debug message
                Thread currentThread = Thread.currentThread();
                log.info("Watcher thread status : " + currentThread.toString() + ", " + currentThread.getState() + ", " + currentThread.isDaemon());

                final long nextRun = lastRun + UPDATE_INTERVAL;
                try {
                    if (nextRun <= System.currentTimeMillis()) {
                        try {
                            updater.update();
                        } catch (Exception e) {
                            log.error("Exception during progress update", e);
                        }
                        lastRun = System.currentTimeMillis();
                    } else {
                        Thread.sleep(nextRun - System.currentTimeMillis());
                    }
                } catch (Exception e) {
                    log.error("Exception during progress update", e);
                }
            }
        }
    }
}
