import java.util.HashSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.*;

public class MScandidate{

static HashMap<Integer,Float> MS;
static ArrayList<Transaction> S;
static String paraFileName="parameter-file.txt";
static String dataFileName="data.txt";
static Vector aftercondition1=new Vector();  //vector containig candidates after executing condition 1 and 2



 public static LinkedList<Integer> sort(HashMap<Integer,Float> MS){
                LinkedList<Integer> M=new LinkedList<Integer>();
                for(Integer itemID: MS.keySet()){
                        if(M.size()==0)
                                M.add(itemID);
                        else{
                                int i=0;
                                while(i<M.size()&&MS.get(M.get(i))<MS.get(itemID))
                                        i++;
                                M.add(i,itemID);
                        }
            }
                return M;
        }

//Function return true if the MIS value of the integer is less than all the other sequence
//@param: takes the integer to be tested; the Hashmap containing the MIS values and Transactions S.
public static ArrayList<Transaction> compareall(Integer integer1){

Float tocompare=MS.get(integer1);
// MS=ReadFileInput.readParameters(paraFileName);
// S=ReadFileInput.readData(dataFileName);
 ArrayList<Transaction> second= new ArrayList<Transaction>(); //this array list will contain the possible transaction that have to be considered

int count,i=0;
boolean result;	//??
//for(Transaction trans: S){
count=0;

        for(ItemSet is: S.get(i++).itemSets){
                            System.out.println(); 
                            count=0;
                              for(Integer integer: is.items){
                                   // System.out.println(tocompare+"   "+MS.get(integer));
                                    if(tocompare<=MS.get(integer) && count!=100){
                                    //System.out.println(tocompare+ " hahah "+ MS.get(integer));
                                    result=true;
                                    second.add(S.get(i));
                                    }else{result=false;count=100;}
                                    
                                      }
                          if(result=true){
                          //System.out.println("Adding"+S.get(i));
                          //second=S;
                          break;
                          }
                       }






//}



return second;
}


/*This function takes the two candidatese and checks for the first conditions
 * condition1: Sequence s1 joins with s2, if the subsequences obtained by dropping 
 * the second item of s1 and the last item of s2 are the same
 *
 * @param: two sequences as strings from conidtioncheck function
 *  If condition is met, a string is returned with the concatenated string as a result
 * */

public static String condition1(String a, String b){
String first=a;
String second=b;
String temp1,temp2;
String seconditem="";
String lastitem="";
int count=0,count2=0;
//getting the second item out of the picture
StringTokenizer st = new StringTokenizer(first,">");
     while (st.hasMoreTokens()) {
          temp1 = st.nextToken();
          //gettiing rid of the second item in s1
          if(count!=1){seconditem=seconditem+temp1;}
  //        System.out.println("Second:"+seconditem.substring(1,seconditem.length()-4).trim());
          count++;
           }


//System.out.println("IN the second string"+second);
StringTokenizer st1 = new StringTokenizer(second,">");
     while (st1.hasMoreTokens()) {
          temp2 = st1.nextToken();
          //this will add every thing in the sequences except for the last elemenet
          if(count2==st1.countTokens()-2){lastitem=lastitem+temp2;}
//          System.out.println("Last: "+lastitem.substring(1,lastitem.length()-4).trim());
          count2++; 
           }

//If the two string seconditem and lastitem are equal then they will be joined
//

if(seconditem.substring(1,seconditem.length()-4).trim().contentEquals(lastitem.substring(1,lastitem.length()-4).trim())){
//System.out.println("Joining: "+ first + "and " + second );

}

String result=first.concat(second);
//System.out.println(result);



return result;

}


/*The condition2 function checks the following conidition
 * Sequence s1 joing sequence s2 if :
 * the MIS value of last item of s2 is GREATER than that of the first item of s1
 *  
 *  @param: two sequences as strings from conidtioncheck function
 *   *  If condition is met, a string is returned with the concatenated string as a result
 *
 * */




public static String condition2(String first,String second){

String temp1,temp;
String lastitem="";   //the last item of s2
String firstitem="";
String result=null;
//System.out.println(second.charAt(second.length()-6));
//extracting the last item of sequnece s2 and finding its MIS value

//System.out.println(second);

//getting the last item
StringTokenizer st1 = new StringTokenizer(second,",");
     while (st1.hasMoreTokens()) {
          temp1 = st1.nextToken();
          //System.out.println(temp1);
  //        System.out.println(temp1+":  "+isNum(temp1));
          if(isNum(temp1)==true){lastitem=temp1;}
          }

//System.out.println("This is the last item: "+ lastitem);


//System.out.println(first);


StringTokenizer st = new StringTokenizer(first,",");
     while (st.hasMoreTokens()) {
          temp = st.nextToken();
	  if(isNum(temp)==true){firstitem=temp;break;}
          
}
//System.out.println("This is the first item"+firstitem);

int lastitem_integer=Integer.parseInt(lastitem.trim());
int firstitem_integer=Integer.parseInt(firstitem.trim());

//System.out.println(MS.get(lastitem_integer)+"   "+MS.get(firstitem_integer));
if(MS.get(lastitem_integer)>MS.get(firstitem_integer)){
result=first+second;
}

//System.out.println(result);


return result;
}

/* This fucntion takes care of the the rest condition:
 * if the last item L in s2 is a seperate element then
 * {l} is appanded at the end of s1 as a seperate element 
 * to form a candidate c1
 * * */

public static String condition3(String first,String second){


String lastitem_s1="";
String lastitem_s2="";

int size_s1=0,length_s1=0; 
int count=0;
int toadd;
String temp1,haha="",lastitem="";
//System.out.println(first);

//If the last item in s2 is a serperate element;

int start=second.lastIndexOf("<");
int end=second.lastIndexOf(">");

System.out.println(first);

for(int i=start;i<end;i++){
if(second.substring(i,i+1).equals(",")){
count++;
}
}
//System.out.println(count);

//if the number of , is less than 3 than this has no more than one element in its last item
///////////////////////////////////////////////////////////////////
if(count<=3){

StringTokenizer st1 = new StringTokenizer(second,",");
     while (st1.hasMoreTokens()) {
          temp1 = st1.nextToken();
          if(isNum(temp1)==true){lastitem=temp1;}
          }





System.out.println(haha);

if(lastitem!=""){
haha=first.concat("<,"+ lastitem+",  , >");
}
else{haha=first;}

//System.out.println(haha);
aftercondition1.add(haha);

 

 ////////////////////////////
 //Third IF of the algorithm
//int size_s1=0;
//int length_s1=0;
String temp2;
//String lastitem_s1="";
//String lastitem_s2="";

System.out.println(first);
StringTokenizer st2 = new StringTokenizer(first,",");
     while (st2.hasMoreTokens()) {
          temp2 = st2.nextToken();
          if(temp2.trim().equals(">")){size_s1++;}    //calculating the siZE of s1
          if(isNum(temp2.trim())==true){length_s1++;}  //calculating the length of s1
          }


String temp3="";
StringTokenizer st3 = new StringTokenizer(first,",");
     while (st3.hasMoreTokens()) {
          temp3 = st3.nextToken();
          if(isNum(temp3)==true){lastitem_s1=temp3;}
          }

System.out.println(first);
System.out.println(lastitem_s1);


String temp4="";
StringTokenizer st4 = new StringTokenizer(second,",");
     while (st4.hasMoreTokens()) {
          temp4 = st4.nextToken();
          if(isNum(temp4)==true){lastitem_s2=temp4;}
          }

System.out.println(second);
System.out.println(lastitem_s2);

String haha1;

///This is the second If
//if(length of s1 is 2 and size of s1 is also 2) AND (last item of s2 is greater than last item os s1)
if((size_s1==2 && length_s1==2) &&( MS.get(lastitem_s2)> MS.get(lastitem_s1))){


if(lastitem_s1!=""){
haha1=first.concat("<,"+ lastitem_s1+",  , >");
}
else{haha1=first;}

aftercondition1.add(haha1);
}

}

/////////////////////////////////// Second IF ends here/////////////////////////////////////////
//
//
///I am doing the elseif as a just if coz it doesnt seeem to matter

String haha3="";

if((length_s1==2 && size_s1==1) && (MS.get(lastitem_s2)> MS.get(lastitem_s1)) || (length_s1>2) ){

haha3= "<, "+lastitem_s2+",  , "+lastitem_s1+",  , >,";

aftercondition1.add(haha3);


}










return "";





}




////////
/*the second if condition: 
 * if(the length and the size of s1 are both 2) AND (the last item of s2 is greater than the last item of s1)
 * then
 * {l} is added  at the end of the last element s1 to form another candidate sequences c2 *
 * */

public static String condition4(String first,String second){
int size_s1=0;
int length_s1=0;
String temp,temp1;
//getting the last item of s1
System.out.println(first);
StringTokenizer st = new StringTokenizer(first,",");
     while (st.hasMoreTokens()) {
          temp = st.nextToken();
          if(temp.trim().equals(">")){size_s1++;}    //calculating the siZE of s1
          if(isNum(temp.trim())==true){length_s1++;}  //calculating the length of s1
          }

//System.out.println(size_s1);
//System.out.println(length_s1);







return "";
}









/*This funciont takes in a string and checks if it is a number of not.Used in condition2
 * to check the last interger that we get
  * */

public static boolean isNum(String s) {
try {
Double.parseDouble(s);
}
catch (NumberFormatException nfe) {
return false;
}
return true;
}

public static void conditioncheck(Vector candidates){

String s1=candidates.toString();
String s2=s1;
String temp1,temp="";
Vector aftercondition1=new Vector();  //vector containig candidates after executing condition 1 and 2
//String[] cand=new String[candidates.size()];
int count=0;
int count_sequences=0;

//////////////////////////////////////////////////////////////////////////
//to count the number of sequences and determine the array size
StringTokenizer st1 = new StringTokenizer(s1,"$");
     while (st1.hasMoreTokens()) {
          temp1 = st1.nextToken();
         ++count_sequences;
          }

String[] cand=new String[count_sequences];




//String cand[] array is filled with each sequences.
////array cand contains the candidates

StringTokenizer st = new StringTokenizer(s1,"$");
     while (st.hasMoreTokens()) {
          temp1 = st.nextToken();
          cand[count++]=temp1;
//          System.out.println(temp1);
         }
 

/////////////////////////////////////////////////////////////////////////






//now tranversing the array for all possible configrations of s1 and s2
String first,second;
//System.out.println(count_sequences);
for(int i=0;i<=count_sequences-2;i++){
        first=cand[i];
	for(int j=0;j<=count_sequences-2;j++){
           second=cand[j];
           
           
          aftercondition1.add(condition1(first,second)); 
          aftercondition1.add(condition2(first,second));     
          condition3(first,second); 



          // System.out.println("First:"+first + "second:" +second);
               }
     }








}







public static void main(String[] args){



 MS=ReadFileInput.readParameters(paraFileName);
 S=ReadFileInput.readData(dataFileName);


//If the MIS value of the first item in a sequecne s1 is less than
//the MIS value of every other iterm in s1

LinkedList<Integer> M=sort(MS);
//comparing


int i=0,count=0,count1=0;
double tocompare=0.0;
boolean forward;
ArrayList<Transaction> todo=new ArrayList<Transaction>();
for(Transaction trans: S){
//System.out.println();
count=0;
	for(ItemSet is: S.get(i++).itemSets){
                            
                      for(Integer integer: is.items){
                                    if(count==0){
                                    //System.out.println(integer+" this is it ");    
                                    //System.out.println(integer);
                                    todo=compareall(integer);    
                                    //System.out.println("The size is"+ todo.size());
                                    }
                                    
                                   count++;
                                      }

                        }


}
/*
 * checked to seee if the compareall gives correct results
int j=0;
for(Transaction trans1:todo){
for(ItemSet haha: todo.get(j++).itemSets){
System.out.print("<");

for(Integer integer1: haha.items){
System.out.print(integer1+"   ");
System.out.print(">   ");

}
}
System.out.println();

}*/


/*Sequence s1 and s2 will join if: (1)the subsequences obtained by dropping the second item of s1
 * and last item of s2 are the same
 * * */



// The candidates are changed into vector for easy processing
// The tuples are stored in the same way as the original data
// $ represents the end of a sequence
Vector candidates = new Vector();
if(todo.isEmpty()!=true){

DataReader reader = new DataReader();

int j=0;
for(Transaction trans1:todo){
for(ItemSet haha: todo.get(j++).itemSets){
//System.out.print("<");
candidates.add("<");
for(Integer integer1: haha.items){
//System.out.print(integer1+"   ");
candidates.add(integer1);
candidates.add(" ");

}
//System.out.print(">");
candidates.add(">");
}
//System.out.print(">");
//System.out.println();
candidates.add("$");
}

conditioncheck(candidates);

//Iterator it = candidates.iterator();
//while(it.hasNext())
//System.out.print(it.next());

}



Iterator itr=aftercondition1.iterator();

while(itr.hasNext()){
System.out.println(itr.next());

}




}

}
