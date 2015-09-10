package network;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Vector2d;

import processor.MathFunctions;
import map.Road;
import model.Human;
import model.Point;


public class MobileSinkNode extends Node{

	Human robot; // robot that serves as the sink node
	
	double energyConsumptionOfMovingPerMeter;
	List<Integer> currentlyTrackedSensorList;
	List<Integer> detectedSensorList; 
	List<Double> lastDetectionTimeAccordingToDetectedSensorList; 
	
	List<Double> interContactTimes;
	
	List<Integer> successFullyCommunicatedSensorList; 

	
	List<Integer> receivedMessageIDList;
	List<Message> receivedMessageList;
	
	List<Double> distanceToDisasterList;
	List<Boolean> eventMissedList;
	
	
	List<Grid> assignedGridList;
	Point basePoint;
	List<Road> assignedRoadList;
	List<Integer> assignedRoadIDList;
	List<Point> assignedWaypointList;
	Vector2d currentGravityForce;
	

	public MobileSinkNode(double batteryLevel, double transmissionRange,
			double energyConsumptionPerTransmission, int numberOfSentMessages,
			int numberOfReceivedMessages, Human mobileSinkRobot,
			double energyConsumptionOfMovingPerMeter,
			Human human) {
		super(batteryLevel, transmissionRange,
				energyConsumptionPerTransmission);
		this.robot = new Human(human.getMaxSpeed(), null, human.getGatePoint(), true, false, false, human.getA(), human.getB(), new ArrayList<Point>());
		this.energyConsumptionOfMovingPerMeter = energyConsumptionOfMovingPerMeter;
		this.currentlyTrackedSensorList = new ArrayList<Integer>();
		this.detectedSensorList = new ArrayList<Integer>();
		this.assignedGridList = new ArrayList<Grid>();
		this.assignedRoadList = new ArrayList<Road>();
		this.assignedRoadIDList = new ArrayList<Integer>();
		this.assignedWaypointList = new ArrayList<Point>();
		this.receivedMessageIDList = new ArrayList<Integer>();
		this.receivedMessageList = new ArrayList<Message>();
		this.distanceToDisasterList = new ArrayList<Double>();
		this.eventMissedList = new ArrayList<Boolean>();
		this.successFullyCommunicatedSensorList = new ArrayList<Integer>();
		this.lastDetectionTimeAccordingToDetectedSensorList = new ArrayList<Double>();
		this.interContactTimes = new ArrayList<Double>();
	}


	public Human getRobot() {
		return robot;
	}


	public double getEnergyConsumptionOfMovingPerMeter() {
		return energyConsumptionOfMovingPerMeter;
	}


	public List<Integer> getCurrentlyTrackedSensorList() {
		return currentlyTrackedSensorList;
	}


	public List<Integer> getDetectedSensorList() {
		return detectedSensorList;
	}
	public void addGrid(Grid g){
		this.assignedGridList.add(g);
	}


	public Point getBasePoint() {
		return basePoint;
	}


	public void setBasePoint(Point point) {
		this.basePoint = point;
	}
	
	public void addRoad(Road r){
		this.assignedRoadList.add(r);
	}
	public void addRoadID(int i){
		this.assignedRoadIDList.add(i);
	}

	public void addWaypoint(Point p){
		this.assignedWaypointList.add(p);
	}
	public void addAllWaypoints(List<Point> waypointList){
		this.assignedWaypointList.addAll(waypointList);
	}



	public List<Grid> getAssignedGridList() {
		return assignedGridList;
	}


	public List<Integer> getAssignedRoadIDList() {
		return assignedRoadIDList;
	}


	public List<Point> getAssignedWaypointList() {
		return assignedWaypointList;
	}


	public void setAssignedWaypointList(List<Point> assignedWaypointList) {
		this.assignedWaypointList = assignedWaypointList;
	}


	public void setRobot(Human robot) {
		this.robot = robot;
	}


	public Vector2d getCurrentGravityForce() {
		return currentGravityForce;
	}


	public void setCurrentGravityForce(Vector2d currentGravityForce) {
		this.currentGravityForce = currentGravityForce;
	}


	public List<Integer> getReceivedMessageIDList() {
		return receivedMessageIDList;
	}


	public List<Message> getReceivedMessageList() {
		return receivedMessageList;
	}


	public List<Road> getAssignedRoadList() {
		return assignedRoadList;
	}
	
	public double addMessage(Message m, int index){
		this.receivedMessageList.add(m);
		this.receivedMessageIDList.add(index);
		double d = MathFunctions.findDistanceBetweenTwoPoints(m.getDisasterEventLocation(), this.getRobot().getPositionPoint());
		this.distanceToDisasterList.add(d);
		return d;
	
	}
	
	public List<Integer> getSuccessFullyCommunicatedSensorList() {
		return successFullyCommunicatedSensorList;
	}


	public void addEventMissed(boolean b){
		this.eventMissedList.add(b);
	}
	
	public void addDetectedSensorList(int sensorId, double currentTime){
		if(!detectedSensorList.contains(sensorId)){
			this.detectedSensorList.add(sensorId);
			this.lastDetectionTimeAccordingToDetectedSensorList.add(currentTime);
		}
		else{
			double lastSessionTime = lastDetectionTimeAccordingToDetectedSensorList.get(detectedSensorList.indexOf(sensorId));
			double intContTime = currentTime - lastSessionTime;
			if(intContTime > 30){
				this.lastDetectionTimeAccordingToDetectedSensorList.set(detectedSensorList.indexOf(sensorId),currentTime);
				this.interContactTimes.add(intContTime);
			}
		}
	}
	
	public void addSucessfullyCommunicatedSensorList(int sensorId){
		if(!successFullyCommunicatedSensorList.contains(sensorId)){
			this.successFullyCommunicatedSensorList.add(sensorId);
		}
	}

	public List<Double> getDistanceToDisasterList() {
		return distanceToDisasterList;
	}


	public List<Boolean> getEventMissedList() {
		return eventMissedList;
	}


	public List<Double> getLastDetectionTimeAccordingToDetectedSensorList() {
		return lastDetectionTimeAccordingToDetectedSensorList;
	}


	public List<Double> getInterContactTimes() {
		return interContactTimes;
	}
	
	
}
