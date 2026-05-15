package algorytmy;

import zadanie.Zadanie;

public interface Algorytm {
	String getName();
	void newProces(Zadanie x);
    void work();
    boolean isFinish();
    double[] getStatics();
}
