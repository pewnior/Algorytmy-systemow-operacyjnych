package glowny;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import algorytmy.Algorytm;
import algorytmy.CScan;
import algorytmy.Edf;
import algorytmy.Fcfs;
import algorytmy.FdScan;
import algorytmy.Scan;
import algorytmy.Sstf;
import zadanie.Zadanie;
import zadanie.Generator;


public class Glowny {

	public static void main(String[] args) {
		int setSize = 100; //rozmiar zbioru danych
		int diskSize = 1000; //rozmiar dysku
		int startPosition = 50; //pozycja startowa
		
		//zestaw 1 losowy
		System.out.println("Zestaw 1 - losowy w jednym momencie");
		Queue<Zadanie>dataSet=Generator.generateListRandomOneMoment(setSize, diskSize);
		List<String[]> allStats = new ArrayList<>();
		
		startalgorytm(dataSet,1,diskSize,startPosition,allStats);
		startalgorytm(dataSet,2,diskSize,startPosition,allStats);
		startalgorytm(dataSet,3,diskSize,startPosition,allStats);
		startalgorytm(dataSet,4,diskSize,startPosition,allStats);
		
		printSummary(allStats);
		
		System.out.println("Zestaw 2 - losowy całkowicie");
		dataSet=Generator.generateListRandom(setSize, diskSize);
		allStats = new ArrayList<>();
		startalgorytm(dataSet,1,diskSize,startPosition,allStats);
		startalgorytm(dataSet,2,diskSize,startPosition,allStats);
		startalgorytm(dataSet,3,diskSize,startPosition,allStats);
		startalgorytm(dataSet,4,diskSize,startPosition,allStats);
		
		printSummary(allStats);
		
		System.out.println("Zestaw 3 - raz początek, raz koniec");
		dataSet=Generator.generateListSkrany1(setSize, diskSize);
		allStats = new ArrayList<>();
		
		startalgorytm(dataSet,1,diskSize,startPosition,allStats);
		startalgorytm(dataSet,2,diskSize,startPosition,allStats);
		startalgorytm(dataSet,3,diskSize,startPosition,allStats);
		startalgorytm(dataSet,4,diskSize,startPosition,allStats);
		
		printSummary(allStats);
		
		System.out.println("Zestaw 4 - RealTime losowy");
		dataSet=Generator.generateListRealTime(setSize, diskSize);
		allStats = new ArrayList<>();
		
		startalgorytm(dataSet,5,diskSize,startPosition,allStats);
		startalgorytm(dataSet,6,diskSize,startPosition,allStats);
		
		printSummary(allStats);
		
		
		
	}
	
	public static void startalgorytm(Queue<Zadanie>dataSetorg, int numer, int diskSize,int startPosition,List<String[]> allStats) { //1-fcfs, 2-sjf, 3-sjf z wywałaszczeniem, 4-rr
		Queue<Zadanie> dataSet = new LinkedList<>();
		for (Zadanie p : dataSetorg) {
	        dataSet.offer(p.copy());
	    }
		
		Algorytm disk;
		
		switch(numer) {
			case 1:
				disk=new Fcfs(diskSize, startPosition);
				break;
			case 2:
				disk = new Sstf(diskSize, startPosition);
				break;
			case 3:
				disk = new Scan(diskSize, startPosition);
				break;
			case 4:
				disk = new CScan(diskSize, startPosition);
				break;
			case 5:
				disk = new Edf(diskSize, startPosition);
				break;
			case 6:
				disk = new FdScan(diskSize, startPosition);
				break;
			default:
				 throw new IllegalArgumentException("Zły numer algorytmu");				
		}
		int time=0;
		int setSize=dataSet.size();
		int procesesSent=0;
		Zadanie pom;
		
		while(procesesSent!=setSize) {
			while(!dataSet.isEmpty() && dataSet.peek().startTime==time) {
				pom=dataSet.poll();
				disk.newProces(pom);
				++procesesSent;
			}
			
			disk.work();
			++time;
		}
		while(!disk.isFinish()) {
			disk.work();
			++time;
		}
		
		//statystyki
		double[] stats = disk.getStatics();
		allStats.add(collectStats(disk.getName(),stats,time));
		
	}

	public static void printStats(String algo, double[] stats, int time) {
		System.out.print("\n");
		System.out.println(algo);
		System.out.println("Czas pracy: " + time);
		System.out.println("Średni czas oczekiwania: " + stats[0]);
		System.out.println("Maksymalny czas oczekiwania: " + stats[1]);
		System.out.println("Zagłodzone żądania: " + stats[2]);
		System.out.println("Liczba przesunięć: " + stats[3]);
	}
	
	public static String[] collectStats(String algo, double[] stats, int time) {
	    return new String[]{
	        algo,
	        String.format("%.2f", stats[0]),
	        String.format("%.0f", stats[1]),
	        String.format("%.0f", stats[2]),
	        String.format("%.0f", stats[3])
	    };
	}
	
	public static void printDataSet(Queue<Zadanie>dataSetorg) {
		int ile=0;
		Queue<Zadanie>pom=new LinkedList<>(dataSetorg);
		while(!pom.isEmpty()) {
			Zadanie druk=pom.poll();
			if(druk.realTime)++ile;
			System.out.println(druk);
		}
		System.out.println(ile);
	}
	
	public static void printSummary(List<String[]> allStats) {
	    String[] headers = {"Algorytm", "Śr. oczekiwanie", "Maks. oczekiwanie", "Zagłodzone", "Przesunięcia"};
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
