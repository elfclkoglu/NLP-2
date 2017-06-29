
public class WordWithTag {
	private String word;
	private String tag;
	private String headofSentence;
	private double probability;
	public WordWithTag(String word, String tag,String headofSentence){
		this.word=word;
		this.tag=tag;
		this.setHeadofSentence(headofSentence);
	}
	public WordWithTag(String word, String tag){
		this.word=word;
		this.tag=tag;
	}
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String getHeadofSentence() {
		return headofSentence;
	}
	public void setHeadofSentence(String headofSentence) {
		this.headofSentence = headofSentence;
	}
	public double getProbability() {
		return probability;
	}
	public void setProbability(double probability) {
		this.probability = probability;
	}
}
