import java.util.HashSet;

/*
 * This is the class used to abstract itemset.
 * Every itemset will contains several items indicated by their itemIDs.
 */
public class ItemSet {
	public HashSet<Integer> items;
	ItemSet(){
		items=new HashSet<Integer>();
	}
}
