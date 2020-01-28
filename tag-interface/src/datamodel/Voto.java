package datamodel;

public class Voto {
	
	private int firstId;
	private int secondId;
	private String column;
	private String firstPhrase;
	private String secondPhrase;
	private int voto;
	private String annotatore;
	private String id;
	
	
	public String getAnnotatore() {
		return annotatore;
	}
	public void setAnnotatore(String annotatore) {
		this.annotatore = annotatore;
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
	public String getFirstPhrase() {
		return firstPhrase;
	}
	public void setFirstPhrase(String firstPhrase) {
		this.firstPhrase = firstPhrase;
	}
	public String getSecondPhrase() {
		return secondPhrase;
	}
	public void setSecondPhrase(String secondPhrase) {
		this.secondPhrase = secondPhrase;
	}
	public int getVoto() {
		return voto;
	}
	public void setVoto(int voto) {
		this.voto = voto;
	}
	public String getId() {return id;}
	public void setId(String id) {this.id = id;}
	
}
