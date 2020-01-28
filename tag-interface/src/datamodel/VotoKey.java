package datamodel;

public class VotoKey {
	
	private int firstId;
	private int secondId;
	private String column;
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((column == null) ? 0 : column.hashCode());
		result = prime * result + firstId;
		result = prime * result + secondId;
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
		VotoKey other = (VotoKey) obj;
		if (column == null) {
			if (other.column != null)
				return false;
		} else if (!column.equals(other.column))
			return false;
		if (firstId != other.firstId)
			return false;
		if (secondId != other.secondId)
			return false;
		return true;
	}
	public int getFirstId() {
		return firstId;
	}
	public void setFirstId(int firstId) {
		this.firstId = firstId;
	}
	public int getSecondId() {
		return secondId;
	}
	public void setSecondId(int secondId) {
		this.secondId = secondId;
	}
	public String getColumn() {
		return column;
	}
	public void setColumn(String column) {
		this.column = column;
	}
	
	

}
