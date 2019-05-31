package org.meshmasterserver.system.controller.meshcontroller;

import org.springframework.stereotype.Service;

@FunctionalInterface
public interface Assignment<ReturnType> {

	ReturnType execute() throws Exception;

}
