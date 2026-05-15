package algorytmy;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import zadanie.Zadanie;


public class FdScan implements Algorytm {
	private String name = "FD-SCAN";
	private ArrayList<Zadanie>procesqueue = new ArrayList<>();
	private ArrayList<Zadanie>realTimeProcesQueue = new ArrayList<>();
	private Zadanie actualProces = null;
	private int starvedProceses=0;
	private double averageWaitingTime=0;
	private int maxWaitingTime=0;
	private int moveNumber=0;
	private int finishedProceses=0;
	private int actualPosition;
	private int diskSize;
	
	public FdScan(int diskSize, int startPosition) {
		this.actualPosition=startPosition;
		this.diskSize=diskSize;
	}
	
	public void newProces(Zadanie x) {
		if(x.realTime) {
			this.realTimeProcesQueue.add(x);
		}
		else {
			this.procesqueue.add(x);
		}
	}
	
	public void work() {
		
		isActualStarved();
		
		if(this.actualProces==null) { //nowy proces
			this.new_proces();
		}
		
		if(this.actualProces!=null) {
			
			doProcesOn(this.actualPosition);
			
			this.do_proces();
			if(this.actualProces.isFinish()) {
				this.updateStatics();
				this.actualProces=null;
			}
		}
		
		
		giveWaitingTime();
		
		deleteStarved();
		
	}
	
	private boolean isPossible(Zadanie x) {
		if(Math.abs(x.position-this.actualPosition)<=(x.starvationTime-x.waitingTime)) {
			return true;
		}
		return false;
	}
	
	private void isActualStarved() {
		if(this.actualProces!=null && this.actualProces.realTime && this.actualProces.starvation) {
			this.starvedProceses++;
			this.actualProces=null;
		}
	}

	private void deleteStarved() {
		ArrayList<Zadanie>toDelate = new ArrayList<>();
		
		for(int i=0;i<this.realTimeProcesQueue.size();++i) {
			if(this.realTimeProcesQueue.get(i).starvation) {
				this.starvedProceses++;
				toDelate.add(this.realTimeProcesQueue.get(i));
			}
		}
		
		this.realTimeProcesQueue.removeAll(toDelate);
	}
	
	private void giveWaitingTime() {
		for (Zadanie p : this.procesqueue) {
		    p.waiting();
		}
		
		for (Zadanie p : this.realTimeProcesQueue){
		    p.waiting();
		}
		
		if(this.actualProces!=null && !this.actualProces.done) {
			this.actualProces.waiting();
		}
	}
	
	private void new_proces() {
		Zadanie newProces = newRealTime();
		if(newProces == null) {
			newProces = newNormal();
		}
		
		if(newProces == null) {
			this.actualProces=null;
		}
		else {
			this.actualProces = newProces;
		}
		
	}
	
	private Zadanie newRealTime() {
		Zadanie newProces;
		
		if(this.realTimeProcesQueue.isEmpty()) {
			return null;
		}
		
		int idNew=-1;
		int minimum=this.diskSize+2;
		
		for(int i=0;i<this.realTimeProcesQueue.size();++i) {
			if(this.realTimeProcesQueue.get(i).starvationTime<minimum && isPossible(this.realTimeProcesQueue.get(i))) {
				idNew=i;
				minimum=this.realTimeProcesQueue.get(i).starvationTime;
			}
		}
		
		if(idNew == -1) {
			return null;
		}
	
		newProces = this.realTimeProcesQueue.get(idNew);
		this.realTimeProcesQueue.remove(idNew);
		
		return newProces;
	}
	
	private Zadanie newNormal() {
		Zadanie newProces;
		
		if(this.procesqueue.isEmpty()) {
			return null;
		}
		
		int idNew=0;
		int distance=this.diskSize+2;;
		for(int i=0;i<this.procesqueue.size();++i) {
			if(Math.abs(this.actualPosition-this.procesqueue.get(i).position)<distance) {
				idNew=i;
				distance=Math.abs(this.actualPosition-this.procesqueue.get(i).position);
			}
		}
		newProces=this.procesqueue.get(idNew);
		this.procesqueue.remove(idNew);
		
		return newProces;
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
	
	private void doProcesOn(int x) {
		Zadanie pom;
		ArrayList<Zadanie>toRemove = new ArrayList<>();
		for(int i=0;i<this.procesqueue.size();++i) {
			if(this.procesqueue.get(i).position==x) {
				pom=this.procesqueue.get(i);
				pom.doing();

				this.finishedProceses++;
				this.averageWaitingTime+=pom.waitingTime;
				if(pom.waitingTime>this.maxWaitingTime)this.maxWaitingTime=pom.waitingTime;
				
				toRemove.add(pom);
			}
		}
		this.procesqueue.removeAll(toRemove);
		
		toRemove = new ArrayList<>();
		for(int i=0;i<this.realTimeProcesQueue.size();++i) {
			if(this.realTimeProcesQueue.get(i).position==x) {
				pom=this.realTimeProcesQueue.get(i);
				pom.doing();

				this.finishedProceses++;
				this.averageWaitingTime+=pom.waitingTime;
				if(pom.waitingTime>this.maxWaitingTime)this.maxWaitingTime=pom.waitingTime;
				
				toRemove.add(pom);
			}
		}
		this.realTimeProcesQueue.removeAll(toRemove);
	}
	
	private void updateStatics() {
		this.finishedProceses++;
		this.averageWaitingTime+=this.actualProces.waitingTime;
		if(this.actualProces.waitingTime>this.maxWaitingTime)this.maxWaitingTime=this.actualProces.waitingTime;
	}
	
	public double[] getStatics() {
		double[] stats = {this.averageWaitingTime/this.finishedProceses, 
				this.maxWaitingTime, 
				this.starvedProceses, 
				this.moveNumber};
		return stats;
	}
	
	public boolean isFinish() {
		if(this.procesqueue.isEmpty() && this.actualProces==null && this.realTimeProcesQueue.isEmpty())return true;
		return false;
	}
	public String getName() {
		return this.name;
	}
}
