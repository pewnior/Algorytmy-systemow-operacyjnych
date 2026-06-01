package glowny;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import algorytmy.Algorytm;
import algorytmy.Alru;
import algorytmy.Fifo;
import algorytmy.Lru;
import algorytmy.Opt;
import algorytmy.Rand;
import strona.Generator;
import strona.Strona;

public class Glowny {

	public static void main(String[] args) {
		int memorySize = 4;
		int procesSize = 500;
		int w = 10;
		int e = 6;
		
		//zestaw 1 
		System.out.println("Zestaw 1 - z treści zadania");
		ArrayList<Strona>dataSet=Generator.generateList1();
		List<String[]> allStats = new ArrayList<>();
		
		startalgorytm(dataSet,1,w,e,memorySize,allStats);
		startalgorytm(dataSet,2,w,e,memorySize,allStats);
		startalgorytm(dataSet,3,w,e,memorySize,allStats);
		startalgorytm(dataSet,4,w,e,memorySize,allStats);
		startalgorytm(dataSet,5,w,e,memorySize,allStats);
		
		//printDataSet(dataSet);
		
		printSummary(allStats);
		
		//zestaw 2 - losowy 
		memorySize = 20;
		procesSize = 1000;
		int procesNumber = 100;
		w = 10;
		e = 6;
		
		
		System.out.println("Zestaw 2 - losowy");
		dataSet=Generator.generateRandom(procesNumber, procesSize);
		allStats = new ArrayList<>();
				
		startalgorytm(dataSet,1,w,e,memorySize,allStats);
		startalgorytm(dataSet,2,w,e,memorySize,allStats);
		startalgorytm(dataSet,3,w,e,memorySize,allStats);
		startalgorytm(dataSet,4,w,e,memorySize,allStats);
		startalgorytm(dataSet,5,w,e,memorySize,allStats);
				
		//printDataSet(dataSet);
				
		printSummary(allStats);
	}
	
	public static void startalgorytm(ArrayList<Strona>dataSetorg, int number,int w,int e, int memorySize, List<String[]>  allStats) {
		ArrayList<Strona> dataSet = new ArrayList<>();
		for (Strona p : dataSetorg) {
	        dataSet.add(p.copy());
	    }
		
		Algorytm algo;
		
		switch(number) {
			case 1:
				algo = new Fifo(memorySize,w,e);
				break;
			case 2:
				algo = new Rand(memorySize,w,e);
				break;
			case 3:
				algo = new Opt(memorySize,w,e, dataSet);
				break;
			case 4:
				algo = new Lru(memorySize,w,e);
				break;
			case 5:
				algo = new Alru(memorySize,w,e);
				break;	
			default:
				 throw new IllegalArgumentException("Zły numer algorytmu");				
		}
		
		int setSize=dataSet.size();
		for(int i=0;i<setSize;++i) {
			algo.reference(dataSet.get(i));
		}
		
		//statystyki
		int[] stats = algo.getStatics();
		allStats.add(collectStats(algo.getName(),stats));
		
	}

	public static void printStats(String algo, int[] stats) {
		System.out.println(algo);
		System.out.println("Liczba błędów stron: " + stats[0]);
		System.out.println("Liczba szamotań: " + stats[1]);
		System.out.println("Max długość szamotań: " + stats[2]);
		
	}
	
	public static String[] collectStats(String algo, int[] stats) {
	    return new String[]{
	        algo,
	        String.format("%d", stats[0]),
	        String.format("%d", stats[1]),
	    };
	}
	
	public static void printDataSet(ArrayList<Strona>dataSetorg) {
		Queue<Strona>pom=new LinkedList<>(dataSetorg);
		System.out.println("Size " + pom.size());
		System.out.println(" ");
		while(!pom.isEmpty()) {
			Strona druk=pom.poll();
			System.out.println(druk);
		}
	
	}
	
	public static void printSummary(List<String[]> allStats) {
	    String[] headers = {"Algorytm", "Liczba błędów stron", "Liczba szamotań"};
	    int cols = headers.length;
	    int[] widths = new int[cols];

	    for (int i = 0; i < cols; i++) widths[i] = headers[i].length();
	    for (String[] row : allStats) {
	        for (int i = 0; i < cols; i++) {
	            widths[i] = Math.max(widths[i], row[i].length());
	        }
	    }

	    StringBuilder line = new StringBuilder("+");
	    for (int w : widths) line.append("-".repeat(w + 2)).append("+");

	    System.out.println(line);
	    StringBuilder header = new StringBuilder("|");
	    for (int i = 0; i < cols; i++) {
	        header.append(String.format(" %-" + widths[i] + "s |", headers[i]));
	    }
	    System.out.println(header);
	    System.out.println(line);

	    for (String[] row : allStats) {
	        StringBuilder sb = new StringBuilder("|");
	        for (int i = 0; i < cols; i++) {
	            sb.append(String.format(" %-" + widths[i] + "s |", row[i]));
	        }
	        System.out.println(sb);
	    }
	    System.out.println(line);
	}
}
