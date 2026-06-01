package proces;

import java.util.ArrayList;

import algorytmy.Algorytm;
import algorytmy.Lru;
import strona.Strona;

public class Proces {
	public int id;
	public int size;
	public ArrayList<Strona> referencesString;
	private Lru memory;
	private int startPageId;
	private int endPageId;
	public int framesSize;
	private int w;
	private int e;
	private int faults;
	private int lastWorkResault;
	public boolean active;
	
	public Proces(int id, int size, int startPageId, int endPageId, int frames, int w, int e) {
		this.id=id;
		this.active = true;
		this.size=size;
		this.startPageId=startPageId;
		this.endPageId=endPageId;
		this.framesSize=frames;
		this.lastWorkResault=0;
		this.w=w;
		this.e=e;
		this.memory=new algorytmy.Lru(framesSize,w,e);
		this.referencesString = Generator.generateProces(this.size, this.startPageId, this.endPageId);
		this.faults=0;
	}
	
	public Proces(int id, int size, int startPageId, int endPageId, int frames, Lru memory, ArrayList<Strona> referencesString) {
		this.id=id;
		this.active = true;
		this.size=size;
		this.startPageId=startPageId;
		this.endPageId=endPageId;
		this.framesSize=frames;
		this.memory=memory;
		this.referencesString = referencesString;
		this.faults=0;
		this.lastWorkResault=0;
	}
	
	public void setFrames(int size) {
		while(this.framesSize!=size) {
			if(this.framesSize>size) {
				delateFrame();
			}
			else {
				addFrame();
			}
		}
		
	}
	
	public int getFaults() {
		return this.faults;
	}
	
	public void addFrame() {
		this.memory.addFrame();
		++this.framesSize;
	}
	
	public void delateFrame() {
		this.memory.delateFrame();
		--this.framesSize;
	}
	
	public void work() {
		if(this.referencesString.size()!=0) {
			this.memory.reference(this.referencesString.get(0));
			this.referencesString.remove(0);
			int temp = this.memory.getFaults();
			if(temp>this.faults)this.lastWorkResault=1;
			else this.lastWorkResault=0;
			this.faults=temp;
		}
		
	}
	
	public boolean ifLastFault() {
		if(this.lastWorkResault==1)return true;
		return false;
	}
	
	public boolean ifFinish() {
		if(this.referencesString.size()==0) {
			//this.active=false;
			return true;
		}
		return false;
	}
	
	public int[] getStatics() {
		return this.memory.getStatics();
	}
	public String toString() {
		String odp="";
		for(int i=0;i<this.referencesString.size();++i) {
			odp+=this.referencesString.get(i) + " ";
		}
		return odp;
	}
	public Proces copy() {

	    ArrayList<Strona> dataSet = new ArrayList<>();

	    for (Strona p : this.referencesString) {
	        dataSet.add(p.copy());
	    }

	    Lru newMemory = new algorytmy.Lru(this.framesSize, this.w, this.e);

	    return new Proces(
	        this.id,
	        this.size,
	        this.startPageId,
	        this.endPageId,
	        this.framesSize,
	        newMemory,
	        dataSet
	    );
	}
	
	public void activeWorkinSet(int size) {
		this.memory.activeWorkingSet(size);
	}
	public ArrayList<Strona> getWorkingSet(){
		return this.memory.workingSet;
	}
	
}
