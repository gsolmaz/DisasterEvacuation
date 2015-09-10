package processor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import map.ThemeParkMap;
import model.SimulationParameters;
import model.SocialForceParameters;
import network.NetworkParameters;
import network.NetworkProcessor;
import osm.OsmMap;
import visualizer.MapVisualizer;

/**
 * @author Gurkan Solmaz
 * 		   Department of EECS - University of Central Florida
 * 		   Disaster Mobility - Spring 2013
 * 		   Advisor: Dr. Damla Turgut
 */
public class MainController {

	public static void main(String args[]) throws IOException, InterruptedException{
		
		List<Double> transmissionRangeList = new ArrayList<Double>();
		transmissionRangeList.add(25.0);
	//	transmissionRangeList.add(50.0); 
	//	transmissionRangeList.add(10.0);
	//	transmissionRangeList.add(100.0); 

		List<String> placementList = new ArrayList<String>();
	
		placementList.add("Weighted"); 
		placementList.add("Random");
		placementList.add("GridBased");
		placementList.add("RoadBased");  
		placementList.add("WaypointDistribution");


		// 200 * 5 = 1000 in total 
		// Expected time = 1000*15 = 15000 seconds
		// 
		
		// 250 * 5 * 4 
		// 1250 minutes
		// 25m transmission range icin: 250 tane + 250 * 25 = 6250 seconds = 2 saat
		int numberOfIterations = 10;
		for(double transRange : transmissionRangeList){ // 4
			for(int numberOfMobileSinks = 1; numberOfMobileSinks<11; numberOfMobileSinks = numberOfMobileSinks +  1){//10
				for(String sinkPlacementStrategy : placementList){ // 5
					System.out.print("NumSinks:" + numberOfMobileSinks + " T-range:" + (int)transRange + " Place:" + sinkPlacementStrategy );
					for(int iteration=0; iteration<numberOfIterations; iteration++){

					long it_start_time = System.currentTimeMillis();
					NetworkParameters np = new NetworkParameters(
							0.02,		// 	double sendProbabilityOfSensorToSensor
							transRange, // double sensorTransmissionRange,
							0.9, 		// double sensorTransmissionProbability,
							100, 		// int sensorMessageStorageCapacity,
							1,  		// double sensorEnergyConsumptionPerTransmission,
							20,  		// double sensorSensingRange, 
							2, 			// 	int transmissionDuration,
							1, 			// 	double sinkEnergyConsumptionPerTransmission,
							1, 			//	double sinkEnergyConsumptionOfMovingPerMeter,
							100, 		// 	double batteryLevelOfSensorNodes,
							1000,   	//	double batteryLevelOfSinkNodes,
							10, 		//	 double messagePacketSize
							numberOfMobileSinks,	//numberOfMobileSinkNodes
							sinkPlacementStrategy, // sinkPlacementStrategy 
							50, 		// squareGridDimensionSize
							1, 			//mobileSinkMaxSpeed
							600,			// event miss time
							20,		 		// number of effected people
							0.5				// sink relative mass
							);
					// set simulation parameters	
					SimulationParameters sp = new SimulationParameters(
							2000,  // simulation time // REAL VALUE: 2000
							0.0,  // min speed
							1,  // max speed
							0,   // number of red zones 
							1000,  // active red zone time
							2, 	  // min road width
							30,   // max road width 
							50,   // visibility     
							50,  // red zone radius // might be 50 while it is 100 in WCM paper and 50 in GC 
							10,	  // random move distance
							200);// number of humans REAL VALUE: 500 
					
					
					sp.setUserInputs(2, // sampling time  -- ideal: 0.5 seconds 
							"MagicKingdom", // park name
							false, // isVisualizerOn ?
							false,  // isMapVisualizerOn (FULL SCREEN, not available for screenshot)
							false); 	// isKeyboardControlOn
		
					
					// set social force parameters
					SocialForceParameters sfp = new SocialForceParameters(0.11, 0.06, 0.84, 0.63, 0.5, 0.1, 0.5, true, false);
					
					// load the map file
					MapLoader ml = new MapLoader(sp.getMapName());
					OsmMap osmMap = ml.getOsmMap();
					
					// create the theme park map
					MapProcessor mapProcessor = new MapProcessor(osmMap, sp.getMinRoadWidth(), sp.getMaxRoadWidth(), sp.getNumberOfRedZones(), sp.getRedZoneRadius());
					ThemeParkMap themeParkMap = mapProcessor.generateRedZoneTimes(sp.getSimulationTime(),sp.getActiveRedZoneTime());
					
					// visualizer theme park map
					if(sp.isMapVisualizerOn()){
						new MapVisualizer(themeParkMap); 
					}
					// create initial humans and waypoints ! 
					MobilityProcessor mp = new MobilityProcessor(sp,themeParkMap,sfp);
					
					// network parameter
					NetworkProcessor network = new NetworkProcessor(np,mp);
					
					// start the simulation
					SimulationController sc = new SimulationController(sp, mp, themeParkMap, network);
					sc.simulate();
					
					
					
					new OutputController(sc.getHumanListFromMobilityData(),sp, sc.getNetwork(),np,0);
					long it_end_time = System.currentTimeMillis();
					System.out.print((int)(it_end_time- it_start_time)/1000 + "s"); 
					System.out.println();

				/*	System.out.print("Iteration " + iteration+ ": ");
					long it_end_time = System.currentTimeMillis();
					System.out.print((int)(it_end_time- it_start_time)/1000 + "s"); 
					System.out.println();*/
					
					}
				}
			}
		}
	
		System.exit(0);		
	
	}
}

// WCM PARAMETERS
/*	SimulationParameters sp = new SimulationParameters(2000,  // simulation time
													40,   // communication range
													0.0,  // min speed
													1,  // max speed
													50,   // number of red zones
													1000,  // active red zone time
													2, 	  // min road width
													30,   // max road width
													100,   // visibility
													100,  // red zone radius
													10,	  // random move distance
													2000);// number of humans
*/		// GLOBECOM
/*	SimulationParameters sp = new SimulationParameters(1000,  // simulation time
		40,   // communication range
		0.5,  // min speed
		2.5,  // max speed
		20,   // number of red zones
		500,  // active red zone time
		2, 	  // min road width
		30,   // max road width
		50,  // visibility
		50,	  // red zone radius
		10,	  // random move distance
		1000);// number of humans
*/
