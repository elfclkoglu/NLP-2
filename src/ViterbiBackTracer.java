import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class ViterbiBackTracer {
	ArrayList<ArrayList<Tag>> allTags = new ArrayList<ArrayList<Tag>>();
	private HashMap<Integer,IntegratedWord> inputWords = new HashMap<Integer,IntegratedWord>();
	ArrayList<Tag> tagsofWord = new ArrayList<Tag>();
	ArrayList<Double> probabilities;
	HashMap<Integer,WordWithTag> sentence;
	String preTag="";
	public ViterbiBackTracer(ArrayList<ArrayList<Tag>> allTags,HashMap<Integer,IntegratedWord> inputWords){
		this.allTags=allTags;
		this.inputWords=inputWords;
		sentence= new HashMap<Integer,WordWithTag>();
		chooseFinalTag();
		chooseTags();
		printSentence();
		
	}
	public void printSentence(){
		System.out.println("********Task 4********");
		for(int i=0;i<sentence.size();i++){
			if(i+1<sentence.size()){
				if(sentence.get(i+1).getHeadofSentence().equals("no"))
					System.out.print(sentence.get(i).getWord()+" ");
				else{
						System.out.print(sentence.get(i).getWord());
						
						System.out.println();
						
				}
			}
			else{
				
					System.out.println(sentence.get(i).getWord());
				
			}
			
				
			}
			
	}
	public void chooseFinalTag(){
		int i=allTags.size()-1;
		tagsofWord=allTags.get(i);
		findProbabilities(tagsofWord,i);
		
		
	}
	public void chooseTags(){
		for(int i=allTags.size()-2;i>=0;i--){
			tagsofWord=allTags.get(i);
			findProbabilities(tagsofWord,i);
			
		}
	}
	public void findProbabilities(ArrayList<Tag> tagsofWord,int index){
		probabilities= new ArrayList<Double>();
		
		for(int i=0;i<tagsofWord.size();i++){
			probabilities.add(tagsofWord.get(i).getProbability());
		
		}
		
		addTagtoWord(index);
	}
	public double chooseMax(){
		Collections.sort(probabilities);
		int size=probabilities.size();
		return probabilities.get(size-1);
	}
	public void addTagtoWord(int index){
		
		if(index==allTags.size()-1){
			String tag=getTag();
			String word=inputWords.get(index).getWord();
			String head=inputWords.get(index).getHeadofLine();			
			String wordWithTag=word+"/"+tag;
			WordWithTag currentTag= new WordWithTag(wordWithTag,"",head);
			sentence.put(index, currentTag);
		}
		else{
			getTagWithConnection(index);
		}
		
		
	}
	public void getTagWithConnection(int index){
		for(int i=0;i<tagsofWord.size();i++){
			String tag = tagsofWord.get(i).getTag();
			
			if(tag.equals(getPreviousTag())){
				String word=inputWords.get(index).getWord();
				String wordWithTag=word+"/"+tag;
				String head = inputWords.get(index).getHeadofLine();
				WordWithTag currentTag = new WordWithTag(wordWithTag,"",head);
				setPreviousTag(tagsofWord.get(i).getPreviousTag());
				sentence.put(index, currentTag);
				
					
				
				
			}
		}
	}
	public String getTag(){
		String tag="";
		double pro=chooseMax();
		for(int i=0;i<tagsofWord.size();i++){
			if(tagsofWord.get(i).getProbability()==pro){
				tag=tagsofWord.get(i).getTag();
				setPreviousTag(tagsofWord.get(i).getPreviousTag());
			}
		}
		return tag;
	}
	public void setPreviousTag(String preTag){
		this.preTag=preTag;
	}
	public String getPreviousTag(){
		return preTag;
	}
}
