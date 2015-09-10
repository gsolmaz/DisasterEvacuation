package osm;

import java.util.HashMap;

/**
 * @author Gurkan Solmaz
 * 		   Department of EECS - University of Central Florida
 * 		   Disaster Mobility - Spring 2013
 * 		   Advisor: Dr. Damla Turgut
 */
public class OsmNode {

	long id;
	long uid;
	double lat;
	double lon;
	boolean isVisible;
	HashMap<String, String> tagMap;
	
	public OsmNode(long id, long uid, double lat, double lon, boolean isVisible) {
		super();
		this.id = id;
		this.uid = uid;
		this.lat = lat;
		this.lon = lon;
		this.isVisible = isVisible;
		tagMap = new HashMap<String, String>();
	}
	public void addTag(String key, String value){
		tagMap.put(key, value);
	}
	public double getLat() {
		return lat;
	}
	public double getLon() {
		return lon;
	}
	public boolean isVisible() {
		return isVisible;
	}
	public HashMap<String, String> getTagMap() {
		return tagMap;
	}
	public long getId() {
		return id;
	}
	 
}
