/**
 * 
 */
package map;

import model.Point;

/**
 * @author Gurkan Solmaz
 * 		   Department of EECS - University of Central Florida
 * 		   Disaster Mobility - Spring 2013
 * 		   Advisor: Dr. Damla Turgut
 */
public class RedZone {
	// Model of a disaster
	double radius;
	Point centerPoint;
	
	double activeStartTime;
	double activeEndTime;
	
	public RedZone(double radius, Point centerPoint) {
		super();
		this.radius = radius;
		this.centerPoint = centerPoint;
	}

	public double getRadius() {
		return radius;
	}

	public Point getCenterPoint() {
		return centerPoint;
	}

	public double getActiveStartTime() {
		return activeStartTime;
	}

	public double getActiveEndTime() {
		return activeEndTime;
	}

	public void setActiveStartTime(double activeStartTime) {
		this.activeStartTime = activeStartTime;
	}

	public void setActiveEndTime(double activeEndTime) {
		this.activeEndTime = activeEndTime;
	}
	
	
	
}
