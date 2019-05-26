package org.meshmasterserver.system.controller.update;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpdateTask implements Runnable{

	private static Logger log = LoggerFactory.getLogger(UpdateTask.class);

	private List<UpdateStep> steps = new ArrayList<>();

	@Autowired
	public UpdateTask(UpdateMesh mesh) {
		steps.add(mesh);
	}


	@Override
	public void run() {

		// TODO *** here: update all nodes  ***

		log.info("Updating nodes");

		for (UpdateStep step : steps) {
			step.run();
		}

	}

}
