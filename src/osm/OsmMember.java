package osm;
/**
 * @author Gurkan Solmaz
 * 		   Department of EECS - University of Central Florida
 * 		   Disaster Mobility - Spring 2013
 * 		   Advisor: Dr. Damla Turgut
 */
public class OsmMember {
	String type;
	String role;
	long refId;
	public OsmMember(String type, String role, long refId) {
		super();
		this.type = type;
		this.role = role;
		this.refId = refId;
	}
	
	
}
