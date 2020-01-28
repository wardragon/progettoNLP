package datamodel;

public class Pair {

	private Riga firstRiga;
	private Riga secondRiga;
	
	
	
	@Override
	public String toString() {
		return "Pair [firstRiga=" + (firstRiga==null ? "null" : firstRiga.getId()) + ", secondRiga=" + (secondRiga==null ? "null" : secondRiga.getId()) + "]";
	}
	public Pair(Riga firstRiga, Riga secondRiga) {
		super();
		this.firstRiga = firstRiga;
		this.secondRiga = secondRiga;
	}
	public Riga getFirstRiga() {
		return firstRiga;
	}
	public void setFirstRiga(Riga firstRiga) {
		this.firstRiga = firstRiga;
	}
	public Riga getSecondRiga() {
		return secondRiga;
	}
	public void setSecondRiga(Riga secondRiga) {
		this.secondRiga = secondRiga;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((firstRiga == null) ? 0 : firstRiga.hashCode());
		result = prime * result + ((secondRiga == null) ? 0 : secondRiga.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pair other = (Pair) obj;
		if (firstRiga == null) {
			if (other.firstRiga != null)
				return false;
		} else if (!firstRiga.equals(other.firstRiga))
			return false;
		if (secondRiga == null) {
			if (other.secondRiga != null)
				return false;
		} else if (!secondRiga.equals(other.secondRiga))
			return false;
		return true;
	}
	
	
	
	
}
