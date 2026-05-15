package algorytmy;

import java.util.ArrayList;

import zadanie.Zadanie;

public class CScan implements Algorytm{
	private String name = "C-SCAN";
	private ArrayList<Zadanie>procesqueue = new ArrayList<>();
	private Zadanie actualProces = null;
	private int stravationsProceses=0;
	private double averageWaitingTime=0;
	private int maxWaitingTime=0;
	private int moveNumber=0;
	private int finishedProceses=0;
	private int actualPosition;
	private int diskSize;
	
	
	public CScan(int diskSize, int startPosition) {
		this.actualPosition=startPosition;
		this.diskSize=diskSize;
	}
	
	public void newProces(Zadanie x) {
		this.procesqueue.add(x);
	}
	
	public void work() {
		
		doProcesOn(this.actualPosition);
		
		if(this.actualPosition==this.diskSize) {
			this.actualPosition=1;
			this.moveNumber++;
		}
		else{
			this.actualPosition++;
			this.moveNumber++;
		}
		
		
		//dodajemy oczekiwanie reszcie
		for (Zadanie p : this.procesqueue) {
		    p.waiting();
		}
		
	}
	
	private void doProcesOn(int x) {
		ArrayList<Zadanie>toRemove = new ArrayList<>();
		for(int i=0;i<this.procesqueue.size();++i) {
			if(this.procesqueue.get(i).position==x) {
				this.actualProces=this.procesqueue.get(i);
				toRemove.add(this.procesqueue.get(i));
				this.actualProces.doing();
				this.updateStatics();
				this.actualProces=null;
			}
		}
		this.procesqueue.removeAll(toRemove);
	}

	
	private void updateStatics() {
		this.finishedProceses++;
		if(this.actualProces.starvation)this.stravationsProceses++;
		this.averageWaitingTime+=this.actualProces.waitingTime;
		if(this.actualProces.waitingTime>this.maxWaitingTime)this.maxWaitingTime=this.actualProces.waitingTime;
	}
	
	public double[] getStatics() {
		double[] stats = {this.averageWaitingTime/this.finishedProceses, 
				this.maxWaitingTime, 
				this.stravationsProceses, 
				this.moveNumber};
		return stats;
	}
	
	public boolean isFinish() {
		if(this.procesqueue.isEmpty() && this.actualProces==null)return true;
		return false;
	}
	public String getName() {
		return this.name;
	}
}
