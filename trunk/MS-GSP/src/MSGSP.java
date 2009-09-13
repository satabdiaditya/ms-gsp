import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;


public class MSGSP {
	// Each element in MS is a pair of itemID and its corresponding minimum support
	private HashMap<Integer,Float> MS;  
	/*
	 * S is the an array of transactions which is abstracted as Transaction class.  
	 * Each transaction is an array of itemsets which is abstracted as ItemSet class.  
	 */
	private ArrayList<Transaction> S; 
	/*
	 * N is the number of transactions in S
	 */
	private int N;
	/*
	 * SUP stores the support count for each item using a HashMap
	 */
	private HashMap<Integer, Integer> SUP = new HashMap<Integer, Integer>();
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
		HashSet<Integer> L= initPass(M);   // make the first over S
		
		FrequentSequence F1 = initPrune(L);	//obtain F1 from L
	}
	
	/*
	 * @Func: Make the first pass over S, first scan the database to count the 
	 * support for each item and then follow the sorted order in M to create
	 * the candidate list L for generating F1
	 * @Param: LinkedList M represents the ascending order of all the items according
	 * to their MIS values
	 * @return: the candidate list L for generating F1   
	 */
	public HashSet<Integer> initPass(LinkedList<Integer> M) {
		HashSet<Integer> L=new HashSet<Integer>();
		DataReader reader = new DataReader();
		Iterator<Integer> it = MS.keySet().iterator();	//get the iterator of MS's key set
		while(it.hasNext()) {	//use the iterator to initialize sup, each item's support count is initially set to 0
			SUP.put(it.next(), new Integer(0));
		}
		Transaction tran = reader.readNextTran();	//read transactions from database
		while (tran != null) {
			N++;
			HashSet<Integer> items = tran.getItems();	//get all the items contained in current transaction
			it = items.iterator();
			while (it.hasNext()) {	//add 1 to the support count for each item
				Integer id = it.next();
				Integer count = SUP.get(id);
				SUP.remove(id);
				SUP.put(id, new Integer(count.intValue() + 1));
			}
			tran = reader.readNextTran();
		}
		it = M.iterator();	//get the iterator on M to iterate each item on an ascending order according to their MIS
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
	public FrequentSequence initPrune(HashSet<Integer> L) {
		FrequentSequence F1 = new FrequentSequence();
		Iterator<Integer> it = L.iterator();	//get the iterator
		while (it.hasNext()) {	//iterate all the items in L to find those who meets their own MIS
			Integer itemId = it.next();
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
