package network;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import model.Human;
import model.Point;
import processor.MathFunctions;
import processor.MobilityProcessor;

public class NetworkProcessor {
	
	NetworkParameters np;
	List<SensorNode> sensorNodeList;
	List<Session> sessionList;
	
	List<Integer> effectedPeopleIDList;
	List<Double> effectedPeopleActiveTimeStartList;
	List<Double> effectedPeopleActiveTimeEndList;
	
	List<Integer> foundEffectedPeopleIDList;
	
	//List<Transmission> listOfCurrentTransmissions;
	//List<Transmission> listOfFinishedTransmissions;
	//List<Transmission> listOfAllTransmissions;

	
	List<Message> listOfMessagesArrivedToSinks; // finished transmission to sink
	List<Message> listOfAllMessages; // sorted according to message IDs
	
	
	int messageIDCounter; 

	MobileSinkCtrl msc;
	
	public NetworkProcessor(NetworkParameters np, MobilityProcessor mp
		) { // initialize variables as empty
		this.np = np;
		this.sessionList= new ArrayList<Session>();
		this.listOfAllMessages = new ArrayList<Message>();
		this.listOfMessagesArrivedToSinks = new ArrayList<Message>();
		this.foundEffectedPeopleIDList = new ArrayList<Integer>();
		//this.listOfCurrentTransmissions = new ArrayList<Transmission>();
		//this.listOfFinishedTransmissions = new ArrayList<Transmission>();
		//this.listOfAllTransmissions = new ArrayList<Transmission>();
		createInitialSensorNodes(mp.getHumanList().size());
		createEffectedPeopleList(mp.getTotalSimulationtime());

		List<MobileSinkNode> mobileSinkList = createInitialMobileSinks(mp.getHumanList());
		this.msc = new MobileSinkCtrl(np.getSinkPlacementStrategy(), mp, np,mobileSinkList );
		this.messageIDCounter = 0;
	}
/*	private void createEffectedPeopleList(double totSimTime) {
		effectedPeopleIDList = new ArrayList<Integer>();
		effectedPeopleActiveTimeStartList = new ArrayList<Double>();
		effectedPeopleActiveTimeEndList = new ArrayList<Double>();
		Random r= new Random();
		while(effectedPeopleIDList.size()<np.getNumberOfEffectedPeople()){			
			int nextEffectedPerson = r.nextInt(sensorNodeList.size());
			if(effectedPeopleIDList.contains(nextEffectedPerson)) continue;
			// create the effect in a random time of the simulation
			double startTime = MathFunctions.pickRandomDoubleValueBetweenTwoNumbers(0, totSimTime);
			double endTime = startTime + totSimTime; // will be unavailable all the time
			effectedPeopleActiveTimeStartList.add(startTime);
			effectedPeopleActiveTimeEndList.add(endTime);
			effectedPeopleIDList.add(nextEffectedPerson);
		}
	}*/
	
	
	private void createEffectedPeopleList(double totSimTime) {
		effectedPeopleIDList = new ArrayList<Integer>();
		effectedPeopleActiveTimeStartList = new ArrayList<Double>();
		effectedPeopleActiveTimeEndList = new ArrayList<Double>();
		while(effectedPeopleActiveTimeStartList.size()<np.getNumberOfEffectedPeople()){			
			// create the effect in a random time of the simulation
			double startTime = MathFunctions.pickRandomDoubleValueBetweenTwoNumbers(0, totSimTime);
			double endTime = startTime + totSimTime; // will be unavailable all the time
			effectedPeopleActiveTimeStartList.add(startTime);
			effectedPeopleActiveTimeEndList.add(endTime);
		}
		Collections.sort(effectedPeopleActiveTimeStartList);
		Collections.sort(effectedPeopleActiveTimeEndList);

	}
	
	
	private List<MobileSinkNode> createInitialMobileSinks(List<Human> humanList) {
		List<MobileSinkNode> mobileSinkList = new ArrayList<MobileSinkNode>();
		for(int i=0;i<np.getNumberOfMobileSinkNodes();i++){
			MobileSinkNode ms = new MobileSinkNode(np.getBatteryLevelOfSinkNodes(), np.getSensorTransmissionRange(), np.getSinkEnergyConsumptionPerTransmission(), 0, 0, null, np.getSinkEnergyConsumptionOfMovingPerMeter(),
					 humanList.get(0));
			mobileSinkList.add(ms);
		}
		return mobileSinkList;
	}
	private void createInitialSensorNodes(int humanListSize) {
		// TODO Auto-generated method stub
		sensorNodeList = new ArrayList<SensorNode>();
		for(int i=0;i<humanListSize;i++){
			SensorNode s = new SensorNode(np.getBatteryLevelOfSensorNodes(),np.getSensorEnergyConsumptionPerTransmission(),np.getSensorTransmissionRange(),i,np.getSensorSensingRange(),np.getSensorMessageStorageCapacity());
			sensorNodeList.add(s);
		}
		
	}
	public List<SensorNode> getSensorNodeList() {
		return sensorNodeList;
	}
	public void setSensorNodeList(List<SensorNode> sensorNodeList) {
		this.sensorNodeList = sensorNodeList;
	}
	public List<Message> getTransmittedMessageList() {
		return listOfMessagesArrivedToSinks;
	}
	public void addTransmittedMessage(Message m) {
		this.listOfMessagesArrivedToSinks.add(m);
	}

	public NetworkParameters getNp() {
		return np;
	}
	public void setNp(NetworkParameters np) {
		this.np = np;
	}
	public MobileSinkCtrl getMsc() {
		return msc;
	}
	public void setMsc(MobileSinkCtrl msc) {
		this.msc = msc;
	}
	public void updateMessages(MobilityProcessor mp) {
	/*
		// this generates messages according to sensing red zones // not used anymore for now
		for(int i=0;i<mp.getHumanList().size();i++){
			// for each human, check if there is any event that can be detected
			Human h = mp.getHumanList().get(i);
			for(int j=0;j<mp.getTpm().getRedZoneList().size();j++){
				RedZone r = mp.getTpm().getRedZoneList().get(j);
				if(r.getActiveStartTime()<=mp.getCurrentSimulationTime() && r.getActiveEndTime()>=mp.getCurrentSimulationTime()){
					// event is active, check if the mobile sink can check the event
					double dist = MathFunctions.findDistanceBetweenTwoPoints(h.getPositionPoint(),r.getCenterPoint()) - r.getRadius();		
					if(dist>np.getSensorSensingRange()) continue;
					// check if event was already detected
					if(sensorNodeList.get(i).getKnownEvents().contains(r.getCenterPoint()))
					continue;
						
					// event is detected, create message and then send the information to the neighbor sinks or sensor nodes
					Message m = new Message(messageIDCounter,i, h.getPositionPoint(),r.getCenterPoint(),mp.getCurrentSimulationTime(),np.getMessagePacketSize(),false,0,0);			
					listOfAllMessages.add(m);
					// add the message to storage of the sensor
					SensorNode sn = sensorNodeList.get(i);
					sn.addSensedMessage(messageIDCounter);
					sn.addMessageToBuffer(m);
					sn.addKnownEventLocation(r.getCenterPoint());
					messageIDCounter++;
					sensorNodeList.set(i, sn);
					
				}
			}			
		}
		*/
		for(int i=0;i<effectedPeopleActiveTimeStartList.size();i++){
		
		//	System.out.println(effectedPeopleActiveTimeStartList.get(i) + " " + mp.getCurrentSimulationTime() );
			if(effectedPeopleActiveTimeStartList.get(i)<=mp.getCurrentSimulationTime() && effectedPeopleActiveTimeEndList.get(i) >= mp.getCurrentSimulationTime()){	
				boolean noActivePersonFlag = false;
				if(effectedPeopleIDList.size()<=i){
					// this effected person is not selected yet, select one randomly
					List<Integer> activePeopleList = new ArrayList<Integer>();
					for(int a=0;a<mp.getHumanList().size();a++){
						if(mp.getHumanList().get(a).isActive()){
							activePeopleList.add(a);
						}
					}
					if(activePeopleList.size()==0){ // no active people left in the map, do not add any more people by breaking this big loop
						noActivePersonFlag = true;
						break;
					}
					Random r = new Random();
					while(true){
						int tmp = r.nextInt(activePeopleList.size());
						tmp = activePeopleList.get(tmp);
						if(!effectedPeopleIDList.contains(tmp)){ // this person is not selected yet, so it`s okay for now
							// find the corresponding person and see if he is active or no
							int persId = sensorNodeList.get(tmp).getPersonID();
							if(mp.getHumanList().get(persId).isActive()){ // this person is okay, just add him to the list
								effectedPeopleIDList.add(tmp);  break;
							}
						}
					}
				}
				
				if(noActivePersonFlag) break;// do not create a new event
			
				// event is active, check if the mobile sink can check the event and create message if necessary
				int id = effectedPeopleIDList.get(i);
				for(int j=0;j<mp.getHumanList().size();j++){
					if(id==j) continue; // cannot sense its own event
					Point disLoc = mp.getHumanList().get(id).getPositionPoint();
					Human h = mp.getHumanList().get(j);
					double dist = MathFunctions.findDistanceBetweenTwoPoints(h.getPositionPoint(),disLoc);		
					if(dist>np.getSensorSensingRange()) continue;
					// check if event was already detected
					if(sensorNodeList.get(j).getKnownSensorsWithEvent().contains(id)) continue;
						
					// event is detected, create message and then send the information to the neighbor sinks or sensor nodes
					Message m = new Message(messageIDCounter,j, h.getPositionPoint(),disLoc,mp.getCurrentSimulationTime(),np.getMessagePacketSize(),false,0,0,id);			
					listOfAllMessages.add(m);
					// add the message to storage of the sensor
					SensorNode sn = sensorNodeList.get(j);
					sn.addSensedMessage(messageIDCounter);
					sn.addMessageToBuffer(m);
					sn.addKnownEventLocation(disLoc,id );
					messageIDCounter++;
					sensorNodeList.set(j, sn);
				}
		
				
			}
		}
		
		updateTransmissionOfMessages(mp);
		
		return;

	}
	private void updateTransmissionOfMessages(MobilityProcessor mp) {
		
	
		// for each sensor node, find session list and for each session find the transmissions
		updateSessionsOfSensorNodes(mp,false);
		
		updateSessionsOfMobileSinks(mp,true);
		
		// check nearby sensor nodes and mobile sinks
		// if no sessions, initiate by sending buffer info
		initiateNewSessions(mp);
		return;
		
			
	}
	private void initiateNewSessions(MobilityProcessor mp) {
		// initiate sessions between sensors
		Random r = new Random(); 

		for(int i=0;i<sensorNodeList.size();i++){
			SensorNode n = sensorNodeList.get(i);
	    	if(n.getMessageIDsInBuffer().size()==0) continue; // no need to initiate a new session while there is no message to send
			Human h= mp.getHumanList().get(n.getPersonID());
			if(!h.isActive() || h.isDead()) continue; // an inactive person cannot initiate a new session
			for(int j=0;j<sensorNodeList.size();j++){
				if(r.nextDouble() > np.getSendProbabilityOfSensorToSensor()) continue;
				if(i==j) continue;				
				SensorNode m = sensorNodeList.get(j);
				Human h2= mp.getHumanList().get(m.getPersonID());
				// we cannot initiate a new session with an inactive responder h2
				if(!h.isActive() || h.isDead()) continue; // an inactive person cannot initiate a new session
				
			    double  dist = MathFunctions.findDistanceBetweenTwoPoints(h.getPositionPoint(), h2.getPositionPoint());
			    if(dist<np.getSensorTransmissionRange()){ // the nodes are inside each others' transmission range, we can open a session if it does not exist
			    	boolean sessionFlag = false;
			    	for(int k = 0; k<sessionList.size();k++){
			    		Session tmp = sessionList.get(k);
			    		if(!tmp.isSessionFinished() && tmp.getInitiator() == i && tmp.getReplier() == j  && ! tmp.isWithMobileSink ){
			    			sessionFlag = true;
			    			break; // two sensors already have a session
			    		}
			    	}
		    		if(sessionFlag){ // sensor i and j already have a session, continue with the j+1
		    			continue;
		    		}
		    		// now, open session
		    		Session s = new Session(i, j, mp.getCurrentSimulationTime(), false);
		    		Transmission t = new Transmission(false, i, j, mp.getCurrentSimulationTime(), mp.getCurrentSimulationTime() + np.getTransmissionDuration(), 
		    				"SendingVector", n.getMessageIDsInBuffer(), null);
		    		s.addTransmission(t);
			    	sessionList.add(s);
			    }	    
			}
		}
		// initiate sessions with sinks
		for(int i=0;i<sensorNodeList.size();i++){
			SensorNode n = sensorNodeList.get(i);
	    	if(n.getMessageIDsInBuffer().size()==0) continue; // no need to initiate a new session while there is no message to send
			Human h= mp.getHumanList().get(n.getPersonID());
			if(!h.isActive() || h.isDead()) continue; // an inactive person cannot initiate a new session
			
			for(int j=0;j<msc.getMobileSinkList().size();j++){
				MobileSinkNode m = msc.getMobileSinkList().get(j);
				Human h2= m.getRobot();
			    double  dist = MathFunctions.findDistanceBetweenTwoPoints(h.getPositionPoint(), h2.getPositionPoint());
			    if(dist<np.getSensorTransmissionRange()){ // the nodes are inside each others' transmission range, we can open a session if it does not exist
			    	boolean sessionFlag = false;
			    	for(int k = 0; k<sessionList.size();k++){
			    		Session tmp = sessionList.get(k);
			    		if(!tmp.isSessionFinished() && tmp.getInitiator() == i && tmp.getReplier() == j  && tmp.isWithMobileSink ){
			    			sessionFlag = true;
			    			break; // two sensors already have a session
			    		}
			    	}
		    		if(sessionFlag){ // sensor i and j already have a session, continue with the j+1
		    			continue;
		    		}
		    		// now, open session
		    		Session s = new Session(i, j, mp.getCurrentSimulationTime(), true);
		    		Transmission t = new Transmission(true, i, j, mp.getCurrentSimulationTime(), mp.getCurrentSimulationTime() + np.getTransmissionDuration(), 
		    				"SendingVector", n.getMessageIDsInBuffer(), null);
		    		m.addDetectedSensorList(i, mp.getCurrentSimulationTime());
		    		msc.replaceMobileSinkList(j, m);
		    		s.addTransmission(t);
		    		sessionList.add(s);
			    }	    
			}
		}

	}
	private void updateSessionsOfSensorNodes(MobilityProcessor mp, boolean isMobileSink) {
		Random r = new Random();
		
		for(int s=0;s<sessionList.size();s++){
			// check the ranges between session elements
			if(sessionList.get(s).isSessionFinished()) continue; 
			Session session = sessionList.get(s);
			if(!session.isWithMobileSink()){
				// check distance between sensors
				int person1 = sensorNodeList.get(session.getInitiator()).getPersonID();
				int person2 = sensorNodeList.get(session.getReplier()).getPersonID();
				Human h1= mp.getHumanList().get(person1); Human h2= mp.getHumanList().get(person2);
				double dist = MathFunctions.findDistanceBetweenTwoPoints(h1.getPositionPoint(), h2.getPositionPoint());
				if( dist > np.getSensorTransmissionRange()){
					session.setSessionFinished(true);
				}
			}
			else{// is with mobile sink	
				int person1 = sensorNodeList.get(session.getInitiator()).getPersonID(); // initiator always must be the sensor
				int person2 = session.getReplier();
				Human h1= mp.getHumanList().get(person1); Human h2= msc.getMobileSinkList().get(person2).getRobot();
				double dist = MathFunctions.findDistanceBetweenTwoPoints(h1.getPositionPoint(), h2.getPositionPoint());
				if( dist > np.getSensorTransmissionRange()){
					session.setSessionFinished(true);
				}
			}
		}
		
		for(int nodeIndex=0;nodeIndex<sensorNodeList.size();nodeIndex++){
			SensorNode sn = sensorNodeList.get(nodeIndex);
			
			for(int s=0;s<sessionList.size();s++){
				Session session = sessionList.get(s);				
				Transmission newTransmission = null;
				if(session.isSessionFinished()) continue; // session already finished no need to check for transmissions
				
				
				if(session.getInitiator()!=nodeIndex && session.getReplier() != nodeIndex) continue; // this session has nothing to do with current sensor node
			
				List<Transmission> transmissionList = session.getTransmissionList();
				
				int t= transmissionList.size() -1;
				// check only the last transmission in the session
				Transmission trans = transmissionList.get(t);				
				if(trans.isToSink()) continue;
				
				if(mp.getCurrentSimulationTime()<trans.getExpectedReceiveTime()) continue; // not sent yet, on the way
				if(trans.isArrivedToReceiverNode()) continue; // already arrived transmission, continue
				if(!trans.isArrivedToReceiverNode() && mp.getCurrentSimulationTime()>trans.getExpectedReceiveTime()){ // error in one of the transmissions,
					// session is disconnected, mark session as closed
					session.setSessionFinished(true); break; // no need to check other transmission in the session
				}
				if(trans.getToID() != nodeIndex){ continue;} // this transmission is not targeting this sensor node, find one which targets this sensor
				
				//the above line also covers the transmissions from mobile sinks to sensor nodes
			    // all before this are arrived and came the new transmission, or this one is the first one and time does not pass yet
			    // succesfully received the transmission   	
				// types "SendVector" "Request", "Response"
				
				if(r.nextDouble()>np.getTransmissionProbability()) continue; // transmission did not occur, continue and later the session will be closed after time check
					
				// because transmission (with index j in currentTransmissions) was received, set this information in system
				trans.setArrivedToReceiverNode(true); 

				if(trans.getTransmissionType().equalsIgnoreCase("SendingVector")){
					// if initiation requested, do comparison of buffers and find unknowns
					List<Integer> receivedBufferInfo = trans.getMessageIdListForBufferInfo();
					List<Integer> unknownMessages = new ArrayList<Integer>();
					for(int k=0; k< receivedBufferInfo.size();k++){
						// vectoral comparison
						if(!sn.getMessageIDsInBuffer().contains(receivedBufferInfo.get(k))){
							unknownMessages.add(receivedBufferInfo.get(k));
						}
					}
					
					// 2nd PHASE: send request to initiator  (initator cannot be mobile sink, it must be a sensor)
					newTransmission = new Transmission(false, nodeIndex, trans.getFromID(), mp.getCurrentSimulationTime(), 
							mp.getCurrentSimulationTime()+np.getTransmissionDuration(), "RequestForUnknowns",unknownMessages, null);
					
				}
				else if(trans.getTransmissionType().equalsIgnoreCase("RequestForUnknowns")){
					// initiator got the 2nd message: reply if needed  
					List<Integer> receivedUnknownMessageIDList = trans.getMessageIdListForBufferInfo();
					List<Message> listOfMessagesToBeSent = new ArrayList<Message>();
					List<Integer> listOfMessageIDsToBeSent = new ArrayList<Integer>();
					
					// create a packet which has all unknown messages which currently exist (if they still exist)
					for(int k=0; k<receivedUnknownMessageIDList.size();k++){
						// vectoral comparison
						if(!sn.getMessageIDsInBuffer().contains(receivedUnknownMessageIDList.get(k))) continue; // initiator does not contain this message anymore
						
						// this requested message is in the buffer of initiator. so it can add message to the transmission packet
						for(int y=0;y<sn.getMessageIDsInBuffer().size();y++){
							if(receivedUnknownMessageIDList.get(k) == sn.getMessageIDsInBuffer().get(y)){ // this is the index of message we are looking for
								listOfMessagesToBeSent.add(sn.getMessagesInBuffer().get(y));
								listOfMessageIDsToBeSent.add(sn.getMessageIDsInBuffer().get(y));
							}
						}
					
					}
												
					// send response including message to node B, by creating a transmission which includes the messages
					if(session.isWithMobileSink()){ // send last message to sink
						newTransmission = new Transmission(true,nodeIndex,trans.getFromID(),mp.getCurrentSimulationTime(),
								mp.getCurrentSimulationTime()+np.getTransmissionDuration(),"ResponseIncludingMessages", listOfMessageIDsToBeSent, listOfMessagesToBeSent);
					}
					else{ // send last message to sensor
						newTransmission = new Transmission(false,nodeIndex,trans.getFromID(),mp.getCurrentSimulationTime(),
							mp.getCurrentSimulationTime()+np.getTransmissionDuration(),"ResponseIncludingMessages", listOfMessageIDsToBeSent, listOfMessagesToBeSent);
					}
					if(listOfMessageIDsToBeSent.size()!= listOfMessageIDsToBeSent.size()){
						System.out.println("Error: Error in number of messages to be sent.");
					}
				}
				else if(trans.getTransmissionType().equalsIgnoreCase("ResponseIncludingMessages")){
					// put unknown messages to your buffer, if they are still unknown
					session.setSessionFinished(true); // finish session

					List<Integer> receivedMessageIDs = trans.getMessageIdListForBufferInfo();
					List<Message> receivedMessages= trans.getSentMessageList();

					// put the message in buffer if the event does not currently exist
					for(int k=0; k< receivedMessageIDs.size();k++){
						//  check if this message already exist in the sensor node's buffer 
						if(sn.getMessageIDsInBuffer().contains(receivedMessageIDs.get(k))) continue;							
						
						
						// this requested message is not already in the buffer of Node B, so store this message. Otherwise ignore it.
						Message m = receivedMessages.get(k);
						
					//	if(sn.getKnownEvents().contains(m.getDisasterEventLocation())) continue; // already have a message about the same event, do not add message to bufffer
						if(sn.getKnownSensorsWithEvent().contains(m.getEffectedSensorID())) continue; // already have a message about the same sensor
						sn.addKnownEventLocation(m.getDisasterEventLocation(), m.getEffectedSensorID());
						
						m.addArrivalPathIDs(trans.getToID()); 
						m.addTransmissionTime(mp.getCurrentSimulationTime());
						
						// store the copy of this message in buffer
						sn.addMessageToBuffer(m);
						sn.increaseNumberOfReceivedMessages(1);
					}
					// no need for next transmission,this was the last step in session so skip this phase
				}
				else{
					System.out.println("Error: Wrong message type is used");
				}
				transmissionList.set(t, trans);	
				session.setTransmissionList(transmissionList); 
				if(newTransmission!=null){
					session.addTransmission(newTransmission);
				}
				sessionList.set(s, session); 
			}
			sensorNodeList.set(nodeIndex, sn);
		}
	}
	
	private void updateSessionsOfMobileSinks(MobilityProcessor mp, boolean isMobileSink) {
		Random r =new Random();
		for(int nodeIndex=0;nodeIndex<msc.mobileSinkList.size();nodeIndex++){
			MobileSinkNode sn = msc.mobileSinkList.get(nodeIndex);
			for(int s=0;s<sessionList.size();s++){
				Session session = sessionList.get(s);
				Transmission newTransmission =null;
				if(session.isSessionFinished()) continue; // session already finished no need to check for transmissions
				if(session.getInitiator()!=nodeIndex && session.getReplier() != nodeIndex) continue; // this session has nothing to do with current sensor node
				if(!session.isWithMobileSink) continue; // a sensor must be initiator while the session is with a mobile sink
				List<Transmission> transmissionList = session.getTransmissionList();
					
				int t = transmissionList.size() - 1; // select the last element in the transmission list of session
				// check for each transmission in the session
				Transmission trans = transmissionList.get(t);
				if(!trans.isToSink()) continue;
				if(mp.getCurrentSimulationTime()<trans.getExpectedReceiveTime()) continue; // not sent yet, on the way
				if(trans.isArrivedToReceiverNode()) continue;
				if(!trans.isArrivedToReceiverNode() && mp.getCurrentSimulationTime()>trans.getExpectedReceiveTime()){ // error in one of the transmissions,
					// session is disconnected, mark session as closed
					session.setSessionFinished(true); break; // no need to check other transmission in the session
				}
				// check the distances between sensor nodes and finish session if it is more than the transmission range
				
				
				if(trans.getToID() != nodeIndex) continue; // this transmission is not targeting this sensor node, find one which targets this sensor 
				// all before this are arrived and came the new transmission, or this one is the first one and time does not pass yet
				// succesfully received the transmission   	
				// types "SendVector" "Request", "Response"
				if(r.nextDouble()>np.getTransmissionProbability()) continue; // transmission did not occur, continue and later the session will be closed after time check

				
				// because transmission (with index j in currentTransmissions) was received, set this information in system
				trans.setArrivedToReceiverNode(true); 

				if(trans.getTransmissionType().equalsIgnoreCase("SendingVector")){
					// if initiation requested, do comparison of buffers and find unknowns
					List<Integer> receivedBufferInfo = trans.getMessageIdListForBufferInfo();
					List<Integer> unknownMessages = new ArrayList<Integer>();
					for(int k=0; k< receivedBufferInfo.size();k++){
						// vectoral comparison
						if(!sn.getReceivedMessageIDList().contains(receivedBufferInfo.get(k))){
							unknownMessages.add(receivedBufferInfo.get(k));
						}
					}
					
					// send request to initiator  (initator cannot be mobile sink, it must be a sensor)
					newTransmission = new Transmission(false, nodeIndex, trans.getFromID(), mp.getCurrentSimulationTime(), 
							mp.getCurrentSimulationTime()+np.getTransmissionDuration(), "RequestForUnknowns",unknownMessages,null);
					
				}
			
				else if(trans.getTransmissionType().equalsIgnoreCase("ResponseIncludingMessages")){
					// put unknown messages to your buffer, if they are still unknown
					session.setSessionFinished(true);

					List<Integer> receivedMessageIDs = trans.getMessageIdListForBufferInfo();
					List<Message> receivedMessages= trans.getSentMessageList();

					// put the message in buffer if they do not currently exist
					for(int k=0; k< receivedMessageIDs.size();k++){
						//  check if this message already exist in the sensor node's buffer 
						if(sn.getReceivedMessageIDList().contains(receivedMessageIDs.get(k)))continue;
						
						// this requested message is not already in the buffer of Mobile Sink Node B, so store this message. Otherwise ignore it.
						Message m = receivedMessages.get(k);
						if(this.foundEffectedPeopleIDList.contains(m.getEffectedSensorID())) continue; // drop the message if it is already received by another mobile sink
						
						this.foundEffectedPeopleIDList.add(m.getEffectedSensorID());
						m.setArrivedToSink(true);
						m.addArrivalPathIDs(trans.getToID()); 
						m.setArrivedSinkID(trans.getToID());
						m.addTransmissionTime(mp.getCurrentSimulationTime());
						m.setHopCount(); 
						m.setTotalDelay(mp.getCurrentSimulationTime());
						
						
						// store the copy of this message in buffer
						
						double distance = sn.addMessage(m, m.getMessageID());
						if(np.getEventMissTime() > (distance/np.getMobileSinkMaxSpeed())*2 + m.getTotalDelay()){
							// event is not missed
							sn.addEventMissed(false);
						}
						else{ // event is missed
							sn.addEventMissed(true);
						}
						
						sn.addSucessfullyCommunicatedSensorList(trans.fromID);
						sn.increaseNumberOfReceivedMessages(1);
						listOfMessagesArrivedToSinks.add(m);
					}
					// no need for next transmission,this was the last step in session so skip this phase
				}
				else if(trans.getTransmissionType().equalsIgnoreCase("RequestForUnknowns")){
					System.out.println("Error: Error in mobile sink transmission.");
				}
				else{
					System.out.println("Error: Wrong message type is used");
				}
				transmissionList.set(t, trans);	
				session.setTransmissionList(transmissionList); 
				if(newTransmission!=null){
					session.addTransmission(newTransmission);
				}
				sessionList.set(s, session); 
			}
			msc.replaceMobileSinkList(nodeIndex,sn);
		}
	}
	public List<Session> getSessionList() {
		return sessionList;
	}
	public List<Message> getListOfMessagesArrivedToSinks() {
		return listOfMessagesArrivedToSinks;
	}
	public List<Message> getListOfAllMessages() {
		return listOfAllMessages;
	}
	public int getMessageIDCounter() {
		return messageIDCounter;
	}
	public List<Integer> getEffectedPeopleIDList() {
		return effectedPeopleIDList;
	}
	public List<Double> getEffectedPeopleActiveTimeStartList() {
		return effectedPeopleActiveTimeStartList;
	}
	public List<Double> getEffectedPeopleActiveTimeEndList() {
		return effectedPeopleActiveTimeEndList;
	}
	public List<Integer> getFoundEffectedPeopleIDList() {
		return foundEffectedPeopleIDList;
	}

	
	
	/*
	 * 
	 * 	private void updateTransmissionOfMessages(MobilityProcessor mp) {
		Random r = new Random();
		
	
		// for each sensor node, find session list and for each session find the transmissions
		updateSessionsOfSensorNodes(mp,false);
		
		updateSessionsOfMobileSinks(mp,true);

		
		
	
/*		
		for(int i=0;i<listOfCurrentTransmissions.size();i++){
			Transmission t = listOfCurrentTransmissions.get(i);
			if(t.getExpectedReceiveTime()>mp.getCurrentSimulationTime()){
				t.setNotSuccesfullyDelivered(true); 
				listOfCurrentTransmissions.remove(i);
				listOfFinishedTransmissions.add(t);
			}
		}*/
		
		
	/*	// sensor nodes
		for(int i=0;i<sensorNodeList.size();i++){
			SensorNode sn = sensorNodeList.get(i);
			List<Transmission> tempFinishedTransmissions = new ArrayList<Transmission>();
			// check for transmissions received
			for(int j=0;j<listOfCurrentTransmissions.size();j++){
				Transmission t = listOfCurrentTransmissions.get(j);
				if(t.getToID() != i) continue;
				// transmission is received by a probability
				if(r.nextDouble() > np.getTransmissionProbability()){ // not transmitted
					t.setNotSuccesfullyDelivered(true);
					tempFinishedTransmissions.add(t);
					listOfFinishedTransmissions.add(t);
				}
				else{ // succesfully received the transmission   	
					// types "SendVector" "Request", "Response"
					
					sn.addTransmissionToNode(t);
					sn.increaseNumberOfReceivedTransmissions(1); 
					
					if(t.getTransmissionType().equalsIgnoreCase("SendingVector")){
						// if initiation requested, do comparison of buffers and find unknowns
						List<Integer> receivedBufferInfo = t.getMessageIdListForBufferInfo();
						List<Integer> unknownMessages = new ArrayList<Integer>();
						for(int k=0; k< receivedBufferInfo.size();k++){
							// vectoral comparison
							if(!sn.getMessageIDsInBuffer().contains(receivedBufferInfo.get(k))){
								unknownMessages.add(receivedBufferInfo.get(k));
							}
						}
						
						// send request to initiator  
						Transmission sentTransmission = new Transmission(transmissionIDCounter, false,i,t.getFromID(),mp.getCurrentSimulationTime()
								,mp.getCurrentSimulationTime()+np.getTransmissionDuration(), "RequestForUnknowns",unknownMessages,null);
						listOfAllTransmissions.add(sentTransmission);
						listOfCurrentTransmissions.add(sentTransmission);
						sn.increaseNumberOfSentTransmissions(1);
						transmissionIDCounter++;
						
						
						// because transmission (with index j in currentTransmissions) was received, set this information in system
						
						listOfCurrentTransmissions.remove(j);
						t.setArrivedToReceiverNode(true); t.setNotSuccesfullyDelivered(false); 
						listOfFinishedTransmissions.add(t);
						
				
					}
					else if(t.getTransmissionType().equalsIgnoreCase("RequestForUnknowns")){
						// send reply if needed 
						// if initiation request, do comparison of buffers and find unknowns
						List<Integer> receivedUnknownMessageIDList = t.getMessageIdListForBufferInfo();
						List<Message> listOfMessagesToBeSent = new ArrayList<Message>();
						List<Integer> listOfMessageIDsToBeSent = new ArrayList<Integer>();

						// create a packet which has all unknown messages which currently exist (if they still exist)
						
						for(int k=0; k< receivedUnknownMessageIDList.size();k++){
							// vectoral comparison
							if(sn.getMessageIDsInBuffer().contains(receivedUnknownMessageIDList.get(k))){
								// this requested message is in the buffer of initiator. so it can add message to the transmission packet
								for(int y=0;y<sn.getMessageIDsInBuffer().size();y++){
									if(sn.getMessageIDsInBuffer().get(y) == receivedUnknownMessageIDList.get(k)){ // this is the index of message we are looking for
										listOfMessagesToBeSent.add(sn.getMessagesInBuffer().get(y));
										listOfMessageIDsToBeSent.add(sn.getMessageIDsInBuffer().get(y));
									}
								}
							}
						}
						
						// send response including message to node B, by creating a transmission which includes the messages
						
						Transmission sentTransmission = new Transmission(transmissionIDCounter,false,i,t.getFromID(),mp.getCurrentSimulationTime(),
								mp.getCurrentSimulationTime()+np.getTransmissionDuration(),"ResponseIncludingMessages", listOfMessageIDsToBeSent, listOfMessagesToBeSent);
						
						if(listOfMessageIDsToBeSent.size()!= listOfMessageIDsToBeSent.size()){
							System.out.println("Error: Error in number of messages to be sent.");
						}
						// set systems new state
						listOfAllTransmissions.add(sentTransmission);
						listOfCurrentTransmissions.add(sentTransmission);
						transmissionIDCounter++;
						//set sensor's new state
						sn.increaseNumberOfSentTransmissions(1);
						sn.increaseNumberOfSentMessages(listOfMessageIDsToBeSent.size());
						sn.addTransmission(t);
						
						// because transmission (with index j in currentTransmissions) was received, set this information in system
						
						listOfCurrentTransmissions.remove(j);
						t.setArrivedToReceiverNode(true); t.setNotSuccesfullyDelivered(false); 
						listOfFinishedTransmissions.add(t);
					}
					else if(t.getTransmissionType().equalsIgnoreCase("ResponseIncludingMessages")){
						// put unknown messages to your buffer, if they are still unknown
						
						List<Integer> receivedMessageIDs = t.getMessageIdListForBufferInfo();
						List<Message> receivedMessages= t.getSentMessageList();
	
						// put the message in buffer if they do not currently exist
						for(int k=0; k< receivedMessageIDs.size();k++){
							//  check if this message already exist in the sensor node's buffer 
							if(! sn.getMessageIDsInBuffer().contains(receivedMessageIDs.get(k))){
								// this requested message is not already in the buffer of Node B, so store this message. Otherwise ignore it.
								Message m = receivedMessages.get(k);
								m.addArrivalPathIDs(t.getToID()); 
								m.addTransmissionTime(mp.getCurrentSimulationTime());
								
								// store the copy of this message in buffer
								sn.addMessageToBuffer(m);
								sn.increaseNumberOfReceivedMessages(1);
							}
							
						}
						
						// no need for next transmission,this was the last step in session so skip this phase
						
						
						// because transmission (with index j in currentTransmissions) was received, set this information in system
						listOfCurrentTransmissions.remove(j);
						t.setArrivedToReceiverNode(true); t.setNotSuccesfullyDelivered(false); 
						listOfFinishedTransmissions.add(t);
					}
					else{
						System.out.println("Error: Wrong message type is used");
					}
	
					
				}
				
				
			} 
			// check nearby sensor nodes
				// if no sessions, initiate by sending buffer info
			
			// check nearby mobile sinks
				// send buffered messages
			
		} */



}
