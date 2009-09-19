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
	
	public static void main(String args[]) {
		Transaction tran1 = new Transaction();
		ItemSet is1 = new ItemSet();
		is1.items.add(new Integer(10));
		is1.items.add(new Integer(12312));
		is1.items.add(new Integer(80));
		tran1.itemSets.add(is1);
		ItemSet is2 = new ItemSet();
		is2.items.add(new Integer(70));
		tran1.itemSets.add(is2);
		ItemSet is3 = new ItemSet();
		is3.items.add(new Integer(50));
		is3.items.add(new Integer(90));
		tran1.itemSets.add(is3);
		Transaction tran2 = new Transaction();
		ItemSet is12 = new ItemSet();
		is12.items.add(new Integer(10));
		is12.items.add(new Integer(80));
		is12.items.add(new Integer(70));
		tran2.itemSets.add(is12);
		ItemSet is22 = new ItemSet();
		is22.items.add(new Integer(50));
		tran2.itemSets.add(is22);
		ItemSet is32 = new ItemSet();
		is32.items.add(new Integer(90));
		is32.items.add(new Integer(132));
		tran2.itemSets.add(is32);
		System.out.println(tran1.specialEqualTo(tran2, 1, 5));
	}
}
