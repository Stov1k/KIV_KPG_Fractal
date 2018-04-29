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
	 * @param target cilovy bod
	 * @return vzdalenost mezi body
	 */
	public double getDistance(Point target) {
		double sx = this.getX() - target.getX();		// slozka smeroveho vektoru
		double sy = this.getY() - target.getY();		// slozka smeroveho vektoru
		double d = Math.sqrt(sx*sx + sy*sy);		// delka smeroveho vektoru
		return d;
	}
	
	/**
	 * Vrati smer
	 * @param target cilovy bod
	 * @return smer
	 */
	public Point getDirection(Point target) {
		double d = getDistance(target);					// vzdalenost
		double sx = target.getX() - this.getX();		// slozka smeroveho vektoru
		double sy = target.getY() - this.getY();		// slozka smeroveho vektoru
		if(d == 0) {
			return new Point(0, 0);
		}
		return new Point(sx/d, sy/d);
	}
	
	/**
	 * Vrati bod mezi 2 body
	 * @param target cilovy bod
	 * @return bod mezi 2 body
	 */
	public Point getMidpoint(Point target) {
		double midX = (target.getX() + this.getX())/2;
		double midY = (target.getY() + this.getY())/2;
		return new Point(midX, midY);
	}
	
	/**
	 * Vrati uhel
	 * @param target cilovy bod
	 * @return uhel
	 */
	public double getAngle(Point target) {
		double angle = Math.atan2(target.getY() - this.getY(), target.getX() - this.getX());
		return angle;
	}
	
	/**
	 * Posunuti vpred
	 * @param distance vzdalenost
	 * @param angle uhel v radianech
	 * @return bod pred
	 */
	public Point getForward(double distance, double angle) {
        double x = distance * Math.cos(angle);
        double y = distance * Math.sin(angle);
        return new Point(this.getX() + x, this.getY() + y);
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
