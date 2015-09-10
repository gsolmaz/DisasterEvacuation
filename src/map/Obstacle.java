package map;

import java.util.List;

import model.Point;

/**
 * @author Gurkan Solmaz
 * 		   Department of EECS - University of Central Florida
 * 		   Disaster Mobility - Spring 2013
 * 		   Advisor: Dr. Damla Turgut
 */

public class Obstacle {
	// use "ray casting algorithm" to find if a point is inside or outside the obstacle	
	List<Point> wayPointList; 
	String type;
	boolean isClosed;
	public Obstacle(List<Point> wayPointList, String type) {
		super();
		this.wayPointList = wayPointList;
		this.type = type;
	}
	public void setClosed(boolean isClosed) {
		this.isClosed = isClosed;
	}
	public List<Point> getWayPointList() {
		return wayPointList;
	}
	public String getType() {
		return type;
	}
	public boolean isClosed() {
		return isClosed;
	}
	
	
}
