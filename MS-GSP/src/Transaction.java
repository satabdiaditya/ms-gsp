import java.util.ArrayList;

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
	public ArrayList<Integer> getItems() {
		ArrayList<Integer> result = new ArrayList<Integer>();
		for (ItemSet i : itemSets) {
			result.addAll(i.items);
		}
		return result;
	}
	
}
