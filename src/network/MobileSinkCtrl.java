package network;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.vecmath.Vector2d;

import map.Road;
import map.ThemeParkMap;
import model.Human;
import model.Point;
import processor.MathFunctions;
import processor.MobilityProcessor;

public class MobileSinkCtrl {
	
	String sinkPlacementStrategy;
	ThemeParkMap themeParkMap;
	NetworkParameters np;
	List<MobileSinkNode> mobileSinkList;
	
	double sinkMass;
	
	List<Grid> gridList;
	public MobileSinkCtrl(String mobileSinkPlacementMethod, MobilityProcessor mp, NetworkParameters np, List<MobileSinkNode> mobileSinkList){
		this.sinkPlacementStrategy = mobileSinkPlacementMethod;
		this.themeParkMap = mp.getTpm();
		this.np=np;
		this.mobileSinkList = mobileSinkList;
		initialOperations(mp);
		placeMobileSinks(mp, true);
		
	}
	
	private void initialOperations(MobilityProcessor mp) {
		if(sinkPlacementStrategy.equalsIgnoreCase("GridBased") ||sinkPlacementStrategy.equalsIgnoreCase("Weighted")
				||sinkPlacementStrategy.equalsIgnoreCase("RoadBased")){
			
			gridList = new ArrayList<Grid>();
			// check the waypoints in the map to find the area
			double maxY=0, maxX=0, minY=10000,minX=10000;
						
			for(int i=0; i<mp.getWayPointList().size(); i++){ // for each road
				Point p =  mp.getWayPointList().get(i);
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
			minX = Math.floor(minX); minY = Math.floor(minY);
			maxX = Math.ceil(maxX); maxY = Math.ceil(maxY);

			// create grids by 1x1 square meters
			int gridDimensionSize = (int) np.getSquareGridDimensionSize(); 
			int rowCounter=0, columnCounter =0; //maxColumn=0;
			for(int i=0;i<maxY-minY;i+=gridDimensionSize){
				
				for(int j=0;j<maxX-minX;j+=gridDimensionSize){
					Point minPoint = new Point(j+minX, i+minY);
					Point maxPoint = new Point(j+minX+gridDimensionSize, i+minY+gridDimensionSize);

					Grid g = new Grid(minPoint, maxPoint, rowCounter, columnCounter);
/*					System.out.println("MinX: " + minPoint.getX() + " MinY: " + minPoint.getY() 
							+ " MaxX: " + maxPoint.getX() +  " MaxY: " + maxPoint.getY()+  " Row: " 
							+ rowCounter + " Column: " + columnCounter);
*/					
					
					int wayPointCounter = 0;
					for(int k=0;k<mp.getWayPointList().size();k++){
						Point p = mp.getWayPointList().get(k);
						if(MathFunctions.isPointInGrid(p,g)){
							wayPointCounter++;
							//wayPointCounter++;
						}
					}
					if(wayPointCounter>=1){
						gridList.add(g);
					}
					columnCounter++;

				}
				//maxColumn= columnCounter;
				columnCounter = 0;
				rowCounter++;
			}
			
						
			List<Point> bestBasePointList = new ArrayList<Point>();
			double bestSumOfShortestDistances = 0;
	//		long sTime = System.currentTimeMillis();
			for(int a=0;a<(int)100000;a++){ // try 1000 random distribution
				//	Sobol s = new Sobol(a);
				//	s.setDimensions(2);
				
				Random s = new Random(System.currentTimeMillis());

				List<Point> basePointList = new ArrayList<Point>();

				// complete random generation of mobile sinks base points
			/*	for(int i=0;i<np.getNumberOfMobileSinkNodes();i++){					
					double[] nextPoint = new double[2];					
					nextPoint[0] = s.nextDouble(); nextPoint[1]=s.nextDouble();
					double tmpX = nextPoint[0]*xDist + minX;
					double tmpY = nextPoint[1]*yDist + minY;		
					basePointList.add(new Point(tmpX,tmpY));
				}
				*/
				// select a random grid's center location
				for(int i=0;i<np.getNumberOfMobileSinkNodes();i++){					
					int gridIndex = s.nextInt(gridList.size());		
					if(!basePointList.contains(gridList.get(gridIndex).getCenterPoint())){ // check if this grid is already selected
						basePointList.add(gridList.get(gridIndex).getCenterPoint());
					}
					else{
						i--;
						continue;
					}
				}
				double sumOfShortestDistancesOfCurrentSetOfBasePoints=0;
				for(int i=0;i<basePointList.size();i++){
					double shortestDistanceOfTheBasePoint = 100000;
					for(int j=0;j<basePointList.size();j++){
						if (i==j) continue;
						double dist = MathFunctions.findDistanceBetweenTwoPoints(basePointList.get(i), 
								basePointList.get(j));
						if(dist<shortestDistanceOfTheBasePoint){
							shortestDistanceOfTheBasePoint = dist;
						}
					}
					sumOfShortestDistancesOfCurrentSetOfBasePoints +=shortestDistanceOfTheBasePoint;	
				}
				if(sumOfShortestDistancesOfCurrentSetOfBasePoints > bestSumOfShortestDistances){
					// update the base points
					bestBasePointList = basePointList;
					bestSumOfShortestDistances= sumOfShortestDistancesOfCurrentSetOfBasePoints;
					//System.out.println(bestSumOfShortestDistances);
				}	
			}
		//	long eTime = System.currentTimeMillis();
		//	System.out.println("Big loop took " + (eTime-sTime)/1000 + " seconds");
			// assign base points to mobile sinks
			for(int i=0;i<bestBasePointList.size();i++){
				MobileSinkNode ms = mobileSinkList.get(i);
				ms.setBasePoint(bestBasePointList.get(i));
				mobileSinkList.set(i, ms);
			}
			// assign grids to points & mobile sinks
			//int wayPointCounter =0, gridCounter =0;
			
			
		//	System.out.println("Number of grids " + gridList.size());
			
			double numberOfGridsToBeAssignedToAMobileSink = (double)gridList.size()/(double)bestBasePointList.size();
			for(int i=0;i<gridList.size();i++){
				double smallestDistance = 5000;
				int closestPointID= -1;
				Grid g = gridList.get(i);
				for(int j=0;j<bestBasePointList.size();j++){
					double distance = MathFunctions.findDistanceBetweenTwoPoints(g.getCenterPoint(), bestBasePointList.get(j));
					if(distance<smallestDistance){
						// check if that closest sink is already assigned to enough number of grids
						if(mobileSinkList.get(j).getAssignedGridList().size()<numberOfGridsToBeAssignedToAMobileSink + 100){
							smallestDistance = distance;
							closestPointID = j;
						}
					}
				}
				// assign grid to the closest point
				MobileSinkNode ms = mobileSinkList.get(closestPointID);
				ms.addGrid(g); // gridCounter++;
				if(sinkPlacementStrategy.equalsIgnoreCase("GridBased") || sinkPlacementStrategy.equalsIgnoreCase("Weighted")){
					for(int k=0;k<mp.getWayPointList().size();k++){
						Point p = mp.getWayPointList().get(k);
						if(MathFunctions.isPointInGrid(p,g)){
							ms.addWaypoint(p);
							//wayPointCounter++;
						}
					}
				}


				mobileSinkList.set(closestPointID, ms);
			}
			
		/*	for(int k=0;k<mobileSinkList.size();k++){
				System.out.println("WPs: " + mobileSinkList.get(k).getAssignedWaypointList().size() + " Grids: " + mobileSinkList.get(k).getAssignedGridList().size() );

			}*/

		//	System.out.println("WPs: " + wayPointCounter + " Grids: " + gridCounter);

			
		}
		if(sinkPlacementStrategy.equalsIgnoreCase("RoadBased")){
			List<Road> roadList = themeParkMap.getRoadList();
			
			
			for(int i=0;i<roadList.size();i++){
				Road r = roadList.get(i);
				// find average point
				double sumX = 0, sumY=0;
				for(int j=0;j<r.getWayPointList().size();j++){
					sumX+=r.getWayPointList().get(j).getX();
					sumY+=r.getWayPointList().get(j).getY();
				}
				// find center (average) point of waypoints of the road
				sumX = sumX/r.getWayPointList().size();
				sumY = sumY/r.getWayPointList().size();
				Point centerPointOfRoad = new Point(sumX, sumY);
				
				// assign the road to a mobile sink (Note: try to assign roads according to closeness and current number of assigned roads to the sinks) 
				
				double mostNumOfRoadsToASink = (double)roadList.size()/(double)np.getNumberOfMobileSinkNodes();
				
				// try to find a mobile sink to assign this road to it
				int bestSinkIndex = -1; double minDist = 60000;
				for(int t=0;t<mobileSinkList.size();t++){
				
					MobileSinkNode ms = mobileSinkList.get(t);
					
					if(i==roadList.size()-1 && ms.getAssignedWaypointList().size() == 0){ // there is still a mobile sink unassigned to any road, go ahead and assign this road
						 bestSinkIndex = t; break;
					}
					
					if(ms.getAssignedRoadList().size()> mostNumOfRoadsToASink){
						continue;
					}
					double distBetweenMsAndRoad = MathFunctions.findDistanceBetweenTwoPoints(ms.getBasePoint(), centerPointOfRoad); 
					if(distBetweenMsAndRoad < minDist){
						minDist = distBetweenMsAndRoad; bestSinkIndex = t;
					}
						
				}
				
				
				// found the best sink to assign the road, now assign road's waypoints to sink
				MobileSinkNode ms  = mobileSinkList.get(bestSinkIndex);
				ms.addRoad(r);
				ms.addRoadID(i); 
				ms.addAllWaypoints(r.getWayPointList());
				mobileSinkList.set(bestSinkIndex, ms);
				
				}
			
			}	
		
			
			//int wayPointCounter =0;
			// assign popular roads
		/*	for(int i=0;i<np.getNumberOfMobileSinkNodes();i++){
				int mostNumberOfWaypoints=0;
				int roadIDToBeAssigned =-1;
				for(int j=0;j<roadList.size();j++){
					if(assignedRoadIDList.contains(j)) continue; // already assigned road
					Road r = roadList.get(j);
					if(r.getWayPointList().size() > mostNumberOfWaypoints){
						mostNumberOfWaypoints = r.getWayPointList().size();
						roadIDToBeAssigned = j; assignedRoadIDList.add(j);
					}
				}
				// a road is found to be assigned to current mobile sink
			//	System.out.println(mostNumberOfWaypoints + " " + roadIDToBeAssigned);
				Road assignedRoad = roadList.get(roadIDToBeAssigned);	

				MobileSinkNode ms = mobileSinkList.get(i);
				ms.addRoad(assignedRoad);
				ms.addRoadID(roadIDToBeAssigned);
				
				
				for(int k=0;k<assignedRoad.getWayPointList().size();k++){
					Point p = mp.getWayPointList().get(k);
						ms.addWaypoint(p);
				}		
				mobileSinkList.set(i, ms);
			}
			*/

	
			/*			// assign the roads according to closeness
		for(int i=0;i<roadList.size();i++){
				Road r = roadList.get(i);
				// find the most percentage of waypoints in grids
				int[] gridWaypointCount = new int[gridList.size()];
				int wayPointCounter=0; int bestGridIndex=-1;
				for(int j=0;j<r.getWayPointList().size();j++){ 
					Point p = r.getWayPointList().get(j);
					for(int k=0;k<gridList.size();k++){
						Grid g = gridList.get(k);
						if(MathFunctions.isPointInGrid(p, g)){
							gridWaypointCount[k]++;
							if(wayPointCounter<gridWaypointCount[k]){
								wayPointCounter = gridWaypointCount[k];
								bestGridIndex = k;
							}
						}
					}
				}
				// find which sink this best grid corresponds to
				for(int t=0;t<mobileSinkList.size();t++){
					if(mobileSinkList.get(t).getAssignedGridList().contains(gridList.get(bestGridIndex))){ // found the mobile sink to assign the road
						MobileSinkNode ms = mobileSinkList.get(t);
						ms.addRoad(r);
						// road is added, now add the waypoints 		
						for(int k=0;k<r.getWayPointList().size();k++){
							Point p = r.getWayPointList().get(k);
							ms.addWaypoint(p);
						}	
						
						mobileSinkList.set(t, ms);
					}
				}
			}	
			*/
			
		else if(sinkPlacementStrategy.equalsIgnoreCase("Weighted")){
			this.sinkMass = mp.getHumanList().size()/mobileSinkList.size();
			this.sinkMass = sinkMass * np.getSinkRelativeMass();
		}
	}
	public void placeMobileSinks(MobilityProcessor mp, boolean isInitialTime){ // setting new target points
		
			if(sinkPlacementStrategy.equalsIgnoreCase("GridBased") || sinkPlacementStrategy.equalsIgnoreCase("RoadBased")){
				placeGridOrRoadBased(mp, isInitialTime);
			}
			else if(sinkPlacementStrategy.equalsIgnoreCase("Weighted")){ // weighted according to people's locations
				if(isInitialTime){
					placeGridOrRoadBased(mp, isInitialTime);
				}
				else{
					placeWeighted(mp);
				}
			}
			else if(sinkPlacementStrategy.equalsIgnoreCase("Random")){
				placeRandom(mp, isInitialTime);
			}
			else if(sinkPlacementStrategy.equalsIgnoreCase("WaypointDistribution")){
				placeWaypointDistributionBased(mp, isInitialTime);
			}
	}
	
	private void placeWeighted(MobilityProcessor mp) {
		// compute the weight force on the mobile sink
		for(int i=0;i<mobileSinkList.size();i++){
			Vector2d totalVectoralForceOnSink = new Vector2d();
			MobileSinkNode ms = mobileSinkList.get(i);
			double activeHumanCounter =0 ;
			for(int j=0;j<mp.getHumanList().size();j++){
				if(!mp.getHumanList().get(j).isActive()) continue;
				activeHumanCounter = activeHumanCounter +1;
				Point humanPosition = mp.getHumanList().get(j).getPositionPoint();
				double distance = MathFunctions.findDistanceBetweenTwoPoints(humanPosition, ms.getRobot().getPositionPoint());
				Vector2d v = findGravityForce(1,distance,sinkMass,1,  ms.getRobot().getPositionPoint(), humanPosition);				
				totalVectoralForceOnSink = MathFunctions.addVector(totalVectoralForceOnSink, v);
			}
			for(int j=0;j<mobileSinkList.size();j++){
				if(i==j) continue;
				Point otherSinkPosition = mobileSinkList.get(j).getRobot().getPositionPoint();
				double distance = MathFunctions.findDistanceBetweenTwoPoints(otherSinkPosition, ms.getRobot().getPositionPoint());
				if(distance ==0) continue;
				Vector2d v = findGravityForce(1,distance,sinkMass,sinkMass * (activeHumanCounter/mp.getHumanList().size()), otherSinkPosition, ms.getRobot().getPositionPoint());				
				totalVectoralForceOnSink = MathFunctions.addVector(totalVectoralForceOnSink, v);
			}
			
			double mag = MathFunctions.findMagnitudeOfVector(totalVectoralForceOnSink);
			double scaleFactor = 1/mag;
			totalVectoralForceOnSink.scale(scaleFactor *3);
			
			Point currentPoint = ms.getRobot().getPositionPoint();		
			double targetX = currentPoint.getX() + totalVectoralForceOnSink.x;
			double targetY= currentPoint.getY() + totalVectoralForceOnSink.y;
			Point targetPoint = new Point(targetX, targetY);
			
			
			Human human = ms.getRobot();
			human.setGatePoint(targetPoint);// update robot's target point
			ms.setRobot(human); // save human in ms
			ms.setCurrentGravityForce(totalVectoralForceOnSink);
			mobileSinkList.set(i, ms); // save ms in the list	
		}	
	}

	public Vector2d findGravityForce(double gravityConstant, double distance, double mobSinkMass, double personMass, Point sinkPoint, Point humanPoint){
		double force= gravityConstant * (mobSinkMass*personMass)/(distance*distance);
		Vector2d dir= MathFunctions.findVectorDirectionBetweenTwoPoints(sinkPoint, humanPoint);
		
		
		return MathFunctions.multiplyVectorByScalar(dir,force);
	}

	private void placeWaypointDistributionBased(MobilityProcessor mp, boolean isInitialTime) {
		for(int i=0;i<mobileSinkList.size();i++){
			MobileSinkNode ms = mobileSinkList.get(i);
			// find all assignedWayPoints
			Random r = new Random();
			int selectedWayPointIndex = r.nextInt(mp.getWayPointList().size());
			Point targetPoint = mp.getWayPointList().get(selectedWayPointIndex);
			
			Human human = ms.getRobot();
			human.setGatePoint(targetPoint);// update robot's target point
			if(isInitialTime){
				human.setMaxSpeed(np.getMobileSinkMaxSpeed());
				int firstWPIndex = r.nextInt(mp.getWayPointList().size());
				human.setPositionPoint(mp.getWayPointList().get(firstWPIndex));
			}
			ms.setRobot(human); // save human in ms
			mobileSinkList.set(i, ms); // save ms in the list
		}		
	}
	
	private void placeRandom(MobilityProcessor mp, boolean isInitialTime) {
		for(int i=0;i<mobileSinkList.size();i++){
			MobileSinkNode ms = mobileSinkList.get(i);
			// find all assignedWayPoints
			Random r = new Random();
			double x = r.nextDouble();
			double y= r.nextDouble();
			double randomPositionX = mp.getTpm().getTerrainLengthX() * x;
			double randomPositionY = mp.getTpm().getTerrainLengthX() * y;
			Point rp = new Point(randomPositionX, randomPositionY);
			
			int closestWayPointIndex =-1; double closestDistance = 50000;
			for(int j=0;j<mp.getWayPointList().size();j++){
				// find the closest waypoint to the randomly decided point
				Point p = mp.getWayPointList().get(j);
				double dist = MathFunctions.findDistanceBetweenTwoPoints(p,rp );
				if(dist<closestDistance){
					closestDistance = dist;
					closestWayPointIndex = j;
				}
			}

			int selectedWayPointIndex = closestWayPointIndex;
			Point targetPoint = mp.getWayPointList().get(selectedWayPointIndex);
			
			Human human = ms.getRobot();
			human.setGatePoint(targetPoint);// update robot's target point
			if(isInitialTime){
				human.setMaxSpeed(np.getMobileSinkMaxSpeed());
				int firstWPIndex = r.nextInt(mp.getWayPointList().size());
				human.setPositionPoint(mp.getWayPointList().get(firstWPIndex));
			}
			ms.setRobot(human); // save human in ms
			mobileSinkList.set(i, ms); // save ms in the list
		}
			
	}

	private void placeGridOrRoadBased(MobilityProcessor mp, boolean isInitialTime) {
		for(int i=0;i<mobileSinkList.size();i++){
			MobileSinkNode ms = mobileSinkList.get(i);
			// find all assignedWayPoints
			Random r = new Random();
		//	System.out.println(ms.getAssignedWaypointList().size());

		//	System.out.println(ms.getAssignedWaypointList().size() + "");
			int selectedWayPointIndex = r.nextInt(ms.getAssignedWaypointList().size());
			
			Point targetPoint = ms.getAssignedWaypointList().get(selectedWayPointIndex);
			Human human = ms.getRobot();
			human.setGatePoint(targetPoint);// update robot's target point
			if(isInitialTime){
				human.setMaxSpeed(np.getMobileSinkMaxSpeed());
				int firstWPIndex = r.nextInt(ms.getAssignedWaypointList().size());
				human.setPositionPoint(ms.getAssignedWaypointList().get(firstWPIndex));
			}
			ms.setRobot(human); // save human in ms
			mobileSinkList.set(i, ms); // save ms in the list
		}
	}

	public List<MobileSinkNode> getMobileSinkList() {
		return mobileSinkList;
	}
	public void replaceMobileSinkList(int mobileSinkIndex, MobileSinkNode ms) {
		mobileSinkList.set(mobileSinkIndex, ms);
	}
	
}
