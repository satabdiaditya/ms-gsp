import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/*
 * This is the class used to read input from files
 */

public class ReadFileInput {
	
	/*
	 * @Func: Read MIS vector from input file
	 * @Param: File name
	 * @Return: A HashMap whose key is the itemID(Integer), value is corresponding minimum support(Float) 
	 */
	public static HashMap<Integer,Float> readParameters(String filename){
		HashMap<Integer,Float> mis=new HashMap<Integer,Float>();
		try{
			Scanner f=new Scanner(new File(filename));
			while(true){
				String record=f.nextLine(); //read each line in parameter-file.txt
				if(f.hasNextLine()){
					// extract the itemID
					Integer itemID=Integer.valueOf(record.substring(record.indexOf('(')+1, record.indexOf(')')));
					// extract the minimum item support
					Float itemMIS=Float.valueOf(record.substring(record.indexOf('=')+2)); // There must be a space after '=' to make this line work
					mis.put(itemID,itemMIS);
				}else{ // last line, read SDC
					MSGSP.SDC=Float.valueOf(record.substring(record.indexOf('=')+2));
					break;
				}
				
			}
			return mis;
		}catch(IOException e){
			e.printStackTrace();
		}
		return null;
	}
	
	/*
	 * @Func: Read MIS vector from input file
	 * @Param: File name
	 * @Return: A HashMap whose key is the itemID(Integer), value is corresponding minimum support(Float) 
	 */
	public static ArrayList<Transaction> readData(String filename){
		ArrayList<Transaction> trans=new ArrayList<Transaction>();
		try{
			Scanner f=new Scanner(new File(filename));
			while(f.hasNextLine()){
				String record=f.nextLine(); // read each line in data file
				Transaction transaction=new Transaction(); // create a new transaction 
				//index for next open parenthesis in current line
				int idxOpenParen=record.indexOf('{'); 
				//index for next closed parenthesis in current line
				int idxClosedParen=record.indexOf('}');
				
				while(idxOpenParen<record.length()-1){
					ItemSet is=new ItemSet(); // create an new itemset for this transaction 
					//index for next comma between current open parentheses
					int idxComma=record.indexOf(',',idxOpenParen); 
					//index indicating the starting position of next number within current parentheses
					int idxNumStart=idxOpenParen+1;
					//index indicating the ending position of next number within current parentheses. The character on this position is either a '}' or ','.
					int idxNumEnd=min(idxClosedParen,idxComma);
										
					while(idxNumEnd<=idxClosedParen){
						is.items.add(Integer.valueOf(record.substring(idxNumStart,idxNumEnd))); // add an item to the itemset
						if(idxNumEnd==idxClosedParen) //reach the end of the itemset
							break;
						// locate beginning and ending positions for next item in the current itemset
						idxNumStart=idxComma+2;
						idxComma=record.indexOf(',',idxNumStart); 
						idxNumEnd=min(idxClosedParen,idxComma);
					}
																
					// locate beginning and ending positions for next itemSet
					idxOpenParen=idxClosedParen+1;
					idxClosedParen=record.indexOf('}',idxOpenParen);
					transaction.itemSets.add(is); // add an itemSet to the current transaction
				}
				
				trans.add(transaction);  // add an transaction to the database
			}
			return trans;
		}catch(IOException e){
			e.printStackTrace();
		}
		return null;
	}
	
	public static int min(int idxClosedParen, int idxComma){
		if(idxComma<0)
			return idxClosedParen;
		else
			return idxClosedParen<idxComma?idxClosedParen:idxComma;
	}
	
	 	

}
