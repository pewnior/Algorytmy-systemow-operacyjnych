package proces;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class generatorListy {
	public static Queue<proces> generateList(int size, int maxTime){
		double p=0.3;
		Queue<proces> lista = new LinkedList<proces>();
		Random rand = new Random();
		int time=0;
		int starvationTime=(int)(size*maxTime*0.3);
		
		while(lista.size()!=size) {
			if (rand.nextDouble() < p) {
				int procesSize;
				if (rand.nextDouble() < 0.7) {
				    procesSize = rand.nextInt(maxTime/2) + 1;    
				} else {
				    procesSize = rand.nextInt(maxTime/2) + maxTime/2+1;    
				}

				lista.offer(new proces(time,procesSize,starvationTime));
			}
			++time;
		}
		return lista;
	}
	
	public static Queue<proces> generateList_duze_male(int size, int maxTime){
		double p=0.3;
		Queue<proces> lista = new LinkedList<proces>();
		Random rand = new Random();
		int time=0;
		int starvationTime=(int)(size*maxTime*0.3);
		
		while(lista.size()!=size) {
			if (rand.nextDouble() < p) {
				int procesSize;
				if (lista.size()<size/2) {
				    procesSize = maxTime;      
				} else {
				    procesSize = 2;    
				}

				lista.offer(new proces(time,procesSize,starvationTime));
			}
			++time;
		}
		return lista;
	}
	
	public static Queue<proces> generateList_takie_same(int size, int maxTime){
		double p=0.3;
		Queue<proces> lista = new LinkedList<proces>();
		Random rand = new Random();
		int time=0;
		int starvationTime=(int)(size*maxTime*0.3);
		
		while(lista.size()!=size) {
			if (rand.nextDouble() < p) {
				int procesSize;
				procesSize=maxTime/2;
				lista.offer(new proces(time,procesSize,starvationTime));
			}
			++time;
		}
		return lista;
	}
}
