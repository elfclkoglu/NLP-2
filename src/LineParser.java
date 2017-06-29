import java.util.HashMap;
import java.util.Locale;

public class LineParser {
	HashMap<Integer,String> lines = new HashMap<Integer,String>();
	HashMap<Integer,IntegratedWord> integratedWords =  new HashMap<Integer,IntegratedWord>();;
	HashMap<Integer,IntegratedWord> words = new HashMap<Integer,IntegratedWord>(); 
	int index=0;
	IntegratedParser intParser;
	public LineParser(){
		
	}
	public void parseLines(HashMap<Integer,String> lines,String control,MarkovModel markov,Searcher searcher){
		this.lines=lines;
		
		/*Integrated means word and tag together.*/
		String []integrateds=null;
		
		if(control.equals("withTag")){
			for(int i=0;i<lines.size();i++){
				integrateds=lines.get(i).split("\\s+");
				addIntegratedWords(integrateds,integratedWords);
			}
			intParser = new IntegratedParser(integratedWords,markov);
			intParser.parseIntegrateds(searcher);
			
		}
		else{
			for(int i=0;i<lines.size();i++){
				integrateds=lines.get(i).split("\\s+");
				addIntegratedWords(integrateds,words);
			}
			index=0;
			
			
		}
	}
	public void addIntegratedWords(String []integrateds,HashMap<Integer,IntegratedWord> integratedWords){
		for(int i=0;i<integrateds.length;i++){
			if(!integrateds[i].equals(null)&&!integrateds[i].equals("")&&!integrateds[i].equals(" ")){
				IntegratedWord word=new IntegratedWord(integrateds[i].toLowerCase(Locale.ENGLISH),"no");
				if(i==0){
					word.setHeadofLine("yes");
				}
				
				integratedWords.put(index, word);
				index++;
			}
			
		}
	}
	public HashMap<Integer,IntegratedWord> getWords(){
		return words;
	}

}
