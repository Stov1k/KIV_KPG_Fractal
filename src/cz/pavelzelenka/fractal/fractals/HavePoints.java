package cz.pavelzelenka.fractal.fractals;

/**
 * Rozhrani pro fraktaly majici vykreslovatelne body
 * @author Pavel Zelenka A16B0176P
 * @version 2018-05-03
 */
public interface HavePoints {

	/** Vrati velikost bodu */
	public double getPointSize();

	/** Nastavi velikost bodu */
	public void setPointSize(double pointSize);
	
}
