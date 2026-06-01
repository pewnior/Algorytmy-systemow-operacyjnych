package przydzial;

import java.util.ArrayList;

import proces.Proces;

public class Rowny implements Przydzial {
	private String name = "Równy";
	private ArrayList<Proces>procesesList;
	
	private int framesSize;
	
	public Rowny(int frames) {
		
		this.framesSize = frames;
		 this.procesesList = new ArrayList<>();
	}
	
	public void addProces(Proces newProces) {
		this.procesesList.add(newProces);
	}
	
	public void setFrames() {
		if(this.procesesList.isEmpty()) {
	        return;
	    }
		
		resetFrames();
		
		int framesPerProces = this.framesSize / this.procesesList.size();
		int freeFrames = this.framesSize % this.procesesList.size();
		
		for(int i=0;i<this.procesesList.size();++i) {
			this.procesesList.get(i).setFrames(framesPerProces);
		}
		
		int temp=0;
		while(freeFrames!=0) {
			this.procesesList.get(temp).addFrame();
			temp = (temp + 1) % this.procesesList.size();
			--freeFrames;
		}
		
	}
	
	private void resetFrames() {
		for(int i=0;i<this.procesesList.size();++i) {
			this.procesesList.get(i).setFrames(0);
		}
	}
	
	public void work() {
		for(int i=0;i<this.procesesList.size();++i) {
			this.procesesList.get(i).work();
		}
	}	
	
	public boolean isFinish() {
		for(int i=0;i<this.procesesList.size();++i) {
			if(!this.procesesList.get(i).ifFinish())return false;
		}
		return true;
	}
	
	
	public int[] getStatics() {
		 int n = this.procesesList.size();
		 int totalFault = 0;
		 int totalTrashing = 0;

		 int[] result = new int[2 + 2 * n];

		 for (int i = 0; i < n; i++) {

			 int[] stats = this.procesesList.get(i).getStatics();

		     int fault = stats[0];
		     int frames = stats[3];

		     totalFault += fault;
		     totalTrashing += stats[1];

		     result[2 + i * 2] = fault;      
		     result[2 + i * 2 + 1] = frames;
		 }

		 result[0] = totalFault;
		 result[1] = totalTrashing;

		 return result;
	}
	
	public String getName() {
		return this.name;
	}

}
