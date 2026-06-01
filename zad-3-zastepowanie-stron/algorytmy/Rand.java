package algorytmy;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import strona.Strona;

public class Rand implements Algorytm{
	private String name = "RAND";
	public int e;
	public int w;
	private int memorySize;
	private Strona[] memory;
	private int pageFaultCount;
	private int time;
	private int trashing;
	private boolean isThrashing;
	private int actualTrashing;
	private Queue<Boolean> recentFaults;
	
	public Rand(int memorySize, int w, int e) {
		this.memorySize=memorySize;
		this.memory = new Strona[this.memorySize];
		this.pageFaultCount=0;
		this.time=0;
		this.e = e;
		this.w = w;
		this.isThrashing = false;
		this.trashing = 0;
		this.trashing = 0;
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
		else {
			this.pageFaultCount++;
			recentFaults.add(true);
		
			int emptyId = emptyMemory();
			if(emptyId != -1) {
				this.memory[emptyId]=x;
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
		Random rand = new Random();
		int toRemoveId = rand.nextInt(this.memorySize);
		
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
				if(this.memory[i].equals(x))return true;
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
		
		int[] stats = {this.pageFaultCount,this.trashing};
		return stats;
	}

}
