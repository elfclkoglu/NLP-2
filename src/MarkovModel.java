import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;



public class MarkovModel {
	HashMap<Integer,WordWithTag> allofWords= new HashMap<Integer,WordWithTag>();
	HashMap<Integer,WordWithTag> headsofSentence = new HashMap<Integer,WordWithTag>();
	HashSet<String> tagsofWord;
	HashSet<Tag> initialTagPro = new HashSet<Tag>();
	HashSet<String> uniqueTags =new HashSet<String>();
	private HashSet<String> uniqueWords =new HashSet<String>();
	HashSet<String> tagController = new HashSet<String>();
	private HashMap<String,HashMap<String,Double>> tagWithNextTags = new HashMap<String,HashMap<String,Double>>();
	private HashMap<String,Integer> tagWithCount= new HashMap<String,Integer>();
	/*wordWithProbability keeps the probability of the word with a specified tag.
	 * And it keeps tags of a word.*/
	HashMap<String,HashMap<String,Double>> wordWithTags = new HashMap<String,HashMap<String,Double>>();
	HashMap<String,Double> controller ;
	int count =0;
	
	private Searcher searcher;
	public MarkovModel(){
	
	}
	public void createMarkovModel(){
		setInitialTagProbabilities();
		setTransitionProbabilities();
		setEmissionProbabilities();
	

	}
	/*it returns a hashmap including tags and count of tags.*/
	public HashMap<String,Double> getTagsofWord(String word){
		return wordWithTags.get(word);
	}
	
	/*it finds the count of the tag that is belongs to a word.*/
	public double getCountofTagofWord(String word,String tag){
		double countofTag = 0;
			
		if(wordWithTags.containsKey(word)){
			if(wordWithTags.get(word).containsKey(tag)){
				countofTag=wordWithTags.get(word).get(tag);
			}
			else{
				countofTag=0;
			}
				
		}
		return countofTag;	
	}
	
	/*It finds  the probabilities of all tags and adds to hashSet.*/
    public void setInitialTagProbabilities(){
    	uniqueTags=searcher.getUniqueTags();
    	Iterator<String> ite= uniqueTags.iterator();
    	while(ite.hasNext()){
    		String tag=ite.next();
    		Double probability=getInitialTagProbability(tag);
    		Tag currentTag= new Tag(tag,probability);
    		initialTagPro.add(currentTag);
    	}
    }
    /*it find p(t)*/
	public double getInitialTagProbability(String tag){
		double totalofTags= headsofSentence.size();
		double countofTag= getCountofTag(tag);
		double probability = calculateProbability(countofTag,totalofTags);
		count=0;
		return probability;
		
	}
	public Integer getCountofTag2(String tag){
		Iterator<String> ite=tagWithCount.keySet().iterator();
		int countt=0;
		while(ite.hasNext()){
			String currentTag=ite.next();
			if(currentTag.equals(tag)){
				countt=tagWithCount.get(currentTag);
			}
		}
		return countt;
	}
	public double getCountofTag(String tag){
		for(int i=0;i< headsofSentence.size();i++){
			if(headsofSentence.get(i).getTag().equals(tag)){
				count++;
				
			}
		}
		return count;
	}
	/*this method finds the probabilities of all words for transition probability.*/
	public void setTransitionProbabilities(){
		controller =  new HashMap<String,Double>() ;
		tagWithNextTags=searcher.getTagWithNextTags();
		Iterator<String> ite = uniqueTags.iterator();
		
		while(ite.hasNext()){
			String firstTag=ite.next();
			if(tagWithNextTags.containsKey(firstTag)){
				controller=tagWithNextTags.get(firstTag);
				setProbabilityOfNexts(firstTag,controller);
			}	
		}
	}
	
	public void setProbabilityOfNexts(String firstTag,HashMap<String,Double> controller){
		Iterator<String> ite=controller.keySet().iterator();
		double totalofFirst=getCountofTag2(firstTag);
		while(ite.hasNext()){
			
			String secondTag=ite.next();
			
			double countt = controller.get(secondTag);
			
			double probability=countt/totalofFirst;
			controller.put(secondTag, probability);
			
		}
		tagWithNextTags.put(firstTag,controller);
		
	}
	/*it finds p(ti+1|ti).It finds the probability of word coming after it, when a word comes.*/
	public double getTransitionProbability(String firstTag,String secondTag){
		double countofTogether = getCountTogether(firstTag,secondTag);
		count=0;
		double countofFirst=getCountofTag2(firstTag);
		count=0;
		double probability = calculateProbability(countofTogether,countofFirst);
		return probability;
	}
	/*it finds all probabilities for a word with a tag.*/
	public void setEmissionProbabilities(){
		
		wordWithTags=searcher.getWordWithTags();
		Iterator<String> ite = wordWithTags.keySet().iterator();
		while(ite.hasNext()){	
			String word=ite.next();
			//System.out.println(word);
			setProbabilitiesofTag(getTagsofWord(word));
			
		}
	}
	public double getEmissionProbability(String word,String tag){
		controller=wordWithTags.get(word);
		double probability=controller.get(tag);
		return probability;
	}
	/*this is used to set probability of the tags of a word.*/
    public void setProbabilitiesofTag(HashMap<String,Double> tags){
    	
    	Iterator<String> ite= tags.keySet().iterator();
		while(ite.hasNext()){
			String tag=ite.next();
			double countofTags=getCountofTag2(tag);
			double countt=tags.get(tag);
			double probability=countt/countofTags;
			tags.put(tag, probability);
			
		}
		
	}
    /*it finds sum of counts of tags belongs to a word.*/
    public double getTotalofTags(String word){
    	double total=0;
    	Iterator<String> ite = getTagsofWord(word).keySet().iterator();
    	while(ite.hasNext()){
    		String tag=ite.next();
    		double countt=getTagsofWord(word).get(tag);
    		total=total+countt;
    	}
    	return total;
    }
	
	public int getCountTogether(String firstTag, String secondTag){
		for(int i=0;i<allofWords.size();i++){
			if(i+1<allofWords.size()){
				if(allofWords.get(i).getTag().equals(firstTag)
						&&allofWords.get(i+1).getTag().equals(secondTag)
								&&allofWords.get(i+1).getHeadofSentence().equals("no")){
					count++;
				}
			}
			
		}
		return count;
	}
	public int getCountofWord(String word){
		for(int i=0;i<allofWords.size();i++){
			if(allofWords.get(i).getWord().equals(word)){
				count++;
			}
		}
		return count;
	}
	
	public double calculateProbability(double numerator,double denominator){
		double probability = numerator/denominator;
		return probability;
	}
	public void setHeadsofSentence(HashMap<Integer,WordWithTag> headsofSentence) {
		this.headsofSentence=headsofSentence;
	}
	public HashMap<Integer,WordWithTag> getHeadsofSentence(){
		return headsofSentence;
	}
	public void setAllofWords(HashMap<Integer,WordWithTag> allofWords) {
		this.allofWords=allofWords;
	}
	public void setWordWithTags(HashMap<String,HashMap<String,Double>> wordWithTags) {
		this.wordWithTags=wordWithTags;
	}
	
	public HashMap<String,HashMap<String,Double>>  getWordWithTags() {
		return wordWithTags;
	}
	public void setSearcher(Searcher searcher){
		this.searcher=searcher;
	}
	public HashSet<String>  getUniqueTags() {
		return uniqueTags;
	}
	public HashMap<Integer, WordWithTag> getAllofWords(){
		return allofWords;
	}
	public HashMap<String,Integer> getTagWithCount() {
		return tagWithCount;
	}
	public void setTagWithCount(HashMap<String,Integer> tagWithCount) {
		this.tagWithCount = tagWithCount;
	}
	public HashMap<String,HashMap<String,Double>> getTagWithNextTags(){
		return tagWithNextTags;
	}
	public void setUniqueTags(HashSet<String> uniqueTags){
		this.uniqueTags=uniqueTags;
	}
	
}
