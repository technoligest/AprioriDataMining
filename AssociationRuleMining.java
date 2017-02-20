import java.util.*;
import java.io.*;
public class AssociationRuleMining {
	public static void main(String[]args){
		Scanner kb = new Scanner(System.in);
		//		String sourceFile = kb.next();
		//		String minSup = kb.next();
		//		String minConf = kb.next();
		HashSet<String> a= new HashSet<String>();
		a.add("1.1");
		a.add("2.2");
		a.add("3.1");
		HashSet<String> b= new HashSet<String>();
		b.add("1.1");
		b.add("2.2");
		b.add("4.1");
		System.out.println(a);
		HashSet<String> result= generateCandidateItemSetFromTwoSubsets(a, b);
		System.out.println(result);
	}
	private static HashMap<String, HashSet<String>> loadDB(String fileName) throws FileNotFoundException{

		HashMap<String, HashSet<String>> result=new HashMap<String,HashSet<String>>();
		Scanner file = new Scanner(new File(fileName));
		StringTokenizer tk= new StringTokenizer(file.nextLine());
		ArrayList<String> headers= new ArrayList<String>();
		while(tk.hasMoreTokens()){
			String temp=tk.nextToken();
			headers.add(temp);
			result.put(temp, new HashSet<String>());
		}

		while(file.hasNextLine()){
			tk = new StringTokenizer(file.nextLine());
			int i=0;
			while(tk.hasMoreTokens()){
				result.get(headers.get(i++)).add(tk.nextToken());
			}
		}
		return result;
	}
	//	public static HashMap<HashSet<String>, Integer> buildFirstItemSet(HashSet<HashSet<String>> all_tuples, int minSup){
	//		return generateFrequent1ItemSet(generateCandidate1ItemSet(all_tuples), minSup);
	//	}

	public static HashMap<String, Integer> generateCandidate1ItemSet(HashSet<HashSet<String>> all_tuples){
		HashMap<String, Integer> result=new HashMap<String,Integer>();;

		int countTmp=0;
		for(HashSet<String> tuple:all_tuples){
			for(String item:tuple){
				if(result.containsKey(item)){
					result.put(item, result.get(item)+1);
				}
				else{
					result.put(item, 1);
				}
			}
		}
		return result;
	}
	public static HashMap<String,Integer> generateFrequent1ItemSet(HashMap<String, Integer> candidates, int support_count){
		HashMap<String,Integer> result=null;
		for(HashMap.Entry<String, Integer> entry :candidates.entrySet()){
			int count=entry.getValue();
			if(count>=support_count){
				result.put(entry.getKey(), entry.getValue());
			}
		}
		return result;
	}

	public static HashSet<String> generateCandidateItemSetFromTwoSubsets(HashSet<String> a, HashSet<String>b){
		HashSet<String> result = new HashSet<String>();
		int size = a.size();
		HashSet<String> tempA = new HashSet<String>(a);
		tempA.retainAll(b);
		System.out.println("temp: "+tempA.size()+"\na: "+a.size());
		if(tempA.size()==a.size()-1){
			result.addAll(a);
			result.removeAll(b);
			result.addAll(b);
		}
		return result;
	}

	public static HashSet<HashSet<String>> generateCandidateKItemsets(HashSet<HashSet<String>> frequentMItemSet,int m ){
		HashSet<HashSet<String>> result = null;

		Iterator<HashSet<String>> iterator1 = frequentMItemSet.iterator();
		Iterator<HashSet<String>> iterator2;
		while(iterator1.hasNext()){
			iterator2 = (Iterator<HashSet<String>>)iterator1.next();
			for(;iterator2.hasNext(); iterator2 = (Iterator<HashSet<String>>)iterator2.next()){
				result.add(generateCandidateItemSetFromTwoSubsets((HashSet<String>)iterator2, (HashSet<String>)iterator1));
			}
		}
		return result;
	}
	public static HashMap<HashSet<HashSet<String>>, Integer> generateFrequentKItemSet(HashSet<HashSet<String>> allFrequent1Itemsets, int supCount){
		HashSet<HashSet<HashSet<String>>> result=null;
		HashMap<Integer, HashSet<HashSet<String>>> re=null;
		HashSet<HashSet<String>> frequentMItemSet=allFrequent1Itemsets;

		int m = 1;
		while(!frequentMItemSet.isEmpty()){
			HashSet<HashSet<String>> candKItemsets = generateCandidateKItemsets(frequentMItemSet,m);
			frequentMItemSet=null;
			++m;
			for(HashSet<String> itemset: candKItemsets){
				int c=0;//NEEDS WORK
				if(c>=supCount){
					result.add(candKItemsets);
					frequentMItemSet.add(candKItemsets);
					
				}
			}
		}
		return result;
	}
}
