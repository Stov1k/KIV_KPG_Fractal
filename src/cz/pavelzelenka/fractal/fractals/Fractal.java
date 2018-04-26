package cz.pavelzelenka.fractal.fractals;

import cz.pavelzelenka.fractal.LineSegment;
import cz.pavelzelenka.fractal.Point;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public interface Fractal {

	public LineSegment[] firstGeneration(GraphicsContext g, Canvas activeCanvas);
	
	public LineSegment[] nextGeneration(LineSegment[] generation);
	
	public Point getTraslation();
	
}
