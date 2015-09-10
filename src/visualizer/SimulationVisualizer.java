package visualizer;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.JFrame;
import javax.vecmath.Vector2d;

import map.Gate;
import map.Node;
import map.Obstacle;
import map.RedZone;
import map.Road;
import map.ThemeParkMap;
import model.Human;
import model.Point;
import network.Grid;
import network.MobileSinkNode;
import network.NetworkProcessor;

/**
 * @author Gurkan Solmaz
 * 		   Department of EECS - University of Central Florida
 * 		   Disaster Mobility - Spring 2013
 * 		   Advisor: Dr. Damla Turgut
 */
public class SimulationVisualizer extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5687055481193363161L;
	/**
	 * 
	 */
	private ThemeParkMap tpm;
	private List<Human> humanList;
	private NetworkProcessor network;
	private double currentTime; 
	
	private Image dbImage;
	private Graphics dbGraphics;
	//private Graphics tpmGraphics;
	
	private boolean resume;
	private boolean finish;
	
	public class ActionListener extends KeyAdapter{
		public void keyPressed(KeyEvent e){
			int keyCode = e.getKeyCode();
			if(keyCode == KeyEvent.VK_UP){
				resume = true;
			}
			else if(keyCode == KeyEvent.VK_RIGHT){
				finish = true;
			}
		}
		public void keyReleased(KeyEvent e){
			
		}
	}
	
	public SimulationVisualizer(ThemeParkMap tpm, NetworkProcessor network){
		addKeyListener(new ActionListener());
		setTitle("Theme Park Simulation-Disaster");
		setSize(700, 700);
		setResizable(false);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		resume=false;
		finish=false;
		this.tpm = tpm;
		this.network = network;
		this.currentTime = 0.0;

	}
	
	public void paint(Graphics g){
		
		dbImage = createImage(getWidth(), getHeight());
		dbGraphics = dbImage.getGraphics();
		paintComponent(dbGraphics);
		g.drawImage(dbImage, 0,0, this);

	}
	
	public void paintComponent(Graphics g){

		g = drawThemePark(g);

		repaint();
	}

	
	  
    private Graphics drawHumans(Graphics g,int margin, double ratio) {
    	// draw nodes
    	if(humanList==null) return g;
		for(int i=0; i<humanList.size(); i++){
			Human h = humanList.get(i);
			
			if(!h.isActive()){
				g.setColor(Color.red);
			}
			else if(h.isDead()){
				g.setColor(Color.gray);
			}
			else{
				g.setColor(Color.orange);
			}
			
			if(h.isSaved()){
				// Do not draw saved people !
				//g.setColor(Color.green);
				continue;
			}
		
			
			// draw a triangle to represent a human
			double scale = 35;
			
			int[] xArray = new int[3];
			int[] yArray = new int[3];
			// below point
			Point p = h.getPositionPoint();
			Vector2d tmpVel = h.getCurrentVelocity();
			
			// upper point
			Point p1 = new Point(p.getX() + tmpVel.x*scale , p.getY() + tmpVel.y*scale);
			// perpendicular unit vector
			Vector2d perp1 = new Vector2d(tmpVel.y* -1, tmpVel.x);
			Vector2d perp2 = new Vector2d(tmpVel.y, tmpVel.x*-1);
			// perpendicular unit vectors
			perp1.normalize(); perp2.normalize();
			
			// find the other two points
			perp1.scale(scale/2); perp2.scale(scale/2);		
			Point p2 = new Point(p.getX() + perp1.x, p.getY() + perp1.y);
			Point p3 = new Point(p.getX() + perp2.x, p.getY() + perp2.y);
			
			xArray[0] =  margin+ (int)Math.floor(p1.getX()*ratio); yArray[0] =(int)Math.floor(p1.getY()*ratio);
			xArray[1] =  margin+ (int)Math.floor(p2.getX()*ratio); yArray[1] =(int)Math.floor(p2.getY()*ratio);		
			xArray[2] =  margin+ (int)Math.floor(p3.getX()*ratio); yArray[2] =(int)Math.floor(p3.getY()*ratio);
		
			
			g.drawPolygon(xArray, yArray, 3);		
			if(!h.isSaved()){
				g.fillPolygon(xArray, yArray, 3);	
			}
			
	/*		g.setColor(Color.gray);
			int radius =3;
			
			g.drawOval(xArray[0]-radius, yArray[0]- radius, radius*2, radius*2);
			g.fillOval(xArray[0]- radius, yArray[0]- radius, radius*2, radius*2);*/
		}
		return g;
	}

	private Graphics drawThemePark(Graphics g) {
    	// find projection proportions
    	double minBound = Math.min(getHeight(), getWidth());
    	int margin = (int)Math.floor((getWidth() - getHeight())/2);
    	double maxWidth = Math.max(tpm.getTerrainLengthX(), tpm.getTerrainLengthY());
    	double ratio = minBound/maxWidth; // projection ratio
    	
    	// draw the background
    	g.setColor(Color.BLACK);
    	g.drawRect(0,0, getWidth(), getHeight());
    	g.fillRect(0,0, getWidth(), getHeight());
    	g.setColor(Color.WHITE);
    	g.drawRect(margin, 0,  getWidth() - 2*margin, getHeight());
    	g.fillRect(margin, 0,  getWidth() -2* margin, getHeight());
       	Font font = new Font ("Monospaced", Font.BOLD , 20); // Put Font.BOLD if you want to make them bold
    		g.setFont(font);
    		g.setColor(Color.RED);
    		g.drawString("Sim time:" + currentTime + " " + network.getNp().getSinkPlacementStrategy(), getWidth()-300, getHeight()-20);
    		
    		// set a standard font for texts in the visualizer 
        	font = new Font ("Monospaced", Font.PLAIN , 14); // Put Font.BOLD if you want to make them bold
    		g.setFont(font);
    	 
    	g= drawObjects(g, margin, ratio);
   
		return g;
}
    private Graphics drawObjects(Graphics g,int margin, double ratio){
		g = drawNodes(g,margin, ratio);
		g = drawRoads(g, margin, ratio);
		g = drawObstacles(g, margin, ratio);
		g = drawGates(g, margin, ratio);
		g = drawHumans(g,margin,ratio);
		g = drawRedZones(g,margin,ratio);
		g = drawMobileSinks(g,margin,ratio);
		g = drawGrids(g,margin,ratio);
		//g = drawMessages(g,margin,ratio);

		return g;
    }
    


	private Graphics drawMobileSinks(Graphics g, int margin, double ratio) {
		// draw nodes
		for(int i=0; i<network.getMsc().getMobileSinkList().size(); i++){
			MobileSinkNode ms = network.getMsc().getMobileSinkList().get(i);
		

			Point p = ms.getRobot().getPositionPoint();
			int radius =8;
			g.setColor(Color.blue);
			
			// draw a ring
			g.drawOval(margin+ (int)Math.floor(p.getX()*ratio),(int)Math.floor(p.getY()*ratio), radius*3, radius*3);
			g.fillOval(margin+ (int)Math.floor(p.getX()*ratio),(int)Math.floor(p.getY()*ratio), radius*3, radius*3);

						
			g.setColor(Color.white);
			g.drawOval(margin+ (int)Math.floor(p.getX()*ratio) + radius/2,(int)Math.floor(p.getY()*ratio)+radius/2, radius*2, radius*2);
			g.fillOval(margin+ (int)Math.floor(p.getX()*ratio) + radius/2,(int)Math.floor(p.getY()*ratio)+radius/2, radius*2, radius*2);
		
			if(network.getNp().getSinkPlacementStrategy().equalsIgnoreCase("Weighted")){
				Vector2d vec = ms.getCurrentGravityForce();
				if(vec == null){
					continue;
				}
				g.setColor(Color.blue);
				g.drawLine(margin+ (int)Math.floor(p.getX()*ratio) + radius/2, (int)Math.floor(p.getY()*ratio)+radius, 
						margin+ (int)Math.floor(p.getX()*ratio + radius/2 + vec.x), (int)Math.floor(p.getY()*ratio+radius/2+vec.y));
			}
			
		}
		return g;

			
	}


	
	private Graphics drawGrids(Graphics g, int margin, double ratio) {
		if(!network.getNp().getSinkPlacementStrategy().equalsIgnoreCase("GridBased")){ // do not draw grids if it is not grid based but grids are created anyways
			return g;
		}
		// draw nodes
    	if(network.getMsc().getMobileSinkList() ==null) return g;
		for(int i=0; i<network.getMsc().getMobileSinkList().size(); i++){
			MobileSinkNode ms = network.getMsc().getMobileSinkList().get(i);
			List<Grid> gList = ms.getAssignedGridList();
			if(gList == null) return g;
			for(int j=0;j<gList.size();j++){
				Grid grid = gList.get(j);
				Point p = grid.getMinPoint();
				
				g.setColor(Color.black);
				
		       	Font font = new Font ("Monospaced", Font.BOLD , 14); // Put Font.BOLD if you want to make them bold
	    		g.setFont(font);

				g.drawRect(margin+ (int)Math.floor(p.getX()*ratio), (int)Math.floor(p.getY()*ratio), (int)Math.floor(grid.getWidth()*ratio), (int)Math.floor(grid.getWidth()*ratio));
				g.drawString(i + "", margin +  (int)Math.floor(p.getX()*ratio +grid.getWidth()/4) ,(int)Math.floor(p.getY()*ratio +grid.getWidth()/4) );
 
			}

		}
		return g;

			
	}
	private Graphics drawRedZones(Graphics g, int margin, double ratio) {
		// draw nodes
		for(int i=0; i<tpm.getRedZoneList().size(); i++){
			RedZone r = tpm.getRedZoneList().get(i);
			Point p = r.getCenterPoint();
			// check if the red zone is active or not
			if(currentTime >=r.getActiveStartTime() && currentTime <=r.getActiveEndTime()){
				
				int radius=(int)Math.floor(r.getRadius()*ratio);
				g.setColor(Color.RED);
				g.drawOval(margin+ (int)Math.floor(p.getX()*ratio),(int)Math.floor(p.getY()*ratio), radius, radius);
				g.fillOval(margin+ (int)Math.floor(p.getX()*ratio),(int)Math.floor(p.getY()*ratio), radius, radius);
			}
		
		}
		return g;
			
	}

	private Graphics drawGates(Graphics g, int margin, double ratio) {
		// draw gates		
		g.setColor(Color.BLUE);
		for(int i=0; i<tpm.getGateList().size(); i++){ // for each road
			Gate gate = tpm.getGateList().get(i);
			int wpRadius=4;
			
			// Put all the way points in the visualizer to integer arrays
			int numWayPoints = gate.getWayPointList().size();
			int[] xArray = new int[numWayPoints];
			int[] yArray = new int[numWayPoints];
			
			for(int j=0;j<numWayPoints;j++){
				Point p =  gate.getWayPointList().get(j);
				xArray[j] = margin+ (int)Math.floor(p.getX()*ratio);
				yArray[j] = (int)Math.floor(p.getY()*ratio);
				// Put the points along the way of conversion :)
				g.drawOval(xArray[j], yArray[j], wpRadius, wpRadius);
				g.fillOval(xArray[j], yArray[j], wpRadius, wpRadius);
				
			} // finished conversion from list to integer arrays	
		//	g.drawString("GATE", xArray[0], yArray[0]);
			// draw the gate itself
			g.drawPolyline(xArray,yArray, numWayPoints); 

		
		}
		return g;
	}

	private Graphics drawObstacles(Graphics g, int margin, double ratio) {
		// draw obstacles
		for(int i=0; i<tpm.getObstacleList().size(); i++){ // for each road
			Obstacle o = tpm.getObstacleList().get(i);
			int wpRadius=1;
			
			// Put all the way points in the visualizer to integer arrays
			int numWayPoints = o.getWayPointList().size();
			int[] xArray = new int[numWayPoints];
			int[] yArray = new int[numWayPoints];
			
			for(int j=0;j<numWayPoints;j++){
				Point p =  o.getWayPointList().get(j);
				xArray[j] = margin+ (int)Math.floor(p.getX()*ratio);
				yArray[j] = (int)Math.floor(p.getY()*ratio);
				// Put the points along the way of conversion :)
				g.drawOval(xArray[j], yArray[j], wpRadius, wpRadius);
				g.fillOval(xArray[j], yArray[j], wpRadius, wpRadius);
			} // finished conversion from list to integer arrays
			
			// set color of the particular obstacle
			if(o.getType().equalsIgnoreCase("water")|| o.getType().equalsIgnoreCase("river")){
				g.setColor(Color.blue);
			} else if(o.getType().equalsIgnoreCase("forest")){
				g.setColor(Color.green);
			}
			else{
				g.setColor(Color.GRAY);
			}
			
			// draw the obstacle itself
			if(o.isClosed()){ // such as buildings
				g.drawPolygon(xArray,yArray, numWayPoints);

			}
			else{	// footway or pedestrian
				g.drawPolyline(xArray,yArray, numWayPoints); 
			}

		}
		return g;
	}

	private Graphics drawRoads(Graphics g, int margin, double ratio) {
		// draw road		
		g.setColor(Color.black);
		for(int i=0; i<tpm.getRoadList().size(); i++){ // for each road
			Road  r = tpm.getRoadList().get(i);
			int wpRadius=2;
			
			// Put all the way points in the visualizer to integer arrays
			int numWayPoints = r.getWayPointList().size();
			int[] xArray = new int[numWayPoints];
			int[] yArray = new int[numWayPoints];
			
			for(int j=0;j<numWayPoints;j++){
				Point p =  r.getWayPointList().get(j);
				xArray[j] = margin+ (int)Math.floor(p.getX()*ratio);
				yArray[j] = (int)Math.floor(p.getY()*ratio);
				// Put the points along the way of conversion :)
				g.drawOval(xArray[j], yArray[j], wpRadius, wpRadius);
				g.fillOval(xArray[j], yArray[j], wpRadius, wpRadius);
			} // finished conversion from list to integer arrays
			
			// draw the road itself
			if(r.isClosed()){ // it means this is a Plaza, so we need to draw a polygon	
				g.drawPolygon(xArray,yArray, numWayPoints);
			}
			else{	// footway or pedestrian
				g.drawPolyline(xArray,yArray, numWayPoints); 
			}
		}
		return g;
	}

	private Graphics drawNodes(Graphics g, int margin, double ratio) {
		// draw nodes
		for(int i=0; i<tpm.getNodeList().size(); i++){
			Node n = tpm.getNodeList().get(i);
			Point p = n.getPoint();
			int radius=0;
			// Tree, toilets, atm, gift, drinks, information
			if(n.getType().equalsIgnoreCase("tree")){
				radius=3;
				g.setColor(Color.green);
				g.drawOval(margin+ (int)Math.floor(p.getX()*ratio),(int)Math.floor(p.getY()*ratio), radius, radius);
				g.fillOval(margin+ (int)Math.floor(p.getX()*ratio),(int)Math.floor(p.getY()*ratio), radius, radius);
	
			}
			else{ radius=8; 
				if(n.getType().equalsIgnoreCase("toilets")){
					g.setColor(Color.DARK_GRAY);
				}
				else if(n.getType().equalsIgnoreCase("atm")){
					g.setColor(Color.MAGENTA);
				}	
				else if(n.getType().equalsIgnoreCase("gift")){
					g.setColor(Color.PINK);
				}
				else if(n.getType().equalsIgnoreCase("drinks")){
					g.setColor(Color.ORANGE);
				}
				else if(n.getType().equalsIgnoreCase("information")){
					g.setColor(Color.BLUE);
				}
			
				g.drawRoundRect(margin+ (int)Math.floor(p.getX()*ratio),(int)Math.floor(p.getY()*ratio), radius, radius , radius/2, radius/2);
			//	g.fillRoundRect(margin+ (int)Math.floor(p.getX()*ratio),(int)Math.floor(p.getY()*ratio), radius, radius , radius/2, radius/2);
				g.drawString(n.getType().substring(0,1), margin + radius/4+ (int)Math.floor(p.getX()*ratio) ,(int)Math.floor(p.getY()*ratio) + (radius*7)/8);
			}
			if(radius==0) System.out.println("Error");
		}
		return g;
	}

	public boolean isResume() {
		return resume;
	}

	public boolean isFinish() {
		return finish;
	}

	public void setFinish(boolean finish) {
		this.finish = finish;
	}

	public void setHumanList(List<Human> humanList) {
		this.humanList = humanList;
	}

	public void setResume(boolean resume) {
		this.resume = resume;
	}

	public void setCurrentTime(double currentTime) {
		this.currentTime = currentTime;
	}

	public void setNetwork(NetworkProcessor network) {
		this.network= network;
	}

	
	
	
}
