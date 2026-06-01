package algorytmy;

import java.util.LinkedList;
import java.util.Queue;

import strona.Strona;

public class Alru implements Algorytm{
	private String name = "apr. LRU";
	public int e;
	public int w;
	private int memorySize;
	private Queue<Strona> memory;
	private int pageFaultCount;
	private int time;
	private int trashing;
	private boolean isThrashing;
	private Queue<Boolean> recentFaults;
	private int actualTrashing;
	
	public Alru(int memorySize, int w, int e) {
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
	
	private void addPage(Strona x) {
		Strona temp = this.memory.peek();
		while(temp.referenceBit!=0) {
			this.memory.poll();
			temp.referenceBit=0;
			this.memory.add(temp);

			temp = this.memory.peek();
		}
			
		this.memory.poll();
		this.memory.add(x);
		
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
		for(Strona temp : this.memory) {
			if(temp.equals(x)) {
				temp.referenceBit = 1;
		        return true;
		    }
		}
		return false;
		
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
