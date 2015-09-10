package network;


public class Node {
	double batteryLevel;
	double transmissionRange;
	
//	List<Transmission> listOfTransmissionsToNode;
//	List<Transmission> listOfTransmissionsFromNode;

//	List<Integer> historyOfAllSeenMessageIDs;

	
	
	double energyConsumptionPerTransmission;
	int numberOfSentMessages;
	int numberOfReceivedMessages;
	
//	int numberOfSentTransmissions;
//	int numberOfReceivedTransmissions;
	
	public Node(double batteryLevel, double transmissionRange,
			double energyConsumptionPerTransmission
		) {
		super();
		this.batteryLevel = batteryLevel;
		this.transmissionRange = transmissionRange;
		this.energyConsumptionPerTransmission = energyConsumptionPerTransmission;
//		this.numberOfSentTransmissions =0;
//		this.numberOfReceivedTransmissions =0;
		this.numberOfSentMessages = 0;
		this.numberOfReceivedMessages = 0;
//		this.listOfTransmissionsToNode = new ArrayList<Transmission>();
	//	this.historyOfAllSeenMessageIDs = new ArrayList<Integer>();

	}

	
/*	public List<Transmission> getTransmissionList() {
		return listOfTransmissionsToNode;
	}

	public void addTransmission(Transmission t){
		this.listOfTransmissionsToNode.add(t);
	}
	*/
/*	public void addMessageIDToSeenMessageIDs(int messageID){
		if(!historyOfAllSeenMessageIDs.contains(messageID)){
			this.historyOfAllSeenMessageIDs.add(messageID);
		}
	}
*/

	public double getBatteryLevel() {
		return batteryLevel;
	}


	public double getTransmissionRange() {
		return transmissionRange;
	}

/*
	public List<Transmission> getListOfTransmissionsToNode() {
		return listOfTransmissionsToNode;
	}


	public List<Transmission> getListOfTransmissionsFromNode() {
		return listOfTransmissionsFromNode;
	}
*/




	public double getEnergyConsumptionPerTransmission() {
		return energyConsumptionPerTransmission;
	}


	public int getNumberOfSentMessages() {
		return numberOfSentMessages;
	}


	public int getNumberOfReceivedMessages() {
		return numberOfReceivedMessages;
	}


/*	public int getNumberOfSentTransmissions() {
		return numberOfSentTransmissions;
	}


	public int getNumberOfReceivedTransmissions() {
		return numberOfReceivedTransmissions;
	}
	public void addTransmissionFromNode(Transmission t){
		this.listOfTransmissionsFromNode.add(t);
	}
	public void addTransmissionToNode(Transmission t){
		this.listOfTransmissionsToNode.add(t);
	}
	public void increaseNumberOfReceivedTransmissions(int x){
		this.numberOfReceivedTransmissions = numberOfReceivedTransmissions + x;
	}
	public void increaseNumberOfSentTransmissions(int x){
		this.numberOfSentTransmissions = numberOfSentTransmissions + x;
	}
	*/
	public void increaseNumberOfReceivedMessages(int x){
		this.numberOfReceivedMessages = numberOfReceivedMessages + x;
	}
	public void increaseNumberOfSentMessages(int x){
		this.numberOfSentMessages = numberOfSentMessages + x;
	}


	public void setBatteryLevel(double batteryLevel) {
		this.batteryLevel = batteryLevel;
	}


	public void setTransmissionRange(double transmissionRange) {
		this.transmissionRange = transmissionRange;
	}

/*
	public void setListOfTransmissionsToNode(
			List<Transmission> listOfTransmissionsToNode) {
		this.listOfTransmissionsToNode = listOfTransmissionsToNode;
	}


	public void setListOfTransmissionsFromNode(
			List<Transmission> listOfTransmissionsFromNode) {
		this.listOfTransmissionsFromNode = listOfTransmissionsFromNode;
	}
*/



	public void setEnergyConsumptionPerTransmission(
			double energyConsumptionPerTransmission) {
		this.energyConsumptionPerTransmission = energyConsumptionPerTransmission;
	}


	public void setNumberOfSentMessages(int numberOfSentMessages) {
		this.numberOfSentMessages = numberOfSentMessages;
	}


	public void setNumberOfReceivedMessages(int numberOfReceivedMessages) {
		this.numberOfReceivedMessages = numberOfReceivedMessages;
	}

/*
	public void setNumberOfSentTransmissions(int numberOfSentTransmissions) {
		this.numberOfSentTransmissions = numberOfSentTransmissions;
	}


	public void setNumberOfReceivedTransmissions(int numberOfReceivedTransmissions) {
		this.numberOfReceivedTransmissions = numberOfReceivedTransmissions;
	}
*/	
	


	
}
