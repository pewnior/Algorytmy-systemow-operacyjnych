package strona;

import java.util.ArrayList;
import java.util.Random;

public class Generator {
	public static ArrayList<Strona> generateList1() {
		ArrayList<Strona>list = new ArrayList<>();
		list.add(new Strona(1));
		list.add(new Strona(2));
		list.add(new Strona(3));
		list.add(new Strona(4));
		list.add(new Strona(1));
		list.add(new Strona(2));
		list.add(new Strona(5));
		list.add(new Strona(1));
		list.add(new Strona(2));
		list.add(new Strona(3));
		list.add(new Strona(4));
		list.add(new Strona(5));
		return list;
	}
	public static ArrayList<Strona> generateRandom(int pageSize, int size) {
	    ArrayList<Strona> list = new ArrayList<>();
	    Random rand = new Random();

	    int current = rand.nextInt(pageSize - 1) + 1;
	    list.add(new Strona(current));

	    for (int i = 1; i < size; i++) {

	        // małe przesunięcie => lokalność
	        int shift = rand.nextInt(7) - 3; // zakres: [-3, +3]
	        current += shift;

	        // zabezpieczenie przed wyjściem poza zakres
	        if (current < 1) current = 1;
	        if (current > pageSize - 1) current = pageSize - 1;

	        list.add(new Strona(current));
	    }

	    return list;
	}
}
