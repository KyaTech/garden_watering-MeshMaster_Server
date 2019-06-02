package org.meshmasterserver.system.controller.meshcontroller;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import org.meshmasterserver.system.controller.config.Config;
import org.meshmasterserver.system.controller.meshcontroller.api.InvalidIndexException;
import org.meshmasterserver.system.controller.meshcontroller.api.InvalidNodeException;
import org.meshmasterserver.system.controller.meshcontroller.assignment.AssignmentPair;
import org.meshmasterserver.system.controller.meshcontroller.assignment.AssignmentPairHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MeshAssignmentPairHandler implements AssignmentPairHandler {

	private final Logger log = LoggerFactory.getLogger(MeshAssignmentPairHandler.class);
	private Queue<AssignmentPair> assignments = new LinkedList<>();
	private final Config config;

	@Autowired
	public MeshAssignmentPairHandler(Config config) {
		this.config = config;
	}

	@Override
	public void add(AssignmentPair assignmentPair) {
		assignments.add(assignmentPair);
	}

	public void run() {
		log.debug("Got {} assignmentpairs", assignments.size());

		Map<AssignmentPair, Integer> failedAssignments = new HashMap<>();

		while (!assignments.isEmpty()) {
			AssignmentPair currentPair = assignments.poll();
			try {
				currentPair.run();
			} catch (InvalidNodeException | InvalidIndexException e) {
				handleRetries(failedAssignments, currentPair, e);
			}
		}
	}

	void handleRetries(Map<AssignmentPair, Integer> failedAssignments, AssignmentPair currentAssignment, IllegalArgumentException exception) {
		if ((failedAssignments.getOrDefault(currentAssignment, 0) + 1) < config.getMeshcontroller().getRetries()) {
			log.warn("Some exception occurred", exception);

			failedAssignments.compute(currentAssignment, (assignmentPair, integer) -> {
				if (integer == null) {
					return 1;
				}
				return (integer + 1);
			});

			assignments.add(currentAssignment);

		} else {
			log.error("Some exception occurred", exception);
		}
	}


}
