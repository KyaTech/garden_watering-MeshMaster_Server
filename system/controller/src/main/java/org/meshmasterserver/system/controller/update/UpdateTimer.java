package org.meshmasterserver.system.controller.update;

import java.util.Timer;
import java.util.TimerTask;

import org.meshmasterserver.system.controller.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpdateTimer {
	private static final Logger log = LoggerFactory.getLogger(UpdateTimer.class);

	private Timer timer = new Timer("UpdateTimer");
	private final UpdateTask task;
	private final Config config;

	@Autowired
	public UpdateTimer(Config config, UpdateTask task) {
		this.task = task;
		this.config = config;
	}

	public void start() {
		timer.scheduleAtFixedRate(buildTimerTask(), 0, config.getUpdatetimer().getPeriod());
	}

	public void stop() {
		timer.cancel();
		timer.purge();
		timer = new Timer("UpdateTimer");
	}

	public void restart() {
		stop();
		start();
	}

	private TimerTask buildTimerTask() {
		return new TimerTask() {
			@Override
			public void run() {
				task.run();
			}
		};
	}
}
