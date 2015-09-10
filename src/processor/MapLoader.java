package processor;
/**
 * @author Gurkan Solmaz
 * 		   Department of EECS - University of Central Florida
 * 		   Disaster Mobility - Spring 2013
 * 		   Advisor: Dr. Damla Turgut
 */
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import osm.OsmBounds;
import osm.OsmMap;
import osm.OsmMember;
import osm.OsmNode;
import osm.OsmRelation;
import osm.OsmWay;
import uk.me.jstott.jcoord.LatLng;
public class MapLoader {
		
	String mapName;
	OsmMap osmMap;
	
	public MapLoader(String mapName) {
		super();
		this.mapName = mapName;
		loadMap(mapName);
	}

	private void loadMap(String mapName) {
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		try {
		    builder = builderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
		    e.printStackTrace();  
		}
		try {
			OsmBounds osmBounds = null;
			List<OsmNode> osmNodeList = new ArrayList<OsmNode>();
			List<OsmWay> osmWayList = new ArrayList<OsmWay>();
			List<OsmRelation> osmRelationList = new ArrayList<OsmRelation>();
			HashMap<Long, LatLng> osmNodeIndexMap = new HashMap<Long, LatLng>();
			
		    Document document = builder.parse(new FileInputStream("maps\\" + mapName + ".osm"));
			Element rootElement = document.getDocumentElement();
			NodeList nodes= rootElement.getChildNodes();
			
			for(int i=0; i<nodes.getLength(); i++){
				  Node node = nodes.item(i);
				  if(node instanceof Element){
				    //a child element to process
				    Element child = (Element) node;
				    String childName= child.getNodeName();
				    
				    if(childName.equalsIgnoreCase("bounds")){// get the first (bounce) node   
					    osmBounds = createOsmBounds(child);
				    }
				    else if(childName.equalsIgnoreCase("node")){ // get nodes
				    	OsmNode tmpNode = createOsmNode(child);
				    	osmNodeList.add(tmpNode);
				    	// put the node to hashmap at the same time with putting to arraylist
				    	osmNodeIndexMap.put(tmpNode.getId(), new LatLng(tmpNode.getLat(), tmpNode.getLon()));
				    }
				    else if(childName.equalsIgnoreCase("way")){ // get ways with nodes and tags
				    	OsmWay tmpWay = createOsmWay(child);
				    	osmWayList.add(tmpWay);
				    }
				    else if(childName.equalsIgnoreCase("relation")){ // get the relations
				    	OsmRelation tmpRelation = createOsmRelation(child);
				    	osmRelationList.add(tmpRelation);
				    }
				    else{
				    	System.out.println("Error: Unexpected element:" + childName + " (Expected : bounds,node,way or relation !");
				    }
				  }
				}
			 osmMap = new OsmMap(osmBounds, osmNodeList, osmWayList, osmRelationList, osmNodeIndexMap);


		} catch (SAXException e) {
		    e.printStackTrace();
		} catch (IOException e) {
		    e.printStackTrace();
		}
	
	}

	public OsmMap getOsmMap(){
		return osmMap;
	}
	private OsmRelation createOsmRelation(Element node) {
		String idStr = node.getAttribute("id");
		String uidStr = node.getAttribute("uid");
		String visibleStr = node.getAttribute("visible");
		long id = Long.parseLong(idStr);
		long uid = Long.parseLong(uidStr);
		boolean visible=false;
		if(visibleStr == null || visibleStr.equalsIgnoreCase("true")){
			visible=true;
		}
		List<OsmMember> memberList = new ArrayList<OsmMember>();
		HashMap<String,String> tagMap = new HashMap<String, String>();

		NodeList children= node.getChildNodes();
		for(int i=0; i<children.getLength(); i++){
			  Node childNode = children.item(i);
			  if(childNode instanceof Element){
			    //a child element to process
			    Element child = (Element) childNode;
			    String childName= child.getNodeName(); // member or tag
			    
			    if(childName.equalsIgnoreCase("member")){
			    	String type = child.getAttribute("type");
			    	String role = child.getAttribute("role");		    	
			    	String tmpStrId = child.getAttribute("ref");
			    	long refId = Long.parseLong(tmpStrId);
			    	OsmMember osmMember = new OsmMember(type, role, refId);
			    	memberList.add(osmMember);
			    }
			    else if(childName.equalsIgnoreCase("tag")){
			        String key = child.getAttribute("k");
					String value = child.getAttribute("v");
					tagMap.put(key,value);
			    }
			    else{
			    	System.out.println("Error: Unexpected element:" + childName + " (Expected : member or tag!");
			    }
			 
			  }
		}
		OsmRelation osmRelation = new OsmRelation(id, uid, visible, memberList, tagMap);
		return osmRelation;
	}

	private OsmWay createOsmWay(Element node) {
		String idStr = node.getAttribute("id");
		String uidStr = node.getAttribute("uid");
		String visibleStr = node.getAttribute("visible");
		long id = Long.parseLong(idStr);
		long uid = Long.parseLong(uidStr);
		boolean visible=false;
		if(visibleStr == null || visibleStr.equalsIgnoreCase("true")){
			visible=true;
		}
				
		List<Long> nodeIdList = new ArrayList<Long>();
		HashMap<String,String> tagMap = new HashMap<String, String>();
		
		NodeList children= node.getChildNodes();
		for(int i=0; i<children.getLength(); i++){
			  Node childNode = children.item(i);
			  if(childNode instanceof Element){
			    //a child element to process
			    Element child = (Element) childNode;
			    String childName= child.getNodeName(); // nd or tag
			    
			    if(childName.equalsIgnoreCase("nd")){
			    	String tmpStrId = child.getAttribute("ref");
			    	long ndId = Long.parseLong(tmpStrId);
			    	nodeIdList.add(ndId);
			    }
			    else if(childName.equalsIgnoreCase("tag")){
			        String key = child.getAttribute("k");
					String value = child.getAttribute("v");
					tagMap.put(key,value);
			    }
			    else{
			    	System.out.println("Error: Unexpected element:" + childName + " (Expected : nd or tag!");
			    }
			 
			  }
		}
		OsmWay osmWay =new OsmWay(id, uid, visible, tagMap, nodeIdList);
		return osmWay;
	}

	private OsmNode createOsmNode(Element node) {
		String idStr = node.getAttribute("id");
		String latStr = node.getAttribute("lat");
		String lonStr = node.getAttribute("lon");
		String uidStr = node.getAttribute("uid");
		String visibleStr = node.getAttribute("visible");
		long id = Long.parseLong(idStr);
		double lat = Double.parseDouble(latStr);
		double lon = Double.parseDouble(lonStr);
		long uid = Long.parseLong(uidStr);
		boolean visible=false;
		if(visibleStr == null || visibleStr.equalsIgnoreCase("true")){
			visible=true;
		}
				
		OsmNode tmpOsmNode = new OsmNode(id, uid, lat, lon, visible);
		
		NodeList tags= node.getChildNodes();
		for(int i=0; i<tags.getLength(); i++){
			  Node tag = tags.item(i);
			  if(tag instanceof Element){
			    //a child element to process
			    Element child = (Element) tag;
			    String childName= child.getNodeName(); // tag
			    if(!childName.equalsIgnoreCase("tag")){
			    	System.out.println("Error: Unexpected element:" + childName + " (Expected :Tag!");
			    }
			    String key = child.getAttribute("k");
				String value = child.getAttribute("v");
				tmpOsmNode.addTag(key,value);    
			  }
		}
		return tmpOsmNode;
	}

	private OsmBounds createOsmBounds(Element child) {
			// set bounds of the map
			String minLatStr = child.getAttribute("minlat");
		    String maxLatStr =  child.getAttribute("maxlat");
		    String minLonStr = child.getAttribute("minlon");
		    String maxLonStr = child.getAttribute("maxlon");
		    double minLat = Double.parseDouble(minLatStr);
		    double maxLat = Double.parseDouble(maxLatStr);
		    double minLon = Double.parseDouble(minLonStr);
		    double maxLon = Double.parseDouble(maxLonStr);
			OsmBounds returnValue = new OsmBounds(minLat, maxLat, minLon, maxLon);
		    return returnValue;	
	}
	
	
}
