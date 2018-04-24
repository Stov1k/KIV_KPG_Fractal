package cz.pavelzelenka.fractal;

/**
 * Usecka
 * @author Pavel Zelenka
 * @version 2018-04-20
 */
public class LineSegment {
	
    public Point A;
    public Point B;
    
    public LineSegment() {
    	this.A = new Point(0D, 0D);
    	this.B = new Point(0D, 0D);
    }
    
    public LineSegment(Point A, Point B) {
		if (A == null || B == null) {
		    throw new IllegalArgumentException("The point cannot be null!");
		}
    	this.A = A;
    	this.B = B;
    }
    
}
