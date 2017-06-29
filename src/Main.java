import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Main {
    static HashMap <Integer,String> allofLines = new HashMap<Integer,String>();
    static Reader reader;
    static MarkovModel markov = new MarkovModel();
    static HashMap<Integer,IntegratedWord> inputWords; 
	private static TagFinder tagFinder;
	static Searcher searcher;
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		readBrown();
		readInputFile();
		findTagsofInput();
		markov.createMarkovModel();
		readForViterbi();
	}
	public static void readBrown() throws IOException{
		reader = new Reader();
		reader.findFiles();
		allofLines = reader.getAllofLines();
		LineParser lineParser = new LineParser();
		lineParser.parseLines(allofLines,"withTag",markov,searcher);
	}
	public static void readInputFile() throws IOException{
		File file = new File("input_tokens.txt");
		reader.readFile(file);
		LineParser parserforInput = new LineParser();
		parserforInput.parseLines(reader.getLinesofInput(),"withoutTag",markov,searcher);
		inputWords=parserforInput.getWords();
	}
	public static void readForViterbi() throws IOException{
		File testFile = new File("test_set.txt");
		reader.readFile(testFile);
		LineParser parserForViterbi = new LineParser();
		parserForViterbi.parseLines(reader.getLinesofInput(),"withoutTag", markov, searcher);
		inputWords=parserForViterbi.getWords();
		ViterbiAlgorithm viterbi=new ViterbiAlgorithm(markov,inputWords);
		
	}

	public static void findTagsofInput(){
		tagFinder = new TagFinder(markov,inputWords);
		tagFinder.getWords();
		
	}

}
