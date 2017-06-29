import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class Reader {
	private BufferedReader bufReader;
	HashMap<Integer,String> allofLines= new HashMap<Integer,String>();
	HashMap<Integer,String> linesofInput; 
	int index=0;
	FileReader fileReader;
	public Reader(){
		
	}
	public void findFiles() throws IOException{
		File testFile = new File("./brown");
		File[] testFiles = testFile.listFiles();
		
		for(File file :testFiles){
			if(file.isFile()){
				readAllFiles(file);
			}
		}
		index=0;
		
		
	}
	public void readAllFiles(File file) throws IOException{
		fileReader = new FileReader(file);
		bufReader = new BufferedReader(fileReader);
		String line = bufReader.readLine();
		if(!line.equals("")){
			allofLines.put(index, line);
			index=index+1;
			
		}
		
		while (line != null) {
			line = bufReader.readLine();
			if(line!=null&&!line.equals("")){
				allofLines.put(index,line );
				index=index+1;
				
			}	
		}
	}
	public void readFile(File file) throws IOException{
		linesofInput = new HashMap<Integer,String>();
		fileReader = new FileReader(file);
		bufReader = new BufferedReader(fileReader);
		String line = bufReader.readLine();
		if(!line.equals("")){
			linesofInput.put(index, line);
			index=index+1;
			
		}
		
		while (line != null) {
			line = bufReader.readLine();
			if(line!=null&&!line.equals("")){
				linesofInput.put(index,line );
				index=index+1;
				
			}	
		}
		index=0;
	}
	public HashMap<Integer,String> getLinesofInput(){
		return linesofInput;
		
	}
	public HashMap<Integer,String> getAllofLines(){
		return allofLines;
		
	}
	
}
