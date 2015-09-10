package visualizer;
import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.image.BufferStrategy;

import map.Gate;
import map.Node;
import map.Obstacle;
import map.Road;
import map.ThemeParkMap;
import model.Point;

/**
 * @author Gurkan Solmaz
 * 		   Department of EECS - University of Central Florida
 * 		   Disaster Mobility - Spring 2013
 * 		   Advisor: Dr. Damla Turgut
 */
public class MapVisualizer {
		ThemeParkMap themeParkMap;
	    Frame mainFrame;


		private static DisplayMode[] BEST_DISPLAY_MODES = new DisplayMode[] {
	        new DisplayMode(640, 480, 32, 0),
	        new DisplayMode(640, 480, 16, 0),
	        new DisplayMode(640, 480, 8, 0)
	    };
		
		public MapVisualizer(ThemeParkMap themeParkMap) {
			super();
			this.themeParkMap = themeParkMap;
			configureGraphics();
		}

		private void configureGraphics() {
		     try {
	            int numBuffers = 2;
	            GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
	            GraphicsDevice device = env.getDefaultScreenDevice();
	            displayGraphics(numBuffers, device);
		        } catch (Exception e) {
		            e.printStackTrace();
		        }
		}
			
	   private void displayGraphics(int numBuffers, GraphicsDevice device) {
			try {
	            GraphicsConfiguration gc = device.getDefaultConfiguration();
	           
	            mainFrame = new Frame(gc);
	            mainFrame.setUndecorated(true);
	            mainFrame.setIgnoreRepaint(true);
	            
	            
	           device.setFullScreenWindow(mainFrame);
	            if (device.isDisplayChangeSupported()) {
	                chooseBestDisplayMode(device);
	            }
	            
	            Rectangle bounds = mainFrame.getBounds();
	            mainFrame.createBufferStrategy(numBuffers);
	            BufferStrategy bufferStrategy = mainFrame.getBufferStrategy();
	            boolean done = false;
	            int counter=0;
	            while(!done) {
	                for (int i = 0; i < numBuffers; i++) {
	                    Graphics g = bufferStrategy.getDrawGraphics();
	                    if (!bufferStrategy.contentsLost()) {
	                        g= drawGraphics(g, bounds);
	                        bufferStrategy.show();
	                        g.dispose();
	                    }
	                    try {
	                    	if(counter==0){
	                    		Thread.sleep((int) 1);  // do not stay in the beginning
	                    		counter++;
	                    	}
	                    	else{		
	                    		Thread.sleep((int)210000); // stay for 20 seconds
	                    	}
	                    } catch (InterruptedException e) {
	                 
	                    }
	                }      done=true;
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	            device.setFullScreenWindow(null);
	        }
		}
	    
	    private Graphics drawGraphics(Graphics g, Rectangle bounds) {
	    	// find projection proportions
	    	double minBound = Math.min(bounds.height, bounds.width);
	    	int margin = (int)Math.floor((bounds.width - bounds.height)/2);
	    	double maxWidth = Math.max(themeParkMap.getTerrainLengthX(), themeParkMap.getTerrainLengthY());
	    	double ratio = minBound/maxWidth; // projection ratio
	    	
	    	// draw the background
	    	g.setColor(Color.BLACK);
	    	g.drawRect(0,0, bounds.width, bounds.height);
	    	g.fillRect(0,0, bounds.width, bounds.height);
	    	g.setColor(Color.WHITE);
	    	g.drawRect(margin, 0, bounds.width - 2*margin, bounds.height);
	    	g.fillRect(margin, 0, bounds.width -2* margin, bounds.height);
	    	 
	    	g= drawObjects(g, margin, ratio);
			return g;
	}
	    private Graphics drawObjects(Graphics g,int margin, double ratio){
	    	
	    	// set a standard font for texts in the visualizer 
	    	Font font = new Font ("Monospaced", Font.PLAIN , 10); // Put Font.BOLD if you want to make them bold
			g.setFont(font);
			
			g = drawNodes(g,margin, ratio);
			g = drawRoads(g, margin, ratio);
			g = drawObstacles(g, margin, ratio);
			g = drawGates(g, margin, ratio);
			return g;
	    }
	    
	    
		private Graphics drawGates(Graphics g, int margin, double ratio) {
			// draw gates		
			g.setColor(Color.BLUE);
			for(int i=0; i<themeParkMap.getGateList().size(); i++){ // for each road
				Gate gate = themeParkMap.getGateList().get(i);
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
			for(int i=0; i<themeParkMap.getObstacleList().size(); i++){ // for each road
				Obstacle o = themeParkMap.getObstacleList().get(i);
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
			for(int i=0; i<themeParkMap.getRoadList().size(); i++){ // for each road
				Road  r = themeParkMap.getRoadList().get(i);
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
			for(int i=0; i<themeParkMap.getNodeList().size(); i++){
				Node n = themeParkMap.getNodeList().get(i);
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

		private DisplayMode getBestDisplayMode(GraphicsDevice device) {
	        for (int x = 0; x < BEST_DISPLAY_MODES.length; x++) {
	            DisplayMode[] modes = device.getDisplayModes();
	            for (int i = 0; i < modes.length; i++) {
	                if (modes[i].getWidth() == BEST_DISPLAY_MODES[x].getWidth()
	                   && modes[i].getHeight() == BEST_DISPLAY_MODES[x].getHeight()
	                   && modes[i].getBitDepth() == BEST_DISPLAY_MODES[x].getBitDepth()
	                   ) {
	                    return BEST_DISPLAY_MODES[x];
	                }
	            }
	        }
	        return null;
	    }
	    
	    public void chooseBestDisplayMode(GraphicsDevice device) {
	        DisplayMode best = getBestDisplayMode(device);
	        if (best != null) {
	            device.setDisplayMode(best);
	        }
	    }
	    
	/*    public void getScreenCapture(Graphics g){
	        JFrame capture = new JFrame();
    	    capture.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	    Toolkit kit = Toolkit.getDefaultToolkit();
    	    final Dimension d = kit.getScreenSize();
    	    capture.setSize(d);
    	 
    	    Rectangle rect = new Rectangle(d);
    	    try {
    	      Robot robot = new Robot();
    	      final BufferedImage image = robot.createScreenCapture(rect);
    	      image.flush();
    	      JPanel panel = new JPanel() {
    	        *//**
				 * 
				 *//*
				private static final long serialVersionUID = 1L;

				public void paintComponent(Graphics g) {
    	          g.drawImage(image, 0, 0, d.width, d.height, this);
    	          this.
    	        }
    	      };
    	      panel.setOpaque(false);
    	      panel.prepareImage(image, panel);
    	      panel.repaint();
    	      capture.getContentPane().add(panel);
    	    } 
    	    catch (Exception e) {
    	      e.printStackTrace();
    	    }
    	    capture.setVisible(true);
    	    capture.get
    	   
	 }*/

}
