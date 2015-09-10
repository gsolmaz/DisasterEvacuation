package processor;

import java.util.List;

import map.ThemeParkMap;
import model.Human;
import model.SimulationParameters;
import network.NetworkProcessor;
import visualizer.SimulationVisualizer;
	/**
	 * @author Gurkan Solmaz
	 * 		   Department of EECS - University of Central Florida
	 * 		   Disaster Mobility - Spring 2013
	 * 		   Advisor: Dr. Damla Turgut
	 */
	public class SimulationController {
		double simulationTime;
		double samplingTime;
		double currentTime;
		boolean isKeyboardInputOn;
		boolean isVisualizerOn;
		MobilityProcessor mp;
		ThemeParkMap tpm;	
		NetworkProcessor network;
		
		SimulationController(SimulationParameters sp, MobilityProcessor mp, ThemeParkMap tpm, NetworkProcessor network){
			this.simulationTime=sp.getSimulationTime();
			this.samplingTime=sp.getSamplingTime();
			this.isKeyboardInputOn=sp.isKeyboardInputOn();
			this.isVisualizerOn=sp.isVisualizerOn();
			this.mp = mp;
			this.tpm = tpm; // used for visualizer
			this.currentTime=0;
			this.network = network;
		}
		
		public void simulate() throws InterruptedException{
			//System.out.println("Simulation started !");
			SimulationVisualizer sv=null;
			if(isVisualizerOn){
				sv = new SimulationVisualizer(tpm,network);
			}
			
			while(true){	
				if(!isKeyboardInputOn || sv.isFinish() || sv.isResume()){ 
					// simulation is allowed to continue 
				//	System.out.println("-------------Simulation time: " + currentTime + " seconds");
	
					// do the necessary moves
					mp.updateEffectedHumans(currentTime, network);

					mp.updateHumans(currentTime);
					network = mp.updateMobileSinks(currentTime, network);
					network.updateMessages(mp);
					// update visualizer & draw	
					if(isVisualizerOn){ 
						sv.setHumanList(mp.getHumanList());
						sv.setNetwork(network);
						sv.setResume(false); sv.setCurrentTime(currentTime);
					}
					currentTime += samplingTime;
					if(currentTime >= simulationTime){
					//	System.out.println("Simulation time: " + currentTime + " seconds");
						break; // simulation time passed, finish the simulation 
					}
				}
				Thread.sleep(1);
			}
			if(sv != null){
				sv.hide();
			}
		}
		public List<Human> getHumanListFromMobilityData(){
			return mp.getHumanList();
		}
		public NetworkProcessor getNetwork(){
			return network;
		}
	}
