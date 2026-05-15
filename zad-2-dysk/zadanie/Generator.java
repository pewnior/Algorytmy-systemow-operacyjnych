package zadanie;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import zadanie.Zadanie;

public class Generator {
	public static Queue<Zadanie> generateListRandom(int size, int diskSize){
		double p=0.3;
		Queue<Zadanie> lista = new LinkedList<Zadanie>();
		Random rand = new Random();
		int time=0;
		int starvationTime=(int)(size*diskSize);
		
		while(lista.size()!=size) {
			if (rand.nextDouble() < p) {
				int procesPosition;
				procesPosition = rand.nextInt(diskSize)+1;
				
				lista.offer(new Zadanie(time,procesPosition,starvationTime,false));
			}
			++time;
		}
		return lista;
	}
	public static Queue<Zadanie> generateListRandomOneMoment(int size, int diskSize){
		double p=0.3;
		Queue<Zadanie> lista = new LinkedList<Zadanie>();
		Random rand = new Random();
		int time=0;
		int starvationTime=(int)(size*diskSize);
		
		while(lista.size()!=size) {
			int procesPosition;
			procesPosition = rand.nextInt(diskSize)+1;
				
			lista.offer(new Zadanie(time,procesPosition,starvationTime,false));	
		}
		
		return lista;
	}
	
	public static Queue<Zadanie> generateListSkrany1(int size, int diskSize){
		double p=0.3;
		Queue<Zadanie> lista = new LinkedList<Zadanie>();
		Random rand = new Random();
		int time=0;
		int starvationTime=(int)(size*diskSize);
		
		while(lista.size()!=size) {
			if (rand.nextDouble() < p) {
				int procesPosition;
				if(lista.size()%2==0) {
					procesPosition = diskSize - rand.nextInt(10) - 1;
				}
				else {
					procesPosition = rand.nextInt(10)+2;
				}
				
				lista.offer(new Zadanie(time,procesPosition,starvationTime,false));
			}
			++time;
		}
		return lista;
	}
	
	public static Queue<Zadanie> generateListRealTime(int size, int diskSize){
		double p=0.4;
		double r=0.5;
		Queue<Zadanie> lista = new LinkedList<Zadanie>();
		Random rand = new Random();
		int time=0;
		
		
		while(lista.size()!=size) {
			if (rand.nextDouble() < p) {
				int procesPosition;
				procesPosition = rand.nextInt(diskSize)+1;
				int starvationTime;
				boolean isRealTime;
				if(rand.nextDouble() < r) {
					isRealTime=true;
					int starvationTimeRange = (int)(diskSize);
					starvationTime = rand.nextInt(starvationTimeRange)+(int)(diskSize*0.5);
				}
				else {
					isRealTime=false;
					starvationTime=(int)(size*diskSize);
				}
				
			
				
				lista.offer(new Zadanie(time,procesPosition,starvationTime,isRealTime));
			}
			++time;
		}
		return lista;
	}
	
	public static Queue<Zadanie> przykladWskazowki(){
		Queue<Zadanie> lista = new LinkedList<Zadanie>();
		lista.offer(new Zadanie(0,98,10000,false));
		lista.offer(new Zadanie(0,183,10000,false));
		lista.offer(new Zadanie(0,37,10000,false));
		lista.offer(new Zadanie(0,122,10000,false));
		lista.offer(new Zadanie(0,14,10000,false));
		lista.offer(new Zadanie(0,124,10000,false));
		lista.offer(new Zadanie(0,65,10000,false));
		lista.offer(new Zadanie(0,67,10000,false));
		
			
		return lista;
	}
}
