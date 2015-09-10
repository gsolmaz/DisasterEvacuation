/**
 * 
 */
package processor;


import java.util.Random;

import javax.vecmath.Vector2d;

import network.Grid;
import model.Point;

/**
 * @author Gurkan Solmaz
 * 		   Department of EECS - University of Central Florida
 * 		   Disaster Mobility - Spring 2013
 * 		   Advisor: Dr. Damla Turgut
 */
public class MathFunctions {
	// commonly used mathematical operations
	
	public static double pickRandomDoubleValueBetweenTwoNumbers(double min, double max){
		Random random = new Random();
		return min + ((max-min)* random.nextDouble());
	}
	
	public static int pickRandomIntegerValueBetweenTwoNumbers(int min, int max){
		Random random = new Random();
		return (int) (min + ((max-min)* random.nextDouble()));
	}
	
	public static double findDistanceBetweenTwoPoints(Point p1, Point p2){
		double returnValue = (p1.getX() - p2.getX()) *(p1.getX() - p2.getX());
		returnValue += (p1.getY() - p2.getY()) *(p1.getY() - p2.getY());
		return Math.sqrt(returnValue);
	}
	
	public static Vector2d findVectorBetweenTwoPoints(Point p1, Point p2){
		Vector2d returnValue = new Vector2d(p2.getX() - p1.getX() ,p2.getY() - p1.getY());
		return returnValue;
	}

	/*// same with the Vector.length() method
	 * public static double findScalarDistance(Vector2d distanceVector) {
		double returnValue = (distanceVector.x * distanceVector.x) + (distanceVector.y * distanceVector.y);
		return Math.sqrt(returnValue);
	}*/ 
	
	public static Vector2d findVectorDirectionBetweenTwoPoints(Point p1, Point p2){
		Vector2d vector = findVectorBetweenTwoPoints(p1,p2);
		vector.normalize();
		return vector;
	}
	
	public static Vector2d findSpeedVectorByTwoPointsAndVelocity (Point p1, Point p2, double velocity) {
		Vector2d unitVector =findVectorDirectionBetweenTwoPoints(p1, p2);
		Vector2d returnValue = multiplyVectorByScalar(unitVector, velocity);
		return returnValue;
	}
	
	public static Vector2d multiplyVectorByScalar(Vector2d vector, double scalar) {
		Vector2d returnValue = new Vector2d(vector.x*scalar, vector.y*scalar);
		return returnValue;
	}
	
	public static Vector2d substructVector (Vector2d v1, Vector2d v2) {
		Vector2d returnValue = new Vector2d(v1.x- v2.x, v1.y - v2.y);
		return returnValue;
	}
	public static Vector2d addVector (Vector2d v1, Vector2d v2) {
		Vector2d returnValue = new Vector2d(v1.x + v2.x, v1.y + v2.y);
		return returnValue;
	}
	public static boolean isPointInGrid(Point p, Grid g) {
		if(p.getX()<g.getMaxPoint().getX() && p.getX() > g.getMinPoint().getX() && 
		   p.getY()<g.getMaxPoint().getY() && p.getY() > g.getMinPoint().getY() ){
			return true;
		}
		else{			
			return false;
		}
	}
	
   public static double findMagnitudeOfVector(Vector2d v) {
	      return Math.sqrt ( v.x*v.x + v.y*v.y );
	  }

	
}
