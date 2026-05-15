package algorytmy;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import zadanie.Zadanie;


public class Sstf implements Algorytm {
	private String name = "SSTF";
	private ArrayList<Zadanie>procesqueue = new ArrayList<>();
	private Zadanie actualProces = null;
	private int stravationsProceses=0;
	private double averageWaitingTime=0;
	private int maxWaitingTime=0;
	private int moveNumber=0;
	private int finishedProceses=0;
	private int actualPosition;
	private int diskSize;
	
	public Sstf(int diskSize, int startPosition) {
		this.actualPosition=startPosition;
		this.diskSize=diskSize;
	}
	
	public void newProces(Zadanie x) {
		this.procesqueue.add(x);
	}
	public void work() {
		if(this.actualProces==null) { //nowy proces
			if(!this.procesqueue.isEmpty()) {
				this.new_proces();
			}	
		}
		
		if(this.actualProces!=null) {
			this.do_proces();
			if(this.actualProces.isFinish()) {
				this.updateStatics();
				this.actualProces=null;
			}
		}
		
		
		//dodajemy oczekiwanie reszcie
		for (Zadanie p : this.procesqueue) {
		    p.waiting();
		}
		if(this.actualProces!=null && !this.actualProces.done) {
			this.actualProces.waiting();
		}
		
	}
	
	private void new_proces() {
		int idNew=0;
		int distance=this.diskSize+2;;
		for(int i=0;i<this.procesqueue.size();++i) {
			if(Math.abs(this.actualPosition-this.procesqueue.get(i).position)<distance) {
				idNew=i;
				distance=Math.abs(this.actualPosition-this.procesqueue.get(i).position);
			}
		}
		this.actualProces=this.procesqueue.get(idNew);
		this.procesqueue.remove(idNew);
		
		
	}
	private void do_proces() {
		if(this.actualPosition==this.actualProces.position) {
			this.actualProces.doing();
		}
		else if(this.actualPosition<this.actualProces.position) {
			++this.actualPosition;
			++this.moveNumber;
		}
		else {
			--this.actualPosition;
			++this.moveNumber;
		}
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
