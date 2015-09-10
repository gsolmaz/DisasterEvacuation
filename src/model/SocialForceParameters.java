package model;
/**
 * @author Gurkan Solmaz
 * 		   Department of EECS - University of Central Florida
 * 		   Disaster Mobility - Spring 2013
 * 		   Advisor: Dr. Damla Turgut
 */
public class SocialForceParameters {
	double A; 				// interactionStrength
	double B; 				//interaction range 
	double deviationA; 		// -+ values possible for each individual, set it to 0 if individuals are  homogeneous
	double deviationB;		// -+ values possible for each individual, set it to 0 if individuals are  homogeneous
	double deltaT;		    //the delta t value used for elliptical model (by default, set deltaT =0,5)
	double lambda;  		// 0<= delta t <= 1, give values close to 0,1 as ideal value coming from evolutionary parameter optimization
	boolean isCircular;     // set if you use the circular or the elliptic model
	boolean isAngular;		// set if you use the angular dependence or not
	double relaxationTime;	// T(alpha) relaxation time
	public SocialForceParameters(double a, double deviationA, double b, 
			double deviationB, double deltaT, double lambda, double relaxationTime, boolean isCircular, boolean isAngular) {
		super();
		this.A = a;
		this.deviationA = deviationA;
		this.B = b;
		this.deviationB = deviationB;
		this.deltaT = deltaT;
		this.lambda = lambda;
		this.relaxationTime = relaxationTime;
		this.isCircular=isCircular;
		this.isAngular= isAngular;
	}
	public double getA() {
		return A;
	}
	public double getB() {
		return B;
	}
	public double getDeviationA() {
		return deviationA;
	}
	public double getDeviationB() {
		return deviationB;
	}
	public double getLambda() {
		return lambda;
	}
	public boolean isCircular() {
		return isCircular;
	}
	public boolean isAngular() {
		return isAngular;
	}
	public double getRelaxationTime() {
		return relaxationTime;
	}
	
}
