package network;


public class NetworkParameters {

	double sensorTransmissionRange; // in meters
	double transmissionProbability; // probability of successful transmission
	int sensorMessageStorageCapacity; // capacity of sensors in terms of holding messages (in number of messages)
	double sensorEnergyConsumptionPerTransmission; 
	double sensorSensingRange; 
	int transmissionDuration;  // delay caused while transmission
	
	double sinkEnergyConsumptionPerTransmission; 
	double sinkEnergyConsumptionOfMovingPerMeter; 
	
	double batteryLevelOfSensorNodes;
	double batteryLevelOfSinkNodes;
	
	double messagePacketSize;
	
	int numberOfMobileSinkNodes;
	
	double squareGridDimensionSize;
	
	double eventMissTime;
	String sinkPlacementStrategy;
	double mobileSinkMaxSpeed;
	
	int numberOfEffectedPeople;
	
	double sinkRelativeMass;
	
	double sendProbabilityOfSensorToSensor;
	
	public NetworkParameters(	double sendProbabilityOfSensorToSensor, double sensorTransmissionRange,
			double transmissionProbability,
			int sensorMessageStorageCapacity,
			double sensorEnergyConsumptionPerTransmission,
			double sensorSensingRange, int transmissionDuration,
			double sinkEnergyConsumptionPerTransmission,
			double sinkEnergyConsumptionOfMovingPerMeter,
			double batteryLevelOfSensorNodes,
			double batteryLevelOfSinkNodes, double messagePacketSize, int numberOfMobileSinkNodes
			,String sinkPlacementStrategy, double squareGridDimensionSize, double mobileSinkMaxSpeed,double eventMissTime, int numberOfEffectedPeople, double sinkRelativeMass) {
		super();
		this.sendProbabilityOfSensorToSensor = sendProbabilityOfSensorToSensor;
		this.sensorTransmissionRange = sensorTransmissionRange;
		this.transmissionProbability = transmissionProbability;
		this.sensorMessageStorageCapacity = sensorMessageStorageCapacity;
		this.sensorEnergyConsumptionPerTransmission = sensorEnergyConsumptionPerTransmission;
		this.sensorSensingRange = sensorSensingRange;
		this.transmissionDuration = transmissionDuration;
		this.sinkEnergyConsumptionPerTransmission = sinkEnergyConsumptionPerTransmission;
		this.sinkEnergyConsumptionOfMovingPerMeter = sinkEnergyConsumptionOfMovingPerMeter;
		this.batteryLevelOfSensorNodes = batteryLevelOfSensorNodes;
		this.batteryLevelOfSinkNodes = batteryLevelOfSinkNodes;
		this.messagePacketSize = messagePacketSize;
		this.sinkPlacementStrategy = sinkPlacementStrategy;
		this.numberOfMobileSinkNodes = numberOfMobileSinkNodes;
		this.squareGridDimensionSize = squareGridDimensionSize;
		this.mobileSinkMaxSpeed= mobileSinkMaxSpeed;
		this.eventMissTime = eventMissTime;
		this.numberOfEffectedPeople= numberOfEffectedPeople;
		this.sinkRelativeMass =sinkRelativeMass;
	}

	public double getSensorTransmissionRange() {
		return sensorTransmissionRange;
	}

	public double getTransmissionProbability() {
		return transmissionProbability;
	}

	public int getSensorMessageStorageCapacity() {
		return sensorMessageStorageCapacity;
	}

	public double getSensorEnergyConsumptionPerTransmission() {
		return sensorEnergyConsumptionPerTransmission;
	}

	public double getSensorSensingRange() {
		return sensorSensingRange;
	}

	public int getTransmissionDuration() {
		return transmissionDuration;
	}

	public double getSinkEnergyConsumptionPerTransmission() {
		return sinkEnergyConsumptionPerTransmission;
	}

	public double getSinkEnergyConsumptionOfMovingPerMeter() {
		return sinkEnergyConsumptionOfMovingPerMeter;
	}

	public double getBatteryLevelOfSensorNodes() {
		return batteryLevelOfSensorNodes;
	}

	public double getBatteryLevelOfSinkNodes() {
		return batteryLevelOfSinkNodes;
	}

	public double getMessagePacketSize() {
		return messagePacketSize;
	}

	public int getNumberOfMobileSinkNodes() {
		return numberOfMobileSinkNodes;
	}

	public String getSinkPlacementStrategy() {
		return sinkPlacementStrategy;
	}

	public double getSquareGridDimensionSize() {
		return squareGridDimensionSize;
	}

	public double getMobileSinkMaxSpeed() {
		return mobileSinkMaxSpeed;
	}

	public double getEventMissTime() {
		return eventMissTime;
	}

	public void setEventMissTime(double eventMissTime) {
		this.eventMissTime = eventMissTime;
	}

	public int getNumberOfEffectedPeople() {
		return numberOfEffectedPeople;
	}

	public double getSinkRelativeMass() {
		return sinkRelativeMass;
	}

	public double getSendProbabilityOfSensorToSensor() {
		return sendProbabilityOfSensorToSensor;
	}

}
