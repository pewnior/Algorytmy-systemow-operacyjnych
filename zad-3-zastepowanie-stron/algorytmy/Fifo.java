package algorytmy;

import java.util.LinkedList;
import java.util.Queue;

import strona.Strona;

public class Fifo implements Algorytm{
	private String name = "FIFO";
	public int e;
	public int w;
	private int memorySize;
	private Queue<Strona> memory;
	private int time;
	private int pageFaultCount;
	private int trashing;
	private boolean isThrashing;
	private Queue<Boolean> recentFaults;
	private int actualTrashing;
	
	public Fifo(int memorySize, int w, int e) {
		this.memorySize=memorySize;
		this.memory = new LinkedList<>();
		this.pageFaultCount=0;
		this.time=0;
		this.e = e;
		this.w = w;
		this.isThrashing = false;
		this.trashing = 0;
		this.actualTrashing=0;
		this.recentFaults = new LinkedList<>();
	}
	
	public String getName() {
		return this.name;
	}

	
	public void reference(Strona x) {
		++this.time;
		
		if(isInMemory(x)) {
			recentFaults.add(false);
		}
		else {
			this.pageFaultCount++;
			this.recentFaults.add(true);
		
			if(emptyMemory()) {
				this.memory.add(x);
			}
			else {
				addPage(x);
			}
		}
		
		recentFaultsSizeCheck();
		
		if(this.time % (this.w / 2) == 0) {
		    trashingCheck();
		}
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
	
	private void addPage(Strona x) {
		this.memory.poll();
		this.memory.add(x);
	}
	
	private boolean isInMemory(Strona x) {
		return this.memory.contains(x);
	}
	
	private boolean emptyMemory() {
		if(this.memory.size()<this.memorySize)return true;
		return false;
	}

	
	public int[] getStatics() {
		
		int[] stats = {this.pageFaultCount, this.trashing};
		return stats;
	}

}
