package cz.pavelzelenka.fractal;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import cz.pavelzelenka.fractal.fractals.Fractal;
import cz.pavelzelenka.fractal.fractals.HavePoints;
import cz.pavelzelenka.fractal.fractals.Hilbert;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
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

	public static final double MIN_POINT_SIZE = 1D;
	public static final double MAX_POINT_SIZE = 18D;
	public static final double MIN_LINE_WIDTH = 1D;
	public static final double MAX_LINE_WIDTH = 14D;
    
    /** Bod */
    private Point traslation;  
	
	/** Zvoleny fraktal */
	private Class fractal = null;
	private Fractal fractalInstance = null;
	
	private int step = 0;
	
	/** Duhova barva */
	private boolean rainbowColor = false;
	/** Barva krivky */
	private Color strokeColor = Color.rgb(41, 128, 185);
	/** Barva pozadi */
	private Color backgroundColor = Color.rgb(255, 255, 255);
	/** Sirka cary */
	private double lineWidth = 2D;
	/** Velikost bodu */
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
		g.setLineJoin(StrokeLineJoin.ROUND);
		g.setLineCap(StrokeLineCap.ROUND);
		g.setStroke(strokeColor);
		g.setFill(strokeColor);
		g.setLineWidth(lineWidth);
		fractalInstance = getFractalInstance(fractal);
		if(fractalInstance != null) {
			fractalInstance.initialize(g);
			fractalInstance.setRainbow(rainbowColor);
			fractalInstance.setStep(step);
			if(fractalInstance instanceof HavePoints) {
				((HavePoints) fractalInstance).setPointSize(pointSize);
			}
			fractalInstance.draw();
		}
	}
	
	/**
	 * Akce kliknuti mysi
	 */
	public void mouseClicked() {
		activeCanvas.setOnMouseClicked(event -> {
			if(fractalInstance != null) {
				if(event.getButton().equals(MouseButton.SECONDARY)) {
					if(fractalInstance.getStep() > 0) {
						this.step--;
						redraw();
					}
				} else {
					if(fractalInstance.getStep() != fractalInstance.getMaximumStep()) {
						this.step++;
						redraw();
					}
				}
			}
		});
	}
	
	/**
	 * Akce mysi
	 */
	public void mouseAction() {
		mouseClicked();
	}
	
	/**
	 * Zahodit soucasny fraktal
	 */
	public void throwOut() {
		this.step = 0;
		redraw();
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
     * Nastavi fraktal
     * Asi nejhnusnejsi metoda, kterou jsem kdy napsal...
     * @param fractalClass trida 
     */
    public void setFractal(Class fractalClass) {
    	this.fractal = fractalClass;
    	this.step = 0;
    	redraw();
    }
    
    /**
     * Vrati instanci fraktalu
     * @param fractalClass trida 
     * @return instance fraktalu
     */
    public Fractal getFractalInstance(Class fractalClass) {
    	if(fractalClass != null) {
    		try {
    			Constructor<?>[] c = Class.forName(fractalClass.getName()).getConstructors();
    			Fractal f = (Fractal) c[0].newInstance();
    			return f;
    		} catch (SecurityException | ClassNotFoundException | InstantiationException |
    				IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
    			e.printStackTrace();

    		}
    	}
    	return null;
    }
    
}
