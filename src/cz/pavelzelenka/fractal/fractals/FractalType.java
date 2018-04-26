package cz.pavelzelenka.fractal.fractals;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Seznam fraktalu
 * @author Pavel Zelenka
 * @version 2018-04-15
 */
public enum FractalType {
	HILBERT(Hilbert.NAME, Hilbert.class),
	DRAGON(Dragon.NAME, Dragon.class),
	KOCH(Koch.NAME, Koch.class);

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
