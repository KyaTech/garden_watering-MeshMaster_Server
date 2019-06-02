package org.meshmasterserver.system.controller.meshcontroller.api;

import org.springframework.stereotype.Service;

@Service
public interface MeshController {

	/**
	 * Requests the state of a valve (ON or OFF)
	 *
	 * @param node  the NodeID which should be addressed
	 * @param index the index of the valve on the node
	 * @return the state of the valve
	 * @throws InvalidNodeException if the node is invalid
	 * @throws InvalidIndexException if the index is invalid
	 */
	ValveState requestState(int node, int index) throws InvalidNodeException, InvalidIndexException;


	/**
	 * Requests the sensor value of a specific sensor of a node
	 *
	 * @param node the NodeID which should be addressed
	 * @return the value of the sensor as double
	 * @throws InvalidNodeException if the node is invalid
	 */
	Double requestSensor(int node) throws InvalidNodeException;

	/**
	 * Requests the sensor value of the sensor of a node
	 *
	 * @param node  the NodeID which should be addressed
	 * @param index the index of the sensor on the node
	 * @return the value of the sensor as double
	 * @throws InvalidNodeException  if the node is invalid
	 * @throws InvalidIndexException if the index is invalid
	 */
	Double requestSensor(int node, int index) throws InvalidNodeException, InvalidIndexException;

	/**
	 * Requests the battery state of a specific node
	 *
	 * @param node the NodeID which should be addressed
	 * @return the battery value in percent
	 * @throws InvalidNodeException if the node is invalid
	 */
	Integer requestBattery(int node) throws InvalidNodeException;

	/**
	 * Changes the state of a valve (ON or OFF)
	 *
	 * @param node  the NodeID which should be addressed
	 * @param index the index of the valve
	 * @param state the state the valve should take, ON or OFF
	 * @return the status of the command, OK or ERROR
	 * @throws InvalidNodeException  if the node is invalid
	 * @throws InvalidIndexException if the index is invalid
	 */
	CommandStatus changeValveState(int node, int index, ValveState state) throws InvalidNodeException, InvalidIndexException;


	/**
	 * Turns the valve with the given parameters off
	 *
	 * @param node  the NodeID which should be addressed
	 * @param index the index of the valve
	 * @return the status of the command, OK or ERROR
	 * @throws InvalidNodeException  if the node is invalid
	 * @throws InvalidIndexException if the index is invalid
	 */
	CommandStatus turnOffValve(int node, int index) throws InvalidNodeException, InvalidIndexException;


	/**
	 * Turns the valve with the given parameters on
	 *
	 * @param node  the NodeID which should be addressed
	 * @param index the index of the valve
	 * @return the status of the command, OK or ERROR
	 * @throws InvalidNodeException  if the node is invalid
	 * @throws InvalidIndexException if the index is invalid
	 */
	CommandStatus turnOnValve(int node, int index) throws InvalidNodeException, InvalidIndexException;
}
