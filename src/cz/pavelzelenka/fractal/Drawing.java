package cz.pavelzelenka.fractal;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import cz.pavelzelenka.fractal.fractals.Fractal;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;
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
    
    /** Bod */
    private Point traslation;  
	
	/** Zvoleny fraktal */
	private Class fractal = null;
	
	/** Duhova barva */
	private boolean rainbowColor = false;
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
	
	/** Pozadovana sirka pro vykresleni obrazku */
	private DoubleProperty requiredWidth = new SimpleDoubleProperty(0D);
	/** Pozadovana vysku pro vykresleni obrazku */
	private DoubleProperty requiredHeight = new SimpleDoubleProperty(0D);
	
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
		
		if(fractal != null) {
			Fractal f = getCurveInstance(fractal);
			if (currentGen == null) {
				currentGen = f.firstGeneration(g, activeCanvas);
				traslation = f.getTraslation();
			} else {
				currentGen = f.nextGeneration(currentGen);
			}
		} else {
			return;
		}
		g.save();
		g.translate(traslation.getX(), traslation.getY());
        drawSegments(currentGen, g);
        g.restore();
		countRequiredWidth();
		countRequiredHeight();
	}
	
	private void drawSegments(LineSegment[] coordinates, GraphicsContext g) {
		g.setLineJoin(StrokeLineJoin.ROUND);
		g.setLineCap(StrokeLineCap.ROUND);
		g.setStroke(strokeColor);
		g.setLineWidth(lineWidth);
        Point[] points = new Point[coordinates.length];
        for (int i = 0; i < points.length; i++) {
        	Color color = strokeColor;
        	if(rainbowColor) {
        		color = getRainbowColor(i, points.length, 1D, 0.8D);
        	}
        	g.setStroke(color);
        	double x1 = coordinates[i].A.getX();
        	double y1 = coordinates[i].A.getY();
        	double x2 = coordinates[i].B.getX();
        	double y2 = coordinates[i].B.getY();
        	g.strokeLine(x1, y1, x2, y2);
        }
        for (int i = 0; i < points.length; i++) {
        	Color color = strokeColor;
        	if(rainbowColor) {
        		color = getRainbowColor(i, points.length, 1D, 0.8D);
        	}
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
		countRequiredWidth();
		countRequiredHeight();
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
	
	/**
	 * Vrati, zdali je nastavena duhova barva
	 * @return vrati TRUE, kdyz je nastavena duhova barva
	 */
	public boolean isRainbowColor() {
		return rainbowColor;
	}

	/**
	 * Nastavi duhovou barvu
	 * @return duhova barva krivky
	 */
	public void setRainbowColor(boolean rainbowColor) {
		this.rainbowColor = rainbowColor;
		redraw();
	}
	
	/**
	 * Vrati pozadovanou sirku pro vykresleni obrazku
	 * @return pozadovana sirka pro vykresleni obrazku
	 */
	public double getRequiredWidth() {
		return requiredWidth.get();
	}
	
	/**
	 * Vrati pozadovanou vysku pro vykresleni obrazku
	 * @return pozadovana vysku pro vykresleni obrazku
	 */
	public double getRequiredHeight() {
		return requiredHeight.get();
	}
	
	/**
	 * Pozadovana sirka pro vykresleni obrazku
	 * @return pozadovana sirka pro vykresleni obrazku
	 */
	public DoubleProperty requiredWidthProperty() {
		return requiredWidth;
	}

	/**
	 * Pozadovana vysku pro vykresleni obrazku
	 * @return pozadovana vysku pro vykresleni obrazku
	 */
	public DoubleProperty requiredHeightProperty() {
		return requiredHeight;
	}

	/**
	 * Spocte pozadovanou sirku pro vykresleni obrazku
	 * @return pozadovana sirka pro vykresleni obrazku
	 */
	public double countRequiredWidth() {
		double maxx = 0D;
		if(currentGen != null) {
			for(int i = 0; i < currentGen.length; i++) {
				if(currentGen[i] != null) {
					if(currentGen[i].A != null) {
						if(currentGen[i].A.getX() > maxx) maxx = currentGen[i].A.getX();
					}
					if(currentGen[i].B != null) {
						if(currentGen[i].B.getX() > maxx) maxx = currentGen[i].B.getX();
					}
				}
			}
			if(this.traslation != null) {
				maxx += this.traslation.getX();
			}
		}
		requiredWidth.set(maxx);
		return maxx;
	}
	
	/**
	 * Spocte pozadovanou vysku pro vykresleni obrazku
	 * @return pozadovana vysku pro vykresleni obrazku
	 */
	public double countRequiredHeight() {
		double maxy = 0D;
		if(currentGen != null) {
			for(int i = 0; i < currentGen.length; i++) {
				if(currentGen[i] != null) {
					if(currentGen[i].A != null) {
						if(currentGen[i].A.getY() > maxy) maxy = currentGen[i].A.getY();
					}
					if(currentGen[i].B != null) {
						if(currentGen[i].B.getY() > maxy) maxy = currentGen[i].B.getY();
					}
				}
			}
			if(this.traslation != null) {
				maxy += this.traslation.getY();
			}
		}
		requiredHeight.set(maxy);
		return maxy;
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
	
    /**
     * Nastavi krivku
     * Asi nejhnusnejsi metoda, kterou jsem kdy napsal...
     * @param fractalClass trida 
     */
    public void setCurve(Class fractalClass) {
    	this.fractal = fractalClass;
    }
    
    /**
     * Vrati instanci krivky
     * Asi nejhnusnejsi metoda, kterou jsem kdy napsal...
     * @param fractalClass trida 
     * @return instance
     */
    public Fractal getCurveInstance(Class fractalClass) {
    	try {
			Constructor<?>[] c = Class.forName(fractalClass.getName()).getConstructors();
			Fractal f = (Fractal) c[0].newInstance();
			return f;
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
    	return null;
    }

	/**
	 * Vrati obrazek
	 * @return obrazek
	 */
	public WritableImage getSplineImage() {
		WritableImage working;
		double minx = Double.MAX_VALUE;
		double maxx = 0D;
		double miny = Double.MAX_VALUE;
		double maxy = 0D;
		double offset = Math.max(pointSize/2, lineWidth/2);
		if(currentGen != null) {
			for(int i = 0; i < currentGen.length; i++) {
				if(currentGen[i] != null) {
					if(currentGen[i].A != null) {
						Point p = currentGen[i].A;
						if(p.getX() < minx) minx = p.getX();
						if(p.getX() > maxx) maxx = p.getX();
						if(p.getY() < miny) miny = p.getY();
						if(p.getY() > maxy) maxy = p.getY();
					}
					if(currentGen[i].B != null) {
						Point p = currentGen[i].B;
						if(p.getX() < minx) minx = p.getX();
						if(p.getX() > maxx) maxx = p.getX();
						if(p.getY() < miny) miny = p.getY();
						if(p.getY() > maxy) maxy = p.getY();
					}
				}
			}
			if(this.traslation != null) {
				minx += this.traslation.getX()-offset;
				maxx += this.traslation.getX()+offset;
				miny += this.traslation.getY()-offset;
				maxy += this.traslation.getY()+offset;
			} else {
				maxx += offset;
				maxy += offset;
				if(minx-offset >= 0) minx-=offset;
				if(miny-offset >= 0) miny-=offset;
			}
		}
		if(minx > maxx) {
			return null;
		}
		clear();
		g.save();
		g.translate(-minx, -miny);
		draw();
		working = new WritableImage((int)(maxx-minx),(int)(maxy-miny));
		SnapshotParameters params = new SnapshotParameters();
		params.setFill(Color.TRANSPARENT);
		activeCanvas.snapshot(params, working);
		g.restore();
		redraw();
		return working;
	}
}
