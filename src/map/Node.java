package map;

import model.Point;
/**
 * @author Gurkan Solmaz
 * 		   Department of EECS - University of Central Florida
 * 		   Disaster Mobility - Spring 2013
 * 		   Advisor: Dr. Damla Turgut
 */
public class Node {
	String type; // Tree, toilets, atm, gifts, drinks, information
	Point point;
	
	public Node(String type, Point point) {
		super();
		this.type = type;
		this.point = point;
	}
	
	public String getType() {
		return type;
	}
	public Point getPoint() {
		return point;
	}
	public void setType(String type) {
		this.type = type;
	}
	public void setPoint(Point point) {
		this.point = point;
	}
	
	
}
