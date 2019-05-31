package org.meshmasterserver.system.controller.meshcontroller;

import java.util.LinkedList;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MeshAssignmentPairHandler implements AssignmentPairHandler {

	private final Logger log = LoggerFactory.getLogger(MeshAssignmentPairHandler.class);
	private Queue<AssignmentPair> assignments = new LinkedList<>();

	@Override
	public void add(AssignmentPair assignmentPair) {
		assignments.add(assignmentPair);
	}

	public void run() {
		log.debug("Got {} assignmentpairs", assignments.size());

		while (!assignments.isEmpty()) {
			AssignmentPair currentPair = assignments.poll();
			try {
				currentPair.run();
			} catch (Exception e) {
				assignments.add(currentPair);
				log.error("Error occurred while execution",e);
				// TODO Handle this exception better
			}
		}
	}


}
