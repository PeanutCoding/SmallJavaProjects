//package ML;

import ML.NGrams;
import java.util.HashMap;

public class Probabilities{
	private HashMap<String, Double> probs;
	public String lang;
	public int uniGrams;
	public Probabilities(String language, HashMap<String, Integer> nGrams){
		this.lang = language;
		this.uniGrams = 0;
		this.probs = new HashMap<String, Double>();
		this.calculate(nGrams);
	}

	private void calculate(HashMap<String, Integer> m){
		int nGramLength = 1;
		int uniGramCount = 0;
		double prob = 0.0;
		for(HashMap.Entry<String, Integer> el : m.entrySet()){
			nGramLength = el.getKey().length();
			if(nGramLength == 1){
				uniGramCount += el.getValue();
			}
		
		}
		this.uniGrams = uniGramCount;
		for(HashMap.Entry<String, Integer> el : m.entrySet()){
			nGramLength = el.getKey().length();
			if(nGramLength == 1){
				prob = (double)el.getValue() / (double)this.uniGrams;
			}
			else{
				String key = this.getContext(el.getKey());
				prob = (double)el.getValue() / (double)m.get(key);
			}
			this.probs.put(el.getKey(), prob);
			System.out.println(el.getKey() + " " + Double.toString(prob));
			
		}
	}
	
	private String getContext(String target){
		String context = "";
		for(int i = 0; i < target.length() - 1; i++){
			context += Character.toString(target.charAt(i));
		}
		return context;
	}
	
	public static void main(String[] args){
		NGrams ngrams = new NGrams("file.txt", 3);
		Probabilities probs = new Probabilities("de", ngrams.getCounts());
		System.out.println(probs.uniGrams);
	}

}


