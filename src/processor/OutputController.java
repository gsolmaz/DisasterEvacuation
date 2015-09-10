package processor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.Human;
import model.Point;
import model.SimulationParameters;
import network.Message;
import network.MobileSinkNode;
import network.NetworkParameters;
import network.NetworkProcessor;
import network.Session;

/**
 * @author Gurkan Solmaz
 * 		   Department of EECS - University of Central Florida
 * 		   Disaster Mobility - Spring 2013
 * 		   Advisor: Dr. Damla Turgut
 */
public class OutputController {
	List<Human> humanList;
	String currentDirectory;
	SimulationParameters sp;
	NetworkProcessor network;
	NetworkParameters np;
	int iterationIndex;
	public OutputController(List<Human> humanList, SimulationParameters sp, NetworkProcessor networkProcessor, NetworkParameters np, int iteration) {
		super();
		this.humanList = humanList;
		this.np = np;
		this.currentDirectory =	System.getProperty("user.dir");
		this.sp=sp;
		this.network = networkProcessor;
		this.iterationIndex = iteration;
		outputResults();
	}

	private void outputResults() {
	//	outputFlightLengths();
	//	outputNumberOfWaitingPoints();
	//	outputDisasterResults();
	//	outputLifetimes();
	
		
	//	outputTrajectories();
	//	outputTraces();
		outputNetworkResults();
	}

	private void outputNetworkResults() {
		// TODO Auto-generated method stub
		// filter events out of messages
		//List<Point> eventList = findNumberOfEvents();
		
		List<Integer> numEffectedPeople = findNumberOfDetectedEffectedPeople();
		
		// find total number of transmissions
		long totalNumberOfTransmissions = findTotalNumberOfTransmissions();

		
		// find minimum hop counts for each message
		List<Integer> minimumHopCountList = findMinHopCounts();
		// find total delay
		List<Double> messageDelayList = findMessageDelayList();
		
		double avgDisasterDistance = averageDistanceToDisasterWhenMessageArrived();
		
	//	double eventSuccessRate = eventAchieveRate();
		double eventSuccessRate = (double) network.getFoundEffectedPeopleIDList().size() / (double) network.getEffectedPeopleIDList().size();
		int numberOfSuccessfullyCommunicatedSensorNodes = findSucessfullyCommunicatedSensorNodeCount();
		
		int numberOfDetectedSensorNodes = findDetectedSensorNodeCount();
		
		List<Double> intContTimeList = findAvgInterContactTime();
		List<Integer> numberOfRecontacts = findNumRecontacts();

		double recontactRate = findRecontactRate();

	//	double averageMoveDistance = findAverageFlightLength();
		
		outputToFile(numEffectedPeople.size(),totalNumberOfTransmissions, minimumHopCountList, messageDelayList,
				avgDisasterDistance, eventSuccessRate, numberOfSuccessfullyCommunicatedSensorNodes,numberOfDetectedSensorNodes, intContTimeList, numberOfRecontacts,recontactRate);
		
	}

	
	
	private double findRecontactRate() {
		double recontactRate = 0;
		int counter=0;
		for(int i=0;i<network.getMsc().getMobileSinkList().size();i++){
			MobileSinkNode ms = network.getMsc().getMobileSinkList().get(i);
			double numIntCont = ms.getInterContactTimes().size();
			double numDetection = ms.getDetectedSensorList().size();
			double rate =0;
			if(numDetection!=0){
				rate = numIntCont/numDetection;
			}
			if(rate!=0){ // only if any detection occured. add this rate
				recontactRate += rate;
				counter++;
			}
		}
		if(counter==0){
			return -1;
		}
		else{
			return recontactRate / counter;
		}
	}

	private List<Integer> findNumRecontacts() {
		List<Integer> listOfIntContTimes = new ArrayList<Integer>();
		for(int i=0;i<network.getMsc().getMobileSinkList().size();i++){
			MobileSinkNode ms = network.getMsc().getMobileSinkList().get(i);
			listOfIntContTimes.add(ms.getInterContactTimes().size());
		}
		return listOfIntContTimes;
	}

	private List<Double> findAvgInterContactTime() {
		List<Double> listOfIntContTimes = new ArrayList<Double>();
		for(int i=0;i<network.getMsc().getMobileSinkList().size();i++){
			MobileSinkNode ms = network.getMsc().getMobileSinkList().get(i);
			listOfIntContTimes.addAll(ms.getInterContactTimes());
		}
		return listOfIntContTimes;
	}

	private int findDetectedSensorNodeCount() {
		List<Integer> listOfDetectedNodes = new ArrayList<Integer>();
		for(int i=0;i<network.getMsc().getMobileSinkList().size();i++){
			MobileSinkNode ms = network.getMsc().getMobileSinkList().get(i);
			for(int j=0;j<ms.getDetectedSensorList().size();j++){
				if(!listOfDetectedNodes.contains(ms.getDetectedSensorList().get(j))){
					// does not contain yet, so add to list
					listOfDetectedNodes.add(ms.getDetectedSensorList().get(j));
				}
			}	
		}
		return listOfDetectedNodes.size();
	}

	private int findSucessfullyCommunicatedSensorNodeCount() {
		List<Integer> listOfCommunicatedNodes = new ArrayList<Integer>();
		for(int i=0;i<network.getMsc().getMobileSinkList().size();i++){
			MobileSinkNode ms = network.getMsc().getMobileSinkList().get(i);
			for(int j=0;j<ms.getSuccessFullyCommunicatedSensorList().size();j++){
				if(!listOfCommunicatedNodes.contains(ms.getSuccessFullyCommunicatedSensorList().get(j))){
					// does not contain yet, so add to list
					listOfCommunicatedNodes.add(ms.getSuccessFullyCommunicatedSensorList().get(j));
				}
			}	
		}
		return listOfCommunicatedNodes.size();
	}

	private double eventAchieveRate() {
		List<Point> eventList = new ArrayList<Point>();
		
		double numberOfMissedEvents=0; double numberOfSuccessEvents=0;
		
		
		for(int i=0;i<network.getMsc().getMobileSinkList().size();i++){
			MobileSinkNode ms = network.getMsc().getMobileSinkList().get(i);
			for(int j=0;j< ms.getReceivedMessageList().size();j++){
				List<Message> messageList = ms.getReceivedMessageList();
				List<Boolean> eventMissList = ms.getEventMissedList();
				for(int k=0;k<messageList.size();k++){
					// disaster point
					Point p = messageList.get(k).getDisasterEventLocation();
					if(eventList.contains(p)){
						continue;
					}
					else{
						if(eventMissList.get(k)){ // event is missed
							numberOfMissedEvents += 1;
						}
						else{
							numberOfSuccessEvents +=1;
						}
					}
				}
				
			}
		}
		if(numberOfSuccessEvents+numberOfMissedEvents  ==0){
			return -1;
		}
		return numberOfSuccessEvents/(numberOfSuccessEvents+ numberOfMissedEvents);
	}

	private double averageDistanceToDisasterWhenMessageArrived() {
		List<MobileSinkNode> tmpList = network.getMsc().getMobileSinkList();
		double totalDistance=0;
		int counter=0;
		for(int i=0;i<tmpList.size();i++){
			MobileSinkNode ms = tmpList.get(i);
			for(int j=0;j<ms.getDistanceToDisasterList().size();j++){
				totalDistance +=ms.getDistanceToDisasterList().get(j);
				counter++;
			}
		}
		if(counter != 0){
			totalDistance = totalDistance/counter; // divide to number of mobile sinks
		}
		else{
			counter=-1;
		}
		return totalDistance;

	}

	
	
	private double findAverageFlightLength() {
		List<MobileSinkNode> tmpList = network.getMsc().getMobileSinkList();
		double totFlightLength=0;
		for(int i=0;i<tmpList.size();i++){
			double avgFlightLength = 0;
			Human h = tmpList.get(i).getRobot();
			double moveDist =0;
			for(int j=0;j<h.getFlightLengthList().size();j++){
				moveDist += h.getFlightLengthList().get(j);
			}
			avgFlightLength = moveDist/h.getFlightLengthList().size();
			totFlightLength+=avgFlightLength;
		}
		totFlightLength = totFlightLength/tmpList.size(); // divide to number of mobile sinks
		return totFlightLength;

	}

	private void outputToFile(int numberOfDetectedEffectedPeople,
			long totalNumberOfTransmissions, List<Integer> minimumHopCountList,
			List<Double> messageDelayList, double avgDisasterDistance, double eventSuccessRate, int numberOfSuccessfullyCommunicatedSensorNodes, int numberOfDetectedSensorNodes, List<Double> intContTimeList, List<Integer> numberOfRecontacts, double recontactRate) {
		FileWriter fstream;
		try {
			new File(currentDirectory + "\\output\\").mkdirs();

			String folderName = "\\output_" +  sp.getNumberOfHumans()  +"people_"+ + sp.getSamplingTime() + "-" + sp.getSimulationTime() + "sec_" + 
							sp.getNumberOfRedZones() +"events\\";
			
			new File(currentDirectory + "\\output\\" + folderName).mkdirs();
			
			currentDirectory = currentDirectory + "\\output\\"  + folderName;
			
			int transRange = (int) np.getSensorTransmissionRange();
			new File(currentDirectory + "\\detectedpeople\\").mkdirs();
			fstream = new FileWriter(currentDirectory + "\\detectedpeople\\"  +"NumDetectedPeople_" + np.getSinkPlacementStrategy()+ "_"
									+ "Sink" + np.getNumberOfMobileSinkNodes()  + "_Range"  + transRange + ".txt", true);
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(numberOfDetectedEffectedPeople +"\n");
			out.close();
			fstream.close();
			
			new File(currentDirectory + "\\numtransmission\\").mkdirs();
			fstream = new FileWriter(currentDirectory + "\\numtransmission\\"   +"NumTransmission_" + np.getSinkPlacementStrategy()+ "_"
					+ "Sink" + np.getNumberOfMobileSinkNodes()  + "_Range"  + transRange + ".txt", true);
			out = new BufferedWriter(fstream);
			out.write(totalNumberOfTransmissions +"\n");
			out.close();
			fstream.close();
			
			new File(currentDirectory + "\\detectedSensor\\").mkdirs();
			fstream = new FileWriter(currentDirectory + "\\detectedsensor\\"   +"NumDetectedSensor_" + np.getSinkPlacementStrategy()+ "_"
					+ "Sink" + np.getNumberOfMobileSinkNodes()  + "_Range"  + transRange + ".txt", true);
			out = new BufferedWriter(fstream);
			out.write(numberOfDetectedSensorNodes +"\n");
			out.close();
			fstream.close();
			
			new File(currentDirectory + "\\numsuccesscomm\\").mkdirs();
			fstream = new FileWriter(currentDirectory + "\\numsuccesscomm\\"   +"NumSuccessCommSensor_" + np.getSinkPlacementStrategy()+ "_"
					+ "Sink" + np.getNumberOfMobileSinkNodes()  + "_Range"  + transRange + ".txt", true);
			out = new BufferedWriter(fstream);
			out.write(numberOfSuccessfullyCommunicatedSensorNodes +"\n");
			out.close();
			fstream.close();
			
			
			if(eventSuccessRate!=-1){
				new File(currentDirectory + "\\eventsuccessrate\\").mkdirs();
				fstream = new FileWriter(currentDirectory + "\\eventsuccessrate\\"  +"EventSuccessRate" + np.getSinkPlacementStrategy()+ "_"
						+ "Sink" + np.getNumberOfMobileSinkNodes()  + "_Range"  + transRange + ".txt", true);
				out = new BufferedWriter(fstream);
				out.write(eventSuccessRate +"\n");
				out.close();
				fstream.close();
			}
			
			if(avgDisasterDistance!=-1){
				new File(currentDirectory + "\\avgdisasterdistance\\").mkdirs();
				fstream = new FileWriter(currentDirectory + "\\avgdisasterdistance\\"  +"AvgDisasterDistance" + np.getSinkPlacementStrategy()+ "_"
						+ "Sink" + np.getNumberOfMobileSinkNodes()  + "_Range"  + transRange + ".txt", true);
				out = new BufferedWriter(fstream);
				out.write(avgDisasterDistance +"\n");
				out.close();
				fstream.close();
			}
			
			new File(currentDirectory + "\\arrivedmessage\\").mkdirs();
			fstream = new FileWriter(currentDirectory + "\\arrivedmessage\\"  +"TotalMessagesArrivedToSink_" + np.getSinkPlacementStrategy()+ "_"
					+ "Sink" + np.getNumberOfMobileSinkNodes()  + "_Range"  + transRange + ".txt", true);
			out = new BufferedWriter(fstream);
			out.write(network.getListOfMessagesArrivedToSinks().size() +"\n");
			out.close();
			fstream.close();
			
			
			
			new File(currentDirectory + "\\hopcounts\\").mkdirs();
			fstream = new FileWriter(currentDirectory + "\\hopcounts\\"  +  "MinHopCounts_" + np.getSinkPlacementStrategy()+ "_"
					+ "Sink" + np.getNumberOfMobileSinkNodes()  + "_Range"  + transRange +  ".txt", true);
			out = new BufferedWriter(fstream);
			for(int i=0;i<minimumHopCountList.size();i++){
				out.write(minimumHopCountList.get(i) + "\n");
			}		
			out.close();
			fstream.close();
			
			new File(currentDirectory + "\\delays\\").mkdirs();
			fstream = new FileWriter(currentDirectory + "\\delays\\" +"MessageDelays_" + np.getSinkPlacementStrategy()+ "_"
					+ "Sink" + np.getNumberOfMobileSinkNodes()  + "_Range"  + transRange + ".txt", true);
			out = new BufferedWriter(fstream);
			for(int i=0;i<messageDelayList.size();i++){
				out.write(messageDelayList.get(i) + "\n");
			}		
			out.close();
			fstream.close();
			

			new File(currentDirectory + "\\intercontact\\").mkdirs();
			fstream = new FileWriter(currentDirectory + "\\intercontact\\" +"InterContactTimes" + np.getSinkPlacementStrategy()+ "_"
					+ "Sink" + np.getNumberOfMobileSinkNodes()  + "_Range"  + transRange +".txt", true);
			out = new BufferedWriter(fstream);
			for(int i=0;i<intContTimeList.size();i++){
				out.write(intContTimeList.get(i) + "\n");
			}		
			out.close();
			fstream.close();
			
			new File(currentDirectory + "\\numrecontacts\\").mkdirs();
			fstream = new FileWriter(currentDirectory + "\\numrecontacts\\" +"NumRecontactSinks" + np.getSinkPlacementStrategy()+ "_"
					+ "Sink" + np.getNumberOfMobileSinkNodes()  + "_Range"  + transRange + ".txt", true);
			out = new BufferedWriter(fstream);
			for(int i=0;i<numberOfRecontacts.size();i++){
				out.write(numberOfRecontacts.get(i) + "\n");
			}		
			out.close();
			fstream.close();
		
			if(recontactRate!=-1){
				new File(currentDirectory + "\\recontactrate\\").mkdirs();
				fstream = new FileWriter(currentDirectory + "\\recontactrate\\"  +"AvgRecontactRate" + np.getSinkPlacementStrategy()+ "_"
						+ "Sink" + np.getNumberOfMobileSinkNodes()  + "_Range"  + transRange + ".txt", true);
				out = new BufferedWriter(fstream);
				out.write(recontactRate +"\n");
				out.close();
				fstream.close();
			}
			
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private long findTotalNumberOfTransmissions() {
		long transmissionCounter = 0;
		
		List<Session> sessionList = network.getSessionList();
		for(int i=0;i<sessionList.size();i++){
			Session s = sessionList.get(i);
			transmissionCounter += s.getTransmissionList().size();
		}
		return transmissionCounter;
			
		
	}

	private List<Double> findMessageDelayList() {
		List<Double> returnList = new ArrayList<Double>();
		for(int i=0;i<network.getMsc().getMobileSinkList().size();i++){
			MobileSinkNode ms = network.getMsc().getMobileSinkList().get(i);
			for(int j=0;j< ms.getReceivedMessageList().size();j++){
				List<Message> messageList = ms.getReceivedMessageList();
				for(int k=0;k<messageList.size();k++){
					// disaster point
					Message m = messageList.get(k);
					double messageDelay = m.getTotalDelay();
					returnList.add(messageDelay);
				}
			}
		}
		return returnList;
	}

	private List<Integer> findMinHopCounts() {
		List<Integer> returnList = new ArrayList<Integer>();
		for(int i=0;i<network.getMsc().getMobileSinkList().size();i++){
			MobileSinkNode ms = network.getMsc().getMobileSinkList().get(i);
			for(int j=0;j< ms.getReceivedMessageList().size();j++){
				List<Message> messageList = ms.getReceivedMessageList();
				for(int k=0;k<messageList.size();k++){
					// disaster point
					int hopCount = messageList.get(k).getHopCount();
					returnList.add(hopCount);
				}
			}
		}
		return returnList;
	}
	
	private List<Integer> findNumberOfDetectedEffectedPeople() {
		List<Integer> eventList = new ArrayList<Integer>();
		for(int i=0;i<network.getMsc().getMobileSinkList().size();i++){
			MobileSinkNode ms = network.getMsc().getMobileSinkList().get(i);	
			for(int j=0;j< ms.getReceivedMessageList().size();j++){
				List<Message> messageList = ms.getReceivedMessageList();
				for(int k=0;k<messageList.size();k++){
					// disaster point
					Integer ee= messageList.get(k).getEffectedSensorID();
					if(eventList.contains(ee)){
						continue;
					}
					else{
						eventList.add(ee);
					}
				}
				
			}
		}
		return eventList;
	}

	private List<Point> findNumberOfEvents() {
		List<Point> eventList = new ArrayList<Point>();
		for(int i=0;i<network.getMsc().getMobileSinkList().size();i++){
			MobileSinkNode ms = network.getMsc().getMobileSinkList().get(i);	
			for(int j=0;j< ms.getReceivedMessageList().size();j++){
				List<Message> messageList = ms.getReceivedMessageList();
				for(int k=0;k<messageList.size();k++){
					// disaster point
					Point p = messageList.get(k).getDisasterEventLocation();
					if(eventList.contains(p)){
						continue;
					}
					else{
						eventList.add(p);
					}
				}
				
			}
		}
		return eventList;
	}

	private void outputLifetimes() {
		FileWriter fstream;
		try {
			fstream = new FileWriter(currentDirectory +"\\output\\" +"DisasterLifetimes.txt");
		
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(humanList.size() +"\n");

			// write number of entries
			for(int i=0;i<humanList.size();i++){
				Human h = humanList.get(i);
				out.write(h.getLifeTime()+"\n");	
			}
			out.close();
			fstream.close();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void outputDisasterResults() {
		FileWriter fstream;
		try {
			fstream = new FileWriter(currentDirectory +"\\output\\" +"DisasterResults.txt");
		
			BufferedWriter out = new BufferedWriter(fstream);
			out.write("Number of humans:" + humanList.size() +"\n");
			
			int saved=0;
			for(int i=0;i<humanList.size();i++){
				Human h = humanList.get(i);
				if(h.isSaved()){
					saved++;
				}
			}
			out.write("Number of saved:" +saved +"\n");

			out.close();
			fstream.close();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void outputFlightLengths() {	
		FileWriter fstream;
		
		try {
			fstream = new FileWriter(currentDirectory + "\\output\\" + "DisasterFlightLengths.txt");
		
			BufferedWriter out = new BufferedWriter(fstream);
			// write number of entries
			for(int i=0;i<humanList.size();i++){
				Human h = humanList.get(i);
				for(int j=0;j<h.getFlightLengthList().size();j++){
					out.write(h.getFlightLengthList().get(j)+"\n");
				}
			}
			out.close();
			fstream.close();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void outputNumberOfWaitingPoints() {
		FileWriter fstream;
		try {
			fstream = new FileWriter(currentDirectory +"\\output\\" +"DisasterNumberOfWaitingPoints.txt");
		
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(humanList.size() +"\n");

			// write number of entries
			for(int i=0;i<humanList.size();i++){
				Human h = humanList.get(i);
				out.write(h.getWaitingPointList().size()+"\n");	
			}
			out.close();
			fstream.close();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


	private void outputTrajectories() {	
		

		FileWriter fstream;
		
		try {
			File f = new File(currentDirectory + "\\output\\" );
			File[] fileList = f.listFiles();
			
			fstream = new FileWriter(currentDirectory + "\\output\\" + "DisasterTrajectories" + (fileList.length+1) + ".txt");

			BufferedWriter out = new BufferedWriter(fstream);
			// write pre information
			
			out.write("NumberOfVisitors: " + humanList.size()+"\n");
			out.write("SimulationTime: " + sp.getSimulationTime() +"\n");
			out.write("SamplingTime: " + sp.getSamplingTime() +"\n");
			
		
			for(int i=0; i<sp.getSimulationTime()/sp.getSamplingTime(); i++){
				// CHECK FOR taking only simulation times of 10 seconds -- OPTIONAL
				if((i*sp.getSamplingTime())%10!=0){
					continue;
				}
				
				
				
				
				// Write Current Sim Time
				out.write("Current Simulation Time: " + i*sp.getSamplingTime() + "\n");
				// write the indices & coordinates of each mobile node for the current time
				for(int j=0; j<humanList.size(); j++){
					// if the node is not "dead", write the trajectory
					if(humanList.get(j).getLifeTime() >=  i*sp.getSamplingTime()){
						out.write("Index: " + j + " ");
						// coordinates 
						out.write("Coordinates: " + humanList.get(j).getTrajectoryPointList().get(i).getX() + " " + humanList.get(j).getTrajectoryPointList().get(i).getY() + "\n");	
					}
				}
			}
			out.close();
			fstream.close();		
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
private void outputTraces()  {	
		

		FileWriter fstream;
		
		try {
			
			File f = new File(currentDirectory + "\\output\\" + "DisasterTraces.txt" );
			boolean isFileLocked = true;
			while(isFileLocked){	
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) { continue;}
				try {
				    org.apache.commons.io.FileUtils.touch(f);
				    isFileLocked = false;
				    System.out.println("Starts writing traces!!");
				} catch (IOException e) { 
				    System.out.println("Cannot write traces, waiting!!");
					continue;
				}
			}

			
			// add the new traces to the existing file
			fstream = new FileWriter(currentDirectory + "\\output\\" + "DisasterTraces.txt", true);

			BufferedWriter out = new BufferedWriter(fstream);
			// write pre information
						
			for(int i=0;i<humanList.size();i++){ // for each node
				for(int j=0;j<=sp.getSimulationTime();j=j+10 ){ //for each time
					out.write(j+ " "); // current time
					if(humanList.get(i).getLifeTime() > j){ // if alive
						Point p = humanList.get(i).getTrajectoryPointList().get(j);
						out.write(p.getX() + " " + p.getY() + "\n" );
					}else{ // dead now
						out.write("None" + " " + "None" + "\n" );
					}
				}
			}
			
			out.close();
			fstream.close();		
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
