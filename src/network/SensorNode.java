package network;

import java.util.ArrayList;
import java.util.List;

import model.Point;

public class SensorNode extends Node {

	int personID;
	double sensingRange;
	int messageStorageCapacity;
	
	
	List<Integer> sensedMessageIDList;
	List<Message> messagesInBuffer; // cannot be more than message storage capacity
	List<Integer> messageIDsInBuffer;
	
	List<Message> messagesInTrash; // will be used for output only

	List<Point> knownEvents;
	List<Integer> knownSensorsWithEvent;
	
	public SensorNode(double batteryLevel, double transmissionRange,
			double energyConsumptionByTransmission, int personID, double sensingRange,
			int messageStorageCapacity) {
		super(batteryLevel, transmissionRange,
				energyConsumptionByTransmission);
		this.personID = personID;
		this.sensingRange = sensingRange;
		this.messageStorageCapacity = messageStorageCapacity;
		this.sensedMessageIDList = new ArrayList<Integer>();
		this.messagesInBuffer = new ArrayList<Message>();
		this.messagesInTrash = new ArrayList<Message>();
		this.messageIDsInBuffer = new ArrayList<Integer>();
		this.knownEvents = new ArrayList<Point>();
		this.knownSensorsWithEvent = new ArrayList<Integer>();
	}
	

	public void addSensedMessage(int x){
		sensedMessageIDList.add(x);
	}



	public int getPersonID() {
		return personID;
	}


	public double getSensingRange() {
		return sensingRange;
	}


	public int getMessageStorageCapacity() {
		return messageStorageCapacity;
	}


	public List<Integer> getSensedMessageIDList() {
		return sensedMessageIDList;
	}

	public void addMessageToBuffer(Message m){
		if(messagesInBuffer.size() == messageStorageCapacity ){
			sendMessageToTrash(1);
			this.messagesInBuffer.add(m);
			this.messageIDsInBuffer.add(m.getMessageID());
		}
		else if(messagesInBuffer.size()< messageStorageCapacity){
			this.messagesInBuffer.add(m);
			this.messageIDsInBuffer.add(m.getMessageID());
		}
		else if(messagesInBuffer.size()> messageStorageCapacity){
			System.out.println("Error: Message Buffer Overflow");
		}
	}

	public void sendMessageToTrash(int numberOfMessages){ // FIFO
		for(int i=0;i<numberOfMessages;i++){
			Message m = messagesInBuffer.remove(i);
			messageIDsInBuffer.remove(i);
			messagesInTrash.add(m);
		}
	}
	

	public List<Message> getMessagesInBuffer() {
		return messagesInBuffer;
	}


	public List<Message> getMessagesInTrash() {
		return messagesInTrash;
	}


	public List<Integer> getMessageIDsInBuffer() {
		return messageIDsInBuffer;
	}
	
	public List<Point> getKnownEvents() {
		return knownEvents;
	}


	public List<Integer> getKnownSensorsWithEvent() {
		return knownSensorsWithEvent;
	}


	public void addKnownEventLocation(Point p, int sensorIndex){
		this.knownEvents.add(p);
		this.knownSensorsWithEvent.add(sensorIndex);
	}

}
