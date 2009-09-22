/*
 * This class read from the data.txt file, to obtain the sequences in this file.
 * The method readNextTran will read one sequence(transaction) from the file, 
 * when this method is called again, it will return the next sequence.
 */
public class DataReader {
	private static int i = 0;
	public Transaction readNextTran() {
		Transaction tran = null;
		if (DataReader.i < 6) {
			tran = new Transaction();
			switch (i) {
			case 0:
				ItemSet is1 = new ItemSet();
				is1.items.add(new Integer(20));
				is1.items.add(new Integer(30));
				is1.items.add(new Integer(80));
				tran.itemSets.add(is1);
				ItemSet is2 = new ItemSet();
				is2.items.add(new Integer(70));
				tran.itemSets.add(is2);
				ItemSet is3 = new ItemSet();
				is3.items.add(new Integer(50));
				is3.items.add(new Integer(70));
				tran.itemSets.add(is3);
				break;
			case 1:
				ItemSet is4 = new ItemSet();
				is4.items.add(new Integer(20));
				is4.items.add(new Integer(30));
				tran.itemSets.add(is4);
				ItemSet is5 = new ItemSet();
				is5.items.add(new Integer(30));
				is5.items.add(new Integer(70));
				is5.items.add(new Integer(80));
				tran.itemSets.add(is5);
				break;
			case 2:
				ItemSet is6 = new ItemSet();
				is6.items.add(new Integer(20));
				is6.items.add(new Integer(40));
				tran.itemSets.add(is6);
				ItemSet is7 = new ItemSet();
				is7.items.add(new Integer(70));
				tran.itemSets.add(is7);
				ItemSet is8 = new ItemSet();
				is8.items.add(new Integer(30));
				is8.items.add(new Integer(70));
				is8.items.add(new Integer(20));
				tran.itemSets.add(is8);
				break;
			case 3:
				ItemSet is9 = new ItemSet();
				is9.items.add(new Integer(10));
				is9.items.add(new Integer(30));
				tran.itemSets.add(is9);
				ItemSet is10 = new ItemSet();
				is10.items.add(new Integer(40));
				tran.itemSets.add(is10);
				break;
			case 4:
				ItemSet is11 = new ItemSet();
				is11.items.add(new Integer(10));
				is11.items.add(new Integer(40));
				is11.items.add(new Integer(90));
				tran.itemSets.add(is11);
				ItemSet is12 = new ItemSet();
				is12.items.add(new Integer(40));
				is12.items.add(new Integer(90));
				tran.itemSets.add(is12);
				break;
			case 5:
				ItemSet is13 = new ItemSet();
				is13.items.add(new Integer(10));
				is13.items.add(new Integer(40));
				is13.items.add(new Integer(70));
				tran.itemSets.add(is13);
				ItemSet is14 = new ItemSet();
				is14.items.add(new Integer(40));
				is14.items.add(new Integer(90));
				tran.itemSets.add(is14);
				break;
			}
			DataReader.i++;
		}
		return tran;
	}

}
