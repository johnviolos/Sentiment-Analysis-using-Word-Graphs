/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package word.graph_classification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import static word.graph_classification.GraphCreator.FromDataTo_UNweightedGraph;

/**
 * @since 12-15-2015
 * @version 1.0
 * @author John Violos
 * 
 */
public class FeatureSelectionTechniques {

/**
 * It estimates the 90% of documents according to the nFold cross validation.
 * @param documents :  An array that contains in its cells all the documents.
 * @param numberOfCategories : The number of the Categories in which the Documents belong.
 * @param nFold : The fold of 10-Fold-Cross-Validation.
 * @return : An array of documents that belong in the n-fold and are 90% of the input documents.
 */    
    
static public String[] InputDocuments( ArrayList<String> documents, int numberOfCategories, int nFold){
String[] categoriesOfDocuments = new String[numberOfCategories+1];                  //The categories which contain the "documents" with which we will make the graph categories.

for (int i=0; i<documents.size(); i++){
    if ( ((i) % 10) != nFold)
    {
        //documentsForClassificationSimilarities.add(documents.get(i));//All the Documents of the 90% portion will be included. Changed
        int categoryOfTheDocument;
        String document= documents.get(i);
                                 //45% of Documents for Graph Representation.
            categoryOfTheDocument=Integer.parseInt(document.substring(document.indexOf('B')+1, document.indexOf('E')));
            categoriesOfDocuments[categoryOfTheDocument]+=document.substring(document.indexOf("E")+1)+"\n";
    }
}//==== End of Assiging |10% of Documents for testing|, |45% for Graph Representation|, |45% for Values Similarities Document-20Categories| ====

return categoriesOfDocuments;
        }    
    
    
    
    
//==============================MutualInformation()===============================
/**It estimates the Mutual Information of each Edge and returns the edges that have less MI degree than a threshold.
 It takes as input the 90% percentage of documents 
* @param documents : An array that contains in its cells all the documents from which the MI will be calculated.
* @param numberOfCategories : The number of the Categories in which the Documents belong.
* @param frame : The frame determines the number of edges will be created to join each Word-Node to the following Words-Nodes.
* @param MI_threshold : The Threshold with which the edges are categorized as unimportant if they have less MI degree than this.
* @param nFold : The fold of 10-Fold-Cross-Validation.
* @return : A set of edges word to word which have smaller MI degree than the MI_threshold.
 */ 
//===============================================================================================    
static public HashSet MutualInformation_Avg(ArrayList<String> documents, int numberOfCategories, int frame, double MI_threshold, int nFold){    
HashSet<String> edgesToBeFilteredOut =new HashSet<>();                                  //Το σύνολο των ακμών που πρέπει να αφερεθούν.
String[] categoriesOfDocuments= InputDocuments(documents,numberOfCategories, nFold);    //Έχουμε στο categoriesOfDocuments[] για κάθε κατηγορία ποια Documents περιέχει.
HashMap<String,int[]> edgesOfGraph= new HashMap<>();                //Κάθε edge έχει ως key το Edge και ως value έναν πίνακα από 20 int που δηλώνει πόσες φορές το βρίκαμε σε κάθε κατηγορία.
int[] documentNumberForEachCategory = new int[numberOfCategories+1];//Κάθε κατηγορία πόσα κείμενα έχει. 
int documentNumberForALLCategories=0;                               //Πλήθος κειμένων που υπάρχουν σινολικά σε όλο το corpus.
float[] probabilityForEachCategory = new float[numberOfCategories+1];//Πηθανότητα ύπαρξης κάθε κατηγορίας. 

for(int category=1; category<=numberOfCategories; category++){      //Διατρέχω όλες της κατηγορίες, ανακτώ τα κείμενα κάθε κατηγορίας category.
System.out.println("We are processing the Category "+category);
String[] documentsBelongedOneCategory = categoriesOfDocuments[category].split("\n");    //Ένας πίνακας από Strings όπου κάθε κελί του πίνακα έχει από ένα document του corpus.
documentNumberForEachCategory[category]=documentsBelongedOneCategory.length;            //Πόσα κείμενα έχει η τρέχων κατηγορία. Ο αριθμός καταχωρήτε στο documentNumberForEachCategory.
documentNumberForALLCategories+=documentsBelongedOneCategory.length;                    //Αυξάνεται το συνολικό πλήθος όλων των κειμένων του Corpus.

    for(int documentPointer=0; documentPointer<documentsBelongedOneCategory.length; documentPointer++){ //Διατρέχω όλα τα κείμενα μιας κατηγορίας.
        HashMap graphOfCurrentDocument= FromDataTo_UNweightedGraph(documentsBelongedOneCategory[documentPointer],frame);// To graphOfCurrentDocument είναι ένα hashmap που περιέχει όλα τα edges ενός κειμένου.
    
        Iterator it = graphOfCurrentDocument.entrySet().iterator();             //Ένα iterator για τα Edges του κειμένου.
        while(it.hasNext()){                                                    //Διατρέχουμε τα edges του κειμένου μέσω του iterator.
            Map.Entry pair = (Map.Entry)it.next();
            String edge=(String) pair.getKey();                                 //Το Edge είναι μία ακμή του κειμένου από το hashmap όλων των ακμών.
        
            if (!edgesOfGraph.containsKey(edge)){                               //Αν η ακμή αυτή που έχουμε ανακτήσει από το κείμενο ΔΕΝ υπάρχει μέσα στο HashMap edgesOfGraph τότε
                int[] categoryValues= new int[numberOfCategories+1];
                categoryValues[category]=1;                                     //Παίρνει ως value έναν πίνακα που έχει όλα τα κελιά-κατηγορίες του 0 εκτός απο το κελή-category που ανήκει. Στο κελί-κατηγορία που ανίκει παίρνει 1.
                edgesOfGraph.put(edge,categoryValues );                         //Βάζω αυτό το array ως value στο entry της ακμής του HashMap edgesOfGraph. 
            }else{
                int[] newCategoryValues =edgesOfGraph.get(edge);                //To edge του κείμενου υπάρχει ήδη οπότε απλώς αυγάνουμε την τιμή του κελιού-κατηγορίας κατά ένα.
                newCategoryValues[category]+=1;
            edgesOfGraph.put(edge,newCategoryValues );
            }
        }
    }
}//Έχω για κάθε edge σε ποιες κατηγορίες βρέθικε και πόσες φορές

for (int categPointer=1; categPointer<=numberOfCategories; categPointer++){
    probabilityForEachCategory[categPointer]= (float)documentNumberForEachCategory[categPointer]/(float)documentNumberForALLCategories;
//    //System.out.println("The category-"+categPointer +"+has: "+documentNumberForEachCategory[categPointer]+" documents and probability: "+probabilityForEachCategory[categPointer]);
}//Εχω για κάθε κατηγορία τι πιθανότητα έχουμε να την συναντήσουμε
//System.out.println("There are total "+documentNumberForALLCategories+" documents.");

//System.out.println("There are "+edgesOfGraph.size()+" edges.");
//System.out.println("O grafos exi edges pou sinanti8ikan:"); 

Iterator it2 = edgesOfGraph.entrySet().iterator();  //Iterator για τις ακμές του γράφου edgesOfGraph.
while(it2.hasNext()){// Διατρέχω τον γράφο και υπολογίζω για κάθε ακμή το MI.
    Map.Entry pair2 = (Map.Entry)it2.next();
    //System.out.print("the edge |"+pair2.getKey()+ "| has values to each category:\t");
    int[] val= (int[]) pair2.getValue();
   
    double MI_EdgeCategory[] = new double[numberOfCategories+1];    //To MI για κάθε Edge-Category
    
    for(int i=1; i<=numberOfCategories; i++){
        //System.out.print(val[i]+" ");
        if(val[i]==0) continue;
        
        int B=0; for(int j=1; j<=numberOfCategories; j++){ if(i==j)continue; B+=val[j];}
        int A_sum_C=documentNumberForEachCategory[i]; 
        MI_EdgeCategory[i]=Math.log10((double)val[i]*(double)documentNumberForALLCategories/((double)A_sum_C*((double)val[i]+(double)B)));
    }//System.out.println();
    double MI_Edge=0.0f;
    for(int i=1; i<=numberOfCategories; i++){
        MI_Edge+= MI_EdgeCategory[i]*probabilityForEachCategory[i];

    }
    if(MI_Edge<MI_threshold){    //If the MI of the edge is smaller than the MI_threshold then the edge is added to the set of the filtered-out edges.
    //System.out.println("the edge |"+pair2.getKey()+ "| has MI="+MI_Edge+" which is smaller than the MI_threshold "+MI_threshold);
    edgesToBeFilteredOut.add((String) pair2.getKey());}
    //System.out.println("\t and MI= "+MI_Edge);
}
return edgesToBeFilteredOut;
}//End of Method MutualInformation() 





//==============================MutualInformation()===============================
/**It estimates the Mutual Information of each Edge and returns the edges that have less MI degree than a threshold.
 It takes as input the 90% percentage of documents 
* @param documents : An array that contains in its cells all the documents from which the MI will be calculated.
* @param numberOfCategories : The number of the Categories in which the Documents belong.
* @param frame : The frame determines the number of edges will be created to join each Word-Node to the following Words-Nodes.
* @param MI_threshold : The Threshold with which the edges are categorized as unimportant if they have less MI degree than this.
* @param nFold : The fold of 10-Fold-Cross-Validation.
* @return : A set of edges word to word which have smaller MI degree than the MI_threshold.
 */ 
//===============================================================================================    
static public HashSet MutualInformation_Max(ArrayList<String> documents, int numberOfCategories, int frame, double MI_threshold, int nFold){    
HashSet<String> edgesToBeFilteredOut =new HashSet<>();                                  //Το σύνολο των ακμών που πρέπει να αφερεθούν.
String[] categoriesOfDocuments= InputDocuments(documents,numberOfCategories, nFold);    //Έχουμε στο categoriesOfDocuments[] για κάθε κατηγορία ποια Documents περιέχει.
HashMap<String,int[]> edgesOfGraph= new HashMap<>();                //Κάθε edge έχει ως key το Edge και ως value έναν πίνακα από 20 int που δηλώνει πόσες φορές το βρίκαμε σε κάθε κατηγορία.
int[] documentNumberForEachCategory = new int[numberOfCategories+1];//Κάθε κατηγορία πόσα κείμενα έχει. 
int documentNumberForALLCategories=0;                               //Πλήθος κειμένων που υπάρχουν σινολικά σε όλο το corpus.
float[] probabilityForEachCategory = new float[numberOfCategories+1];//Πηθανότητα ύπαρξης κάθε κατηγορίας. 

for(int category=1; category<=numberOfCategories; category++){      //Διατρέχω όλες της κατηγορίες, ανακτώ τα κείμενα κάθε κατηγορίας category.
System.out.println("We are processing the Category "+category);
String[] documentsBelongedOneCategory = categoriesOfDocuments[category].split("\n");    //Ένας πίνακας από Strings όπου κάθε κελί του πίνακα έχει από ένα document του corpus.
documentNumberForEachCategory[category]=documentsBelongedOneCategory.length;            //Πόσα κείμενα έχει η τρέχων κατηγορία. Ο αριθμός καταχωρήτε στο documentNumberForEachCategory.
documentNumberForALLCategories+=documentsBelongedOneCategory.length;                    //Αυξάνεται το συνολικό πλήθος όλων των κειμένων του Corpus.

    for(int documentPointer=0; documentPointer<documentsBelongedOneCategory.length; documentPointer++){ //Διατρέχω όλα τα κείμενα μιας κατηγορίας.
        HashMap graphOfCurrentDocument= FromDataTo_UNweightedGraph(documentsBelongedOneCategory[documentPointer],frame);// To graphOfCurrentDocument είναι ένα hashmap που περιέχει όλα τα edges ενός κειμένου.
    
        Iterator it = graphOfCurrentDocument.entrySet().iterator();             //Ένα iterator για τα Edges του κειμένου.
        while(it.hasNext()){                                                    //Διατρέχουμε τα edges του κειμένου μέσω του iterator.
            Map.Entry pair = (Map.Entry)it.next();
            String edge=(String) pair.getKey();                                 //Το Edge είναι μία ακμή του κειμένου από το hashmap όλων των ακμών.
        
            if (!edgesOfGraph.containsKey(edge)){                               //Αν η ακμή αυτή που έχουμε ανακτήσει από το κείμενο ΔΕΝ υπάρχει μέσα στο HashMap edgesOfGraph τότε
                int[] categoryValues= new int[numberOfCategories+1];
                categoryValues[category]=1;                                     //Παίρνει ως value έναν πίνακα που έχει όλα τα κελιά-κατηγορίες του 0 εκτός απο το κελή-category που ανήκει. Στο κελί-κατηγορία που ανίκει παίρνει 1.
                edgesOfGraph.put(edge,categoryValues );                         //Βάζω αυτό το array ως value στο entry της ακμής του HashMap edgesOfGraph. 
            }else{
                int[] newCategoryValues =edgesOfGraph.get(edge);                //To edge του κείμενου υπάρχει ήδη οπότε απλώς αυγάνουμε την τιμή του κελιού-κατηγορίας κατά ένα.
                newCategoryValues[category]+=1;
            edgesOfGraph.put(edge,newCategoryValues );
            }
        }
    }
}//Έχω για κάθε edge σε ποιες κατηγορίες βρέθικε και πόσες φορές

for (int categPointer=1; categPointer<=numberOfCategories; categPointer++){
    probabilityForEachCategory[categPointer]= (float)documentNumberForEachCategory[categPointer]/(float)documentNumberForALLCategories;
//    //System.out.println("The category-"+categPointer +"+has: "+documentNumberForEachCategory[categPointer]+" documents and probability: "+probabilityForEachCategory[categPointer]);
}//Εχω για κάθε κατηγορία τι πιθανότητα έχουμε να την συναντήσουμε
//System.out.println("There are total "+documentNumberForALLCategories+" documents.");

//System.out.println("There are "+edgesOfGraph.size()+" edges.");
//System.out.println("O grafos exi edges pou sinanti8ikan:"); 

Iterator it2 = edgesOfGraph.entrySet().iterator();  //Iterator για τις ακμές του γράφου edgesOfGraph.
while(it2.hasNext()){// Διατρέχω τον γράφο και υπολογίζω για κάθε ακμή το MI.
    Map.Entry pair2 = (Map.Entry)it2.next();
    //edoSystem.out.print("the edge |"+pair2.getKey()+ "| has values to each category:\t");
    int[] val= (int[]) pair2.getValue();
   
    double MI_EdgeCategory[] = new double[numberOfCategories+1];    //To MI για κάθε Edge-Category
    
    for(int i=1; i<=numberOfCategories; i++){
        //edoSystem.out.print(val[i]+" ");
        if(val[i]==0) continue;
        
        int B=0; for(int j=1; j<=numberOfCategories; j++){ if(i==j)continue; B+=val[j];}
        int A_sum_C=documentNumberForEachCategory[i]; 
        MI_EdgeCategory[i]=Math.log10((double)val[i]*(double)documentNumberForALLCategories/((double)A_sum_C*((double)val[i]+(double)B)));
    }//System.out.println();
    double MI_Edge=0.0f;
    for(int i=1; i<=numberOfCategories; i++){
        if(MI_EdgeCategory[i]>MI_Edge)
              MI_Edge = MI_EdgeCategory[i];                 //MI_Edge+= MI_EdgeCategory[i]*probabilityForEachCategory[i];

    }
    if(MI_Edge<MI_threshold){    //If the MI of the edge is smaller than the MI_threshold then the edge is added to the set of the filtered-out edges.
    //System.out.println("the edge |"+pair2.getKey()+ "| has MI="+MI_Edge+" which is smaller than the MI_threshold "+MI_threshold);
    edgesToBeFilteredOut.add((String) pair2.getKey());}
    //edo System.out.println("\t and MI= "+MI_Edge);
}
return edgesToBeFilteredOut;
}//End of Method MutualInformation() 
}//Enf of Class FeatureSelectionTechniques
