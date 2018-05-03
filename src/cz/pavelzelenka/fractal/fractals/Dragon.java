package cz.pavelzelenka.fractal.fractals;

import cz.pavelzelenka.fractal.LineSegment;
import cz.pavelzelenka.fractal.Point;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;

/**
 * Hilbertova krivka
 * @author Pavel Zelenka
 * @version 2018-04-26
 */
public class Dragon implements Fractal, HavePoints {
	
	/** Duhova barva */
	private boolean rainbowColor = false;
	/** Ovladaci prvek kresleni */
	private GraphicsContext g;
	/** Platno */
	private Canvas activeCanvas;
	
	private int maximumStep = 10;
	private int step = 0;
	
	/** Usecky */
	private LineSegment[] currentGen;
    private LineSegment[] nextGen;
    /** Bod posunu */
    private Point traslation;
    /** Velikost bodu */
    private double pointSize = 4D;
    
	/**
	 * Inicializace, nastaveni kresliciho nastroje
	 */
	public void initialize(GraphicsContext g) {
		this.g = g;
		this.activeCanvas = g.getCanvas();
	}
    
	/**
	 * Vykresleni obrazu
	 */
	public void draw() {
		currentGen = firstGeneration(g, activeCanvas);
		for(int i = 0; i < step; i++) {
			currentGen = nextGeneration(currentGen);
		}
		traslation = getTraslation();
		g.save();
		g.translate(traslation.getX(), traslation.getY());
        drawSegments(currentGen);
        g.restore();
	}
	
	/**
	 * Vykresleni usecek
	 * @param coordinates souradnice
	 */
	private void drawSegments(LineSegment[] coordinates) {
        Point[] points = new Point[coordinates.length];
        for (int i = 0; i < points.length; i++) {
        	if(rainbowColor) {
        		g.setStroke(getRainbowColor(i, points.length, 1D, 0.8D));
        	}
        	double x1 = coordinates[i].A.getX();
        	double y1 = coordinates[i].A.getY();
        	double x2 = coordinates[i].B.getX();
        	double y2 = coordinates[i].B.getY();
        	g.strokeLine(x1, y1, x2, y2);
        }
        for (int i = 0; i < points.length; i++) {
        	if(rainbowColor) {
        		g.setFill(getRainbowColor(i, points.length, 1D, 0.8D));
        	}
        	g.fillOval(coordinates[i].A.getX()-pointSize/2, coordinates[i].A.getY()-pointSize/2, pointSize, pointSize);
        	if(i == points.length-1) {
        		if(coordinates[i].B.getX() == coordinates[0].A.getX()) {
        			g.fillArc(coordinates[i].B.getX()-pointSize/2, coordinates[i].B.getY()-pointSize/2, pointSize, pointSize, -45D, 180D, ArcType.OPEN);
        		} else {
        			g.fillOval(coordinates[i].B.getX()-pointSize/2, coordinates[i].B.getY()-pointSize/2, pointSize, pointSize);
        		}
        	}
        }
    }
	
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
	
	public double getPointSize() {
		return pointSize;
	}

	public void setPointSize(double pointSize) {
		this.pointSize = pointSize;
	}

	/**
	 * Vrati barvu
	 * @param position aktualni pozice
	 * @param total celkovy pocet pozic
	 * @param saturation sytost
	 * @param brightness svetlost
	 * @return barva
	 */
	public Color getRainbowColor(int position, int total, double saturation, double brightness) {
    	double hue = Math.floor((double)position * 360D/(double)(total));
    	Color color = Color.hsb(hue, saturation, brightness);
    	return color;
	}

	public void setRainbow(boolean value) {
		this.rainbowColor = value;
	}

	public int getMaximumStep() {
		return maximumStep;
	}
	
	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		if(step < maximumStep) {
			this.step = step;
		} else {
			this.step = maximumStep;
		}
	}
	
}
