package cz.pavelzelenka.fractal.fractals;

import cz.pavelzelenka.fractal.LineSegment;
import cz.pavelzelenka.fractal.Point;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class Dragon implements Fractal {

	public static final String NAME = "Dragon";
	
	/** Usecky */
    private LineSegment[] nextGen;  
	
    /** Bod */
    private Point traslation;  
    
	public LineSegment[] firstGeneration(GraphicsContext g, Canvas activeCanvas) {
        LineSegment[] triangle = new LineSegment[3];
        for(int i = 0; i < triangle.length; i++) {
        	triangle[i] = new LineSegment();
        }
        double lineLength = 0.5D * activeCanvas.getHeight();
        Point lineShape = new Point(lineLength, 0);
        double sixtyDegrees = Math.PI / 3;
        triangle[0].A = new Point(0, 0);
        triangle[0].B = new Point(lineShape.getX(), lineShape.getY());
        lineShape = rotateVector(lineShape, -sixtyDegrees);
        triangle[1].A = triangle[0].B;
        triangle[1].B = lineShape;
        triangle[2].A = lineShape;
        triangle[2].B = triangle[0].A;
        double x = 0.5D * (activeCanvas.getWidth() - lineLength);
        double y = Math.abs(lineShape.getY()) + 0.5D * (activeCanvas.getHeight() - 1.25D*Math.abs(lineShape.getY()));
        traslation = new Point(x, y);
        return triangle;
    }
	
	public LineSegment[] nextGeneration(LineSegment[] generation) {
        nextGen = new LineSegment[generation.length * 2];
        for(int i = 0; i < nextGen.length; i++) {
        	nextGen[i] = new LineSegment();
        }
        double fortyFiveDegrees = Math.PI / 4;
        for (int i = 0; i < generation.length; i++) {
        	double lineLength = generation[i].A.getDistance(generation[i].B);
            Point lineDirection = generation[i].A.getDirection(generation[i].B);
            double coef = 1;
            lineDirection = rotateVector(lineDirection, fortyFiveDegrees * coef);
            nextGen[i * 2 + 0].A = generation[i].A;
            double x0 = generation[i].A.getX() + lineDirection.getX() * lineLength * (1.0D / Math.sqrt(2));
            double y0 = generation[i].A.getY() + lineDirection.getY() * lineLength * (1.0D / Math.sqrt(2));
            nextGen[i * 2 + 0].B = new Point(x0, y0);
            nextGen[i * 2 + 1].A = nextGen[i * 2 + 0].B;
            nextGen[i * 2 + 1].B = generation[i].B;
        }

        return nextGen;
    }
    
	private Point rotateVector(Point vector, double angle) {
		double x = vector.getX() * Math.cos(angle) - vector.getY() * Math.sin(angle);
		double y = vector.getX() * Math.sin(angle) + vector.getY() * Math.cos(angle);
		vector = new Point(x, y);
        return vector;
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
