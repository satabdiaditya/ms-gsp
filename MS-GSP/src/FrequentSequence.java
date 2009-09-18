import java.util.HashSet;

/*
 * This is the class used to abstract L, Fk, and Ck in MS-GSP
 * All of them are sets of transactions, so simply define one structure
 * to represent all of them. 
 */
public class FrequentSequence {
	public HashSet<Transaction> sequences;
	FrequentSequence(){
		sequences=new HashSet<Transaction>();
	}
	
	public void addTransaction(Transaction tran) {
		sequences.add(tran);
	}
}
