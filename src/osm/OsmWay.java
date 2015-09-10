package osm;

import java.util.HashMap;
import java.util.List;

/**
 * @author Gurkan Solmaz
 * 		   Department of EECS - University of Central Florida
 * 		   Disaster Mobility - Spring 2013
 * 		   Advisor: Dr. Damla Turgut
 */
public class OsmWay {

	long id;
	long uid; 
	boolean isVisible;
	HashMap<String, String> tagMap;
	List<Long> nodeIdList;
	public OsmWay(long id, long uid, boolean visible,
			HashMap<String, String> tagMap, List<Long> nodeIdList) {
		super();
		this.id = id;
		this.uid = uid;
		this.isVisible = visible;
		this.tagMap = tagMap;
		this.nodeIdList = nodeIdList;
	}
	public long getId() {
		return id;
	}
	public boolean isVisible() {
		return isVisible;
	}
	public HashMap<String, String> getTagMap() {
		return tagMap;
	}
	public List<Long> getNodeIdList() {
		return nodeIdList;
	}
	public void setId(long id) {
		this.id = id;
	}
	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}
	public void setTagMap(HashMap<String, String> tagMap) {
		this.tagMap = tagMap;
	}
	public void setNodeIdList(List<Long> nodeIdList) {
		this.nodeIdList = nodeIdList;
	}
	

	
	
	
}
