package network;

import java.util.ArrayList;
import java.util.List;

import model.Point;

public class Message {
	
	int creatorSensorID;
	int arrivedSinkID;
	
	int messageID;
	
	Point sensedLocation;
	Point disasterEventLocation;
	double eventSensedTime;
	List<Integer> arrivalPathIDs; // includes creator and arrivedSinkID at the end
	List<Double> transmissionTimesList;
	
	int effectedSensorID;
	double messagePacketSize;
	boolean isArrivedToSink;
	double totalDelay;
	int hopCount;
	
	public Message(int messageID, int startNode, 
			Point sensedLocation, Point disasterEventLocation,
			double eventSensedTime, 
			double messagePacketSize,
			boolean isArrivedToSink, double totalDelay, int hopCount, int effectedSensorID) {
		super();
		this.messageID = messageID;
		this.creatorSensorID = startNode;
		this.sensedLocation = sensedLocation;
		this.disasterEventLocation = disasterEventLocation;
		this.eventSensedTime = eventSensedTime;
		this.messagePacketSize = messagePacketSize;
		this.isArrivedToSink = isArrivedToSink;
		this.totalDelay = totalDelay;
		this.hopCount = hopCount;
		this.arrivedSinkID = -1;
		this.arrivalPathIDs = new ArrayList<Integer>();
		arrivalPathIDs.add(startNode);
		this.transmissionTimesList = new ArrayList<Double>();
		this.effectedSensorID = effectedSensorID;
	}

	public void setArrived(double currentSimulationTime){
		isArrivedToSink = true;
		setHopCount();
		setTotalDelay(currentSimulationTime);
	}

	public void setHopCount(){
		hopCount = arrivalPathIDs.size() -1;
	}
	public void setTotalDelay(double currentSimulationTime){
		totalDelay = currentSimulationTime - eventSensedTime;
	}

	public boolean isArrivedToSink() {
		return isArrivedToSink;
	}

	public void setArrivedToSink(boolean isArrivedToSink) {
		this.isArrivedToSink = isArrivedToSink;
	}


	public double getTotalDelay() {
		return totalDelay;
	}

	public int getCreatorSensorID() {
		return creatorSensorID;
	}

	public void setCreatorSensorID(int creatorSensorID) {
		this.creatorSensorID = creatorSensorID;
	}

	public int getArrivedSinkID() {
		return arrivedSinkID;
	}

	public void setArrivedSinkID(int arrivedSinkID) {
		this.arrivedSinkID = arrivedSinkID;
	}

	public int getMessageID() {
		return messageID;
	}

	public void setMessageID(int messageID) {
		this.messageID = messageID;
	}

	public Point getSensedLocation() {
		return sensedLocation;
	}

	public void setSensedLocation(Point sensedLocation) {
		this.sensedLocation = sensedLocation;
	}

	public Point getDisasterEventLocation() {
		return disasterEventLocation;
	}

	public void setDisasterEventLocation(Point disasterEventLocation) {
		this.disasterEventLocation = disasterEventLocation;
	}

	public double getEventSensedTime() {
		return eventSensedTime;
	}

	public void setEventSensedTime(double eventSensedTime) {
		this.eventSensedTime = eventSensedTime;
	}

	public List<Integer> getArrivalPathIDs() {
		return arrivalPathIDs;
	}

	public void setArrivalPathIDs(List<Integer> arrivalPathIDs) {
		this.arrivalPathIDs = arrivalPathIDs;
	}

	public List<Double> getTransmissionTimesList() {
		return transmissionTimesList;
	}

	public void setTransmissionTimesList(List<Double> transmissionTimesList) {
		this.transmissionTimesList = transmissionTimesList;
	}

	public double getMessagePacketSize() {
		return messagePacketSize;
	}

	public void setMessagePacketSize(double messagePacketSize) {
		this.messagePacketSize = messagePacketSize;
	}

	public int getHopCount() {
		return hopCount;
	}

	public void setHopCount(int hopCount) {
		this.hopCount = hopCount;
	}
	
	public void addArrivalPathIDs(int nodeID){
		this.arrivalPathIDs.add(nodeID);
	}
	
	public void addTransmissionTime(double receiveTime){
		this.transmissionTimesList.add(receiveTime);
	}

	public int getEffectedSensorID() {
		return effectedSensorID;
	}
	
	
	
}
