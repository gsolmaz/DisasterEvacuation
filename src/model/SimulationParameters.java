package model;
/**
 * @author Gurkan Solmaz
 * 		   Department of EECS - University of Central Florida
 * 		   Disaster Mobility - Spring 2013
 * 		   Advisor: Dr. Damla Turgut
 */
public class SimulationParameters {

	// Simulation settings
	double simulationTime; 				// in seconds
	double communicationRange; 			// in meters
	double minSpeed; 					// in meters/second (minimum speed of visitors)
	double maxSpeed; 					// in meters/ second (max. speed of visitors)
	int numberOfRedZones; 				// number of events (red zones) happen in total simulation time
	double activeRedZoneTime;			// happening time of disasters in seconds (used for red zones)
	double minRoadWidth;				// minimum width of a road
	double maxRoadWidth;				// maximum width of a road
	double visibility; 					// local visibility of visitors (in meters)
	double redZoneRadius;				// radius of red zones
	double randomMoveDistance;			// exploration distance of human when it cannot see any waypoint around
	int numberOfHumans;					// number of visitors

	// Simulation user inputs
	double samplingTime; 				// resolution of the simulation in seconds
	String mapName;		 				// name of the map that will be loaded
	boolean isVisualizerOn; 			// input for simulation visualizer
	boolean isKeyboardInputOn;			// keyboard input
	boolean isMapVisualizerOn;			// map visualizer 
	
	public SimulationParameters(double simulationTime,
								double minSpeed,
								double maxSpeed, 
								int numberOfRedZones, 
								double activeRedZoneTime,
								double minRoadWidth, 
								double maxRoadWidth, 
								double visibility, 
								double redZoneRadius, 
								double randomMoveDistance,
								int numberOfHumans) {
		super();
		this.simulationTime = simulationTime;
		this.minSpeed = minSpeed;
		this.maxSpeed = maxSpeed;
		this.numberOfRedZones = numberOfRedZones;
		this.activeRedZoneTime = activeRedZoneTime;
		this.minRoadWidth = minRoadWidth;
		this.maxRoadWidth = maxRoadWidth;
		this.visibility = visibility;
		this.redZoneRadius = redZoneRadius;
		this.randomMoveDistance=randomMoveDistance;
		this.numberOfHumans = numberOfHumans;
	}
	
	public void setUserInputs(double samplingTime,String mapName, boolean isVisualizerOn,
			boolean isMapVisualizerOn, boolean isKeyboardInputOn){
		this.samplingTime = samplingTime;
		this.mapName = mapName;
		this.isVisualizerOn = isVisualizerOn;
		this.isMapVisualizerOn=isMapVisualizerOn;
		this.isKeyboardInputOn = isKeyboardInputOn;
	}
	public double getMinSpeed() {
		return minSpeed;
	}
	public double getMaxSpeed() {
		return maxSpeed;
	}
	public int getNumberOfRedZones() {
		return numberOfRedZones;
	}
	public String getMapName() {
		return mapName;
	}
	public boolean isVisualizerOn() {
		return isVisualizerOn;
	}
	public boolean isKeyboardInputOn() {
		return isKeyboardInputOn;
	}
	public double getMinRoadWidth() {
		return minRoadWidth;
	}
	public double getMaxRoadWidth() {
		return maxRoadWidth;
	}
	public double getVisibility() {
		return visibility;
	}
	public double getRedZoneRadius() {
		return redZoneRadius;
	}
	public boolean isMapVisualizerOn() {
		return isMapVisualizerOn;
	}

	public int getNumberOfHumans() {
		return numberOfHumans;
	}

	public double getSimulationTime() {
		return simulationTime;
	}

	public double getCommunicationRange() {
		return communicationRange;
	}

	public double getActiveRedZoneTime() {
		return activeRedZoneTime;
	}

	public double getSamplingTime() {
		return samplingTime;
	}

	public double getRandomMoveDistance() {
		return randomMoveDistance;
	}
	
}
