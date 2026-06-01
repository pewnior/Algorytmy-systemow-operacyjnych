package strona;

public class Strona {
	public int id;
	public int waitingTime;
	public int referenceBit;
	
	public Strona (int id){
		this.id=id;
		this.waitingTime=0;
		this.referenceBit=1;
	}
	
	public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Strona page = (Strona) o;

        return this.id==page.id;
    }
	public int compareTo(Strona other) {
	    return Integer.compare(this.id, other.id);
	}
	public Strona copy() {
	    return new Strona(this.id);
	}
	public String toString() {
		return this.id + " ";
	}
}
