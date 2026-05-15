package algorytmy;

import java.util.TreeSet;

import proces.proces;

public class sjf_wyw implements algorytm {
	private TreeSet<proces> procesqueue = new TreeSet<>();
	private proces actualProces = null;
	private int stravationsProceses=0;
	private double averageWaitingTime=0;
	private int maxWaitingTime=0;
	private int switchNumber=0;
	private int finishedProceses=0;
	
	public void newProces(proces x) {
		if(this.actualProces!=null) {
			int wynik = this.actualProces.compareTo(x);
			if(wynik<0) {
				this.procesqueue.add(x);
			}
			else {
				this.procesqueue.add(this.actualProces);
				this.actualProces=x;
				++this.switchNumber;
			}
		}
		else {
			this.procesqueue.add(x);
		}
	}
	public void work() {
		if(this.actualProces==null) { //nowy proces
			if(!this.procesqueue.isEmpty()) {
				this.actualProces=this.procesqueue.pollFirst();
				++this.switchNumber;
			}	
		}
		
		if(this.actualProces!=null) {
			this.actualProces.doing();
			if(this.actualProces.isFinish()) { //proces skończony
				updateStats();
				this.actualProces=null;
			}
		}
		
		
		//dodajemy oczekiwanie reszcie
		updateWaiting();
		
	}
	
	private void updateStats() {
		this.finishedProceses++;
		if(this.actualProces.starvation)this.stravationsProceses++;
		this.averageWaitingTime+=this.actualProces.waitingTime;
		if(this.actualProces.waitingTime>this.maxWaitingTime)this.maxWaitingTime=this.actualProces.waitingTime;
	}
	
	private void updateWaiting() {
		for (proces p : this.procesqueue) {
		    p.waiting();
		}
	}
	
	public double[] getStatics() {
		double[] stats = {this.averageWaitingTime/this.finishedProceses, 
				this.maxWaitingTime, 
				this.stravationsProceses, 
				this.switchNumber};
		return stats;
	}
	
	public boolean isFinish() {
		if(this.procesqueue.isEmpty() && this.actualProces==null)return true;
		return false;
	}
	public String getName() {
		return "SJF z wywłaszczeniem";
	}
}
