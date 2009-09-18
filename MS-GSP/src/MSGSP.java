import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;


public class MSGSP {
	// Each element in MS is a pair of itemID and its corresponding minimum support
	public static HashMap<Integer,Float> MS;  
	/*
	 * S is the an array of transactions which is abstracted as Transaction class.  
	 * Each transaction is an array of itemsets which is abstracted as ItemSet class.  
	 */
	private ArrayList<Transaction> S; 
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
		LinkedList<Integer> L= initPass(M);   // make the first over S
		System.out.println("L is ");
		System.out.println(L);
		
		FrequentSequence F1 = initPrune(L);	//obtain F1 from L
		System.out.println("F1 is ");
		for (Transaction t : F1.sequences) { 
			for (ItemSet is : t.itemSets)
				System.out.print(is.items);
			System.out.println();
		}
		MSCandidateGen gen = new MSCandidateGen();
		FrequentSequence C2= gen.level2CandidateGen(L);
		System.out.println("C2 is ");
		for (Transaction t : C2.sequences) { 
			for (ItemSet is : t.itemSets)
				System.out.print(is.items);
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
	public LinkedList<Integer> initPass(LinkedList<Integer> M) {
		LinkedList<Integer> L=new LinkedList<Integer>();
		DataReader reader = new DataReader();
		Iterator<Integer> it = MS.keySet().iterator();	//get the iterator of MS's key set
		for (Integer i : MS.keySet()) {	//initialize SUP, set each item's support count to 0
			SUP.put(i, new Integer(0));
		}
		Transaction tran = reader.readNextTran();	//read transactions from database
		while (tran != null) {
			N++;
			ArrayList<Integer> items = tran.getItems();	//get all the items contained in current transaction
			for (Integer id : items) {	//add 1 to the support count for each item
				Integer count = SUP.get(id);
				SUP.put(id, new Integer(count.intValue() + 1));
			}
			tran = reader.readNextTran();
		}

		Integer minId = null;	//used to store the id of the first item who meets its MIS
		for (Integer itemId : M) {	//find the first item who meets its MIS
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
	public FrequentSequence initPrune(LinkedList<Integer> L) {
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
