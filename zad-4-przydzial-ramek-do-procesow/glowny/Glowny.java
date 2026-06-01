package glowny;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import algorytmy.Algorytm;
import proces.Proces;
import przydzial.Proporcjonalny;
import przydzial.Przydzial;
import przydzial.Rowny;
import przydzial.Scbs;
import przydzial.Strefowy;
import strona.Strona;

public class Glowny {
	
	
	public static void main(String[] args) {
		int framesSize = 20;
		
		System.out.println("Test 1");
		List<String[]> allStats = new ArrayList<>();
		
		ArrayList<Proces>dataSet = new ArrayList<>();
		dataSet.add(new Proces(0,50,1,10,0,10,4));
		dataSet.add(new Proces(1,100,11,50,0,10,4));
		dataSet.add(new Proces(2,500,51,150,0,10,4));
		dataSet.add(new Proces(3,50,151,200,0,10,4));
		dataSet.add(new Proces(4,50,201,210,0,10,4));
		dataSet.add(new Proces(5,50,211,220,0,10,4));
		dataSet.add(new Proces(6,50,221,230,0,10,4));
		dataSet.add(new Proces(7,50,231,240,0,10,4));
		
		startalgorytm(1,framesSize,dataSet,allStats);
		startalgorytm(2,framesSize,dataSet,allStats);
		startalgorytm(3,framesSize,dataSet,allStats);
		startalgorytm(4,framesSize,dataSet,allStats);
		
		//printDataSet(dataSet);
		
		printSummary(allStats);
		
	}
	
	public static void startalgorytm(int number,int framesSize, ArrayList<Proces>ProcesListorg, List<String[]>  allStats) {
		ArrayList<Proces> dataSet = new ArrayList<>();
		for (Proces p : ProcesListorg) {
	        dataSet.add(p.copy());
	    }
		
		//dla sterowanych
		double l = 0.2;
		double u = 0.3;
		int dt = 25;
		double h = 0.85;
		
		Przydzial algo;
		switch(number) {
			case 1:
				algo=new Rowny(framesSize);
				break;
			case 2:
				algo=new Proporcjonalny(framesSize);
				break;
			case 3:
				algo=new Scbs(framesSize);
				((Scbs)algo).setParameters(l, u, h, dt);
				break;
			case 4:
				algo=new Strefowy(framesSize);
				((Strefowy)algo).setParameters(l, u, h, dt);
				break;
			default:
				 throw new IllegalArgumentException("Zły numer algorytmu");				
		}
		
		for (int i=0;i<dataSet.size();++i) {
	        algo.addProces(dataSet.get(i));
	    }
		
		algo.setFrames(); //inicjacja przydziału
		
		while(!algo.isFinish()) {
			algo.work();
		}
		
		
		//statystyki
		int[] stats = algo.getStatics();
		allStats.add(collectStats(algo.getName(),stats));
		
		if(algo instanceof Strefowy) {
			System.out.println("Uśpione: Strefowy " + ((Strefowy)algo).disactivated);
		}
		if(algo instanceof Scbs) {
			System.out.println("Uśpione: SCBS " + ((Scbs)algo).disactivated);
		}
		
	}

	public static void printStats(String algo, int[] stats) {
		System.out.println(algo);
		System.out.println("Liczba błędów stron: " + stats[0]);
		
	}
	
	public static String[] collectStats(String algo, int[] stats) {

	    String[] row = new String[stats.length + 1];

	    row[0] = algo;

	    for (int i = 0; i < stats.length; i++) {
	        row[i + 1] = String.valueOf(stats[i]);
	    }

	    return row;
	}
	
	/*public static void printDataSet(ArrayList<Strona>dataSetorg) {
		Queue<Strona>pom=new LinkedList<>(dataSetorg);
		while(!pom.isEmpty()) {
			Strona druk=pom.poll();
			System.out.println(druk);
		}
	
	}*/
	
	public static void printSummary(List<String[]> allStats) {

		int cols = allStats.get(0).length;

		String[] headers = new String[cols];

		headers[0] = "Algorytm";
		headers[1] = "Suma błędów";
		headers[2] = "Suma szamotań";

		int processCount = (cols - 3) / 2;

		for (int i = 0; i < processCount; i++) {
			headers[3 + i * 2] = "P" + i + "_błedy";
			headers[4 + i * 2] = "P" + i + "_ramki";
		}

		int[] widths = new int[cols];

		// szerokości
		for (int i = 0; i < cols; i++) {
			widths[i] = headers[i].length();
		}

		for (String[] row : allStats) {
			for (int i = 0; i < cols; i++) {
				if (row[i] != null) {
					widths[i] = Math.max(widths[i], row[i].length());
				}
			}
		}

		// linia
		StringBuilder line = new StringBuilder("+");
		for (int w : widths) {
			line.append("-".repeat(w + 2)).append("+");
		}

		System.out.println(line);

		// header
		StringBuilder header = new StringBuilder("|");
		for (int i = 0; i < cols; i++) {
			header.append(String.format(" %-" + widths[i] + "s |",
				headers[i] == null ? "" : headers[i]));
		}

		System.out.println(header);
		System.out.println(line);

		// dane
		for (String[] row : allStats) {

			StringBuilder sb = new StringBuilder("|");

			for (int i = 0; i < cols; i++) {

				String val = (row[i] == null) ? "0" : row[i];

				sb.append(String.format(" %-" + widths[i] + "s |", val));
			}

			System.out.println(sb);
		}
		
		System.out.println(line);
	}
}
