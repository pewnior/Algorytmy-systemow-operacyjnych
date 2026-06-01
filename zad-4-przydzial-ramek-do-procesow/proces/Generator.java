package proces;

import java.util.ArrayList;
import java.util.Random;

import strona.Strona;

public class Generator {
	public static ArrayList<Strona> generateProces(int size, int start, int end) {
	    ArrayList<Strona>list = new ArrayList<>();
	    Random rand = new Random();
	    int localityRange = 5;
	    int current = 0;
	    while(current<size) {
	        int localityStart = rand.nextInt(end-start-localityRange)+start;
	        int localityEnd = localityStart+localityRange;
	        int localityLength = rand.nextInt(20)+10;
	        for(int i=0;i<localityLength && current<size;++i) {
	            int number = rand.nextInt(localityEnd-localityStart+1)+localityStart;
	            list.add(new Strona(number));
	            ++current;
	        }
	    }
	    return list;
	}
	
}
