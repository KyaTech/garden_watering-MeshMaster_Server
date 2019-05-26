package org.meshmasterserver.system.controller;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.meshmasterserver.system.controller.config.Config;
import org.meshmasterserver.system.controller.database.repository.NodeRepository;
import org.meshmasterserver.system.controller.database.repository.SensorRepository;
import org.meshmasterserver.system.controller.database.repository.SensorValueRepository;
import org.meshmasterserver.system.controller.database.repository.ValveRepository;
import org.meshmasterserver.system.controller.update.UpdateTimer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@EnableConfigurationProperties(Config.class)
@SpringBootApplication
public class ControllerApplication implements CommandLineRunner {

	//private final UpdateTimer timer;
	private final UpdateTimer timer;
	private final SensorRepository sensors;
	private final ValveRepository valveRepository;
	private final SensorValueRepository sensorValueRepository;


	@Autowired
	public ControllerApplication(UpdateTimer timer, SensorRepository sensors, ValveRepository valveRepository, SensorValueRepository sensorValueRepository) {
		this.timer = timer;
		this.sensors = sensors;
		this.valveRepository = valveRepository;
		this.sensorValueRepository = sensorValueRepository;
	}



	public static void main(String[] args) {
		new SpringApplicationBuilder(ControllerApplication.class).web(WebApplicationType.NONE).run(args);
	}

	@Override
	public void run(String... args) throws Exception {
		timer.start();
	}


}
