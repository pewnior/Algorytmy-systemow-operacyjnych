package przydzial;

import proces.Proces;

public interface Przydzial {

	int[] getStatics();
	String getName();
	boolean isFinish();
	void addProces(Proces newProces);
	void work();
	void setFrames();
}
