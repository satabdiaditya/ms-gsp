import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

/*
 * This is the class used to abstract transaction.
 * Every transaction will contains a list of itemsets.
 */
public class Transaction {
	public ArrayList<ItemSet> itemSets;
	Transaction(){
		itemSets=new ArrayList<ItemSet>();
	}
	
	/*
	 * This function returns all the items contained in this transaction as a set
	 * so there will be no duplicate among the items
	 */
	public HashSet<Integer> getItems() {
		HashSet<Integer> result = new HashSet<Integer>();
		Iterator<ItemSet> it = itemSets.iterator();
		while (it.hasNext()) {
			result.addAll(it.next().items);
		}
		return result;
	}
	
}
