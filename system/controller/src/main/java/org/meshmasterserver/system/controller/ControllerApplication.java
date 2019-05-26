package org.meshmasterserver.system.controller;

import org.meshmasterserver.system.controller.config.Config;
import org.meshmasterserver.system.controller.database.repository.SensorRepository;
import org.meshmasterserver.system.controller.database.repository.SensorValueRepository;
import org.meshmasterserver.system.controller.database.repository.ValveRepository;
import org.meshmasterserver.system.controller.update.UpdateTimer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(Config.class)
@SpringBootApplication
public class ControllerApplication implements CommandLineRunner {

	//private final UpdateTimer timer;
	private final UpdateTimer timer;


	@Autowired
	public ControllerApplication(UpdateTimer timer) {
		this.timer = timer;
	}



	public static void main(String[] args) {
		new SpringApplicationBuilder(ControllerApplication.class).run(args);
	}

	@Override
	public void run(String... args) throws Exception {
		timer.start();
	}


}
