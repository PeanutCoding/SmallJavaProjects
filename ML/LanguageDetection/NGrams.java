package ML;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.FileReader;
public class NGrams{
	
	private HashMap<String, Integer> counts;
	private String filename;
	private int size;
	public NGrams(String file, int nGramSize){
		this.filename = file;
		this.size = nGramSize;
		this.counts = new HashMap<String, Integer>();
		this.readIn();
	};
	
	public HashMap<String, Integer> getCounts(){
		return this.counts;
	};

	private String getNgram(String target,int start, int size){
		String ngram = "";
		for(int i = 0; i < size; i++){
			ngram += Character.toString(target.charAt(i + start));
		}
		return ngram;
	};

	private void readIn(){
		String text = "";
		String temp = "";
		try(BufferedReader br = new BufferedReader(new FileReader("./file.txt"))){
			temp = br.readLine();
			while(temp != null){
				text += temp;
				temp = br.readLine();
			}
		}
		catch(Exception e){
		};
		int length = text.length();
		for(int j = this.size; j > 0; j--){
			for(int i = 0; i < length - j; i++){
				String key = this.getNgram(text, i, j);
				if(this.counts.get(key) == null){
					counts.put(key, 1);
				}
				else{
					counts.put(key, counts.get(key) + 1);
				}
			}
		}
		
	};

	public static void main(String[] args){
		NGrams ngrams = new NGrams("sdlfä+#+ä+qe+üwf#3+2ốr,\n", 3);
		for (HashMap.Entry<String, Integer> entry : ngrams.getCounts().entrySet()) {
			String key = entry.getKey();
			Integer value = entry.getValue();
			System.out.println(key + " " + (String) Integer.toString(value));
		}
	}
}
