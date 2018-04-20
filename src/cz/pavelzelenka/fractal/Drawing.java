package cz.pavelzelenka.fractal;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point2D;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;

/**
 * Kresba
 * @author Pavel Zelenka A16B0176P
 * @version 2018-04-15
 */
public class Drawing {
	
	/** Seznam bodu */
	private List<Point> points = new ArrayList<Point>();
	/** Vybrany bod */
	private Point selected = null;
	/** Zamek akci mysi */
	private boolean lock = false;
	
	/** Barva krivky */
	private Color splineColor = Color.rgb(41, 128, 185);
	/** Sirka cary */
	private double lineWidth = 2D;
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
	}
	
	/**
	 * Vykresleni mrizky
	 */
	public void drawGrid() {
		double width = activeCanvas.getWidth();
		double height = activeCanvas.getHeight();
		int markDis = 10;
		g.setStroke(Color.rgb(200, 200, 200));
	
		Point2D x0 = new Point2D(width/2, 0);
		Point2D xmax = new Point2D(width/2, height);
		Point2D y0 = new Point2D(0, height/2);
		Point2D ymax = new Point2D(width, height/2);
		Point2D center = new Point2D(width/2, height/2);
		
		g.strokeLine(x0.getX(), x0.getY(), xmax.getX(), xmax.getY());
		g.strokeLine(y0.getX(), y0.getY(), ymax.getX(), ymax.getY());
		
		for(int i = (int)center.getX()+markDis; i < width; i+=markDis) {
			Point2D tr = new Point2D(i, 0);
			Point2D br = new Point2D(i, height);
			g.strokeLine(tr.getX(), tr.getY(), br.getX(), br.getY());
		}
		for(int i = (int)center.getX()-markDis; i > 0; i-=markDis) {
			Point2D tr = new Point2D(i, 0);
			Point2D br = new Point2D(i, height);
			g.strokeLine(tr.getX(), tr.getY(), br.getX(), br.getY());
		}
		for(int i = (int)center.getY()+markDis; i < height; i+=markDis) {
			Point2D tr = new Point2D(0, i);
			Point2D br = new Point2D(width, i);
			g.strokeLine(tr.getX(), tr.getY(), br.getX(), br.getY());
		}
		for(int i = (int)center.getY()-markDis; i > 0; i-=markDis) {
			Point2D tr = new Point2D(0, i);
			Point2D br = new Point2D(width, i);
			g.strokeLine(tr.getX(), tr.getY(), br.getX(), br.getY());
		}
	}
	
	/**
	 * Vykresleni osy X,Y
	 */
	public void drawAxis() {
		double width = activeCanvas.getWidth();
		double height = activeCanvas.getHeight();
		int markDis = 10;
		int markSize = 3;
		
		g.setStroke(Color.rgb(50, 50, 50));
		
		Point2D x0 = new Point2D(width/2, 0);
		Point2D xmax = new Point2D(width/2, height);
		Point2D y0 = new Point2D(0, height/2);
		Point2D ymax = new Point2D(width, height/2);
		Point2D center = new Point2D(width/2, height/2);
		
		g.strokeLine(x0.getX(), x0.getY(), xmax.getX(), xmax.getY());
		g.strokeLine(y0.getX(), y0.getY(), ymax.getX(), ymax.getY());
		
		for(int i = (int)center.getX()+markDis; i < width; i+=markDis) {
			Point2D tr = new Point2D(i, height/2-markSize);
			Point2D br = new Point2D(i, height/2+markSize);
			g.strokeLine(tr.getX(), tr.getY(), br.getX(), br.getY());
		}
		for(int i = (int)center.getX()-markDis; i > 0; i-=markDis) {
			Point2D tr = new Point2D(i, height/2-markSize);
			Point2D br = new Point2D(i, height/2+markSize);
			g.strokeLine(tr.getX(), tr.getY(), br.getX(), br.getY());
		}
		for(int i = (int)center.getY()+markDis; i < height; i+=markDis) {
			Point2D tr = new Point2D(width/2-markSize, i);
			Point2D br = new Point2D(width/2+markSize, i);
			g.strokeLine(tr.getX(), tr.getY(), br.getX(), br.getY());
		}
		for(int i = (int)center.getY()-markDis; i > 0; i-=markDis) {
			Point2D tr = new Point2D(width/2-markSize, i);
			Point2D br = new Point2D(width/2+markSize, i);
			g.strokeLine(tr.getX(), tr.getY(), br.getX(), br.getY());
		}
	}
	/**
	 * Vykresleni bodu
	 */
	public void drawPoints() {
		double ps = 10D;			// Point Size
		double hps = ps/2;			// Half Point Size
		Point prev = null;
		for(Point p : points) {
			if(p != selected) {
				g.setFill(Color.rgb(39, 174, 96));
			} else {
				g.setFill(Color.rgb(211, 84, 0));
			}
			if(prev != null) {
				if(p != selected && prev != selected) {
					g.setStroke(Color.rgb(39, 174, 96));
				} else {
					g.setStroke(Color.rgb(211, 84, 0));
				}
				g.strokeLine(prev.getX(), prev.getY(), p.getX(), p.getY());
			}
			g.fillOval(p.getX()-hps, p.getY()-hps, 10, 10);
			prev = p;
		}
	}
	
	/**
	 * Vykresleni obrazu
	 */
	public void draw() {
		countRequiredWidth();
		countRequiredHeight();
	//	drawGrid();
	//	drawAxis();
		drawPoints();
	}
	
	/**
	 * Akce pusteni mysi
	 */
	public void mousePressed() {
		activeCanvas.setOnMousePressed(event -> {
			lock = false;
		});
	}
	
	/**
	 * Akce tazeni mysi
	 */
	public void mouseDragged() {
		double ps = 12D;			// Point Size
		double hps = ps/2;			// Half Point Size
		activeCanvas.setOnMouseDragged(event -> {
			for(Point p : points) {
				if(p.getX()-hps <= event.getX() && p.getX()+hps >= event.getX()) {
					if(p.getY()-hps <= event.getY() && p.getY()+hps >= event.getY()) {
						if(!lock) {
							selected = p;
							lock = true;
						}
					}
				}
				if(lock) {
					double x = event.getX();
					double y = event.getY();
					if(x < 0) x = 0;
					if(y < 0) y = 0;
					selected.setLocation(x, y);
					redraw();
					break;
				}
			}
		});
	}
	
	/**
	 * Akce kliknuti mysi
	 */
	public void mouseClicked() {
		double ps = 12D;			// Point Size
		double hps = ps/2;			// Half Point Size
		activeCanvas.setOnMouseClicked(event -> {
			if(event.getButton().equals(MouseButton.PRIMARY)) {
				boolean select = false;
				for(Point p : points) {
					if(p.getX()-hps <= event.getX() && p.getX()+hps >= event.getX()) {
						if(p.getY()-hps <= event.getY() && p.getY()+hps >= event.getY()) {
							selected = p;
							select = true;
						}
					}
				}
				if(!select) {
					selected = null;
					lock = false;
				}
				if(selected != null) {
					selected.setLocation(event.getX(), event.getY());
				}
				redraw();
			} else if(event.getButton().equals(MouseButton.SECONDARY)) {
				Point newPoint = new Point(event.getX(), event.getY());
				points.add(newPoint);
				selected = newPoint;
				redraw();
			}
		});
	}
	
	/**
	 * Akce mysi
	 */
	public void mouseAction() {
		mouseClicked();
		mouseDragged();
		mousePressed();
	}
	
	/**
	 * Vrati barvu krivky
	 * @return barva krivky
	 */
	public Color getSplineColor() {
		return splineColor;
	}

	/**
	 * Nastavi barvu krivky
	 * @param splineColor barva krivky
	 */
	public void setSplineColor(Color splineColor) {
		this.splineColor = splineColor;
		redraw();
	}

	/**
	 * Vrati sirku cary
	 * @return  sirka cary
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
	 * Zahodit soucasnou spline
	 */
	public void throwOutSpline() {
		points.clear();
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
		for(Point p : points) {
			if(p.getX() > maxx) maxx = p.getX();
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
		for(Point p : points) {
			if(p.getY() > maxy) maxy = p.getY();
		}
		requiredHeight.set(maxy);
		return maxy;
	}
	
}
