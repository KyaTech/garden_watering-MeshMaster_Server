package systemcontroller.update;

import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import systemcontroller.config.Config;

public class Update {
	private static final Logger log = LoggerFactory.getLogger(Update.class);
	private static Timer timer = new Timer("Update");
	private static Runnable runnable = () -> log.info("Task scheduled");

	public static void start() {
		timer.scheduleAtFixedRate(buildTimerTask(), 0, Config.getTimerPeriod());
	}

	public static void stop() {
		timer.cancel();
		timer.purge();
		timer = new Timer("Update");
	}

	public static void restart() {
		stop();
		start();
	}

	private static TimerTask buildTimerTask() {
		return new TimerTask() {
			@Override
			public void run() {
				runnable.run();
			}
		};
	}


	/**
	 * Changes the runnable
	 * ! Without calling restart !
	 * @param runnable
	 */
	static void changeRunnable(Runnable runnable) {
		Update.runnable = runnable;
	}
}
