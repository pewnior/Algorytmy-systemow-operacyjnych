package przydzial;

import java.util.ArrayList;

import proces.Proces;

public class Scbs implements Przydzial {
	private String name = "Ster. Częst. Błedów";
	private ArrayList<Proces>procesesList;
	private int framesSize;
	private int freeFrames;
	private int[] faultsInProces;
	private int time;
	private double l;
	private double u;
	private int dt;
	private double h;
	private int disacivateProceses;
	public int disactivated;
	
	public Scbs(int frames) {
		this.framesSize = frames;
		this.time=0;
		this.procesesList = new ArrayList<>();
		this.l=0.1;
		this.u=0.4;
		this.dt=25;
		this.h=0.7;
		this.disacivateProceses=0;
		this.disactivated=0;
	}
	
	@Override
	public void setFrames() {
		if(this.faultsInProces==null) {
			this.faultsInProces = new int[this.procesesList.size()];
			for(int i=0;i<this.faultsInProces.length;++i) {
				this.faultsInProces[i]=0;
			}
			initialSetFrames();
		}
	}
	
	private void initialSetFrames() {
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
		
		this.freeFrames=this.framesSize-temp;
		
	}
	
	@Override
	public void addProces(Proces newProces) {
		this.procesesList.add(newProces);
	}
		
	@Override
	public void work() {
		++this.time;
		for(int i=0;i<this.procesesList.size();++i) {
			if(this.procesesList.get(i).active) {
				this.procesesList.get(i).work();
				if(this.procesesList.get(i).ifLastFault())this.faultsInProces[i]++;
			}
		}
		
		if(this.time==this.dt) {
			for(int i=0;i<this.procesesList.size();++i) {
				if(this.procesesList.get(i).active) {
					double tempPpf = (double) this.faultsInProces[i] / this.dt;
				
					setFramesWithPpf(i,tempPpf);
				}
				
			}
			this.time=0;
			for(int i=0;i<this.faultsInProces.length;++i) {
				this.faultsInProces[i]=0;
			}
		}
		
		if(this.disacivateProceses>0) {
			reactiveProceses();
		}
		updateFreeFrames();
		
		
	}
	
	private void updateFreeFrames() {
		for(int i=0;i<this.procesesList.size();++i) {
			if(this.procesesList.get(i).ifFinish() && this.procesesList.get(i).active) {
				this.procesesList.get(i).active = false;
				this.freeFrames+=this.procesesList.get(i).framesSize-2;
				this.procesesList.get(i).setFrames(2);
				//System.out.println("Zabrano wszystko (do 2):" + i + " " + this.procesesList.get(i).ifFinish());
				
			}
			else if((!this.procesesList.get(i).active && !this.procesesList.get(i).ifFinish()) && this.procesesList.get(i).framesSize>2) {
				this.freeFrames+=this.procesesList.get(i).framesSize-2;
				this.procesesList.get(i).setFrames(2);
				//System.out.println("Zabrano wszystko (do 2):" + i + " " + this.procesesList.get(i).ifFinish());
			}
			
		}
	}
	private void reactiveProceses() {

	    ArrayList<Integer> toActive = new ArrayList<>();

	    for (int i = 0; i < this.procesesList.size(); ++i) {

	        if (!this.procesesList.get(i).ifFinish() && !this.procesesList.get(i).active && this.freeFrames >= 2) {

	            this.procesesList.get(i).addFrame();
	            this.procesesList.get(i).addFrame();

	            this.freeFrames -= 2;

	            if (!toActive.contains(i)) {
	                toActive.add(i);
	            }
	        }
	    }

	    for (int i : toActive) {
	        this.procesesList.get(i).active = true;
	        //System.out.println("Reaktywowano:" + i);
	    }

	    this.disacivateProceses -= toActive.size();
	}
	
	private void setFramesWithPpf(int x, double ppf) {
	    
		if(ppf >= this.h) {
	    	//System.out.println("Wygaszono:" + x + " ppf: " + ppf);
	        this.procesesList.get(x).active = false;
	        this.disacivateProceses++;
	        this.disactivated++;
	    }
	    else if(ppf > this.u) {
	        if(this.freeFrames > 0) {
	        	int temp=this.freeFrames/2;
	        	for(int i=0;i<temp;++i) {
	        		//System.out.println("Dodano:" + x + " ppf: " + ppf);
	            	this.procesesList.get(x).addFrame();
	            	this.freeFrames--;
	        	}
	        }
	        
	    }
	    else if(ppf < this.l) {
	        if(this.procesesList.get(x).framesSize > 2) {
	        	//System.out.println("Zabrano:" + x + " ppf: " + ppf);
	            this.procesesList.get(x).delateFrame();
	            this.freeFrames++;
	        }
	    }
	    
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

	@Override
	public boolean isFinish() {
		for(int i=0;i<this.procesesList.size();++i) {
			if(!this.procesesList.get(i).ifFinish())return false;
		}
		return true;
	}

	private void resetFrames() {
		for(int i=0;i<this.procesesList.size();++i) {
			this.procesesList.get(i).setFrames(0);
		}
	}
	
	public void setParameters(double l, double u, double h, int dt) {
		this.l=l;
		this.u=u;
		this.dt=dt;
		this.h=h;
	}

}
