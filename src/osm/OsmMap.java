package osm;

import java.util.HashMap;
import java.util.List;

import uk.me.jstott.jcoord.LatLng;

/**
 * @author Gurkan Solmaz
 * 		   Department of EECS - University of Central Florida
 * 		   Disaster Mobility - Spring 2013
 * 		   Advisor: Dr. Damla Turgut
 */
public class OsmMap {
	OsmBounds osmBounds;
	List<OsmNode> osmNodeList;
	HashMap<Long, LatLng> osmNodeIndexMap;
	List<OsmWay> osmWayList;
	List<OsmRelation> osmRelationList;
	
	public OsmMap(OsmBounds osmBounds, List<OsmNode> osmNodeList,
			List<OsmWay> osmWayList, List<OsmRelation> osmRelationList, HashMap<Long, LatLng> osmNodeIndexMap) {
		super();
		this.osmBounds = osmBounds;
		this.osmNodeList = osmNodeList;
		this.osmWayList = osmWayList;
		this.osmRelationList = osmRelationList;
		this.osmNodeIndexMap = osmNodeIndexMap;
	
	}

	public OsmBounds getOsmBounds() {
		return osmBounds;
	}

	public List<OsmNode> getOsmNodeList() {
		return osmNodeList;
	}

	public List<OsmWay> getOsmWayList() {
		return osmWayList;
	}

	public List<OsmRelation> getOsmRelationList() {
		return osmRelationList;
	}

	public HashMap<Long, LatLng> getOsmNodeIndexMap() {
		return osmNodeIndexMap;
	}
	
	
}
