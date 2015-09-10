package network;

import model.Point;

public class Grid {
	Point minPoint, maxPoint;
	Point centerPoint;
	int rowIndex;
	int columnIndex;
	double width; 
	
	public Grid(Point minPoint, Point maxPoint, int rowIndex, int columnIndex) {
		super();
		this.minPoint = minPoint;
		this.maxPoint = maxPoint;
		this.rowIndex = rowIndex;
		this.columnIndex = columnIndex;
		setCenterPoint();
		setWidth();
	}
	private void setWidth() {
		this.width = maxPoint.getX() - minPoint.getX();
	}
	private void setCenterPoint() {
		double centerX = (maxPoint.getX() - minPoint.getX())/2 + minPoint.getX();
		double centerY = (maxPoint.getY() - minPoint.getY())/2 + minPoint.getY();
		this.centerPoint = new Point(centerX, centerY);

	}
	public Point getMinPoint() {
		return minPoint;
	}
	public Point getMaxPoint() {
		return maxPoint;
	}
	public int getRowIndex() {
		return rowIndex;
	}
	public int getColumnIndex() {
		return columnIndex;
	}
	public Point getCenterPoint() {
		return centerPoint;
	}
	public void setCenterPoint(Point centerPoint) {
		this.centerPoint = centerPoint;
	}
	public double getWidth() {
		return width;
	}
	
	
}
