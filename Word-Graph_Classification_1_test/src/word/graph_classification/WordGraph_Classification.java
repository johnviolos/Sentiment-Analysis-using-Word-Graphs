package word.graph_classification;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import static word.graph_classification.DimensionalRepresentation.CategoriesAndDocumentsNumericalRepresentations_CS;
import static word.graph_classification.DimensionalRepresentation.CategoriesAndDocumentsNumericalRepresentations_CS_MI;
import static word.graph_classification.DimensionalRepresentation.CategoriesAndDocumentsNumericalRepresentations_MCSDE;
import static word.graph_classification.DimensionalRepresentation.CategoriesAndDocumentsNumericalRepresentations_MCSN;
import static word.graph_classification.DimensionalRepresentation.CategoriesAndDocumentsNumericalRepresentations_MCSUE;
import static word.graph_classification.DimensionalRepresentation.CategoriesAndDocumentsNumericalRepresentations_TS;
import static word.graph_classification.GraphComparator.Compare_NGram_WeightedGraphs;
import static word.graph_classification.GraphCreator.FromAllDocumentsTo_One_UNweightedGraph;
import static word.graph_classification.GraphCreator.FromDataTo_UNweightedGraph;
import static word.graph_classification.GraphCreator.FromDataTo_WeightedGraph;
import static word.graph_classification.Utilities.Choiceof_Classification_or_GraphRepresentation;
import static word.graph_classification.Utilities.FrameSize;
import static word.graph_classification.Utilities.InputFileName;
import static word.graph_classification.Utilities.MethodChoice;
import static word.graph_classification.Utilities.NumberOfCategories;
import static word.graph_classification.Utilities.ReadDocuments;
import static word.graph_classification.Utilities.WriteIntoFilesTrainingTestingData;


/** A class of methods to evaluate experimentally the Word Graph Classification model. 
* @since 10-6-2015
* @version 1.1
* @author John Violos */
public class WordGraph_Classification {
 
public static void main(String[] args) throws IOException {

if(Choiceof_Classification_or_GraphRepresentation().equals("GR"))
    OneWordGraph_Corpus_Representation(InputFileName(),NumberOfCategories(),FrameSize());
else//Eδω θα βάλω αν θέλω κ feature selection τεχνική. ας ξεκινήσω με το CS   
switch(MethodChoice().toUpperCase()){
case "CS":
    CategoriesAndDocumentsNumericalRepresentations_CS(InputFileName(),NumberOfCategories(),FrameSize());   
break;
case "MCSN":
    CategoriesAndDocumentsNumericalRepresentations_MCSN(InputFileName(),NumberOfCategories(),FrameSize());    
break;
case "MCSDE":
    CategoriesAndDocumentsNumericalRepresentations_MCSDE(InputFileName(),NumberOfCategories(),FrameSize());  
break;
case "MCSUE":
    CategoriesAndDocumentsNumericalRepresentations_MCSUE(InputFileName(),NumberOfCategories(),FrameSize()); 
break;
case "TS":
    CategoriesAndDocumentsNumericalRepresentations_TS(InputFileName(),NumberOfCategories(),FrameSize());    
break;
    case "CS_MI":
    CategoriesAndDocumentsNumericalRepresentations_CS_MI(InputFileName(),NumberOfCategories(),FrameSize());  
}

}



//============================================= OneWordGraph_Corpus_Representation Method() =============================================
/** The Corpus will be represented as a Word Graph two files will be the output. One contains the edges as two two adjacent Node_Numbers 
 * separated by a comma ","and the other one the Label in which belong each Node_Number separated by a comma ",". i.e Edges: 756,2195 
 * Labels: 2195, 4. There exist a directed edge which joins the Node_Numbers 756->2195 and the Node_Number belongs in the 4th Label_Number.
* @param inputFileName The filename that includes the Documents will be processed.
* @param numberOfCategories The number of the Categories in which the Documents belong.
* @param frame The Number "N" of the edges will be created having as Source Node each Word and as Terminal Nodes the "N" following Words. */ 
//=========================================================================================================================================
private static void OneWordGraph_Corpus_Representation(String inputFileName, int numberOfCategories, int frame) throws IOException{
ArrayList<String> documents = new ArrayList<>();                                    //The list of all the Documents.
System.out.println("It is being processed the UNweighted Word Graph Representation of "+inputFileName);
ReadDocuments(inputFileName,documents);                                             //The documents from the file are read and assigned in the ArrayList documents.
FromAllDocumentsTo_One_UNweightedGraph(documents,frame); //SOS meta 8a to bgalo apo sxolio.
//FeatureSelectionTechniques.MutualInformation(documents, numberOfCategories, frame, 0.08f,1);


}//====================================== End of OneWordGraph_Corpus_Representation() ======================================    



//============================================ Evaluation_10Fold_UNweightedGraphs Method() ============================================
/** The Documents are taken from inputFileName and 90% are used for training the representation of Unweighted Word Graphs for each Category.
* The rest 10% of Documents are used to test the model. The process is repeated 10 times following the 10 fold cross validation method.
* Finally for each testing Document is returned the actual Category in which belong and the Category in which Predicted to belong.
* @param inputFileName The filename that includes the Documents will be processed.
* @param numberOfCategories The number of the Categories in which the Documents belong.
* @param frame The Number "N" of the edges will be created having as Source Node each Word and as Terminal Nodes the "N" following Words. */ 
//===========================================================================================================================================
private static void Evaluation_10Fold_UNweightedGraphs(String inputFileName, int numberOfCategories, int frame) throws IOException {
ArrayList<String> documents = new ArrayList<>();//The list of Documents.
ArrayList<String> NGramGraphs= new ArrayList<>();
    String[] numbers ={"One","Two","Three","Four","Five","Six","Seven","Eight","Nine","Ten"};
String category_classForAllTestingDocumentsIn1Fold="";//We make empty the String which declares for every testing document its category and class.
System.out.println("It is being processed the UNweighted Word Graph Classification of "+inputFileName);

ReadDocuments(inputFileName,documents);//We read the documents from the file  and we put them in the ArrayList documents.

System.out.println("There exist "+Integer.toString(numberOfCategories)+" categories");
String[] categoriesOfDocuments = new String[numberOfCategories+1];//Here we have all the classes which contain the documents with which we will make the graph categories.
HashMap<String,Boolean>[] nGramGraphsOfCategories = new HashMap[numberOfCategories+1];
String testDocuments;//Here we have all the documents which will be tested-classified.

for (int nFold=0; nFold<=9; nFold++){    //The fold of 10-Fold-Cross-Validation
category_classForAllTestingDocumentsIn1Fold +="Fold_"+numbers[nFold]+"\n";
System.out.println("*** Fold-"+Integer.toString(nFold+1)+" is in process***");
//String category_classForAllTestingDocumentsIn1Fold="";//We make empty the String which declares for every testing document its category and class.
for (int i=0;i<=numberOfCategories;i++){categoriesOfDocuments[i]="";} //We make empty the Category Array of Strings for every fold.
testDocuments="";//We make empty the Testing-String for every fold.

//====It is assigned 90% of Documents in Categories and 10% of Documents for testing.====
for (int i=0; i<documents.size(); i++){
    if ( ((i) % 10) == nFold)
        {testDocuments+=Integer.toString(i+1)+documents.get(i)+"\n";}
    else
    {
        int categoryOfTheDocument;
        String document= documents.get(i);
        categoryOfTheDocument=Integer.parseInt(document.substring(document.indexOf('B')+1, document.indexOf('E')));
        categoriesOfDocuments[categoryOfTheDocument]+=document.substring(document.indexOf("E")+1)+"\n";
    }
}
//System.out.println("90% of documents asigned in their categories and 10% will be used for testing.");
//====End of assigning the Docuents. ====

//====We make the Word-Graphs of Categories.====
for (int i=1; i<=numberOfCategories;i++){
nGramGraphsOfCategories[i]=FromDataTo_UNweightedGraph(categoriesOfDocuments[i],frame);
//System.out.println("We made the Word Graph of the category-"+Integer.toString(i));
}//System.out.println("All Category N-Gram Graphs have been constructed.");
//====End of the graph making of the categories.====

//====We compare all the NGramsGraphs of testing documents with all the NGramsGraphs of Categories.====
String[] arrayOfTestingDocuments = testDocuments.split("\n");
//System.out.println("There are "+Integer.toString(arrayOfTestingDocuments.length)+" documents to be tested.");
float result;
for (String arrayOfTestingDocument : arrayOfTestingDocuments) { // i=Document. All testingDocuments are iterrated.
    HashMap current_TestingDocument_NGramGraph;
    current_TestingDocument_NGramGraph = FromDataTo_UNweightedGraph(arrayOfTestingDocument.substring(arrayOfTestingDocument.lastIndexOf("E") + 1),frame);
    float similarityNumberDocument_Category=0.0f;
    int documentCategories=0;
    //====1_TestingDocument-all_Category Comparison.====
    for(int j=1; j<=numberOfCategories; j++){       //j=Category. All the NGramGraph of Categories are iterrated.
        
   System.out.print("\n\nThe Document-" + arrayOfTestingDocument.substring(0, arrayOfTestingDocument.indexOf('B')));
   System.out.print(" really belongs in the categories: " + arrayOfTestingDocument.substring(arrayOfTestingDocument.indexOf('B') + 1, arrayOfTestingDocument.lastIndexOf("E")).replaceAll("E", " "));
   System.out.println(" and compared with the category "+Integer.toString(j));
        
        
        
        //result = Compare_UNweightedGraphs_Using_MCSN(current_TestingDocument_NGramGraph,nGramGraphsOfCategories[j],"");
result = GraphComparator. Compare_UNweightedGraphs_Using_CS (current_TestingDocument_NGramGraph,nGramGraphsOfCategories[j]);
        if(result>similarityNumberDocument_Category){
            similarityNumberDocument_Category=result;
            documentCategories=j;// We put the the most appropriate categories in the array documentCategories
        }
    }//====End for 1_TestingDocument-all_Category Comparison.====

    //====We demonstrate the results of one document comparison with the Categories.====
    //System.out.print("The Document-" + arrayOfTestingDocument.substring(0, arrayOfTestingDocument.indexOf('B')));
    //System.out.print(" really belongs in the categories: " + arrayOfTestingDocument.substring(arrayOfTestingDocument.indexOf('B') + 1, arrayOfTestingDocument.lastIndexOf("E")).replaceAll("E", " "));
    //System.out.println(" estimated that belongs in the category "+Integer.toString(documentCategories));
    category_classForAllTestingDocumentsIn1Fold += arrayOfTestingDocument.substring(arrayOfTestingDocument.indexOf('B') + 1, arrayOfTestingDocument.lastIndexOf("E")).replaceAll("E", " ") + "-" + Integer.toString(documentCategories) + "\n";
    //====End of the result demonstrations one document with all categories.====     
}//====End of comparison all the NGramsGraphs of testing documents with all the NGramsGraphs of Categories.==== 

//====We demonstrate the evaluation of one Fold.====
//System.out.println("Fold-"+(nFold+1)+"\n"+category_classForAllTestingDocumentsIn1Fold);

//====End of the demonstration of the evalauation for this Fold.==== 
for (int i=1; i<=numberOfCategories; i++){ // We clean the hash map
nGramGraphsOfCategories[i].clear();
}

} //End of Fold

System.out.println(category_classForAllTestingDocumentsIn1Fold);
}//====================================== End of Evaluation_10Fold_3Gram_UNweightedGraphs Method() ======================================



//============================================ Evaluation_10Fold_3Gram_WeightedGraphs Method() =============================================
/** The Documents are taken from inputFileName and 90% are used for training the representation of Weighted Word Graphs for each Category.
* The rest 10% of Documents are used to test the model. The process is repeated 10 times following the 10 fold cross validation method.
* Finally for each testing Document is returned the actual Category in which belong and the Category in which Predicted to belong.
* @param inputFileName The filename that includes the Documents will be processed.
* @param numberOfCategories The number of the Categories in which the Documents belong.
* @param frame The Number "N" of the edges will be created having as Source Node each Word and as Terminal Nodes the "N" following Words.*/
//==========================================================================================================================================
private static void Evaluation_10Fold_WeightedGraphs(String inputFileName, int numberOfCategories, int frame) throws IOException {
ArrayList<String> documents = new ArrayList<>();//The list of Documents.
ArrayList<String> NGramGraphs= new ArrayList<>();    
String[] numbers ={"One","Two","Three","Four","Five","Six","Seven","Eight","Nine","Ten"};
String category_classForAllTestingDocumentsIn1FoldVS="VS Similarity\n";//We make empty the String which declares for every testing document its category and class.
String category_classForAllTestingDocumentsIn1FoldNVS="NVS Similarity\n";//We make empty the String which declares for every testing document its category and class.
System.out.println("We are processing the Weighted Word Graph Classification of "+inputFileName);

ReadDocuments(inputFileName,documents);//We read the documents from the file documents.txt and we put them in the ArrayList documents.
System.out.println("we have "+Integer.toString(numberOfCategories)+" categories");
String[] categoriesOfDocuments = new String[numberOfCategories+1];//Here we have all the classes which contain the documents with which we will make the graph categories.
HashMap<String,Integer>[] nGramGraphsOfCategories = new HashMap[numberOfCategories+1];
String testDocuments;//Here we have all the documents which will be tested-classified.

for (int nFold=0; nFold<=9; nFold++){    //The fold of 10-Fold-Cross-Validation
category_classForAllTestingDocumentsIn1FoldVS +="Fold_"+numbers[nFold]+"\n";
category_classForAllTestingDocumentsIn1FoldNVS +="Fold_"+numbers[nFold]+"\n";
System.out.println("*** Fold-"+Integer.toString(nFold+1)+" is in process***");
//String category_classForAllTestingDocumentsIn1Fold="";//We make empty the String which declares for every testing document its category and class.
for (int i=0;i<=numberOfCategories;i++){categoriesOfDocuments[i]="";} //We make empty the Category Array of Strings for every fold.
testDocuments="";//We make empty the Testing-String for every fold.

//====It is assigned 90% of Documents in Categories and 10% of Documents for testing.====
for (int i=0; i<documents.size(); i++){
    if ( ((i) % 10) == nFold)
        {testDocuments+=Integer.toString(i+1)+documents.get(i)+"\n";}
    else
    {
        int categoryOfTheDocument;
        String document= documents.get(i);
        categoryOfTheDocument=Integer.parseInt(document.substring(document.indexOf('B')+1, document.indexOf('E')));
        categoriesOfDocuments[categoryOfTheDocument]+=document.substring(document.indexOf("E")+1)+"\n";
    }
}
//System.out.println("90% of documents asigned in their categories and 10% will be used for testing.");
//====End of assigning the Docuents. ====

//====We make the NGramsGraphs of the Categories.====
for (int i=1; i<=numberOfCategories;i++){
nGramGraphsOfCategories[i]=FromDataTo_WeightedGraph(categoriesOfDocuments[i],frame);
//System.out.println("We made the Word Graph of the category-"+Integer.toString(i));
}//System.out.println("All Category N-Gram Graphs have been constructed.");
//====End of the graph making of the categories.====

//====We compare all the NGramsGraphs of testing documents with all the NGramsGraphs of Categories.====
String[] arrayOfTestingDocuments = testDocuments.split("\n");
float result[];
for (String arrayOfTestingDocument : arrayOfTestingDocuments) { // i=Document. All testingDocuments are iterrated.
    HashMap current_TestingDocument_NGramGraph;
    current_TestingDocument_NGramGraph = FromDataTo_WeightedGraph(arrayOfTestingDocument.substring(arrayOfTestingDocument.lastIndexOf("E") + 1),frame);
    float similarityNumberDocument_CategoryVS=0.0f;
    float similarityNumberDocument_CategoryNVS=0.0f;
    int documentCategoriesVS=0;
    int documentCategoriesNVS=0;
    for(int j=1; j<=numberOfCategories; j++){       //j=Category. All the NGramGraph of Categories are iterrated.
        result = Compare_NGram_WeightedGraphs(current_TestingDocument_NGramGraph,nGramGraphsOfCategories[j]);
        
        //System.out.println("result vs: "+result[0] +"\nresultNVS: "+result[1]);
       // System.out.println("We compare category-"+j+nGramGraphsOfCategories[j]+" and current document: "+current_TestingDocument_NGramGraph);
        if(result[0]>similarityNumberDocument_CategoryVS){
            similarityNumberDocument_CategoryVS=result[0];
            documentCategoriesVS=j;// We put the the most appropriate categories in the array documentCategories
        }
        if(result[1]>similarityNumberDocument_CategoryNVS){
            similarityNumberDocument_CategoryNVS=result[1];
            documentCategoriesNVS=j;// We put the the most appropriate categories in the array documentCategories
        }
        category_classForAllTestingDocumentsIn1FoldVS += arrayOfTestingDocument.substring(arrayOfTestingDocument.indexOf('B') + 1, arrayOfTestingDocument.lastIndexOf("E")).replaceAll("E", " ") + "-" + Integer.toString(documentCategoriesVS) + "\n";
        category_classForAllTestingDocumentsIn1FoldNVS += arrayOfTestingDocument.substring(arrayOfTestingDocument.indexOf('B') + 1, arrayOfTestingDocument.lastIndexOf("E")).replaceAll("E", " ") + "-" + Integer.toString(documentCategoriesNVS) + "\n";
    }//====End of comparison all the NGramsGraphs of testing documents with all the NGramsGraphs of Categories.==== 
}
for (int i=1; i<=numberOfCategories; i++){ // We clean the hash map
    nGramGraphsOfCategories[i].clear();
}
} //End of Fold
System.out.println(category_classForAllTestingDocumentsIn1FoldVS);
System.out.println(category_classForAllTestingDocumentsIn1FoldNVS);
}//====================================== End of Evaluation_10Fold_3Gram_WeightedGraphs Method() ======================================

}// End of Class WordGraph_Classification



