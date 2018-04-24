package cz.pavelzelenka.fractal.fractals;

import cz.pavelzelenka.fractal.LineSegment;
import cz.pavelzelenka.fractal.Point;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class Hilbert {

	/** Usecky */
    private LineSegment[] nextGen;  
	
    /** Bod */
    private Point traslation;  
    
    /**
     * Vrati hodnotu na poslednich 2 bitech
     * @param input vstupni hodnota
     * @return hodnotu na poslednich 2 bitech
     */
    private int lastBits(int input) {
    	return (input & 3);
    }
    
    private Point hindex2xy(int hindex, int N) {

    	System.out.println("INDEX = " + hindex + " START (N = " + N + ")");
    	
    	Point[] positions = new Point[4];
    	positions[0] = new Point(0D, 0D);
    	positions[1] = new Point(0D, 1D);
    	positions[2] = new Point(1D, 1D);
    	positions[3] = new Point(1D, 0D);

        Point tmp = positions[lastBits(hindex)];
        hindex = (hindex >>> 2);

        double x = tmp.getX();
        double y = tmp.getY();

        System.out.println("INDEX >> " + hindex + " x: " + x + " y: " + y);
         
        double loc = x;
        
        for (int n = 4; n <= N; n *= 2) {
            double n2 = n / 2;

            switch (lastBits(hindex)) {
            	case 0:
            		loc = x;
            		x = y;
            		y = loc;
            		break;
            	case 1:
            		// x se nemeni
            		y = y + n2;
            		break;
            	case 2:
            		x = x + n2;
            		y = y + n2;
            		break;
            	case 3:
            		loc = y;
            		y = (n2-1) - x;
            		x = (n2-1) - loc;
            		x = x + n2;
            		break;
            	}
            hindex = (hindex >>> 2);
            System.out.println("INDEX >> " + hindex + " x: " + x + " y: " + y);
        }
        return new Point(x, y);
    }
    
	public LineSegment[] firstGeneration(GraphicsContext g, Canvas activeCanvas) {
		
		int N = 2;

		Point prev = new Point(0, 0);
		Point curr = prev;

		double lineLength = 0.5D * activeCanvas.getHeight();  
		
		LineSegment[] line = new LineSegment[N*N];
		
		for (int i = 0; i < N*N; i += 1) {
		    curr = hindex2xy(i, N);
		    curr.setLocation(curr.getX()*lineLength, curr.getY()*lineLength);

		    line[i] = new LineSegment(prev, curr);
		    
		    prev = curr;
		    
		}
		
        traslation = new Point(0, 0);
		
		return line;
    }
	
	public LineSegment[] nextGeneration(LineSegment[] generation) {	
		System.out.println("==============================================================");
		
		int N = (int) Math.sqrt(generation.length) * 2;
		
		Point prev = new Point(0, 0);
		Point curr = prev;
		
        nextGen = new LineSegment[N*N];       
        
        double lineLength = generation[1].A.getDistance(generation[1].B);
        
        
		for (int i = 0; i < N*N; i += 1) {
		    curr = hindex2xy(i, N);
		    curr.setLocation(curr.getX()*(lineLength/2), curr.getY()*(lineLength/2));

		    nextGen[i] = new LineSegment(prev, curr);

		    prev = curr;
		    
		}
		
		return nextGen;
		
    }
	
	public LineSegment[] getNextGen() {
		return nextGen;
	}

	public Point getTraslation() {
		return traslation;
	}
	
}
