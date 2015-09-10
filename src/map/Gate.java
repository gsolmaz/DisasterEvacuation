/**
 * 
 */
package map;

import java.util.List;

import model.Point;

/**
 * @author Gurkan Solmaz
 * 		   Department of EECS - University of Central Florida
 * 		   Disaster Mobility - Spring 2013
 * 		   Advisor: Dr. Damla Turgut
 */
public class Gate {
	List<Point> wayPointList; 
	double length;
	public Gate(List<Point> wayPointList) {
		super();
		this.wayPointList = wayPointList;
	}
	public double getLength() {
		return length;
	}
	public void setLength(double length) {
		this.length = length;
	}
	public List<Point> getWayPointList() {
		return wayPointList;
	}
	
	
}
