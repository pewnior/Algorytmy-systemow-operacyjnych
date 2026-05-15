package algorytmy;

import proces.proces;

public interface algorytm {
	String getName();
	void newProces(proces x);
    void work();
    boolean isFinish();
    double[] getStatics();
}
