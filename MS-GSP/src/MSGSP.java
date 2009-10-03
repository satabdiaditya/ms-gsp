import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;


public class MSGSP {
	// Each element in MS is a pair of itemID and its corresponding minimum support
	public static HashMap<Integer,Float> MS;  
	/*
	 * S is the an array of transactions which is abstracted as Transaction class.  
	 * Each transaction is an array of itemsets which is abstracted as ItemSet class.  
	 */
	public ArrayList<Transaction> S; 
	/*
	 * N is the number of transactions in S
	 */
	public static int N;
	/*
	 * SDC is support distance constraint
	 */
	public static double SDC;
	/*
	 * SUP stores the support count for each item using a HashMap
	 */
	public static HashMap<Integer, Integer> SUP = new HashMap<Integer, Integer>();
	/*
	 * @Func: Constructor for MSGSP class
	 * @Param: data file name and MIS file name
	 */
	MSGSP(String paraFileName, String dataFileName){
		MS=ReadFileInput.readParameters(paraFileName);
		S=ReadFileInput.readData(dataFileName);
	}
	
	
	public void main(){
		/* test if I build the right MIS from input
		for(Integer itemID : MIS.keySet()) {
			System.out.println(itemID+" "+MIS.get(itemID));
		}
		
		// test if I correctly read the data
		int i=0;
		for(Transaction trans: S){
			for(ItemSet is: S.get(i++).itemSets){
				System.out.print("<");
				for(Integer integer: is.items)
					System.out.print(integer+"  ");
				System.out.print(">	");
			}
			System.out.println();
		}
		*/
		
		LinkedList<Integer> M=sort(MS); // according to MIS(i)'s stored in MS
		/* test if sort function works
		for(Integer itemID: M)
			System.out.print(itemID+" ");
		*/
		ArrayList<Integer> L= initPass(M);   // make the first over S
		
		ArrayList<FrequentSequence> F=new ArrayList<FrequentSequence>(); //F1 U F2 U F3....U Fk 
	    // In order to make the index here synchronized with that in the book(start from 1), let F0 be a empty FrequentSequence  
		F.add(new FrequentSequence()); 
		
		
		F.add(initPrune(L));	//obtain F1 from L
		
		MSCandidateGen gen = new MSCandidateGen(); // Define a new instance of MSCandidateGen class
		FrequentSequence Fk_1;
		for(int k=2; !(Fk_1=F.get(k-1)).sequences.isEmpty(); k++){
			FrequentSequence Ck;
			if(k==2)
				Ck= gen.level2CandidateGen(L); 
			else
				Ck=gen.candidateGen(Fk_1);
				
		    for(Transaction s: S)
		    	for(Transaction c: Ck.sequences){
		    		if( c.containedIn(s))
		    			c.count++;
		    	}
		    
		    FrequentSequence Fk=new FrequentSequence();

		    for(Transaction c: Ck.sequences) {
		    	if(c.count>=MS.get(c.getItems().get(c.minMISItem()))*N)
		    		Fk.sequences.add(c);
		    }
		    F.add(Fk);
		}
		F.remove(F.size()-1);
		
		
		// Print F
		int k=0;
		while(++k<F.size()){
			FrequentSequence Fk=F.get(k);
			System.out.println(k+"-Sequence");
			System.out.println();
			for(Transaction tran: Fk.sequences) 
				tran.print();
			System.out.println();
			System.out.println("The total number of "+k+"-Sequence = "+Fk.sequences.size());
			System.out.println();
		}

		
	}
	
	/*
	 * @Func: Make the first pass over S, first scan the database to count the 
	 * support for each item and then follow the sorted order in M to create
	 * the candidate list L for generating F1
	 * @Param: LinkedList M represents the ascending order of all the items according
	 * to their MIS values
	 * @return: the candidate list L for generating F1   
	 */
	public ArrayList<Integer> initPass(LinkedList<Integer> M) {
		ArrayList<Integer> L=new ArrayList<Integer>();
		Iterator<Integer> it = M.iterator();	//get the iterator of MS's key set
		for (Integer i : MS.keySet()) {	//initialize SUP, set each item's support count to 0
			SUP.put(i, new Integer(0));
		}
		
		for (Transaction tran : S) {
			N++;
			HashSet<Integer> items = tran.getItemsAsSet();	//get all the items contained in current transaction
			for (Integer id : items) {	//add 1 to the support count for each item
				Integer count = SUP.get(id);
				SUP.put(id, new Integer(count.intValue() + 1));
			}
		}
		Integer minId = null;	//used to store the id of the first item who meets its MIS
		while (it.hasNext()) {	//find the first item who meets its MIS
			Integer itemId = it.next();
			if (SUP.get(itemId)*1.0/N >= MS.get(itemId).floatValue()) {
				minId = itemId;
				L.add(itemId);
				break;
			}
		}
		while (it.hasNext()) {	//find in the following items who meets item minId's MIS
			Integer itemId = it.next();
			if (SUP.get(itemId)*1.0/N >= MS.get(minId).floatValue()) {
				L.add(itemId);
			}
		}
		return L;
	}

	/*
	 * @Func: Prune L get from function init-pass to get Frequent 1-sequences
	 * @Param: L, the set of items' id obtained from init-pass
	 * @return: Frequent 1-sequences
	 */
	public FrequentSequence initPrune(ArrayList<Integer> L) {
		FrequentSequence F1 = new FrequentSequence();
		for (Integer itemId : L) {	//iterate all the items in L to find those who meets their own MIS
			if (SUP.get(itemId)*1.0/N >= MS.get(itemId).floatValue()) {	//Create a 1-sequence, and add it to F1
				ItemSet is = new ItemSet();
				is.items.add(itemId);
				Transaction tran = new Transaction();
				tran.itemSets.add(is);
				F1.addTransaction(tran);	
			}
		}
		return F1;
	}
	
	/*
	 * @Func: Sort the items in ascending order according to their MIS values stored in MS
	 * @Param: HashMap MS contains all pairs of itemID and its corresponding minimum support
	 * @return: An ordered list of itemIDS.
	 */
	public LinkedList<Integer> sort(HashMap<Integer,Float> MS){
		LinkedList<Integer> M=new LinkedList<Integer>();
		for(Integer itemID: MS.keySet()){
			if(M.size()==0)
				M.add(itemID);
			else{
				int i=0;
				while(i<M.size()&&MS.get(M.get(i))<MS.get(itemID))
					i++;
				M.add(i,itemID);
			}
	    }
		return M;
	}
	
	//public void sort(HashMap)
	
	public static void main(String[] args) {
		new MSGSP("parameter-file.txt","data.txt").main();		
	}

}
