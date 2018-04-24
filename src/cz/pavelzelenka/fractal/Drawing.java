package cz.pavelzelenka.fractal;

import cz.pavelzelenka.fractal.fractals.Dragon;
import cz.pavelzelenka.fractal.fractals.Hilbert;
import cz.pavelzelenka.fractal.fractals.Koch;
import cz.pavelzelenka.fractal.fractals.Tree;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
    
/**
 * Kresba
 * @author Pavel Zelenka A16B0176P
 * @version 2018-04-15
 */
public class Drawing {

	public static final double MIN_POINT_SIZE = 1D;
	public static final double MAX_POINT_SIZE = 18D;
	public static final double MIN_LINE_WIDTH = 1D;
	public static final double MAX_LINE_WIDTH = 14D;
	
	/** Usecky */
	private LineSegment[] currentGen;
    private LineSegment[] nextGen;    
    
    /** Bod */
    private Point traslation;  
    
	/** Vybrany bod */
	private Point selected = null;
	/** Zamek akci mysi */
	private boolean lock = false;
	
	/** ID krivky */
	private int curveID = 3;
	
	/** Barva krivky */
	private Color strokeColor = Color.rgb(41, 128, 185);
	/** Barva pozadi */
	private Color backgroundColor = Color.rgb(255, 255, 255);
	/** Sirka cary */
	private double lineWidth = 2D;
	/** Sirka cary */
	private double pointSize = 4D;
	/** Ovladaci prvek kresleni */
	private GraphicsContext g;
	/** Platno */
	private Canvas activeCanvas;
	
	/**
	 * Vytvoreni instance kresby
	 * @param canvas platno
	 */
	public Drawing(Canvas canvas) {
		this.activeCanvas = canvas;
		g = canvas.getGraphicsContext2D();
		observeCanvasSize();
		mouseAction();
	}
	
	private void generateAndRedraw(GraphicsContext g, Canvas activeCanvas) {
		clear();
		if (currentGen == null) {
			if(curveID == 2) {
				Koch koch = new Koch();
				currentGen = koch.firstGenerationSpecialKoch(g, activeCanvas);
				traslation = koch.getTraslation();
			} else if(curveID == 3) {
				Dragon dragon = new Dragon();
				currentGen = dragon.firstGeneration(g, activeCanvas);
				traslation = dragon.getTraslation();
			} else if(curveID == 4) {
				Tree tree = new Tree();
				currentGen = tree.firstGeneration(g, activeCanvas);
				traslation = tree.getTraslation();
			} else {
				Hilbert hilbert = new Hilbert();
				currentGen = hilbert.firstGeneration(g, activeCanvas);
				traslation = new Point(MAX_POINT_SIZE/2, MAX_POINT_SIZE/2);
			}
		} else {
			if(curveID == 3) {
				Dragon dragon = new Dragon();
				currentGen = dragon.nextGeneration(currentGen);
			} else if(curveID == 4) {
				Tree tree = new Tree();
				currentGen = tree.nextGeneration(currentGen);
			} else if(curveID == 2) {
				Koch koch = new Koch();
				currentGen = koch.nextGeneration(currentGen);
			} else {
				Hilbert hilbert = new Hilbert();
				currentGen = hilbert.nextGeneration(currentGen);
			}
		}
		g.save();
		g.translate(traslation.getX(), traslation.getY());
        drawSegments(currentGen, g);
        g.restore();
	}
	
	private void drawSegments(LineSegment[] coordinates, GraphicsContext g) {
		g.setLineJoin(StrokeLineJoin.ROUND);
		g.setLineCap(StrokeLineCap.ROUND);
		g.setStroke(strokeColor);
		g.setLineWidth(lineWidth);
        Point[] points = new Point[coordinates.length];
        for (int i = 0; i < points.length; i++) {
        	
        	double hue = Math.floor((double)i * 360D/(double)(points.length));
        	double saturation = 1D;
        	double brightness = 0.8D;
        	Color color = Color.hsb(hue, saturation, brightness);
        	g.setStroke(color);
        	
        	double x1 = coordinates[i].A.getX();
        	double y1 = coordinates[i].A.getY();
        	double x2 = coordinates[i].B.getX();
        	double y2 = coordinates[i].B.getY();
        	g.strokeLine(x1, y1, x2, y2);
        }
        for (int i = 0; i < points.length; i++) {
        	double hue = Math.floor((double)i * 360D/(double)(points.length));
        	double saturation = 1D;
        	double brightness = 0.8D;
        	Color color = Color.hsb(hue, saturation, brightness);
        	g.setFill(color);
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
	 * Pozorovani zmen velikosti platna
	 */
	private void observeCanvasSize() {
		activeCanvas.widthProperty().addListener(event -> {
			redraw();
		});
		
		activeCanvas.heightProperty().addListener(event -> {
			redraw();
		});
	}
		
	/**
	 * Prekresli plochu
	 */
	public void redraw() {
		clear();
		draw();
	}
	
	/**
	 * Vycisti plochu
	 */
	public void clear() {
		g.clearRect(0, 0, activeCanvas.getWidth(), activeCanvas.getHeight());
		g.setFill(backgroundColor);
		g.fillRect(0, 0, activeCanvas.getWidth(), activeCanvas.getHeight());
	}
	
	/**
	 * Vykresleni obrazu
	 */
	public void draw() {
		if (currentGen != null && g != null && traslation != null) {
			clear();
			g.save();
			g.translate(traslation.getX(), traslation.getY());
	        drawSegments(currentGen, g);
	        g.restore();
		}
	}
	
	/**
	 * Akce pusteni mysi
	 */
	public void mousePressed() {
		activeCanvas.setOnMousePressed(event -> {
			generateAndRedraw(this.g, this.activeCanvas);
		//	lock = false;
		});
	}
	
	/**
	 * Akce mysi
	 */
	public void mouseAction() {
		mousePressed();
	}
	
	/**
	 * Zahodit soucasnou spline
	 */
	public void throwOut() {
		currentGen = null;
		redraw();
	}
	
	
	/**
	 * Vrati barvu obtazeni
	 * @return barva obtazeni
	 */
	public Color getStrokeColor() {
		return strokeColor;
	}

	/**
	 * Nastavi barvu obtazeni
	 * @param color barva obtazeni
	 */
	public void setStrokeColor(Color color) {
		this.strokeColor = color;
		redraw();
	}

	/**
	 * Vrati barvu pozadi
	 * @return barva pozadi
	 */
	public Color getBackgroundColor() {
		return backgroundColor;
	}

	/**
	 * Nastavi barvu pozadi
	 * @param color barva pozadi
	 */
	public void setBackgroundColor(Color color) {
		this.backgroundColor = color;
		redraw();
	}
	
	/**
	 * Vrati sirku cary
	 * @return sirka cary
	 */
	public double getLineWidth() {
		return lineWidth;
	}

	/**
	 * Nastavi sirku cary
	 * @param lineWidth sirka cary
	 */
	public void setLineWidth(double lineWidth) {
		this.lineWidth = lineWidth;
		redraw();
	}
	
	/**
	 * Vrati velikost bodu
	 * @return velikost bodu
	 */
	public double getPointSize() {
		return this.pointSize;
	}

	/**
	 * Nastavi velikost bodu
	 * @param pointSize velikost bodu
	 */
	public void setPointSize(double pointSize) {
		this.pointSize = pointSize;
		redraw();
	}
	
    public int getCurve() {
        return this.curveID;
    }
	
    public void setCurve(int id) {
        this.curveID = id;
    }
}
