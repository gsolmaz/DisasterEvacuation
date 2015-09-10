package network;

import java.util.ArrayList;
import java.util.List;

public class Session {
	
	int initiator; // Node A 
	int replier; // Node B
	double sessionStartTime;
	boolean isSessionFinished;
	List<Transmission> transmissionList;
	
	boolean isWithMobileSink;
	
	public Session(int initiator, int replier, double sessionStartTime, boolean isWithMobileSink) {
		super();
		this.initiator = initiator;
		this.replier = replier;
		this.sessionStartTime = sessionStartTime;
		this.isSessionFinished = false;
		this.transmissionList = new ArrayList<Transmission>();
		this.isWithMobileSink = isWithMobileSink;
	}
	public int getInitiator() {
		return initiator;
	}
	public void setInitiator(int initiator) {
		this.initiator = initiator;
	}
	public int getReplier() {
		return replier;
	}
	public void setReplier(int replier) {
		this.replier = replier;
	}
	public double getSessionStartTime() {
		return sessionStartTime;
	}
	public void setSessionStartTime(double sessionStartTime) {
		this.sessionStartTime = sessionStartTime;
	}
	public boolean isSessionFinished() {
		return isSessionFinished;
	}
	public void setSessionFinished(boolean isSessionFinished) {
		this.isSessionFinished = isSessionFinished;
	}
	
	public void addTransmission(Transmission t){
		this.transmissionList.add(t);
}
	public List<Transmission> getTransmissionList() {
		return transmissionList;
	}
	public boolean isWithMobileSink() {
		return isWithMobileSink;
	}
	public void setTransmissionList(List<Transmission> transmissionList) {
		this.transmissionList = transmissionList;
	}
	
}
