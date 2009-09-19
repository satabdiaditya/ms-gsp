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
	 * This function returns all the items contained in this transaction
	 */
	public ArrayList<Integer> getItems() {
		ArrayList<Integer> result = new ArrayList<Integer>();
		for (ItemSet i : itemSets) {
			result.addAll(i.items);
		}
		return result;
	}
	
	/*
	 * This function returns the id of the first item in the transaction
	 */
	public Integer getFirstItem() {
		return itemSets.get(0).items.get(0);
	}
	
	/*
	 * This function returns the id of the last item in the transaction
	 */
	public Integer getLastItem() {
		ArrayList<Integer> items = itemSets.get(itemSets.size()-1).items;
		return items.get(items.size()-1);
	}
	
	/*
	 * test whether the item with id of itemId has the lowest MIS in current
	 * transaction.
	 * When flag=0, itemId is the first item,
	 * flag =1, itemId is the last item.
	 */
	public boolean isSmallest(Integer itemId, int flag) {
		boolean result = true;
		ArrayList<Integer> items = this.getItems();
		if (flag == 0)
			items.remove(0);
		else if (flag == 1)
			items.remove(items.size()-1);
		for (Integer id : items) {
			if (MSGSP.MS.get(id).doubleValue() <= MSGSP.MS.get(itemId).doubleValue()) {
				result = false;
				break;
			}
		}
		return result;
	}
	
	/*
	 * This method takes out the index1th item of current transaction object
	 * and the index2th item of tran, and compares the rests to test whether
	 * they are the same.
	 */
	public boolean specialEqualTo(Transaction tran, int index1, int index2) {
		boolean result = false;
		ArrayList<Integer> items1 = this.getItems();
		items1.remove(index1);
		ArrayList<Integer> items2 = tran.getItems();
		items2.remove(index2);
		if (items1.containsAll(items2) && items1.size() == items2.size())
			result = true;
		return result;
	}

}
