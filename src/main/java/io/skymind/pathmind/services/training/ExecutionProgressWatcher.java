package io.skymind.pathmind.services.training;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class ExecutionProgressWatcher implements ApplicationListener<ContextRefreshedEvent>, DisposableBean {
    private static Logger log = LoggerFactory.getLogger(ExecutionProgressWatcher.class);
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
        private static final long updateInterval = 60 * 1000;

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
                final long nextRun = lastRun + updateInterval;
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
