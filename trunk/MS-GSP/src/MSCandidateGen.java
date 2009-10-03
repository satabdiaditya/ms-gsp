import java.util.ArrayList;


public class MSCandidateGen {	
	public FrequentSequence candidateGen(FrequentSequence F) {
		FrequentSequence C = new FrequentSequence();
		for (Transaction tr1 : F.sequences){
			for (Transaction tr2 : F.sequences) {
				Transaction s1 = tr1.copy();
				Transaction s2 = tr2.copy();
				int condition = checkCondition(s1, s2);
				if (condition != 0) {
					Transaction candidate = joinSequences(s1, s2, condition);
					if (candidate.hashCode() != 0)
						C.addTransaction(candidate);
				}
			}
		}
		return prune(C,F); //F means F(k-1)?
	}
	
	/*
	 * @Func: generate the 2-sequences candidate sequential pattern
	 * @Param: L, the set of items' id obtained from init-pass
	 * @return: 2-sequences candidate sequential pattern
	 */
	public FrequentSequence level2CandidateGen(ArrayList<Integer> L) {
		FrequentSequence C2 = new FrequentSequence();
		//find the item who meets its MIS, then compare the following items with this item's MIS
		for (int i=0; i < L.size(); i++) {	
			if (MSGSP.SUP.get(L.get(i))*1.0/MSGSP.N < MSGSP.MS.get(L.get(i)).floatValue())
				continue;
			else {
				for (int j=i; j < L.size(); j++) {
					if (MSGSP.SUP.get(L.get(j))*1.0/MSGSP.N >= MSGSP.MS.get(L.get(i)).floatValue()) {
						/*
						 * to add the transaction containing two item a and b into the 
						 * frequent sequence, we need to add two sequences <{a}, {b}> and 
						 * <{a, b}>
						 */
						//Whether the order within a sequence has been maintained??
						if (Math.abs(MSGSP.SUP.get(L.get(i)).intValue() - MSGSP.SUP.get(L.get(j)).intValue()) <= MSGSP.SDC * MSGSP.N)
						{
							ItemSet is = new ItemSet();
							if(L.get(i) <= L.get(j)) {
								is.items.add(L.get(i));
								is.items.add(L.get(j));
							}
							else {
								is.items.add(L.get(j));
								is.items.add(L.get(i));
							}
							if (! L.get(i).equals(L.get(j))) {
								Transaction tran = new Transaction();
								tran.itemSets.add(is);					
								C2.addTransaction(tran);	//tran is <{a, b}>
							}
							ItemSet is1 = new ItemSet();
							is1.items.add(L.get(i));
							ItemSet is2 = new ItemSet();
							is2.items.add(L.get(j));
							Transaction tran2 = new Transaction();
							tran2.itemSets.add(is1);
							tran2.itemSets.add(is2);
							C2.addTransaction(tran2);	//tran2 is <{a}, {b}>
							ItemSet is3 = new ItemSet();
							is3.items.add(L.get(j));
							ItemSet is4 = new ItemSet();
							is4.items.add(L.get(i));
							if (! L.get(i).equals(L.get(j))) {
								Transaction tran3 = new Transaction();
								tran3.itemSets.add(is3);
								tran3.itemSets.add(is4);
								C2.addTransaction(tran3);	//tran3 is <{b}, {a}>
							}
						}
					}
				}
			}
		}

		return C2;
	}
	
	/*
	 * 
	 */
	private int checkCondition(Transaction s1, Transaction s2) {
		int result = 0;
		int i = partition(s1);
		if(testPair(s1, s2, i))
			result = i;
		else if(testPair(s1, s2, 3))
			result = 3;
		return result;
	}
	/*
	 * This method partitions the k-1 length frequent sequence F into three
	 * subset s1, s2, and s3.
	 * s1 is those whose first item has the minimum MIS.
	 * s2 is those whose last item has the minimum MIS.
	 * s3 is the rest.
	 */
	private int partition(Transaction tran) {
		int result = 0;
			Integer firstItem = tran.getFirstItem();
			Integer lastItem = tran.getLastItem();
			if (tran.isSmallest(firstItem, 0))
				result = 1;
			else if (tran.isSmallest(lastItem, 1))
				result = 2;
		return result;
	}
	
	/*
	 * This method finds the corresponding join sequences for s1, s2, and s3.
	 * Parameter tran is the transaction in s1, s2, or s3, which will be found 
	 * a pair for.
	 * Parameter i indicates which of the three, s1, s2, and s3, does tran belong to. 
	 */
	private boolean testPair(Transaction tran, Transaction tr, int i) {
		boolean result = false;
		switch (i) {
		case 1:
			if (tran.specialEqualTo(tr, 1, tr.getItems().size()-1) &&
					MSGSP.MS.get(tran.getFirstItem()).doubleValue() < MSGSP.MS.get(tr.getLastItem()).doubleValue() &&
					Math.abs(MSGSP.SUP.get(tran.getItems().get(1)).intValue() - MSGSP.SUP.get(tr.getLastItem()).intValue()) <= MSGSP.SDC*MSGSP.N)
				result = true;
			break;
		case 2:
			if (tran.specialEqualTo(tr, tran.getItems().size()-2, 0) &&
					MSGSP.MS.get(tr.getFirstItem()).doubleValue() > MSGSP.MS.get(tran.getLastItem()).doubleValue() &&
					Math.abs(MSGSP.SUP.get(tran.getItems().get(tran.getItems().size()-2)).intValue() - MSGSP.SUP.get(tr.getFirstItem()).intValue()) <= MSGSP.SDC*MSGSP.N)
				result = true;
			break;
		case 3:
			if (tran.specialEqualTo(tr, 0, tr.getItems().size()-1) &&
					Math.abs(MSGSP.SUP.get(tran.getFirstItem()).intValue() - MSGSP.SUP.get(tr.getLastItem()).intValue()) <= MSGSP.SDC*MSGSP.N)
				result = true;
			break;
		}
		return result;
	}
	
	/*
	 * This method joins s1, s2, or s3 with their corresponding pairs to 
	 * form the k-length frequent sequence candidates.
	 * Parameter i indicates for which of the three, s1, s2, and s3, are we
	 * going to join their pairs.
	 * Parameter fs is s1, s2, or s3.
	 */
	private Transaction joinSequences(Transaction tran, Transaction tr, int i) {
		Transaction candidate = new Transaction();
		switch(i) {
		case 1:
				Transaction trans = tran.copy();
				if (tr.itemSets.get(tr.itemSets.size()-1).items.size() == 1) {
					candidate = new Transaction();
					candidate.itemSets.addAll(trans.itemSets);
					candidate.itemSets.add(tr.itemSets.get(tr.itemSets.size()-1));
					if (trans.itemSets.size()==2 && trans.getItems().size()==2 && MSGSP.MS.get(trans.getLastItem()).doubleValue() < MSGSP.MS.get(tr.getLastItem())) {
						candidate = new Transaction();
						candidate.itemSets.addAll(trans.itemSets);
						candidate.itemSets.get(candidate.itemSets.size()-1).items.add(tr.getLastItem());
						
					}
				}
				else if (trans.getItems().size() > 2 ||(trans.itemSets.size()==1 && trans.getItems().size()==2 && MSGSP.MS.get(trans.getLastItem()).doubleValue() < MSGSP.MS.get(tr.getLastItem()))) {
					candidate = new Transaction();
					candidate.itemSets.addAll(trans.itemSets);
					candidate.itemSets.get(candidate.itemSets.size()-1).items.add(tr.getLastItem());
					
				}
			
			break;
		case 2:
				trans = tran.copy();
				if (tr.reverse().itemSets.get(tr.reverse().itemSets.size()-1).items.size() == 1) {
					candidate = new Transaction();
					candidate.itemSets.addAll(trans.reverse().itemSets);
					candidate.itemSets.add(tr.reverse().itemSets.get(tr.reverse().itemSets.size()-1));
					
					if (trans.reverse().itemSets.size()==2 && trans.reverse().getItems().size()==2 && MSGSP.MS.get(trans.reverse().getLastItem()).doubleValue() < MSGSP.MS.get(tr.reverse().getLastItem())) {
						candidate = new Transaction();
						candidate.itemSets.addAll(trans.reverse().itemSets);
						candidate.itemSets.get(candidate.itemSets.size()-1).items.add(tr.reverse().getLastItem());
						
					}
				}
				else if (trans.reverse().getItems().size() > 2 ||(trans.reverse().itemSets.size()==1 && trans.reverse().getItems().size()==2 && MSGSP.MS.get(trans.reverse().getLastItem()).doubleValue() < MSGSP.MS.get(tr.reverse().getLastItem()))) {
					candidate = new Transaction();
					candidate.itemSets.addAll(trans.reverse().itemSets);
					candidate.itemSets.get(candidate.itemSets.size()-1).items.add(tr.reverse().getLastItem());
					
				}
				candidate = candidate.reverse();
			break;
		case 3:
				trans = tran.copy();
				if (tr.itemSets.get(tr.itemSets.size()-1).items.size() == 1) {
					candidate = new Transaction();
					candidate.itemSets.addAll(trans.itemSets);
					candidate.itemSets.add(tr.itemSets.get(tr.itemSets.size()-1));
					
				}
				else {
					candidate = new Transaction();
					candidate.itemSets.addAll(trans.itemSets);
					candidate.itemSets.get(candidate.itemSets.size()-1).items.add(tr.getLastItem());
					
				}
			
			break;
		}
		return candidate;
	}
	
	/*
	 * The prune step in MScandidate-gen-SPM function
	 */
	private FrequentSequence prune(FrequentSequence fs, FrequentSequence fk_1) {
		Integer minItem; // Item that has the minimum MIS in a sequence
		FrequentSequence fsPruned=new FrequentSequence(); // Frequent sequence set after prune step
		for(Transaction t: fs.sequences){
			minItem=new Integer(t.minMISItem());
			boolean frequent=true; // indicator of if t is frequent or not
			for(int i=0;i<t.itemSets.size();i++){ // walk through the itemsets of a sequence
				if(t.itemSets.get(i).items.contains(minItem)){ // if the itemset contains the item with min MIS
					Transaction copy=t.copy();
					for(int k=0;k<t.itemSets.size();k++){
						if(!frequent) 
							break;
						for(Integer item: t.itemSets.get(k).items){
							if(!(k==i&&item==minItem)){ //except the minItem
								copy.itemSets.get(k).items.remove(item); // generate a k-1 subsequence 
								if(!frequent(copy, fk_1)){//if this subsequence is not frequent, then the sequence is not frequent either.
									frequent=false;
									break;
								}
							}
						}
					}
					if(!frequent)// if a sequence is already infrequent the no need to continue check the remaining itemsets
						break;
				}
			}
			if(frequent) //if this sequence is frequent, add to the result
				fsPruned.sequences.add(t);
		}
		return fsPruned;
	}
	
	
	/*
	 * This method is used to determine if a k-1 length subsequence is frequent
	 */
	private boolean frequent(Transaction t, FrequentSequence fk_1){
		boolean	frequent=false; 
		for(Transaction freq: fk_1.sequences)
			if(t.containedIn(freq)){
				frequent=true; //there is one sequence in F(k-1) includes the subsequence
				break;
			}
		return frequent;
	}
	
}
