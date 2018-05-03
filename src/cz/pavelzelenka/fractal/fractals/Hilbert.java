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
public class Hilbert implements Fractal, HavePoints {
	
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
