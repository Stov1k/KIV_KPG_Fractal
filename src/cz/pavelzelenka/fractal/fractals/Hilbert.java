package cz.pavelzelenka.fractal.fractals;

import cz.pavelzelenka.fractal.LineSegment;
import cz.pavelzelenka.fractal.Point;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

/**
 * Hilbertova krivka
 * Reseno dle navodu https://marcin-chwedczuk.github.io/iterative-algorithm-for-drawing-hilbert-curve
 * @author Pavel Zelenka
 * @version 2018-04-26
 */
public class Hilbert implements Curve {

	public static final String NAME = "Hilbert";
	
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
    
    private Point processIndex(int index, int N) {
    	int processed = index;
    	Point[] positions = new Point[4];
    	positions[0] = new Point(0D, 0D);
    	positions[1] = new Point(0D, 1D);
    	positions[2] = new Point(1D, 1D);
    	positions[3] = new Point(1D, 0D);
        Point tmp = positions[lastBits(processed)];
        processed = (processed >>> 2);
        double x = tmp.getX();
        double y = tmp.getY();
        double temp = x;
        for (int n = 4; n <= N; n *= 2) {
            double n2 = n / 2;
            switch (lastBits(processed)) {
            	case 0:
            		temp = x;
            		x = y;
            		y = temp;
            		break;
            	case 1:
            		y = y + n2;
            		break;
            	case 2:
            		x = x + n2;
            		y = y + n2;
            		break;
            	case 3:
            		temp = y;
            		y = (n2 - 1) - x;
            		x = (n2 - 1) - temp;
            		x = x + n2;
            		break;
            	}
            processed = (processed >>> 2);
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
		    curr = processIndex(i, N);
		    curr.setLocation(curr.getX()*lineLength, curr.getY()*lineLength);
		    line[i] = new LineSegment(prev, curr);
		    prev = curr;
		}
        traslation = new Point(10, 10);
		return line;
    }
	
	public LineSegment[] nextGeneration(LineSegment[] generation) {	
		int N = (int) Math.sqrt(generation.length) * 2;
		Point prev = new Point(0, 0);
		Point curr = prev;
        nextGen = new LineSegment[N*N];       
        double lineLength = generation[1].A.getDistance(generation[1].B);
		for (int i = 0; i < N*N; i += 1) {
		    curr = processIndex(i, N);
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
	
	@Override
	public String toString() {
		return NAME;
	}
}
