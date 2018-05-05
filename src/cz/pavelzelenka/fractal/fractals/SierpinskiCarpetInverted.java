package cz.pavelzelenka.fractal.fractals;

import java.util.ArrayList;
import java.util.List;

import cz.pavelzelenka.fractal.LineSegment;
import cz.pavelzelenka.fractal.Point;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Sierpinsky Carpet (Inverted)
 * @author Pavel Zelenka
 */
public class SierpinskiCarpetInverted implements Fractal {
	
	/** Duhova barva */
	private boolean rainbowColor = false;
	/** Ovladaci prvek kresleni */
	private GraphicsContext g;
	/** Platno */
	private Canvas activeCanvas;
	
	private int maximumStep = 8;
	private int step = 0;
	private int totalRectangles = 0;
	private int currentRectangles = 0;
	
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
		
		currentRectangles = 0;
		totalRectangles = 0;
		for(int i = step; i >= 0; i--) {
			totalRectangles += (int) Math.pow(8, i);
		}
		drawRect(first, side, 0, step);
	}
	
	/**
	 * Vykresleni ctverce
	 * @param point stred
	 * @param side delka strany
	 * @param iteraction soucasna iterace
	 * @param max maximalni iterace
	 */
	public void drawRect(Point point, double side, int iteraction, int max) {
		currentRectangles++;
		if(rainbowColor) {
			g.setFill(getRainbowColor(currentRectangles, totalRectangles, 1D, 0.8));
		}
		g.fillRect(point.getX()-side/2, point.getY()-side/2, side, side);
		if(iteraction < max) {
			Point leftTop = new Point(point.getX()-side, point.getY()-side);
			drawRect(leftTop, side/3, iteraction+1, max);
			Point centerTop = new Point(point.getX(), point.getY()-side);
			drawRect(centerTop, side/3, iteraction+1, max);
			Point rightTop = new Point(point.getX()+side, point.getY()-side);
			drawRect(rightTop, side/3, iteraction+1, max);
			Point leftCenter = new Point(point.getX()-side, point.getY());
			drawRect(leftCenter, side/3, iteraction+1, max);
			Point rightCenter = new Point(point.getX()+side, point.getY());
			drawRect(rightCenter, side/3, iteraction+1, max);
			Point leftBottom = new Point(point.getX()-side, point.getY()+side);
			drawRect(leftBottom, side/3, iteraction+1, max);
			Point centerBottom = new Point(point.getX(), point.getY()+side);
			drawRect(centerBottom, side/3, iteraction+1, max);
			Point rightBottom = new Point(point.getX()+side, point.getY()+side);
			drawRect(rightBottom, side/3, iteraction+1, max);
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
