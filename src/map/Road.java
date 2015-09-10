package map;

import java.util.List;

import model.Point;

/**
 * @author Gurkan Solmaz
 * 		   Department of EECS - University of Central Florida
 * 		   Disaster Mobility - Spring 2013
 * 		   Advisor: Dr. Damla Turgut
 */

public class Road {
	
	List<Point> wayPointList; 
	double wayLength;
	double width;
	String type;
	boolean isClosed;
	
	//boolean isOneWay;
	
	boolean isBroken;
	Point breakPoint;
	
	public Road(List<Point> wayPointList, double width,  String type) {
		super();
		this.wayPointList = wayPointList;;
		this.width = width;
		this.type= type;
		this.isClosed = false;

	}
	public Road(List<Point> wayPointList, String type) {
		super();
		this.wayPointList = wayPointList;;
		this.type = type;
		this.isClosed = true;
	}
	public boolean isBroken() {
		return isBroken;
	}
	public Point getBreakPoint() {
		return breakPoint;
	}
	public void setBroken(boolean isBroken) {
		this.isBroken = isBroken;
	}
	public void setBreakPoint(Point breakPoint) {
		this.breakPoint = breakPoint;
	}
	public List<Point> getWayPointList() {
		return wayPointList;
	}
	public double getWayLength() {
		return wayLength;
	}
	public double getWidth() {
		return width;
	}
	public String getType() {
		return type;
	}
	public void setWayPointList(List<Point> wayPointList) {
		this.wayPointList = wayPointList;
	}
	public void setWayLength(double wayLength) {
		this.wayLength = wayLength;
	}
	public boolean isClosed() {
		return isClosed;
	}	
	
	
}
