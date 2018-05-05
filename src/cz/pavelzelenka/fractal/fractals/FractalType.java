package cz.pavelzelenka.fractal.fractals;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Seznam fraktalu
 * @author Pavel Zelenka
 * @version 2018-04-15
 */
public enum FractalType {
	DRAGON("Dragon", Dragon.class),
	HEXAFLAKE("Hexaflake", Hexaflake.class),
	HILBERT("Hilbert", Hilbert.class),
	KOCH("Koch", Koch.class),
	SIERPINSKI_CARPET("Sierpinski Carpet", SierpinskiCarpet.class),
	SIERPINSKI_CARPET_INVERTED("Sierpinski Carpet (Inverted)", SierpinskiCarpetInverted.class),
	SIERPINSKI_HEXAGON("Sierpinski Hexagon", SierpinskiHexagon.class),
	T_SQUARE("T-Square", TSquare.class);

	/** nazev */
    private final String name;

	/** fraktal */
    private final Class fractal;
    
    /**
     * Konstruktor
     * @param name nazev
     * @param fractal fraktal
     */
    private FractalType(String name, Class fractal) {
        this.name = name;
        this.fractal = fractal;
    }

    /**
     * Vrati nazev
     * @return nazev
     */
    public String getName() {
    	return this.name;
    }
    
    /**
     * Vrati tridu
     * @return trida
     */
	public Class getFractal() {
		return this.fractal;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	
	/**
	 * Vrati seznam fraktalu
	 * @return seznam fraktalu
	 */
	public static ObservableList<FractalType> getDefaultList() {
		ObservableList<FractalType> result = FXCollections.observableArrayList(FractalType.values());
		return result;
	}
}
