package algorytmy;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import strona.Strona;

public class Lru implements Algorytm{
	private String name = "LRU";
	public int e;
	public int w;
	private int memorySize;
	private ArrayList<Strona> memory;
	private int pageFaultCount;
	private int time;
	private int trashing;
	private boolean isThrashing;
	private Queue<Boolean> recentFaults;
	private int maxTrashing;
	private int actualTrashing;
	public ArrayList<Strona> workingSet;
	private int workingSetSize;
	private boolean activeWorkingSet;
	
	public Lru(int memorySize, int w, int e) {
		this.memorySize=memorySize;
		this.memory = new ArrayList<>();
		this.pageFaultCount=0;
		this.time=0;
		this.e = e;
		this.w = w;
		this.isThrashing = false;
		this.trashing = 0;
		this.maxTrashing=0;
		this.actualTrashing=0;
		this.recentFaults = new LinkedList<>();
		this.activeWorkingSet=false;
	}
	
	public String getName() {
		return this.name;
	}

	public int getFaults() {
		return this.pageFaultCount;
	}
	
	public void reference(Strona x) {
		++this.time;
		if(this.activeWorkingSet) {
			this.workingSet.add(x);
			if(this.workingSet.size()>this.workingSetSize)this.workingSet.remove(0);
		}
		
		
		if(isInMemory(x)) {
			recentFaults.add(false);
		}
		else {
			this.pageFaultCount++;
			this.recentFaults.add(true);
		
			if(this.memory.size() < this.memorySize) {
				this.memory.add(x);
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
	    for(Strona s : this.memory) {
	        s.waitingTime++;
	    }
	}
	
	private void addPage(Strona x) {
		int toRemoveId=0;
		int maxWaiting=0;
		
		for(int i=0;i<this.memorySize;++i) {
			Strona temp=this.memory.get(i);
			int tempWaitingTime=temp.waitingTime;
			
			if(tempWaitingTime>maxWaiting) {
				maxWaiting=tempWaitingTime;
				toRemoveId=i;
				
			}
		}
		
		this.memory.set(toRemoveId, x);
		
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
	    	this.maxTrashing = Math.max(this.actualTrashing, this.maxTrashing);
	        this.isThrashing = false;
	        this.actualTrashing=0;
	    }
	}
	
	private boolean isInMemory(Strona x) {
		if(this.memory.contains(x)) {
			int id = this.memory.indexOf(x);
			this.memory.get(id).waitingTime=0;
			return true;
		}
		return false;
	}
	
	public int[] getStatics() {
		this.maxTrashing = Math.max(this.actualTrashing, this.maxTrashing);
		int[] stats = {this.pageFaultCount, this.trashing, this.maxTrashing, this.memorySize};
		return stats;
	}
	
	@Override
	public void addFrame() {
		this.memorySize++;
	}

	@Override
	public void delateFrame() {
		this.memorySize--;
		if(this.memory.size()>this.memorySize)this.memory.remove(0);
		
	}
	
	public void activeWorkingSet(int size) {
		this.activeWorkingSet=true;
		this.workingSet=new ArrayList<>();
		this.workingSetSize=size;
	}
}
