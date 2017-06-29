
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


public class ViterbiAlgorithm {
	private HashMap<String,HashMap<String,Double>> tagWithNextTags = new HashMap<String,HashMap<String,Double>>();
	private HashMap<String,HashMap<String,Double>> wordWithTags = new HashMap<String,HashMap<String,Double>>();
	private HashMap<Integer,IntegratedWord> inputWords = new HashMap<Integer,IntegratedWord>();
	HashMap<Integer,WordWithTag> headsofSentence = new HashMap<Integer,WordWithTag>();
	/*allTags includes tags found with emission and transition probabilities.*/
	ArrayList<ArrayList<Tag>> allTags;
	ArrayList<Tag> chosenTags;
	ArrayList<String> tagsofWord;
	HashMap<String,Double> controller= new HashMap<String,Double>();
	private ArrayList<Tag> previousTags;
	private ArrayList<Double> emissionPro;
	private ArrayList<Tag> tagController;
	private double max=-1000;
	int index=0;
	double key=0;
	private ArrayList<HashMap<Integer,IntegratedWord>> wordofSentence = new ArrayList<HashMap<Integer,IntegratedWord>>();
	private HashMap<String,Double> nextTags = new HashMap<String,Double>();
	HashMap<Integer,IntegratedWord> sentence;
	private ViterbiBackTracer backTracer;
	public ViterbiAlgorithm(MarkovModel markov,HashMap<Integer,IntegratedWord> inputWords){
		this.tagWithNextTags=markov.getTagWithNextTags();
		this.wordWithTags=markov.getWordWithTags();
		this.inputWords=inputWords;
		this.headsofSentence=markov.getHeadsofSentence();
		allTags = new ArrayList<ArrayList<Tag>>();
		chooseFirstTag();
		lookForWords();
		backTracer = new ViterbiBackTracer(allTags,inputWords);
		
	}

	public void setTagofSentence(){
		for(int i=0;i<wordofSentence.size();i++){
			allTags = new ArrayList<ArrayList<Tag>>();
			inputWords=wordofSentence.get(i);
			chooseFirstTag();
			lookForWords();
			backTracer = new ViterbiBackTracer(allTags,inputWords);
		}
	}
	public void parseSentence(){
		sentence=new HashMap<Integer,IntegratedWord>();
		for(int i=0;i<inputWords.size();i++){
			
			if(i+1<inputWords.size()){
				if(inputWords.get(i+1).getHeadofLine().equals("no")){
					sentence.put(index, inputWords.get(i));
					index++;
				}
				else if(inputWords.get(i+1).getHeadofLine().equals("yes")){
					sentence.put(index, inputWords.get(i));
					index=0;
					wordofSentence.add(sentence);
					sentence= new HashMap<Integer,IntegratedWord>();
					
				}
			}
			else{
				sentence.put(index, inputWords.get(i));
				wordofSentence.add(sentence);
			}
			
			
			
			
		}
	}

	public void lookForWords(){
		for(int i=1;i<inputWords.size();i++){
			String currentWord=inputWords.get(i).getWord();
			/*controller includes all of the tags of current word.*/
			controller = wordWithTags.get(currentWord);
			chooseTags(controller);
		}
	}
	public void chooseTags(HashMap<String, Double> controller){
		chosenTags=new ArrayList<Tag>();
		tagsofWord=new ArrayList<String>();
		emissionPro= new ArrayList<Double>();
		Iterator<String> ite=controller.keySet().iterator();
		while(ite.hasNext()){
			String tag = ite.next();
			Double pro= controller.get(tag);
			emissionPro.add(pro);
			tagsofWord.add(tag);
			
		}
		compareToPrevious();
		allTags.add(chosenTags);
		
		
	}
	public void compareToPrevious(){
		for(int i=0;i<tagsofWord.size();i++){
			
			String currentTag=tagsofWord.get(i);
			double pro=emissionPro.get(i);
			previousTags=getPreviousTags();
			
			findPreviousTags(currentTag,previousTags,pro);
			
		}
		
	}
	public ArrayList<Tag> getPreviousTags(){
		previousTags = new ArrayList<Tag>();
		return allTags.get(allTags.size()-1);
		
		
	}
	public void findPreviousTags(String currentTag,ArrayList<Tag>previousTags,double pro){
		tagController = new ArrayList<Tag>();
		for(int i=0;i<previousTags.size();i++){
			String previousTag=previousTags.get(i).getTag();
			if(tagWithNextTags.containsKey(previousTag)){
				
				nextTags=tagWithNextTags.get(previousTag);
			    if(nextTags.containsKey(currentTag)){
			    	double tagsPro= nextTags.get(currentTag);
			    	double proOfPrevious = previousTags.get(i).getProbability();
			    	double proWithWord =pro;
			    	Tag tag =  new Tag(previousTag,proOfPrevious,currentTag,getLog(tagsPro),getLog(proWithWord));
			    	tagController.add(tag);
			    	
			    }
			    			
			    		
			    	   
			    	
			    
			}
		}
		
		setChosenWords(tagController);
	}
	public void setChosenWords(ArrayList<Tag> tagController){
		for(int i=0;i<tagController.size();i++){
			Tag tag= tagController.get(i);
			
			findMax(tag);
		}
		
		
		calculatePro(tagController);
		setMax(-10000);
	}
	public void calculatePro(ArrayList<Tag> tagController){
		for(int i=0;i<tagController.size();i++){
			Tag tag=tagController.get(i);
			double total=tag.getPreProbability()+tag.getTransitionPro();
			if(getMax()==total){
				double pro = tag.getProbability()+tag.getPreProbability()+tag.getTransitionPro();
				Tag currentTag = new Tag(tag.getPreviousTag(),tag.getTag(),pro);
			    chosenTags.add(currentTag);
				
					
			}
		}
	}
	public void findMax(Tag tag){
		double preEmissionPro=0;
		double transitionPro=0;
		double total=0;
		preEmissionPro=tag.getPreProbability();
		transitionPro = tag.getTransitionPro();
		total = preEmissionPro+transitionPro;
		if(total>getMax()){
			
			setMax(total);
		
		}
	}
	
	public void chooseFirstTag(){
		String firstWord = inputWords.get(0).getWord();
		if(wordWithTags.containsKey(firstWord)){
			controller=wordWithTags.get(firstWord);
			addFirstTags(controller);
		}
	}
	
	public void addFirstTags(HashMap<String,Double> controller){
		chosenTags=new ArrayList<Tag>();
		Iterator<String> ite = controller.keySet().iterator();
		while(ite.hasNext()){
			String tag=ite.next();
			Double probability = getLog(controller.get(tag))+getLog(startProbability(tag)/headsofSentence.size());
			key=0;
			if(getHead(tag).equals("yes")){
				Tag currentTag= new Tag(tag,probability);
				chosenTags.add(currentTag);
				
			}
		}
		addTags(chosenTags);
		
	}
	public double startProbability(String tag){
		for(int i=0;i<headsofSentence.size();i++){
			String currentTag=headsofSentence.get(i).getTag();
			if(tag.equals(currentTag)){
				key++;
			}
		}
		return key;
	}
	/*is tag head of a sentence*/
	public String getHead(String tag){
		String chosenHead="";
		for(int i=0;i<headsofSentence.size();i++){
			String currentTag=headsofSentence.get(i).getTag();
			String head= headsofSentence.get(i).getHeadofSentence();
			if(tag.equals(currentTag)){
				chosenHead=head;
				break;
			}
		}
		return chosenHead;
	}
	/*for back trace. Allof tags and probabilities are added in this method.*/
	public void addTags(ArrayList<Tag> tagsofWord){
		allTags.add(tagsofWord);
	}
	public double getLog(double probability){
		probability=Math.log(probability)/Math.log(2);
		return probability;
	}

	public HashMap<String,HashMap<String,Double>> getTagWithNextTags() {
		return tagWithNextTags;
	}

	public void setTagWithNextTags(HashMap<String,HashMap<String,Double>> tagWithNextTags) {
		this.tagWithNextTags = tagWithNextTags;
	}

	public HashMap<String,HashMap<String,Double>> getWordWithTags() {
		return wordWithTags;
	}

	public void setWordWithTags(HashMap<String,HashMap<String,Double>> wordWithTags) {
		this.wordWithTags = wordWithTags;
	}
	public HashMap<Integer,IntegratedWord> getInputWords() {
		return inputWords;
	}
	public void setInputWords(HashMap<Integer,IntegratedWord> inputWords) {
		this.inputWords = inputWords;
	}
	public void setMax(double max){
		this.max=max;
	}
	public double getMax(){
		return max;
	}
	public ViterbiBackTracer getBackTracer() {
		return backTracer;
	}
	public void setBackTracer(ViterbiBackTracer backTracer) {
		this.backTracer = backTracer;
	}
	public ArrayList<HashMap<Integer,IntegratedWord>> getWordofSentence() {
		return wordofSentence;
	}
	public void setWordofSentence(ArrayList<HashMap<Integer,IntegratedWord>> wordofSentence) {
		this.wordofSentence = wordofSentence;
	}
	

}

