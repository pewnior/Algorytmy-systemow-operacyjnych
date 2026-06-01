package algorytmy;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import strona.Strona;

public class Lru implements Algorytm{
	private String name = "LRU";
	private int memorySize;
	public int e;
	public int w;
	private Strona[] memory;
	private int pageFaultCount;
	private int time;
	private int trashing;
	private boolean isThrashing;
	private Queue<Boolean> recentFaults;
	private int actualTrashing;
	
	public Lru(int memorySize, int w, int e) {
		this.memorySize=memorySize;
		this.memory = new Strona[this.memorySize];
		this.pageFaultCount=0;
		this.time=0;
		this.e = e;
		this.w = w;
		this.isThrashing = false;
		this.trashing = 0;
		this.actualTrashing=0;
		this.recentFaults = new LinkedList<>();
		
		for(int i=0;i<this.memorySize;++i) {
			this.memory[i]=null;
		}
		
		
	}
	
	public String getName() {
		return this.name;
	}

	
	public void reference(Strona x) {
		++this.time;
		
		if(isInMemory(x)) {
			recentFaults.add(false);
		}
		
		else{
			this.pageFaultCount++;
			this.recentFaults.add(true);
		
			int emptyId = emptyMemory();
			if(emptyId != -1) {
				this.memory[emptyId]=x;
			}
			else {
				addPage(x);
			}
		}
		
		giveWaitingTime();
		recentFaultsSizeCheck();
		
		if(this.time % (this.w / 2) == 0) {
		    trashingCheck();
		}
	}
	
	private void giveWaitingTime() {
		for(int i=0;i<this.memorySize;++i) {
			if(this.memory[i] != null) {
			    this.memory[i].waitingTime++;
			}
		}
	}
	
	private void addPage(Strona x) {
		int toRemoveId=0;
		int maxWaiting=0;
		
		for(int i=0;i<this.memorySize;++i) {
			Strona temp=this.memory[i];
			int tempWaitingTime=temp.waitingTime;
			
			if(tempWaitingTime>maxWaiting) {
				maxWaiting=tempWaitingTime;
				toRemoveId=i;
				
			}
		}
		
		this.memory[toRemoveId]=x;
		
	}
	
	private void recentFaultsSizeCheck() {
		if(this.recentFaults.size() > this.w) {
		    this.recentFaults.poll();
		}
	}
	
	private void trashingCheck() {
	
		if(this.recentFaults.size() < this.w)
		    return;

	    int faults = 0;

	    for(Boolean b : this.recentFaults) {
	        if(b)faults++;
	    }

	    if(faults > this.e){
	        
	            this.trashing++;
	            this.actualTrashing++;
	            this.isThrashing = true;
	        
	    } 
	    else{
	    
	        this.isThrashing = false;
	        this.actualTrashing=0;
	    }
	}
	
	private boolean isInMemory(Strona x) {
		for(int i=0;i<this.memorySize;++i) {
			if(this.memory[i]!=null) {
				if(this.memory[i].equals(x)) {
					this.memory[i].waitingTime = 0;
					return true;
				}
			}		
		}
		return false;
	}
	
	private int emptyMemory() {
		for(int i=0;i<this.memorySize;++i) {
			if(this.memory[i]==null)return i;
		}
		return -1;
	}

	
	public int[] getStatics() {

		int[] stats = {this.pageFaultCount, this.trashing};
		return stats;
	}

}
