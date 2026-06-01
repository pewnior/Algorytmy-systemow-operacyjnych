package przydzial;

import java.util.ArrayList;

import proces.Proces;

public class Proporcjonalny implements Przydzial {
	private String name = "Proporcjonalny";
	private ArrayList<Proces>procesesList;
	private int framesSize;
	
	public Proporcjonalny(int frames) {
		this.framesSize = frames;
		this.procesesList = new ArrayList<>();
	}
	
	@Override
	public void setFrames() {
		if(this.procesesList.isEmpty())return;
		
		int n=0;
		for(int i=0;i<this.procesesList.size();++i) {
			n+=this.procesesList.get(i).size;
		}
		
		resetFrames();
		
		int temp=0;
		for(int i=0;i<this.procesesList.size();++i) {
			int f = (this.procesesList.get(i).size * this.framesSize) / n;
			this.procesesList.get(i).setFrames(f);
			temp+=f;
		}
		
		int freeFrames=this.framesSize-temp;
		temp=0;
		
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
	
	@Override
	public void addProces(Proces newProces) {
		this.procesesList.add(newProces);
	}

	@Override
	public void work() {
		for(int i=0;i<this.procesesList.size();++i) {
			this.procesesList.get(i).work();
		}
	}
	
	@Override
	public boolean isFinish() {
		for(int i=0;i<this.procesesList.size();++i) {
			if(!this.procesesList.get(i).ifFinish())return false;
		}
		return true;
	}
	@Override
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

	@Override
	public String getName() {
		return this.name;
	}

	

	

}
