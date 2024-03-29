import java.util.HashSet;

/*
 * This is the class used to abstract L, Fk, and Ck in MS-GSP
 * All of them are sets of transactions, so simply define one structure
 * to represent all of them. 
 */
public class FrequentSequence {
	public HashSet<Transaction> sequences = new HashSet<Transaction>();
	FrequentSequence(){
		sequences=new HashSet<Transaction>();
	}
	
	FrequentSequence(HashSet<Transaction> sequence) {
		sequences.addAll(sequence);
	}
	
	public void addTransaction(Transaction tran) {
		sequences.add(tran);
	}
	
	public void addFrequentSequence(FrequentSequence fs) {
		sequences.addAll(fs.sequences);
	}
}
