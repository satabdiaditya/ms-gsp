import java.util.ArrayList;
import java.util.LinkedList;


public class MSCandidateGen {	
	public FrequentSequence candidateGen(FrequentSequence F) {
		FrequentSequence C = new FrequentSequence();
		ArrayList<FrequentSequence> ss = partition(F);
		for (int i=0; i<=2; i++) {
			for (Transaction tran : ss.get(i).sequences) {
				FrequentSequence pair = findPair(tran, i);
				C.addFrequentSequence(joinSequences(tran, pair, i));
			}
		}
		return prune(C);
	}
	
	/*
	 * @Func: generate the 2-sequences candidate sequential pattern
	 * @Param: L, the set of items' id obtained from init-pass
	 * @return: 2-sequences candidate sequential pattern
	 */
	public FrequentSequence level2CandidateGen(LinkedList<Integer> L) {
		FrequentSequence C2 = new FrequentSequence();
		//find the item who meets its MIS, then compare the following items with this item's MIS
		for (int i=0; i < L.size(); i++) {	
			if (MSGSP.SUP.get(L.get(i))*1.0/MSGSP.N < MSGSP.MS.get(L.get(i)).floatValue())
				continue;
			else {
				for (int j=i+1; j < L.size(); j++) {
					if (MSGSP.SUP.get(L.get(j))*1.0/MSGSP.N >= MSGSP.MS.get(L.get(i)).floatValue()) {
						/*
						 * to add the transaction containing two item a and b into the 
						 * frequent sequence, we need to add two sequences <{a}, {b}> and 
						 * <{a, b}>
						 */
						//TODO There maybe some problem with the following codes
						//Whether the order within a sequence has been maintained??
						ItemSet is = new ItemSet();
						is.items.add(L.get(i));
						is.items.add(L.get(j));
						Transaction tran = new Transaction();
						tran.itemSets.add(is);
						//TODO deal with the sdc
						C2.addTransaction(tran);	//tran is <{a, b}>
						ItemSet is1 = new ItemSet();
						is1.items.add(L.get(i));
						ItemSet is2 = new ItemSet();
						is2.items.add(L.get(j));
						Transaction tran2 = new Transaction();
						tran2.itemSets.add(is1);
						tran2.itemSets.add(is2);
						//TODO deal with the sdc
						C2.addTransaction(tran2);	//tran2 is <{a}, {b}>
					}
				}
			}
		}
		return C2;
	}
	
	/*
	 * This method partitions the k-1 length frequent sequence F into three
	 * subset s1, s2, and s3.
	 * s1 is those whose first item has the minimum MIS.
	 * s2 is those whose last item has the minimum MIS.
	 * s3 is the rest.
	 */
	private ArrayList<FrequentSequence> partition(FrequentSequence F) {
		ArrayList<FrequentSequence> ss = new ArrayList<FrequentSequence>();
		//TODO
		return ss;
	}
	
	/*
	 * This method finds the corresponding join sequences for s1, s2, and s3.
	 * Parameter tran is the transaction in s1, s2, or s3, which will be found 
	 * a pair for.
	 * Parameter i indicates which of the three, s1, s2, and s3, does tran belong to. 
	 */
	private FrequentSequence findPair(Transaction tran, int i) {
		FrequentSequence pair = new FrequentSequence();
		//TODO
		return pair;
	}
	
	/*
	 * This method joins s1, s2, or s3 with their corresponding pairs to 
	 * form the k-length frequent sequence candidates.
	 * Parameter i indicates for which of the three, s1, s2, and s3, are we
	 * going to join their pairs.
	 * Parameter fs is s1, s2, or s3.
	 */
	private FrequentSequence joinSequences(Transaction tran, FrequentSequence fs, int i) {
		FrequentSequence result = new FrequentSequence();
		//TODO
		return result;
	}
	
	private FrequentSequence prune(FrequentSequence fs) {
		//TODO
		return fs;
	}
}
