import java.util.ArrayList;

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
	 * and the index2th item of tran, and compares the generated subsequences 
	 * to test whether they are the same.
	 */
	public boolean specialEqualTo(Transaction tran, int index1, int index2) {
		boolean result = false;
		int s1 = this.itemSets.size();
		int s2 = tran.itemSets.size();
		int l1 = this.getItems().size();
		int l2 = tran.getItems().size();
		Transaction current = new Transaction();
		Transaction compare = new Transaction();
		current = this.copy();
		compare = tran.copy();
		int i=0, j=0, index;
		for (index=0; index<s1; index++) {
			ItemSet is = current.itemSets.get(index);
			j = i;
			i += is.items.size();
			if (i > index1)
				break;
		}
		current.itemSets.get(index).items.remove(index1 - j);
		if (current.itemSets.get(index).items.size() == 0)
			current.itemSets.remove(index);
		i = 0;
		j = 0;
		for (index=0; index<s2; index++) {
			ItemSet is = compare.itemSets.get(index);
			j = i;
			i += is.items.size();
			if (i > index2)
				break;
		}
		compare.itemSets.get(index).items.remove(index2 - j);
		if (compare.itemSets.get(index).items.size() == 0)
			compare.itemSets.remove(index);
		if (current.containedIn(compare) && s1 == s2 && l1 == l2)
			result = true;
		return result;
	}
	
	private Transaction copy() {
		Transaction tran = new Transaction();
		for (int i=0; i<this.itemSets.size(); i++) {
			ItemSet is = new ItemSet();
			is.items.addAll(this.itemSets.get(i).items);
			tran.itemSets.add(is);
		}
		return tran;
	}
	
	/*
	 * This method is used to check if this transaction is contained in the transaction indicated by an para.
	 */
	public boolean containedIn(Transaction tran){
		boolean result = true;
		int m = this.itemSets.size();
		int n = tran.itemSets.size();
		int i=0, j=0;
		for(i=0; i<m; i++) {
			ItemSet is = this.itemSets.get(i);
			do {
				if (m-i > n-j) {	//The number of rest elements in current sequence
									//is greater than the number of rest elements in tran
					result = false;
					break;
				}
				if (is.isSubset(tran.itemSets.get(j))) {	//find an element in tran which is the super set of current element in current sequence
					j++;
					break;
				}
				j++;
			} while(j<n);
			if (result == false) {
				break;
			}
			if (i==m-1 && j==n) {
				result = is.isSubset(tran.itemSets.get(j-1));
			}
		}
		return result;
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
	 *  This method is used to create and return an transaction which is an 
	 *  reverse of this transaction (also have to reverse the element/item set) 
	 */
	public Transaction reverse(){
		Transaction rev=new Transaction();
		ItemSet revIS=new ItemSet();
		int i,j;
		for(i=this.itemSets.size()-1;i>=0;i--){
			ItemSet is=this.itemSets.get(i);
			for(j=is.items.size()-1;j>=0;j--)
				revIS.items.add(is.items.get(j));
			rev.itemSets.add(revIS);
			revIS.items.clear();
		}
		return rev;
	}
	
	
	
	/*
	 * This method is used to print a transaction in a line
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
