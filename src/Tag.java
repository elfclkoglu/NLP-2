
public class Tag {
	private String previousTag;
	private String tag;
	private double count;
	private double transitionPro;
	private double preProbability;
	public Tag(String tag,double count){
		this.tag=tag;
		this.count=count;
	}
	public Tag(String previousTag, String tag,double count){
		this.previousTag=previousTag;
		this.tag=tag;
		this.count=count;
	}
	public Tag(String previousTag,double preProbability,String tag,double transitionPro,double count ){
		this.previousTag=previousTag;
		this.preProbability=preProbability;
		this.tag=tag;
		this.transitionPro=transitionPro;
		this.count=count;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public double getProbability() {
		return count;
	}
	public double getTransitionPro(){
		return transitionPro;
	}
	public void setProbability(double probability) {
		this.count = probability;
	}
	public String getPreviousTag() {
		return previousTag;
	}
	public void setPreviousTag(String previousTag) {
		this.previousTag = previousTag;
	}
	public double getPreProbability() {
		return preProbability;
	}
	public void setPreProbability(double preProbability) {
		this.preProbability = preProbability;
	}

}