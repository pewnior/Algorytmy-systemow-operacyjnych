package algorytmy;

import strona.Strona;

public interface Algorytm {
	String getName();
	void reference(Strona x);
	int[] getStatics();
	void addFrame();
	void delateFrame();
}
