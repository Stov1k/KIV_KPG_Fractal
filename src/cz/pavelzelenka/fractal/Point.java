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
	 * Vrati vzdalenost mezi body
	 * @param point bod
	 * @return vzdalenost mezi body
	 */
	public double getDistance(Point point) {
		double sx = this.getX() - point.getX();		// slozka smeroveho vektoru
		double sy = this.getY() - point.getY();		// slozka smeroveho vektoru
		double d = Math.sqrt(sx*sx + sy*sy);		// delka smeroveho vektoru
		//System.out.println("Distance: " + this.getX() + " x " + this.getY() + " B: " + point.getX() + " x " + point.getY() );
		return d;
	}
	
	/**
	 * Vrati smer
	 * @param point bod
	 * @return smer
	 */
	public Point getDirection(Point point) {
		double d = getDistance(point);				// vzdalenost
		double sx = point.getX() - this.getX();		// slozka smeroveho vektoru
		double sy = point.getY() - this.getY();		// slozka smeroveho vektoru
		//System.out.println("Direction: " + this.getX() + " x " + this.getY() + " B: " + point.getX() + " x " + point.getY() );
		if(d == 0) {
			return new Point(0, 0);
		}
		return new Point(sx/d, sy/d);
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
