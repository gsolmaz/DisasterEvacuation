package processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import map.Gate;
import map.Node;
import map.Obstacle;
import map.RedZone;
import map.Road;
import map.ThemeParkMap;
import model.Point;
import osm.OsmMap;
import osm.OsmNode;
import osm.OsmWay;
import uk.me.jstott.jcoord.LatLng;
import uk.me.jstott.jcoord.UTMRef;
/**
 * @author Gurkan Solmaz
 * 		   Department of EECS - University of Central Florida
 * 		   Disaster Mobility - Spring 2013
 * 		   Advisor: Dr. Damla Turgut
 */
public class MapProcessor {
	OsmMap osmMap;
	ThemeParkMap themeParkMap;
	double minRoadWidth;
	double maxRoadWidth;
	
	int numberOfRedZones;
	double redZoneRadius;
	
	Point referencePoint;

	public MapProcessor(OsmMap osmMap, double minRoadWidth, double maxRoadWidth, int numberOfRedZones, double redZoneRadius) {
		super();
		this.osmMap = osmMap;
		this.minRoadWidth= minRoadWidth;
		this.maxRoadWidth=maxRoadWidth;
		this.numberOfRedZones = numberOfRedZones;
		this.redZoneRadius = redZoneRadius;
		createThemeParkMap();
	}

	private void createThemeParkMap() {
		setBoundsAndReferencePoint(); // set reference point (0,0) of the map
		
		createNodes();  // add specific nodes such as trees
		createRoadsAndObstacles(); 
		createRedZones();
	}
	

	private void setBoundsAndReferencePoint() {
		double minLongitude = osmMap.getOsmBounds().getMinLon();
		double maxLongitude = osmMap.getOsmBounds().getMaxLon();
		double minLatitude = osmMap.getOsmBounds().getMinLat();
		double maxLatitude = osmMap.getOsmBounds().getMaxLat();

		// set reference point  (down-left::SOUTH-WEST point)
		LatLng baseLatLng = new LatLng(minLatitude, minLongitude);
		UTMRef utmRef = baseLatLng.toUTMRef();
		referencePoint = new Point(utmRef.getEasting(), utmRef.getNorthing());
		
		// set terrain lengths - find the top (up-right::NORTH-EAST) point first
		LatLng topLatLng = new LatLng(maxLatitude, maxLongitude);
		utmRef = topLatLng.toUTMRef();
				
		double terrainLengthX=  utmRef.getEasting()- referencePoint.getX();
		double terrainLengthY= utmRef.getNorthing() - referencePoint.getY();
		themeParkMap = new ThemeParkMap(terrainLengthX, terrainLengthY);
		
	}
	

	private void createNodes() {
		List<OsmNode> osmNodeList = osmMap.getOsmNodeList();
		for(int i=0;i<osmNodeList.size(); i++){
			OsmNode osmNode = osmNodeList.get(i);
			
			if(!osmNode.isVisible()) continue; // check if visible	
			
			if(osmNode.getTagMap().size()!=0){
				// filter-out unnecessary tags, use only the necessary tags
				// Currently used Tags: Tree, toilets, atm, gifts, drinks, information
				String nodeType=null;
				if(osmNode.getTagMap().containsValue("tree")){
					nodeType ="tree";
				}
				else if(osmNode.getTagMap().containsValue("toilets")){
					nodeType ="toilets";
				}
				else if(osmNode.getTagMap().containsValue("atm")){
					nodeType ="atm";
				}
				else if(osmNode.getTagMap().containsValue("gift")){
					nodeType ="gift";
				}
				else if(osmNode.getTagMap().containsValue("drinks")){
					nodeType ="drinks";
				}
				else if(osmNode.getTagMap().containsValue("information")){
					nodeType ="information";
				}
				else if(osmNode.getTagMap().containsKey("barrier") && osmNode.getTagMap().get("barrier").equalsIgnoreCase("gate")){
					// check if the access to gate is private
					if(osmNode.getTagMap().containsKey("access") && (osmNode.getTagMap().get("access").equalsIgnoreCase("private") || 
																		osmNode.getTagMap().get("access").equalsIgnoreCase("no"))){
						continue;
					}
					else{ // create the gate for the specific node
						/*Point p = getRealCoordinates(osmNode.getLat(), osmNode.getLon());
						List<Point> wayPointList = new ArrayList<Point>();
						wayPointList.add(p);
						Gate gate = new Gate(wayPointList);
						themeParkMap.addGate(gate);*/
						continue;
					}
						
				}
				
				else if(osmNode.getTagMap().containsValue("station") || 
						osmNode.getTagMap().containsValue("ferry_terminal")  ||
						osmNode.getTagMap().containsValue("bus_stop")  ||
						osmNode.getTagMap().containsValue("attraction") || 
						osmNode.getTagMap().containsValue("entrance") || 
						osmNode.getTagMap().containsValue("lock_gate") ||
						osmNode.getTagMap().containsValue("gate") || 
						osmNode.getTagMap().containsValue("toll_booth") || 
						osmNode.getTagMap().containsValue("restaurant") || 
						osmNode.getTagMap().containsValue("fast_food") ||
						osmNode.getTagMap().containsValue("level_crossing") ||  // for highways
						osmNode.getTagMap().containsValue("traffic_signals") || // for highways
						osmNode.getTagMap().containsValue("stop") ||  // for highways
						osmNode.getTagMap().containsValue("crossing")){ // ignore these nodes
					continue;
				}
				else{
					continue;
				}
				//Always do the world coordinate conversion from latitude and longitude 
				Point p = getRealCoordinates(osmNode.getLat(), osmNode.getLon());
				Node n = new Node(nodeType, p);
				themeParkMap.addNode(n);
			}
		}
	}
	private void createRedZones() {
		// create initial red zones without setting active times
	/*	for(int i=0;i<numberOfRedZones;i++){
			// find center point
			double x = MathFunctions.pickRandomDoubleValueBetweenTwoNumbers(0, themeParkMap.getTerrainLengthX());
			double y = MathFunctions.pickRandomDoubleValueBetweenTwoNumbers(0, themeParkMap.getTerrainLengthY());

			Point centerPoint = new Point(x, y);
			RedZone r = new RedZone(redZoneRadius, centerPoint);
			themeParkMap.addRedZone(r);
		}*/
		double maxY=0, maxX=0, minY=10000,minX=10000;
		for(int i=0;i<themeParkMap.getRoadList().size();i++){
			Road r = themeParkMap.getRoadList().get(i);
			// find average point
			for(int k=0; k<r.getWayPointList().size(); k++){ // for each road
					Point p =  r.getWayPointList().get(k);
					if(p.getX()>maxX){
						maxX = p.getX();
					}
					if(p.getX()<minX){
						minX = p.getX();
					}
					if(p.getY()>maxY){
						maxY = p.getY();
					}
					if(p.getY()<minY){
						minY = p.getY();
					}
				
			}
		}


		
		for(int i=0;i<numberOfRedZones;i++){
			// find center point
			double x = MathFunctions.pickRandomDoubleValueBetweenTwoNumbers(minX, maxX);
			double y = MathFunctions.pickRandomDoubleValueBetweenTwoNumbers(minY, maxY);

			Point centerPoint = new Point(x, y);
			RedZone r = new RedZone(redZoneRadius, centerPoint);
			themeParkMap.addRedZone(r);
		}
		
	}

	
	
	
	private void createRoadsAndObstacles() {
		List<OsmWay> osmWayList= osmMap.getOsmWayList();
		
		for(int i=0;i<osmWayList.size();i++){
			OsmWay osmWay = osmWayList.get(i);
			if(!osmWay.isVisible())	continue; // check point
			
			String type = getTypeOfWay(osmWay.getTagMap());
			if(type.equalsIgnoreCase("other")){ // this is not a determined type of an object in the theme park simulation 
				//System.out.println(osmWay.getTagMap());
				continue;
			}
			
			List<Point> wayPointList = new ArrayList<Point>();
			List<Long> wayPointIdList= osmWay.getNodeIdList();
			for(int j=0;j<wayPointIdList.size();j++){
				long wayPointNodeId=wayPointIdList.get(j);
				// find the particular node
			    LatLng wayPointLatLng= osmMap.getOsmNodeIndexMap().get(wayPointNodeId);
			    Point p = getRealCoordinatesFromLatLng(wayPointLatLng);
			    wayPointList.add(p);
			}		
			
		    // create and add the road
			if(!(wayPointList==null || wayPointList.size()==1)){ // the road can be created 
				// ROADS
				if(type.equalsIgnoreCase("footway")|| type.equalsIgnoreCase("pedestrian") ){
					double roadWidth = getRoadWidth(type);
					Road road = new Road(wayPointList, roadWidth,type);
					themeParkMap.addRoad(road);
				}
				else if(type.equalsIgnoreCase("plaza")){
					Road road = new Road(wayPointList,type);
					themeParkMap.addRoad(road);
				}
				// GATES
				else if(type.equalsIgnoreCase("gate")){
					Gate gate = new Gate(wayPointList);
					themeParkMap.addGate(gate);
				}
				// OBSTACLES
				else if(type.equalsIgnoreCase("building") || type.equalsIgnoreCase("water") ||type.equalsIgnoreCase("forest")){		
					Obstacle obstacle = new Obstacle(wayPointList, type);
					obstacle.setClosed(true);
					themeParkMap.addObstacle(obstacle);
				}
				else if(type.equalsIgnoreCase("river") || type.equalsIgnoreCase("wall")){ 
					Obstacle obstacle = new Obstacle(wayPointList, type);
					obstacle.setClosed(false);
					themeParkMap.addObstacle(obstacle);
				}
				else{
					System.out.println("Error: Error while creating ways in the theme park MapProcessor.java");
				}
			}
			else{
				System.out.println("Error: The way" + osmWay.getId() + " does not include any waypoint");
			}
		}
		// create manual gates
		if(themeParkMap.getGateList().size()==0){
			List<Point> pList = new ArrayList<Point>();
			List<Point> pList2 = new ArrayList<Point>();

			for(int i=0;i<10; i++){
				Point p = new Point(themeParkMap.getTerrainLengthX()/2+ i, 0);
				pList.add(p);
				p = new Point(themeParkMap.getTerrainLengthX()/2+ i, themeParkMap.getTerrainLengthY());
				pList2.add(p);
			}
			Gate g= new Gate(pList);
			Gate g2= new Gate(pList2);
			themeParkMap.addGate(g);
			themeParkMap.addGate(g2); 
		}
	
	}
	
	private double getRoadWidth(String type) {
		double roadWidth=0;
		if(type.equalsIgnoreCase("footway")){
			roadWidth = minRoadWidth;
		}
		else if(type.equalsIgnoreCase("pedestrian")){
			 roadWidth= MathFunctions.pickRandomDoubleValueBetweenTwoNumbers(minRoadWidth, maxRoadWidth);
		}
		return roadWidth;
	}
	
	
	private String getTypeOfWay(HashMap<String, String> tagMap) {
		String wayType = null;
		if(tagMap.containsValue("footway") ){
			wayType ="footway";
		}
		else if(tagMap.containsValue("pedestrian")){
			if(tagMap.containsKey("area") && tagMap.get("area").equalsIgnoreCase("yes")){
				wayType="plaza";
			}
			else{
				wayType ="pedestrian";
			}
		}
		else if(tagMap.containsKey("building") && tagMap.get("building").equalsIgnoreCase("yes")){
			wayType="building";
		}
		else if(tagMap.containsKey("barrier") && 
			(tagMap.get("barrier").equalsIgnoreCase("fence") ||  tagMap.get("barrier").equalsIgnoreCase("hedge") ||tagMap.get("barrier").equalsIgnoreCase("wall") )){
			wayType="wall";
		}
		else if(tagMap.containsKey("waterway") && tagMap.get("waterway").equalsIgnoreCase("river")){
			wayType="river";
		}
		else if(tagMap.containsKey("natural") && tagMap.get("natural").equalsIgnoreCase("water")){
			wayType="water";
		}
		else if(tagMap.containsKey("landuse") && tagMap.get("landuse").equalsIgnoreCase("forest")){
			wayType="forest";
		}
		else if(tagMap.containsKey("barrier") && tagMap.get("barrier").equalsIgnoreCase("gate")){
			wayType="gate";
		}
		else if(tagMap.containsKey("name") && tagMap.get("name").contains("Gate")){
			wayType="gate";
		}
		else{
			wayType="other";
		//	System.out.println(tagMap);
		}
		return wayType;
	}


	
	private Point getRealCoordinates(double lat, double lon){
		// gets latitude and longitude of a node and returns the real coordinates
		// real coordinates are according to our new simulation map
		
		LatLng latLng = new LatLng(lat, lon);
		UTMRef utmRef = latLng.toUTMRef();
		double x = utmRef.getEasting()- referencePoint.getX();
		double y = utmRef.getNorthing() - referencePoint.getY();
		Point p = new Point(x, y);
		return p;
	}
	
	private Point getRealCoordinatesFromLatLng(LatLng latLng){
		// gets LatLng structure of a node and returns the real coordinates
		// real coordinates are according to our new simulation map
		
		UTMRef utmRef = latLng.toUTMRef();
		double x = utmRef.getEasting()- referencePoint.getX();
		double y = utmRef.getNorthing() - referencePoint.getY();
		Point p = new Point(x, y);
		return p;
	}

	public ThemeParkMap getThemeParkMap() {
		return themeParkMap;
	}

	public void setThemeParkMap(ThemeParkMap themeParkMap) {
		this.themeParkMap = themeParkMap;
	}

	public ThemeParkMap generateRedZoneTimes(double simulationTime, double activeRedZoneTimes) {
		for(int i=0;i<numberOfRedZones;i++){
			double startTime = MathFunctions.pickRandomDoubleValueBetweenTwoNumbers(0, simulationTime);
			double endTime = startTime + activeRedZoneTimes;
			themeParkMap.getRedZoneList().get(i).setActiveStartTime(startTime);
			themeParkMap.getRedZoneList().get(i).setActiveEndTime(endTime);
		}
		return this.themeParkMap;
	}
	

}
