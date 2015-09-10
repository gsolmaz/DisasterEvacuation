package osm;

import java.util.HashMap;
import java.util.List;

/**
 * @author Gurkan Solmaz
 * 		   Department of EECS - University of Central Florida
 * 		   Disaster Mobility - Spring 2013
 * 		   Advisor: Dr. Damla Turgut
 */
public class OsmRelation {
	long id;
	long uid;
	boolean isVisible;
	
	List<OsmMember> osmMemberList;
	HashMap<String, String> tagMap;
	public OsmRelation(long id, long uid, boolean isVisible,
			List<OsmMember> osmMemberList, HashMap<String, String> tagMap) {
		super();
		this.id = id;
		this.uid = uid;
		this.isVisible = isVisible;
		this.osmMemberList = osmMemberList;
		this.tagMap = tagMap;
	}

	

}
