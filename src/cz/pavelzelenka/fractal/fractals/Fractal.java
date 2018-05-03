package cz.pavelzelenka.fractal.fractals;

import javafx.scene.canvas.GraphicsContext;

/**
 * Rozhrani pro fraktaly
 * @author Pavel Zelenka A16B0176P
 * @version 2018-05-03
 */
public interface Fractal {

	/** Inicializace grafickeho nastroje */
	public void initialize(GraphicsContext g);
	
	/** Pouziti duhove barvy */
	public void setRainbow(boolean value);
	
	/** Vykresli */
	public void draw();
	
	/** Vrati maximalni mozny krok */
	public int getMaximumStep();
	
	/** Vrati krok */
	public int getStep();
	
	/** Nastavi krok */
	public void setStep(int step);
}
