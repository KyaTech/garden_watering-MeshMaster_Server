package org.meshmasterserver.system.controller.meshcontroller.assignment;

import org.springframework.stereotype.Service;

@Service
public interface AssignmentPairHandler {

	void add(AssignmentPair assignmentPair);
	void run();

}
