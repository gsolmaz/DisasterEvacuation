package map;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Gurkan Solmaz
 * 		   Department of EECS - University of Central Florida
 * 		   Disaster Mobility - Spring 2013
 * 		   Advisor: Dr. Damla Turgut
 */
public class ThemeParkMap {

	// lengths of x and y dimensions
	double terrainLengthX;
	double terrainLengthY;

	// all the areas which are not obstacles or roads are "Lands" 
	List<Node> nodeList;
	List<Gate> gateList;
	List<Obstacle> obstacleList;
	List<Road> roadList;
	List<RedZone> redZoneList;

	
	// constructor, we only need to set the bounds of the map at the beginning
	public ThemeParkMap(double terrainLengthX, double terrainLengthY) {
		super();
		this.terrainLengthX = terrainLengthX;
		this.terrainLengthY = terrainLengthY;
		this.nodeList=new ArrayList<Node>();
		this.gateList= new ArrayList<Gate>();
		this.obstacleList = new ArrayList<Obstacle>();
		this.roadList = new ArrayList<Road>();
		this.redZoneList = new ArrayList<RedZone>();
	}
	
	public void addNode(Node n){
		nodeList.add(n);
	}	
	public void addGate(Gate g){
		gateList.add(g);
	}
	public void addObstacle(Obstacle o){
		obstacleList.add(o);
	}
	public void addRoad(Road r){
		roadList.add(r);
	}
	public void addRedZone(RedZone rz){
		redZoneList.add(rz);
	}
	public double getTerrainLengthX() {
		return terrainLengthX;
	}
	public double getTerrainLengthY() {
		return terrainLengthY;
	}
	public List<Node> getNodeList() {
		return nodeList;
	}
	public List<Gate> getGateList() {
		return gateList;
	}
	public List<Obstacle> getObstacleList() {
		return obstacleList;
	}
	public List<Road> getRoadList() {
		return roadList;
	}
	public List<RedZone> getRedZoneList() {
		return redZoneList;
	}
}
