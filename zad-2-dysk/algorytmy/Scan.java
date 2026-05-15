package algorytmy;

import java.util.ArrayList;

import zadanie.Zadanie;

public class Scan implements Algorytm{
	private String name = "SCAN";
	private ArrayList<Zadanie>procesqueue = new ArrayList<>();
	private Zadanie actualProces = null;
	private int stravationsProceses=0;
	private double averageWaitingTime=0;
	private int maxWaitingTime=0;
	private int moveNumber=0;
	private int finishedProceses=0;
	private int actualPosition;
	private int diskSize;
	private boolean toEnd;
	
	public Scan(int diskSize, int startPosition) {
		this.actualPosition=startPosition;
		this.diskSize=diskSize;
		this.toEnd=true;
	}
	
	public void newProces(Zadanie x) {
		this.procesqueue.add(x);
	}
	
	public void work() {
		
		doProcesOn(this.actualPosition);
		
		if(this.actualPosition==this.diskSize) {
			this.toEnd=false;
			//doProcesOn(this.actualPosition);
		}
		if(this.actualPosition==1) {
			this.toEnd=true;
			//doProcesOn(this.actualPosition);
		}
		
		if(this.toEnd) {
			this.actualPosition++;
			this.moveNumber++;
		}
		else if(!this.toEnd) {
			this.actualPosition--;
			this.moveNumber++;
		}
		
		
		
		//dodajemy oczekiwanie reszcie
		
		for(int i=0;i<this.procesqueue.size();++i) {
			this.procesqueue.get(i).waiting();
			//System.out.println(i);
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
		if(this.actualProces.waitingTime>this.maxWaitingTime) {
			this.maxWaitingTime=this.actualProces.waitingTime;
			//System.out.println(this.actualProces.position);
			//System.out.println(this.actualProces.waitingTime);
		}
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
