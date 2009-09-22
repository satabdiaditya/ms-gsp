import java.util.ArrayList;
import java.util.LinkedList;


public class MSCandidateGen {	
	public FrequentSequence candidateGen(FrequentSequence F) {
		FrequentSequence C = new FrequentSequence();
		ArrayList<FrequentSequence> ss = partition(F);
		for (int i=0; i<=2; i++) {
			for (Transaction tran : ss.get(i).sequences) {
				FrequentSequence pair = findPair(F, tran, i);
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
						if (Math.abs(MSGSP.MS.get(i).doubleValue() - MSGSP.MS.get(j).doubleValue()) <= MSGSP.SDC);
						{
							ItemSet is = new ItemSet();
							is.items.add(L.get(i));
							is.items.add(L.get(j));
							Transaction tran = new Transaction();
							tran.itemSets.add(is);					
							C2.addTransaction(tran);	//tran is <{a, b}>
							ItemSet is1 = new ItemSet();
							is1.items.add(L.get(i));
							ItemSet is2 = new ItemSet();
							is2.items.add(L.get(j));
							Transaction tran2 = new Transaction();
							tran2.itemSets.add(is1);
							tran2.itemSets.add(is2);
							C2.addTransaction(tran2);	//tran2 is <{a}, {b}>
						}
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
		FrequentSequence s1 = new FrequentSequence();
		FrequentSequence s2 = new FrequentSequence();
		FrequentSequence s3 = new FrequentSequence();
		for (Transaction tran : F.sequences) {
			Integer firstItem = tran.getFirstItem();
			Integer lastItem = tran.getLastItem();
			if (tran.isSmallest(firstItem, 0))
				s1.addTransaction(tran);
			else if (tran.isSmallest(lastItem, 1))
				s2.addTransaction(tran);
			else s3.addTransaction(tran);
		}
		ss.add(s1);
		ss.add(s2);
		ss.add(s3);
		return ss;
	}
	
	/*
	 * This method finds the corresponding join sequences for s1, s2, and s3.
	 * Parameter tran is the transaction in s1, s2, or s3, which will be found 
	 * a pair for.
	 * Parameter i indicates which of the three, s1, s2, and s3, does tran belong to. 
	 */
	private FrequentSequence findPair(FrequentSequence F, Transaction tran, int i) {
		FrequentSequence pair = new FrequentSequence(F.sequences);
		switch (i) {
		case 0:
			for (Transaction tr : pair.sequences) {
				if (tran.specialEqualTo(tr, 1, tr.getItems().size()-1) &&
						MSGSP.MS.get(tran.getFirstItem()).doubleValue() < MSGSP.MS.get(tr.getLastItem()).doubleValue());
				else
					pair.sequences.remove(tr);
			}
			break;
		case 1:
			for (Transaction tr : pair.sequences) {
				if (tran.specialEqualTo(tr, 0, tr.getItems().size()-2) &&
						MSGSP.MS.get(tran.getFirstItem()).doubleValue() > MSGSP.MS.get(tr.getLastItem()).doubleValue());
				else
					pair.sequences.remove(tr);
			}
			break;
		case 2:
			for (Transaction tr : pair.sequences) {
				if (tran.specialEqualTo(tr, 0, tr.getItems().size()-1));
				else
					pair.sequences.remove(tr);
			}
			break;
		}
		return pair;
	}
	
	/*
	 * This method joins s1, s2, or s3 with their corresponding pairs to 
	 * form the k-length frequent sequence candidates.
	 * Parameter i indicates for which of the three, s1, s2, and s3, are we
	 * going to join their pairs.
	 * Parameter fs is s1, s2, or s3.
	 */
	private FrequentSequence joinSequences(Transaction tran, FrequentSequence pair, int i) {
		FrequentSequence result = new FrequentSequence();
		Transaction candidate = new Transaction();
		switch(i) {
		case 0:
			for (Transaction tr : pair.sequences) {
				if (tr.itemSets.get(tr.itemSets.size()-1).items.size() == 1) {
					candidate = new Transaction();
					candidate.itemSets.addAll(tran.itemSets);
					candidate.itemSets.add(tr.itemSets.get(tr.itemSets.size()-1));
					result.addTransaction(candidate);
					if (tran.itemSets.size()==2 && tran.getItems().size()==2 && MSGSP.MS.get(tran.getLastItem()).doubleValue() < MSGSP.MS.get(tr.getLastItem())) {
						candidate = new Transaction();
						candidate.itemSets.addAll(tran.itemSets);
						candidate.itemSets.get(candidate.itemSets.size()-1).items.add(tr.getLastItem());
						result.addTransaction(candidate);
					}
				}
				else if (tran.getItems().size() > 2 ||(tran.itemSets.size()==1 && tran.getItems().size()==2 && MSGSP.MS.get(tran.getLastItem()).doubleValue() < MSGSP.MS.get(tr.getLastItem()))) {
					candidate = new Transaction();
					candidate.itemSets.addAll(tran.itemSets);
					candidate.itemSets.get(candidate.itemSets.size()-1).items.add(tr.getLastItem());
					result.addTransaction(candidate);
				}
			}
			break;
		case 1:
			for (Transaction tr : pair.sequences) {
				if (tr.itemSets.get(0).items.size() == 1) {
					candidate = new Transaction();
					candidate.itemSets.add(tr.itemSets.get(0));
					candidate.itemSets.addAll(tran.itemSets);
					result.addTransaction(candidate);
					/*
					if (tran.itemSets.size()==2 && tran.getItems().size()==2 && MSGSP.MS.get(tran.getLastItem()).doubleValue() < MSGSP.MS.get(tr.getLastItem())) {
						candidate = new Transaction();
						candidate.itemSets.addAll(tran.itemSets);
						candidate.itemSets.get(0).items.add(0, tr.getFirstItem());
					}
					//What is judgement in the if statement???
					*/
				}
				/*
				else if (tran.getItems().size() > 2 || (tran.itemSets.size()==1 && tran.getItems().size()==2 && MSGSP.MS.get(tran.getLastItem()).doubleValue() < MSGSP.MS.get(tr.getLastItem()))) {
					candidate = new Transaction();
					candidate.itemSets.addAll(tran.itemSets);
					candidate.itemSets.get(0).items.add(0, tr.getFirstItem());
				}
				//What is the judegement in the else if statement???
				*/
			}
			break;
		case 2:
			for (Transaction tr : pair.sequences) {
				if (tr.itemSets.get(tr.itemSets.size()-1).items.size() == 1) {
					candidate = new Transaction();
					candidate.itemSets.addAll(tran.itemSets);
					candidate.itemSets.add(tr.itemSets.get(tr.itemSets.size()-1));
					result.addTransaction(candidate);
				}
				else {
					candidate = new Transaction();
					candidate.itemSets.addAll(tran.itemSets);
					candidate.itemSets.get(candidate.itemSets.size()-1).items.add(tr.getLastItem());
					result.addTransaction(candidate);
				}
			}
			break;
		}
		return result;
	}
	
	/*
	 * The prune step in MScandidate-gen-SPM function
	 */
	private FrequentSequence prune(FrequentSequence fs) {
		//TODO
		return fs;
	}
}
