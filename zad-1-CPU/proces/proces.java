package proces;

public class proces implements Comparable<proces>{
	public int startTime;
	public int size;
	public int waitingTime;
	public int doneTime;
	public boolean starvation;
	public int starvationTime;
	
	public proces(int start,int size, int starvationTime) {
		this.startTime=start;
		this.size=size;
		this.waitingTime=0;
		this.doneTime=0;
		this.starvation=false;
		this.starvationTime=starvationTime;
	}
	
	public void doing() {
		this.doneTime++;
	}
	public boolean isFinish() {
		if(this.size==this.doneTime)return true;
		return false;
	}
	public void waiting() {
		this.waitingTime++;
		if(this.waitingTime==this.starvationTime)this.starvation=true;
	}
	public String toString() {
		return startTime + " " + size;
	}
	public int compareTo(proces other) {
	    int thisPriority = this.size - this.doneTime;
	    int otherPriority = other.size - other.doneTime;
	    int cmp = Integer.compare(thisPriority, otherPriority);
	    if (cmp != 0) return cmp;
	    return Integer.compare(this.startTime, other.startTime);
	}
	public proces copy() {
	    return new proces(this.startTime, this.size,this.starvationTime);
	}
	
}
