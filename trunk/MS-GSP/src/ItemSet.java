import java.util.ArrayList;

/*
 * This is the class used to abstract itemset.
 * Every itemset will contains several items indicated by their itemIDs.
 */
public class ItemSet {
	public ArrayList<Integer> items;
	ItemSet(){
		items=new ArrayList<Integer>();
	}
}
