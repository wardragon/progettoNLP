package datamodel;
import java.util.List;

public class Riga {

	private Integer id;
	private String timeStamp;
	private String codice;
	private String data;
	private String messaggio;
	private List<String> messaggioWords;
	private List<String> messaggioTags;
	private String sentimentMessaggio;
	private String argomento;
	private List<String> argomentoWords;
	private List<String> argomentoTags;
	private String sentimentArgomento;
	private String chiarimenti;
	private List<String> chiarimentiWords;
	private List<String> chiarimentiTags;
	private String sentimentChiarimenti;
	private String annotatore;
	
	public String getJth(int j) {
		if(j==0) {
        	return messaggio;
        }else if(j==1) {
        	return argomento;
        }else {
        	return chiarimenti;
        }
	}
	
	public List<String> getJthWords(int j) {
		if(j==0) {
        	return messaggioWords;
        }else if(j==1) {
        	return argomentoWords;
        }else {
        	return chiarimentiWords;
        }
	}
	
	public List<String> getJthTags(int j) {
		if(j==0) {
        	return messaggioTags;
        }else if(j==1) {
        	return argomentoTags;
        }else {
        	return chiarimentiTags;
        }
	}
	
	public void setJthWords(int j, List<String> s) {
		if(j==0) {
        	messaggioWords=s;
        }else if(j==1) {
        	argomentoWords=s;
        }else {
        	chiarimentiWords=s;
        }
	}
	
	public void setJthTags(int j, List<String> s) {
		if(j==0) {
        	messaggioTags=s;
        }else if(j==1) {
        	argomentoTags=s;
        }else {
        	chiarimentiTags=s;
        }
	}
	
	public void setJthSentiment(int j, String s) {
		if(j==0) {
			sentimentMessaggio=s;
        }else if(j==1) {
        	sentimentArgomento=s;
        }else {
        	sentimentChiarimenti=s;
        }
	}
	
	public String getJthSentiment(int j) {
		if(j==0) {
			return sentimentMessaggio;
        }else if(j==1) {
        	return sentimentArgomento;
        }else {
        	return sentimentChiarimenti;
        }
	}
	

	public String getAnnotatore() {
		return annotatore;
	}

	public void setAnnotatore(String annotatore) {
		this.annotatore = annotatore;
	}

	public String getSentimentMessaggio() {
		return sentimentMessaggio;
	}

	public void setSentimentMessaggio(String sentimentMessaggio) {
		this.sentimentMessaggio = sentimentMessaggio;
	}

	public String getSentimentArgomento() {
		return sentimentArgomento;
	}

	public void setSentimentArgomento(String sentimentArgomento) {
		this.sentimentArgomento = sentimentArgomento;
	}

	public String getSentimentChiarimenti() {
		return sentimentChiarimenti;
	}

	public void setSentimentChiarimenti(String sentimentChiarimenti) {
		this.sentimentChiarimenti = sentimentChiarimenti;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<String> getMessaggioWords() {
		return messaggioWords;
	}

	public void setMessaggioWords(List<String> messaggioWords) {
		this.messaggioWords = messaggioWords;
	}

	public List<String> getMessaggioTags() {
		return messaggioTags;
	}

	public void setMessaggioTags(List<String> messaggioTags) {
		this.messaggioTags = messaggioTags;
	}

	public List<String> getArgomentoWords() {
		return argomentoWords;
	}

	public void setArgomentoWords(List<String> argomentoWords) {
		this.argomentoWords = argomentoWords;
	}

	public List<String> getArgomentoTags() {
		return argomentoTags;
	}

	public void setArgomentoTags(List<String> argomentoTags) {
		this.argomentoTags = argomentoTags;
	}

	public List<String> getChiarimentiWords() {
		return chiarimentiWords;
	}

	public void setChiarimentiWords(List<String> chiarimentiWords) {
		this.chiarimentiWords = chiarimentiWords;
	}

	public List<String> getChiarimentiTags() {
		return chiarimentiTags;
	}

	public void setChiarimentiTags(List<String> chiarimentiTags) {
		this.chiarimentiTags = chiarimentiTags;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getCodice() {
		return codice;
	}

	public void setCodice(String codice) {
		this.codice = codice;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getMessaggio() {
		return messaggio;
	}

	public void setMessaggio(String messaggio) {
		this.messaggio = messaggio;
	}

	public String getArgomento() {
		return argomento;
	}

	public void setArgomento(String argomento) {
		this.argomento = argomento;
	}

	public String getChiarimenti() {
		return chiarimenti;
	}

	public void setChiarimenti(String chiarimenti) {
		this.chiarimenti = chiarimenti;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Riga other = (Riga) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	
	
	
}
