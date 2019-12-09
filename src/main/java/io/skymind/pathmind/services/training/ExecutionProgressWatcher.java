package io.skymind.pathmind.services.training;

import io.skymind.pathmind.mock.MockDefaultValues;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ExecutionProgressWatcher implements ApplicationListener<ContextRefreshedEvent>, DisposableBean {
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
        private final long UPDATE_INTERVAL = (MockDefaultValues.isDebugAccelerate() ? MockDefaultValues.DEBUG_ACCELERATE_UPDATE_INTERVAL : 60) * 1000;

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
