package cz.pavelzelenka.fractal.fractals;

import java.util.ArrayList;
import java.util.List;

import cz.pavelzelenka.fractal.LineSegment;
import cz.pavelzelenka.fractal.Point;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class SierpinskiHexagon implements Fractal {
	
	/** Duhova barva */
	private boolean rainbowColor = false;
	/** Ovladaci prvek kresleni */
	private GraphicsContext g;
	/** Platno */
	private Canvas activeCanvas;
	
	private int maximumStep = 5;
	private int step = 0;
	private int totalHexagons = 0;
	private int currentHexagon = 0;
	
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
		List<Point> points = new ArrayList<Point>();
		
		double width = activeCanvas.getWidth();
		double height = activeCanvas.getHeight();
		double hw = width/2;
		double hh = height/2;
		double side = Math.min(width, height)/3;
		
		Point first = new Point(hw, hh);
		points.add(first);
		
		int n = step;
		currentHexagon = 0;
		totalHexagons = (int) Math.pow(6, n);
        drawHexagon(first, side, 0, n);
	}
	
	/**
	 * Vrati strany sestiuhelniku
	 * @param hw stred osy Y
	 * @param hh stred osy X
	 * @param side strana
	 * @return strany sestiuhelniku
	 */
	public LineSegment[] calculateHexagon(double hw, double hh, double side) {
		LineSegment[] hexagon = new LineSegment[6];
        for(int i = 0; i < hexagon.length; i++) {
        	hexagon[i] = new LineSegment();
        }
		
		Point first = null;
		Point prev = null;
		
        for (int i = 0; i < 6; i++) {
            double xval = (hw + side * Math.cos(i * 2 * Math.PI / 6D));
            double yval = (hh + side * Math.sin(i * 2 * Math.PI / 6D));
            if(prev != null) {
            	hexagon[i-1].A = prev;
            	hexagon[i-1].B = new Point(xval, yval);
            	if(i == 5) {
            		hexagon[i].A = hexagon[i-1].B;
            		hexagon[i].B = first;
            	}
            } else {
            	first = new Point(xval, yval);
            }
            prev = new Point(xval, yval);
        }
        
        return hexagon;
	}
	
	/**
	 * Vykresleni
	 * @param center stred
	 * @param side delka strany
	 * @param iteraction soucasna iterace
	 * @param max maximalni iterace
	 */
	public void drawHexagon(Point center, double side, int iteraction, int max) {
		LineSegment[] hexagon = calculateHexagon(center.getX(), center.getY(), side);
		if(iteraction == max) {
			currentHexagon++;
			for(int i=0; i<hexagon.length; i++) {
				if(rainbowColor) {
					g.setStroke(getRainbowColor(currentHexagon, totalHexagons, 1D, 0.8));
				}
				LineSegment segment = hexagon[i];
				g.strokeLine(segment.A.getX(), segment.A.getY(), segment.B.getX(), segment.B.getY());
			}
		}
        if(iteraction < max) {
        	
        	Point leftTop = new Point(center.getX()-side/2, center.getY()-((side/3)*Math.sqrt(3))/2);
        	drawHexagon(leftTop, side/3, iteraction+1, max);
        	
        	Point leftBottom = new Point(center.getX()-side/2, center.getY()+((side/3)*Math.sqrt(3))/2);
        	drawHexagon(leftBottom, side/3, iteraction+1, max);
        	
        	Point centerTop = new Point(center.getX(), center.getY()-((side/3)*Math.sqrt(3)));
        	drawHexagon(centerTop, side/3, iteraction+1, max);
        	
        	Point centerBottom = new Point(center.getX(), center.getY()+((side/3)*Math.sqrt(3)));
        	drawHexagon(centerBottom, side/3, iteraction+1, max);
        	
        	Point rightTop = new Point(center.getX()+side/2, center.getY()-((side/3)*Math.sqrt(3))/2);
        	drawHexagon(rightTop, side/3, iteraction+1, max);
        	
        	Point rightBottom = new Point(center.getX()+side/2, center.getY()+((side/3)*Math.sqrt(3))/2);
        	drawHexagon(rightBottom, side/3, iteraction+1, max);
        }
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
