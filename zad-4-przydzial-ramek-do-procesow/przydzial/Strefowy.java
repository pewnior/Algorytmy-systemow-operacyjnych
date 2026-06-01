package przydzial;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import proces.Proces;
import strona.Strona;

public class Strefowy implements Przydzial{
	private String name = "Strefowy";
	private ArrayList<Proces>procesesList;
	private int framesSize;
	private int freeFrames;
	private int[] faultsInProces;
	private int time;
	private double l;
	private double u;
	private int dt;
	private int c;
	private double h;
	private ArrayList<Proces>disacivateProceses;
	private ArrayList<Proces>finishProceses;
	public int disactivated;
	
	public Strefowy(int frames) {
		this.framesSize = frames;
		this.time=0;
		this.procesesList = new ArrayList<>();
		this.disacivateProceses = new ArrayList<>();
		this.finishProceses = new ArrayList<>();
		this.l=0.1;
		this.u=0.4;
		this.dt=25;
		this.h=0.7;
		this.c = this.dt/2;
		this.disactivated=0;
		
	}

	private void initialSetFrames() {
		int n=0;
		for(int i=0;i<this.procesesList.size();++i) {
		    n+=this.procesesList.get(i).size;
		}
		int temp=0;
		for(int i=0;i<this.procesesList.size();++i) {
		    int f = (this.procesesList.get(i).size * this.framesSize) / n;
		    if (f == 0) f = 1; 
		    this.procesesList.get(i).setFrames(f);
		    temp+=f;
		}
		
	}
	
	@Override
	public void addProces(Proces newProces) {
		this.procesesList.add(newProces);
		newProces.activeWorkinSet(this.dt);
	}
		
	@Override
	public void work() {
		++this.time;
		
		ArrayList<Proces> temp = new ArrayList<>();
		for(int i=0;i<this.procesesList.size();++i) {
			Proces actual = this.procesesList.get(i);
			if(actual.ifFinish()) {
				temp.add(actual);
				this.freeFrames+=actual.framesSize;
				actual.setFrames(0);
				this.finishProceses.add(actual);
			}
			else {
				actual.work();
			}
		}
		this.procesesList.removeAll(temp);
		
		if(this.time == this.c) {
			this.time=0;
			
			collectWorkingSets();
			
		}
		
		
		
	}
	
	private void collectWorkingSets() {
		ArrayList<ArrayList<Strona>>workingSets = new ArrayList<>();
		for(int i=0;i<this.procesesList.size();++i) {
			if(this.procesesList.get(i).ifFinish())workingSets.add(null);
			else {
				workingSets.add(this.procesesList.get(i).getWorkingSet());
			}
		}
		
		countWSS(workingSets);
	}
	
	private void countWSS(ArrayList<ArrayList<Strona>>workingSets) {
		ArrayList<Integer>WSS = new ArrayList<>();
		for(int i=0;i<workingSets.size();++i) {
			if(workingSets.get(i)==null)WSS.add(1);
			else WSS.add(countPages(workingSets.get(i)));
		}
		
		countD(workingSets,WSS);
	}
	
	private void countD(ArrayList<ArrayList<Strona>>workingSets, ArrayList<Integer>WSS) {
		int D=0;
		for(int i=0;i<WSS.size();++i) {
			D+=WSS.get(i);
		}
		
		if(D<=this.framesSize) {
			alocateFrames(WSS);
			this.freeFrames=this.framesSize-D;
			reactivate();
		}
		else {
			deactivateProceses(D, WSS);
		}
	}
	
	private void alocateFrames(ArrayList<Integer>WSS) {
		for(int i=0;i<this.procesesList.size();++i) {
			this.procesesList.get(i).setFrames(WSS.get(i));
		}
	}
	
	private void deactivateProceses(int D, ArrayList<Integer>WSS) {
		while(D>this.framesSize) {
			Proces temp = this.procesesList.get(0);
			this.disacivateProceses.add(temp);
			D-=WSS.get(0);
			this.disactivated++;
			WSS.remove(0);
			this.procesesList.remove(0);
		}
		alocateFrames(WSS);
		this.freeFrames=this.framesSize-D;
		reactivate();
	}
	
	private int countPages(ArrayList<Strona>set) {
		int result=0;
		Set<Integer> liczby = new HashSet<>();
		for(int i=0;i<set.size();++i) {
			liczby.add(set.get(i).id);
		}
		result = liczby.size();
		return result;
	}
	
	private void reactivate() {
		ArrayList<Proces>temp = new ArrayList<>();
		for(int i=0;i<this.disacivateProceses.size();++i) {
			Proces actual = this.disacivateProceses.get(i);
			if(countPages(actual.getWorkingSet())<=this.freeFrames) {
				temp.add(actual);
				actual.setFrames(countPages(actual.getWorkingSet()));
				this.freeFrames-=countPages(actual.getWorkingSet());
				this.procesesList.add(actual);
			}
		}
		this.disacivateProceses.removeAll(temp);
	}
	@Override
	public int[] getStatics() {
		this.finishProceses.sort(Comparator.comparingInt(p -> p.id));
		int n = this.finishProceses.size();
		 int totalFault = 0;
		 int totalTrashing = 0;

		 int[] result = new int[2 + 2 * n];

		 for (int i = 0; i < n; i++) {

			 int[] stats = this.finishProceses.get(i).getStatics();

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
		if(this.procesesList.size()==0 && this.disacivateProceses.size()==0)return true;
		return false;
		
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
		this.c=dt/2;
	}

	public void setFrames() {
		
			this.faultsInProces = new int[this.procesesList.size()];
			for(int i=0;i<this.faultsInProces.length;++i) {
				this.faultsInProces[i]=0;
			}
			initialSetFrames();
		
	}

}
