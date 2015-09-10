/**
 * 
 */
package model;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Vector2d;

/**
 * @author Gurkan Solmaz
 * 		   Department of EECS - University of Central Florida
 * 		   Disaster Mobility - Spring 2013
 * 		   Advisor: Dr. Damla Turgut
 */
public class Human {
	
	double maxSpeed;
	Vector2d currentVelocity;
	Vector2d acceleration;
	// current coordinates
	Point positionPoint;
	
	// next destination coordinates
	Point nextDestinationPoint;  
	
	// destination gate index
	Point gatePoint;
	
	// status of the human
	boolean isActive;
	boolean isDead;
	boolean isSaved;
	
	double lifeTime;
	
	
	// social force values
	double A;
	double B;
	
	// store the trajectories 
	List<Point> trajectoryPointList;
	double tmpFlightLength;
	List<Double> flightLengthList;
	List<Point> waitingPointList;

	public Human(double maxSpeed, Point positionPoint, Point gatePoint,
			boolean isActive, boolean isDead, boolean isSaved, double A, double B,
			List<Point> trajectoryPointList) {
		super();
		this.maxSpeed = maxSpeed;
		this.positionPoint = positionPoint;
		this.gatePoint = gatePoint;
		this.isActive = isActive;
		this.isDead = isDead;
		this.isSaved = isSaved;
		this.A = A;
		this.B=B;
		this.trajectoryPointList = trajectoryPointList;
		this.acceleration=(new Vector2d(0,0));
		// start with the first point as the first waiting point
		this.waitingPointList = trajectoryPointList; 
		this.tmpFlightLength = 0;
		flightLengthList = new ArrayList<Double>();

	}

	public double getMaxSpeed() {
		return maxSpeed;
	}

	public Vector2d getCurrentVelocity() {
		return currentVelocity;
	}

	public Point getPositionPoint() {
		return positionPoint;
	}

	public Point getNextDestinationPoint() {
		return nextDestinationPoint;
	}

	public Point getGatePoint() {
		return gatePoint;
	}

	public boolean isActive() {
		return isActive;
	}

	public boolean isDead() {
		return isDead;
	}

	public boolean isSaved() {
		return isSaved;
	}

	public List<Point> getTrajectoryPointList() {
		return trajectoryPointList;
	}

	public void setCurrentVelocity(Vector2d currentVelocity) {
		this.currentVelocity = currentVelocity;
	}

	public void setPositionPoint(Point positionPoint) {
		this.positionPoint = positionPoint;
	}

	public void setNextDestinationPoint(Point nextDestinationPoint) {
		this.nextDestinationPoint = nextDestinationPoint;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public void setDead(boolean isDead) {
		this.isDead = isDead;
	}

	public void setSaved(boolean isSaved) {
		this.isSaved = isSaved;
	}
	
	public void addTrajectoryPoint(Point tp) {
		this.trajectoryPointList.add(tp);
	}

	public double getA() {
		return A;
	}

	public double getB() {
		return B;
	}

	public Vector2d getAcceleration() {
		return acceleration;
	}

	public void setAcceleration(Vector2d acceleration) {
		this.acceleration = acceleration;
	}
	public void addWaitingPoint(Point wp){
		this.waitingPointList.add(wp);
	}

	public double getLifeTime() {
		return lifeTime;
	}

	public List<Point> getWaitingPointList() {
		return waitingPointList;
	}

	public void setLifeTime(double lifeTime) {
		this.lifeTime = lifeTime;
	}
	public void addTmpFlightLength(double d){
		this.tmpFlightLength +=d;
	}

	public void setTmpFlightLength(double tmpFlightLength) {
		this.tmpFlightLength = tmpFlightLength;
	}
	public void addFlightLengthList(double length){
		this.flightLengthList.add(length);
	}

	public List<Double> getFlightLengthList() {
		return flightLengthList;
	}

	public double getTmpFlightLength() {
		return tmpFlightLength;
	}

	public void setGatePoint(Point gatePoint) {
		this.gatePoint = gatePoint;
	}

	public void setMaxSpeed(double maxSpeed) {
		this.maxSpeed = maxSpeed;
	}
	
}
