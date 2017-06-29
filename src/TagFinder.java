import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;

public class TagFinder {
	MarkovModel markov = new MarkovModel();
	HashMap<Integer,IntegratedWord> inputWords = new HashMap<Integer,IntegratedWord>();
	HashMap<String, HashMap<String, Double>> wordWithTags = new HashMap<String,HashMap<String,Double>>();
	HashMap<String,Double> controller = new HashMap<String,Double>();
	ArrayList<WordWithTag> wordsofLine;
	
	private double max=0;
	String currentTag="";
	public TagFinder(MarkovModel markov,HashMap<Integer,IntegratedWord> inputWords){
		this.markov=markov;
		this.inputWords=inputWords;
		/*wordWithTags includes word , tags of this word and count of tags in this class.*/
		
		/*setEmissionProbabilities set the probability of tags of all word.*/
		markov.setEmissionProbabilities();
		this.wordWithTags=markov.getWordWithTags();
	
		
		
		
	}
	
	public void getWords(){
		System.out.println("********Task 3********");
		wordsofLine= new ArrayList<WordWithTag>();
		String tag="";
		WordWithTag chosenWord;
		for(int i=0;i<inputWords.size()-1;i++){
			
			String word = inputWords.get(i).getWord();
			
			if(wordWithTags.containsKey(word)){
				tag=chooseTag(word);
				/*words will be added until the end of the line*/
				if(isNextHeadofLine(i+1)==false){
					createSentence(word,tag,inputWords.get(i).getHeadofLine());
                    			
				}
				/*this is used to print a line*/
				else if(isNextHeadofLine(i+1)==true){
					createSentence(word,tag,inputWords.get(i).getHeadofLine());
					printLine();
					wordsofLine= new ArrayList<WordWithTag>();
					
				}	
				/*this control is used to find the last word in the last line.*/
				if((i+2)==inputWords.size()){
					String finalWord = inputWords.get(i+1).getWord();
					String finalTag = chooseTag(finalWord);
					createSentence(finalWord,finalTag,inputWords.get(i+1).getHeadofLine());
					printLine();
					wordsofLine= new ArrayList<WordWithTag>();
				}
			}
		}
	}
	public void printLine(){
		String sentence="";
		for(int i=0;i<wordsofLine.size();i++){
			if(i==0){
				
				sentence=sentence+setUpperCase(wordsofLine.get(i).getWord())+"/"+wordsofLine.get(i).getTag()+" ";
			}
			else{
				sentence=sentence+wordsofLine.get(i).getWord()+"/"+wordsofLine.get(i).getTag()+" ";
			}
			
		}
		System.out.println(sentence);
	}
	/*is the word head of a line*/
	public boolean isNextHeadofLine(int index){
		String head=inputWords.get(index).getHeadofLine();
		if(head.equals("yes")){
			return true;
		}
		else{
			return false;
		}
		
	}
	
	public void createSentence(String word,String tag,String headofLine){
		WordWithTag wordWithTag = new WordWithTag(word,tag,headofLine);
		wordsofLine.add(wordWithTag);
		
		
	}
	/*this method will choose the tag of the word that has the maximum count.*/
	public String chooseTag(String word){
		
		String chosenTag="";
		controller=wordWithTags.get(word);
		Iterator<String> ite= controller.keySet().iterator();
		while(ite.hasNext()){
			
			String tag=ite.next();
			chosenTag=findMaxTag(controller,tag,max); 
			
			
		}
		max=0;
		return chosenTag;
		
		
	}
	/*it finds tag that has maximum count.*/
	public String findMaxTag(HashMap<String,Double> controller,String tag,double max){
		double count=controller.get(tag);
		if(max<count){
			setMax(count);
			currentTag=tag;
		}
		return currentTag;
	
	}
	public double getEmissionProbability(String word,String tag){
		double probability =markov.getEmissionProbability(word, tag);
		return probability;
	}
	public void setMax(double max){
		this.max=max;
	}
	public String setUpperCase(String word){
		String upperWord=Character.toString(word.charAt(0)).toUpperCase(Locale.ENGLISH)+word.substring(1);
		return upperWord;
	}
	

}
