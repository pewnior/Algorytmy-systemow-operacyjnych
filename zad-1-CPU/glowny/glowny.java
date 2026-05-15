package glowny;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import proces.*;
import algorytmy.*;


public class glowny {

	public static void main(String[] args) {
		int setSize=1000; //ilosc procesow
		int procesMaxTime=50; //maksymalny czas procesu
		int q=procesMaxTime/2; //kwant czasu dla rr
		
		//zestaw 1 losowy
		System.out.println("Zestaw 1 - losowy");
		Queue<proces>dataSet=generatorListy.generateList(setSize, procesMaxTime);
		List<String[]> allStats = new ArrayList<>();
		startalgorytm(dataSet,1,0,allStats);
		startalgorytm(dataSet,2,0,allStats);
		startalgorytm(dataSet,3,0,allStats);
		startalgorytm(dataSet,4,q,allStats);
		
		printSummary(allStats);
		
		//zestaw 2 najpierw duże, potem małe
		System.out.println("Zestaw 2 - duże, małe");
		dataSet=generatorListy.generateList_duze_male(setSize, procesMaxTime);
		allStats = new ArrayList<>();
		startalgorytm(dataSet,1,0,allStats);
		startalgorytm(dataSet,2,0,allStats);
		startalgorytm(dataSet,3,0,allStats);
		startalgorytm(dataSet,4,q,allStats);
				
		printSummary(allStats);
		
		//zestaw 3 takie same
		System.out.println("Zestaw 3 - takie same");
		dataSet=generatorListy.generateList_takie_same(setSize, procesMaxTime);
		allStats = new ArrayList<>();
		startalgorytm(dataSet,1,0,allStats);
		startalgorytm(dataSet,2,0,allStats);
		startalgorytm(dataSet,3,0,allStats);
		startalgorytm(dataSet,4,q,allStats);
						
		printSummary(allStats);
		
	}
	
	public static void startalgorytm(Queue<proces>dataSetorg, int numer, int q, List<String[]> allStats) {
		Queue<proces> dataSet = new LinkedList<>();
		for (proces p : dataSetorg) {
	        dataSet.offer(p.copy());
	    }
		
		algorytm procesor;
		
		switch(numer) {
			case 1:
				procesor=new fcfs();
				break;
			case 2:
				procesor=new sjf();
				break;
			case 3:
				procesor=new sjf_wyw();
				break;
			case 4:
				procesor=new rr();
				if(q>0) {
					((rr) procesor).setQ(q);
				}
				break;
			default:
				 throw new IllegalArgumentException("Zły numer algorytmu");				
		}
		int time=0;
		int setSize=dataSet.size();
		int procesesSent=0;
		proces pom;
		
		while(procesesSent!=setSize) {
			while(!dataSet.isEmpty() && dataSet.peek().startTime==time) {
				pom=dataSet.poll();
				procesor.newProces(pom);
				++procesesSent;
			}
			
			procesor.work();
			++time;
		}
		while(!procesor.isFinish()) {
			procesor.work();
			++time;
		}
		
		//statystyki
		double[] stats = procesor.getStatics();
		allStats.add(collectStats(procesor.getName(),stats,time));
		
	}

	public static void printStats(String algo, double[] stats, int time) {
		System.out.print("\n");
		System.out.println(algo);
		System.out.println("Czas pracy: " + time);
		System.out.println("Średni czas oczekiwania: " + stats[0]);
		System.out.println("Maksymalny czas oczekiwania: " + stats[1]);
		System.out.println("Zagłodzone procesy: " + stats[2]);
		System.out.println("Liczba przełączeń: " + stats[3]);
	}
	
	public static String[] collectStats(String algo, double[] stats, int time) {
	    return new String[]{
	        algo,
	        String.valueOf(time),
	        String.format("%.2f", stats[0]),
	        String.format("%.0f", stats[1]),
	        String.format("%.0f", stats[2]),
	        String.format("%.0f", stats[3])
	    };
	}
	
	public static void printDataSet(Queue<proces>dataSetorg) {
		Queue<proces>pom=new LinkedList<>(dataSetorg);
		while(!pom.isEmpty()) {
			System.out.println(pom.poll());
		}
	}
	
	public static void printSummary(List<String[]> allStats) {
	    String[] headers = {"Algorytm", "Czas pracy", "Śr. oczekiwanie", "Maks. oczekiwanie", "Zagłodzone", "Przełączenia"};
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

