import java.util.HashMap;
import java.util.Locale;

public class IntegratedParser {
	HashMap<Integer,IntegratedWord> integratedWords= new HashMap<Integer,IntegratedWord>();
	HashMap<Integer,WordWithTag> allofWords = new HashMap<Integer,WordWithTag>();
	HashMap<Integer,WordWithTag> headsofSentence = new HashMap<Integer,WordWithTag>();
	int index=0;
	int key=0;
	private Searcher searcher;
	MarkovModel markov = new MarkovModel();
	public IntegratedParser(HashMap<Integer,IntegratedWord> integratedWords,MarkovModel markov){
		this.integratedWords=integratedWords;
		this.markov=markov;
		
	}
	public void parseIntegrateds(Searcher searcher){	
		
		String[] wordandTag=null;
		for(int i=0;i<integratedWords.size();i++){
			wordandTag=integratedWords.get(i).getWord().split("/");
			addWordandTag(wordandTag,i);
			
			
		}
		
		searcher=new Searcher(allofWords,headsofSentence);
		this.searcher=searcher;
		markov.setAllofWords(searcher.getAllofWords());
		markov.setHeadsofSentence(searcher.getHeadsofSentence());
		markov.setWordWithTags(searcher.getWordWithTags());
		markov.setSearcher(searcher);
		markov.setTagWithCount(searcher.getTagWithCount());
		markov.setUniqueTags(searcher.getUniqueTags());
		
		
	    
		
	}
	public void addWordandTag(String [] wordandTag,int index){
		
		String word=wordandTag[0].toLowerCase(Locale.ENGLISH);
		String tag=wordandTag[1];
		String head=integratedWords.get(index).getHeadofLine();
		if(wordandTag.length!=2){
			addUnifiedWords(wordandTag,head,index);
		}
		else{
			
			WordWithTag wordWithTag = new WordWithTag(word,tag,head);
			allofWords.put(index,wordWithTag);
			index++;
			if(head.equals("yes")){
				headsofSentence.put(key, wordWithTag);
				key++;
			}
		}
		
	}
	/*for example, over/under/jj. These are added as over/jj and under/jj.*/
	public void addUnifiedWords(String []wordandTag,String head,int index){
		String tag=wordandTag[wordandTag.length-1];
		for(int i=0;i<wordandTag.length-1;i++){
			String currentWord=wordandTag[i].toLowerCase(Locale.ENGLISH);
			WordWithTag wordWithTag = new WordWithTag(currentWord,tag,head);
			allofWords.put(index, wordWithTag);
			index++;
            if(head.equals("yes")){
				
				headsofSentence.put(key, wordWithTag);
				key++;
				//System.out.println(word);
			}
		}
		
	}
	public Searcher getSearcher() {
		return searcher;
	}
	public void setSearcher(Searcher searcher) {
		this.searcher = searcher;
	}

	
	
}
