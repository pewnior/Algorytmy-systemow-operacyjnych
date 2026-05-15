package zadanie;

public class Zadanie implements Comparable<Zadanie>{
	public int startTime;
	public int position;
	public int waitingTime;
	public boolean done;
	public boolean starvation;
	public int starvationTime;
	public boolean realTime;
	
	public Zadanie(int start, int position, int starvationTime, boolean realTime) {
		this.startTime=start;
		this.position=position;
		this.waitingTime=0;
		this.done=false;
		this.starvation=false;
		this.starvationTime=starvationTime;
		this.realTime = realTime;
	}
	
	public void doing() {
		this.done=true;
	}
	public boolean isFinish() {
		return this.done;
	}
	public void waiting() {
		this.waitingTime++;
		if(this.waitingTime==this.starvationTime)this.starvation=true;
	}
	public String toString() {
		return startTime + " " + position + " " + realTime + " " + starvationTime;
	}
	public int compareTo(Zadanie other) {
	    return Integer.compare(this.position, other.position);
	}
	public Zadanie copy() {
	    return new Zadanie(this.startTime, this.position, this.starvationTime, this.realTime);
	}
}
