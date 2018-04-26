package cz.pavelzelenka.fractal.fractals;

import cz.pavelzelenka.fractal.LineSegment;
import cz.pavelzelenka.fractal.Point;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class Koch implements Fractal {

	public static final String NAME = "Koch";
	
	/** Usecky */
    private LineSegment[] nextGen;  
	
    /** Bod */
    private Point traslation;  
    
    public LineSegment[] firstGeneration(GraphicsContext g, Canvas activeCanvas) {
		LineSegment[] triangle = new LineSegment[6];
        for(int i = 0; i < triangle.length; i++) {
        	triangle[i] = new LineSegment();
        }
		double lineLength = 0.25D * 3.0D * activeCanvas.getHeight();
		Point lineShape = new Point(lineLength, 0);
		double sixtyDegrees = Math.PI / 3;
        triangle[0].A = new Point(0, 0);
        triangle[0].B = new Point(lineShape.getX(), lineShape.getY());
        lineShape = rotateVector(lineShape, -sixtyDegrees);
        triangle[1].A = triangle[0].B;
        triangle[1].B = lineShape;
        triangle[2].A = lineShape;
        triangle[2].B = triangle[0].A;
        triangle[3].A = triangle[2].B;
        triangle[3].B = triangle[2].A;
        triangle[4].A = triangle[1].B;
        triangle[4].B = triangle[1].A;
        triangle[5].A = triangle[0].B;
        triangle[5].B = triangle[0].A;
        double x = 0.5D * (activeCanvas.getWidth() - lineLength);
        double y = Math.abs(lineShape.getY()) + 0.25D * (activeCanvas.getHeight() - Math.abs(lineShape.getY()));
        traslation = new Point(x, y);
        return triangle;
    }
	
    public LineSegment[] nextGeneration(LineSegment[] generation) {
        nextGen = new LineSegment[generation.length * 4];
        for(int i = 0; i < nextGen.length; i++) {
        	nextGen[i] = new LineSegment();
        }
        double sixtyDegrees = Math.PI / 3;
        for (int i = 0; i < generation.length; i++) {
        	double lineLength = generation[i].A.getDistance(generation[i].B);
            Point lineDirection = generation[i].A.getDirection(generation[i].B);
            double coef = 1;
            nextGen[i * 4 + 0].A = generation[i].A;
            double x0 = generation[i].A.getX() + lineDirection.getX() * 0.333333D * lineLength;
            double y0 = generation[i].A.getY() + lineDirection.getY() * 0.333333D * lineLength;
            nextGen[i * 4 + 0].B = new Point(x0, y0);
            lineDirection = rotateVector(lineDirection, sixtyDegrees * coef);
            nextGen[i * 4 + 1].A = nextGen[i * 4 + 0].B;
            double x1 = nextGen[i * 4 + 0].B.getX() + lineDirection.getX() * 0.333333D * lineLength;
            double y1 = nextGen[i * 4 + 0].B.getY() + lineDirection.getY() * 0.333333D * lineLength;
            nextGen[i * 4 + 1].B = new Point(x1, y1);
            lineDirection = rotateVector(lineDirection, -2 * sixtyDegrees * coef);
            nextGen[i * 4 + 2].A = nextGen[i * 4 + 1].B;
            double x2 = nextGen[i * 4 + 1].B.getX() + lineDirection.getX() * 0.333333D * lineLength;
            double y2 = nextGen[i * 4 + 1].B.getY() + lineDirection.getY() * 0.333333D * lineLength;
            nextGen[i * 4 + 2].B = new Point(x2, y2);
            nextGen[i * 4 + 3].A = nextGen[i * 4 + 2].B;
            nextGen[i * 4 + 3].B = generation[i].B;
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
