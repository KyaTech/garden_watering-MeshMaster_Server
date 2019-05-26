package org.meshmasterserver.system.controller.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource(value = "classpath:${configFile}",factory = YamlPropertySourceFactory.class)
@ConfigurationProperties("config")
public class Config {
	private String version;
	@NestedConfigurationProperty
	private UpdateTimerConfig updatetimer;
	@NestedConfigurationProperty
	private MeshControllerConfig meshcontroller;

	public String getVersion() {
		return version;
	}

	public UpdateTimerConfig getUpdatetimer() {
		return updatetimer;
	}

	public MeshControllerConfig getMeshcontroller() {
		return meshcontroller;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public void setUpdatetimer(UpdateTimerConfig updatetimer) {
		this.updatetimer = updatetimer;
	}

	public void setMeshcontroller(MeshControllerConfig meshcontroller) {
		this.meshcontroller = meshcontroller;
	}
}
