import java.util.ArrayList;

import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

/*
 * This is the class used to abstract transaction.
 * Every transaction will contains a list of itemsets.
 */
public class Transaction {
	public ArrayList<ItemSet> itemSets;
	public int count;  // count the occurrence of the transaction
	Transaction(){
		itemSets=new ArrayList<ItemSet>();
		count=0;
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
	
	/*
	 * This method is used to check if this transaction is contained in the transaction indicated by an para.
	 */
	public boolean containedIn(Transaction tran){
		
		return false;
	}
    
	/*
	 * This method is used to return the MIS of the item that has the lowest MIS value in the transaction
	 */
	public Float minMIS(){
		ArrayList<Integer> items = this.getItems();
	    Float ret=MSGSP.MS.get(items.get(0));
	    float mis;
	    for(int i=1;i<items.size();i++){
	    	if((mis=MSGSP.MS.get(items.get(i)))<ret)
	    		ret=mis;
	    }
	    return ret;
	        	
		
	}
	
	
	
	
	/*
	 *  This method is used to print a transaction in a line
	 */
	public void print(){
		int i;
		System.out.print("< ");
		for(ItemSet is: itemSets){
			System.out.print("{");
			for(i=0;i<is.items.size()-1;i++){  // print an element except the last item
				System.out.print(is.items.get(i)+",");
			}
			System.out.print(is.items.get(i)+"}"); //print the last item in an element
		}
		System.out.println(" >");
			
	}
}
