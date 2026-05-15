package algorytmy;

import java.util.LinkedList;
import java.util.Queue;

import proces.*;

public class fcfs implements algorytm{
	private Queue<proces>procesqueue = new LinkedList<>();
	private proces actualProces = null;
	private int stravationsProceses=0;
	private double averageWaitingTime=0;
	private int maxWaitingTime=0;
	private int switchNumber=0;
	private int finishedProceses=0;
	
	public void newProces(proces x) {
		this.procesqueue.offer(x);
	}
	public void work() {
		if(this.actualProces==null) {
			if(!this.procesqueue.isEmpty()) {
				this.actualProces=this.procesqueue.poll();
				++this.switchNumber;
			}	
		}
		
		if(this.actualProces!=null) {
			this.actualProces.doing();
			if(this.actualProces.isFinish()) {
				updateStats();
				this.actualProces=null;
			}
		}
		
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
		return "FCFS";
	}
}
