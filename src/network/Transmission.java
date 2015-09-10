package network;

import java.util.List;

public class Transmission {
	
	boolean isToSink;
	int fromID;
	int toID;
	double sendingStartTime;
	double expectedReceiveTime; // check if we arrived to that time or no
	boolean isArrivedToReceiverNode;	
	
	boolean notSuccesfullyDelivered;
	String transmissionType; // "SendVector" "Request", "Response"
	List<Integer> messageIdListForBufferInfo;
	List<Message> sentMessageList;

	


	public Transmission(boolean isToSink, int fromID,
			int toID, double sendingStartTime, double expectedReceiveTime,
			String transmissionType, List<Integer> messageIdListForBufferInfo,
			List<Message> sentMessageList) {
		super();
		this.isToSink = isToSink;
		this.fromID = fromID;
		this.toID = toID;
		this.isArrivedToReceiverNode = false;
		this.sendingStartTime = sendingStartTime;
		this.expectedReceiveTime = expectedReceiveTime;
		this.transmissionType = transmissionType;
		this.messageIdListForBufferInfo = messageIdListForBufferInfo;
		this.sentMessageList = sentMessageList;
		this.notSuccesfullyDelivered = false;
	}
	public boolean isToSink() {
		return isToSink;
	}
	public void setToSink(boolean isToSink) {
		this.isToSink = isToSink;
	}
	public int getFromID() {
		return fromID;
	}
	public void setFromID(int fromID) {
		this.fromID = fromID;
	}
	public int getToID() {
		return toID;
	}
	public void setToID(int toID) {
		this.toID = toID;
	}
	public double getSendingStartTime() {
		return sendingStartTime;
	}
	public void setSendingStartTime(double sendingStartTime) {
		this.sendingStartTime = sendingStartTime;
	}
	public double getExpectedReceiveTime() {
		return expectedReceiveTime;
	}
	public void setExpectedReceiveTime(double receiveTime) {
		this.expectedReceiveTime = receiveTime;
	}
	public boolean isArrivedToReceiverNode() {
		return isArrivedToReceiverNode;
	}
	public void setArrivedToReceiverNode(boolean isArrivedToReceiverNode) {
		this.isArrivedToReceiverNode = isArrivedToReceiverNode;
	}
	public String getTransmissionType() {
		return transmissionType;
	}
	public void setTransmissionType(String type) {
		this.transmissionType = type;
		
	}

	public List<Message> getSentMessageList() {
		return sentMessageList;
	}

	public List<Integer> getMessageIdListForBufferInfo() {
		return messageIdListForBufferInfo;
	}

	public boolean isNotSuccesfullyDelivered() {
		return notSuccesfullyDelivered;
	}

	public void setNotSuccesfullyDelivered(boolean notSuccesfullyDelivered) {
		this.notSuccesfullyDelivered = notSuccesfullyDelivered;
	}

	public void setSentMessageList(List<Message> sentMessageList) {
		this.sentMessageList = sentMessageList;
	}

	public void setMessageIdListForBufferInfo(
			List<Integer> messageIdListForBufferInfo) {
		this.messageIdListForBufferInfo = messageIdListForBufferInfo;
	}
	
	
	
}
