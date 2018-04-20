package cz.pavelzelenka.fractal;

/**
 * Bod
 * @author Pavel Zelenka
 * @version 2018-04-16
 */
public class Point {
	
	/** souradnice */
	private double x, y;
	
	/**
	 * Vytvoreni instance bodu na zvolene souradnici
	 * @param x souradnice
	 * @param y souradnice
	 */
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Nastaveni souradnice bodu
	 * @param x souradnice
	 * @param y souradnice
	 */
	public void setLocation(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Vrati souradnici na ose X
	 * @return souradnici X
	 */
	public double getX() {
		return this.x;
	}
	
	/**
	 * Vrati souradnici na ose Y
	 * @return souradnici Y
	 */
	public double getY() {
		return this.y;
	}
	
	@Override
	public String toString() {
		return "[" + (int)x + "x" + (int)y + "]";
	}
}
