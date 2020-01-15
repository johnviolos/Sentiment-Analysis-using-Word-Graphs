package word.graph_classification;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import static word.graph_classification.GraphCreator.FromDataTo_UNweightedGraph;
import static word.graph_classification.Utilities.ReadDocuments;
import static word.graph_classification.Utilities.WriteIntoFilesTrainingTestingData;
//import static word.graph_classification.WordGraph_Classification.documents;

/** Methods for dimensional representations for each Topic Category and 
 * dimensional representation for each new-coming Document. 
 * The dimensions correspond to the number of Categories.
 * These data will be passed to a classifier (SVM, NB)
 * 45% of Documents will be used to construct the N Graph Representations.
 * 45%(or 90%) of Documents will be used to make the N-dimensional vector 
 * for each Document (training_Documents_Similarities) in combination with
 * the Category in which it belongs (training_Documents_Categories).
 * 10% of Documents will be used for Testing testing_Documents_Similarities.
 * @since 10-6-2015
 * @version 1.1
 * @author John Violos */
public class DimensionalRepresentation {
    
//=============================================CategoriesAndDocumentsNumericalRepresentations_CS()============================================== 
/**A text file is read, 45% of Documents are for Word-Graph representation, 45% for the Value Similarities N-vectors,
 * and 10% for testing. The Similarity metric will be used is the --|Containment Similarity|--.
 * 10-fold cross validation is applied. Each fold involves separate representation word-graphs,Value similarities  N-Vector and
 * testing documents.
 * The method outputs 4 text files for each fold. 
 * training_Documents_Similarities.txt : The N-Vector value similarities of 45% of Training-Documents.
 * training_Documents_Categories.txt   : The Category in which belong each document of the aforementioned Training-Documents.
 * testing_Documents_Similarities.txt  : The N-Vector value similarities of 10% of Testing-Documents.
 * testing_Document_Categories.txt     : The Category in which belong each document of the aforementioned Testing-Documents.
 * @param inputFileName text file that contains all the (training & testing) documents and their categories.
 * @param numberOfCategories the number of distinct categories.
 * @param frame the number of edges will be added for each word. Each word will be a source node and the following words, 
 * contained in a frame, will be terminal nodes. The last words of a paragraph will create less edges than the frame number.*/ 
//============================================================================================================================== 
public static void CategoriesAndDocumentsNumericalRepresentations_CS(String inputFileName, int numberOfCategories, int frame) throws IOException{
ArrayList<String> documents = new ArrayList<>();                                    //The list of all the Documents.
String[] numbers ={"One","Two","Three","Four","Five","Six","Seven","Eight","Nine","Ten"};
System.out.println("It is being processed the UNweighted Word Graph Classification of "+inputFileName);
ReadDocuments(inputFileName,documents);                                             //The documents from the file are read and assigned in the ArrayList documents.
String[] categoriesOfDocuments = new String[numberOfCategories+1];                  //The categories which contain the "documents" with which we will make the graph categories.
String testDocuments;                                                               //Here we have all the documents which will be tested-classified.
HashMap<String,Boolean>[] nGramGraphsOfCategories=new HashMap[numberOfCategories+1];//
ArrayList<String> documentsForClassificationSimilarities = new ArrayList<>(); //

for (int nFold=0; nFold<=9; nFold++){                                               
System.out.println(" Fold_"+numbers[nFold]);  //One fold of the ten folds of 10-Fold-Cross-Validation
for (int i=0;i<=numberOfCategories;i++){categoriesOfDocuments[i]="";}           //In the begining of the fold each category is Initialized not to include any text.
testDocuments="";                                                               //In the begining of the fold there are not any documents for testing
documentsForClassificationSimilarities.clear();                                 //In the begining of the fold there are not any document similarities values

//==== Assiging |10% of Documents for testing|, |45% for Graph Representation|, |45% for Values Similarities Document-20Categories| ====
Boolean document_for_the_CaetgoryRepresentation=true;
for (int i=0; i<documents.size(); i++){
    if ( ((i) % 10) == nFold)
        {testDocuments+=Integer.toString(i+1)+documents.get(i)+"\n";}           //10% of Documents for testing.
    else{
        //documentsForClassificationSimilarities.add(documents.get(i));//All the Documents of the 90% portion will be included. Changed
        int categoryOfTheDocument;
        String document= documents.get(i);
        if(document_for_the_CaetgoryRepresentation){                            //45% of Documents for Graph Representation.
            categoryOfTheDocument=Integer.parseInt(document.substring(document.indexOf('B')+1, document.indexOf('E')));
            categoriesOfDocuments[categoryOfTheDocument]+=document.substring(document.indexOf("E")+1)+"\n";
            document_for_the_CaetgoryRepresentation=false;}
        else{                                                                   //45% of Documents for Values Similarities Document-20Categories
            document_for_the_CaetgoryRepresentation=true;
            documentsForClassificationSimilarities.add(documents.get(i));}
    }
}//==== End of Assiging |10% of Documents for testing|, |45% for Graph Representation|, |45% for Values Similarities Document-20Categories| ====

//==== Categories WordGraphs construction. ====
for (int i=1; i<=numberOfCategories;i++){nGramGraphsOfCategories[i]=FromDataTo_UNweightedGraph(categoriesOfDocuments[i],frame);}
//==== End of Categories WordGraphs construction. ====

StringBuilder training_Documents_Similarities = new StringBuilder();
StringBuilder training_Documents_Categories   = new StringBuilder();
StringBuilder testing_Documents_Similarities  = new StringBuilder();
StringBuilder testing_Document_Categories     = new StringBuilder();

Float result; //The value of comparison result between a category graph to a document graph.
//==== *Similarity Values* between the |45% of Training Documents| to |WordGraph Categories|. ====
for(String one_documentForClassificationSimilarities: documentsForClassificationSimilarities){
    training_Documents_Categories.append(one_documentForClassificationSimilarities.substring(one_documentForClassificationSimilarities.indexOf('B')+1, one_documentForClassificationSimilarities.indexOf('E'))).append("\n");
    HashMap graphRepresentation_of_one_documentForClassificationSimilarities =FromDataTo_UNweightedGraph(one_documentForClassificationSimilarities.substring(one_documentForClassificationSimilarities.lastIndexOf("E") + 1),frame);
    for(int j=1; j<=numberOfCategories; j++){
        result=GraphComparator.Compare_UNweightedGraphs_Using_CS(graphRepresentation_of_one_documentForClassificationSimilarities,nGramGraphsOfCategories[j]);
        if(result.isNaN()){result=0.0f; }//System.out.println("Under the limited amount of words the text: "+one_documentForClassificationSimilarities);
        
            training_Documents_Similarities.append(result).append(" ");
    }training_Documents_Similarities.setLength(training_Documents_Similarities.length()-1);
    training_Documents_Similarities.append("\n");
}//==== End of *Similarity Values* between the |45% of Training Documents| to |WordGraph Categories|. ====

//==== *Similarity Values* between the |10% of Testing Documents| to |WordGraph Categories|. ====
String[] arrayOfTestingDocuments = testDocuments.split("\n");

for (String arrayOfTestingDocument : arrayOfTestingDocuments) { // All testingDocuments are iterrated.
    HashMap current_TestingDocument_NGramGraph;
    current_TestingDocument_NGramGraph = FromDataTo_UNweightedGraph(arrayOfTestingDocument.substring(arrayOfTestingDocument.lastIndexOf("E") + 1),frame);
    testing_Document_Categories.append(arrayOfTestingDocument.substring(arrayOfTestingDocument.indexOf('B') + 1, arrayOfTestingDocument.lastIndexOf("E"))).append("\n"); //====1_TestingDocument-all_Category Comparison.====
    for(int j=1; j<=numberOfCategories; j++){       //j=Category. All the NGramGraph of Categories are iterrated. //System.out.print("\n\nThe Document-" + arrayOfTestingDocument.substring(0, arrayOfTestingDocument.indexOf('B'))); System.out.print(" really belongs in the categories: " + arrayOfTestingDocument.substring(arrayOfTestingDocument.indexOf('B') + 1, arrayOfTestingDocument.lastIndexOf("E")).replaceAll("E", " ")); System.out.println(" and compared with the category "+Integer.toString(j));
        result= GraphComparator.Compare_UNweightedGraphs_Using_CS(current_TestingDocument_NGramGraph,nGramGraphsOfCategories[j]);
        if (result.isNaN()){result=0.0f; }//System.out.println("Under the limited amount of words the text: "+arrayOfTestingDocument);
        testing_Documents_Similarities.append(result).append(" ");
    }testing_Documents_Similarities.setLength(testing_Documents_Similarities.length()-1);
    testing_Documents_Similarities.append("\n");
}//==== End of *Similarity Values* between the |10% of Testing Documents| to |WordGraph Categories|. ====

training_Documents_Similarities.setLength(training_Documents_Similarities.length()-1);
training_Documents_Categories.setLength(training_Documents_Categories.length()-1);
testing_Documents_Similarities.setLength(testing_Documents_Similarities.length()-1);
testing_Document_Categories.setLength(testing_Document_Categories.length()-1);
WriteIntoFilesTrainingTestingData("Fold_"+numbers[nFold],training_Documents_Similarities,training_Documents_Categories,testing_Documents_Similarities,testing_Document_Categories);
for (int i=1; i<=numberOfCategories; i++){nGramGraphsOfCategories[i].clear();} // The hash map is cleaned.
} //End of One fold of the ten folds of 10-Fold-Cross-Validation
}//====================================== End of CategoriesAndDocumentsNumericalRepresentations_CS() ======================================    



//=============================================CategoriesAndDocumentsNumericalRepresentations_TS()============================================== 
/**A text file is read, 45% of Documents are for Word-Graph representation, 45% for the Value Similarities N-vectors,
 * and 10% for testing. The Similarity metric will be used is the --|Tversky Similarity|--.
 * 10-fold cross validation is applied. Each fold involves separate representation word-graphs,Value similarities  N-Vector and
 * testing documents.
 * The method outputs 4 text files for each fold. 
 * training_Documents_Similarities.txt : The N-Vector value similarities of 45% of Training-Documents.
 * training_Documents_Categories.txt   : The Category in which belong each document of the aforementioned Training-Documents.
 * testing_Documents_Similarities.txt  : The N-Vector value similarities of 10% of Testing-Documents.
 * testing_Document_Categories.txt     : The Category in which belong each document of the aforementioned Testing-Documents.
 * @param inputFileName text file that contains all the (training & testing) documents and their categories.
 * @param numberOfCategories the number of distinct categories.
 * @param frame the number of edges will be added for each word. Each word will be a source node and the following words, 
 * contained in a frame, will be terminal nodes. The last words of a paragraph will create less edges than the frame number.*/ 
//============================================================================================================================== 
public static void CategoriesAndDocumentsNumericalRepresentations_TS(String inputFileName, int numberOfCategories, int frame) throws IOException {
ArrayList<String> documents = new ArrayList<>();//The list of Documents.
String[] numbers ={"One","Two","Three","Four","Five","Six","Seven","Eight","Nine","Ten"};
System.out.println("It is being processed the UNweighted Word Graph Classification of "+inputFileName);
ReadDocuments(inputFileName,documents);                                             //The documents from the file are read and assigned in the ArrayList documents.
String[] categoriesOfDocuments = new String[numberOfCategories+1];                  //The categories which contain the "documents" with which we will make the graph categories.
String testDocuments;                                                               //Here we have all the documents which will be tested-classified.
HashMap<String,Boolean>[] nGramGraphsOfCategories=new HashMap[numberOfCategories+1];//
ArrayList<String> documentsForClassificationSimilarities = new ArrayList<>(); //

for (int nFold=0; nFold<=9; nFold++){                                               
System.out.println("*** Fold-"+Integer.toString(nFold+1)+" is in process***");  //One fold of the ten folds of 10-Fold-Cross-Validation
for (int i=0;i<=numberOfCategories;i++){categoriesOfDocuments[i]="";}           //In the begining of the fold each category is Initialized not to include any text.
testDocuments="";                                                               //In the begining of the fold there are not any documents for testing
documentsForClassificationSimilarities.clear();                                 //In the begining of the fold there are not any document similarities values

//==== Assiging |10% of Documents for testing|, |45% for Graph Representation|, |45% for Values Similarities Document-20Categories| ====
Boolean document_for_the_CaetgoryRepresentation=true;
for (int i=0; i<documents.size(); i++){
    if ( ((i) % 10) == nFold)
        {testDocuments+=Integer.toString(i+1)+documents.get(i)+"\n";}           //10% of Documents for testing.
    else{
        //documentsForClassificationSimilarities.add(documents.get(i));//All the Documents of the 90% portion will be included. Changed
        int categoryOfTheDocument;
        String document= documents.get(i);
        if(document_for_the_CaetgoryRepresentation){                            //45% of Documents for Graph Representation.
            categoryOfTheDocument=Integer.parseInt(document.substring(document.indexOf('B')+1, document.indexOf('E')));
            categoriesOfDocuments[categoryOfTheDocument]+=document.substring(document.indexOf("E")+1)+"\n";
            document_for_the_CaetgoryRepresentation=false;}
        else{                                                                   //45% of Documents for Values Similarities Document-20Categories
            document_for_the_CaetgoryRepresentation=true;
            documentsForClassificationSimilarities.add(documents.get(i));}
    }
}//==== End of Assiging |10% of Documents for testing|, |45% for Graph Representation|, |45% for Values Similarities Document-20Categories| ====

//==== Categories WordGraphs construction. ====
for (int i=1; i<=numberOfCategories;i++){nGramGraphsOfCategories[i]=FromDataTo_UNweightedGraph(categoriesOfDocuments[i],frame);}
//==== End of Categories WordGraphs construction. ====

StringBuilder training_Documents_Similarities = new StringBuilder();
StringBuilder training_Documents_Categories   = new StringBuilder();
StringBuilder testing_Documents_Similarities  = new StringBuilder();
StringBuilder testing_Document_Categories     = new StringBuilder();

Float result; //The value of comparison result between a category graph to a document graph.
//==== *Similarity Values* between the |45% of Training Documents| to |WordGraph Categories|. ====
for(String one_documentForClassificationSimilarities: documentsForClassificationSimilarities){
    training_Documents_Categories.append(one_documentForClassificationSimilarities.substring(one_documentForClassificationSimilarities.indexOf('B')+1, one_documentForClassificationSimilarities.indexOf('E'))).append("\n");
    HashMap graphRepresentation_of_one_documentForClassificationSimilarities =FromDataTo_UNweightedGraph(one_documentForClassificationSimilarities.substring(one_documentForClassificationSimilarities.lastIndexOf("E") + 1),frame);
    for(int j=1; j<=numberOfCategories; j++){
        result=GraphComparator.Compare_UNweightedGraphs_Using_TS(graphRepresentation_of_one_documentForClassificationSimilarities, nGramGraphsOfCategories[j]);
        if(result.isNaN()){ System.out.println("Under the limited amount of words the text: "+one_documentForClassificationSimilarities);}
            training_Documents_Similarities.append(result).append(" ");
    }training_Documents_Similarities.setLength(training_Documents_Similarities.length()-1);
    training_Documents_Similarities.append("\n");
}//==== End of *Similarity Values* between the |45% of Training Documents| to |WordGraph Categories|. ====

//==== *Similarity Values* between the |10% of Testing Documents| to |WordGraph Categories|. ====
String[] arrayOfTestingDocuments = testDocuments.split("\n");

for (String arrayOfTestingDocument : arrayOfTestingDocuments) { // i=Document. All testingDocuments are iterrated.
    HashMap current_TestingDocument_NGramGraph;
    current_TestingDocument_NGramGraph = FromDataTo_UNweightedGraph(arrayOfTestingDocument.substring(arrayOfTestingDocument.lastIndexOf("E") + 1),frame);
    testing_Document_Categories.append(arrayOfTestingDocument.substring(arrayOfTestingDocument.indexOf('B') + 1, arrayOfTestingDocument.lastIndexOf("E"))).append("\n"); //====1_TestingDocument-all_Category Comparison.====
    for(int j=1; j<=numberOfCategories; j++){       //j=Category. All the NGramGraph of Categories are iterrated. //System.out.print("\n\nThe Document-" + arrayOfTestingDocument.substring(0, arrayOfTestingDocument.indexOf('B'))); System.out.print(" really belongs in the categories: " + arrayOfTestingDocument.substring(arrayOfTestingDocument.indexOf('B') + 1, arrayOfTestingDocument.lastIndexOf("E")).replaceAll("E", " ")); System.out.println(" and compared with the category "+Integer.toString(j));
        result=(float) GraphComparator.Compare_UNweightedGraphs_Using_TS(current_TestingDocument_NGramGraph,nGramGraphsOfCategories[j]);
        if (result.isNaN()){System.out.println("Under the limited amount of words the text: "+arrayOfTestingDocument);}
        testing_Documents_Similarities.append(result).append(" ");
    }testing_Documents_Similarities.setLength(testing_Documents_Similarities.length()-1);
    testing_Documents_Similarities.append("\n");
}//==== End of *Similarity Values* between the |10% of Testing Documents| to |WordGraph Categories|. ====

training_Documents_Similarities.setLength(training_Documents_Similarities.length()-1);
training_Documents_Categories.setLength(training_Documents_Categories.length()-1);
testing_Documents_Similarities.setLength(testing_Documents_Similarities.length()-1);
testing_Document_Categories.setLength(testing_Document_Categories.length()-1);
WriteIntoFilesTrainingTestingData("Fold_"+numbers[nFold],training_Documents_Similarities,training_Documents_Categories,testing_Documents_Similarities,testing_Document_Categories);
for (int i=1; i<=numberOfCategories; i++){nGramGraphsOfCategories[i].clear();} // The hash map is cleaned.
} //End of One fold of the ten folds of 10-Fold-Cross-Validation
}//====================================== End of CategoriesAndDocumentsNumericalRepresentations_TS() ======================================    



//=============================================CategoriesAndDocumentsNumericalRepresentations_MCSN()============================================== 
/**A text file is read, 45% of Documents are for Word-Graph representation, 45% for the Value Similarities N-vectors,
 * and 10% for testing. The Similarity metric will be used is the --|the Number of Nodes of the Maximum Common Subgraph|--.
 * 10-fold cross validation is applied. Each fold involves separate representation word-graphs,Value similarities  N-Vector and
 * testing documents.
 * The method outputs 4 text files for each fold. 
 * training_Documents_Similarities.txt : The N-Vector value similarities of 45% of Training-Documents.
 * training_Documents_Categories.txt   : The Category in which belong each document of the aforementioned Training-Documents.
 * testing_Documents_Similarities.txt  : The N-Vector value similarities of 10% of Testing-Documents.
 * testing_Document_Categories.txt     : The Category in which belong each document of the aforementioned Testing-Documents.
 * @param inputFileName text file that contains all the (training & testing) documents and their categories.
 * @param numberOfCategories the number of distinct categories.
 * @param frame the number of edges will be added for each word. Each word will be a source node and the following words, 
 * contained in a frame, will be terminal nodes. The last words of a paragraph will create less edges than the frame number.*/ 
//============================================================================================================================== 
public static void CategoriesAndDocumentsNumericalRepresentations_MCSN(String inputFileName, int numberOfCategories, int frame) throws IOException {
ArrayList<String> documents = new ArrayList<>();//The list of Documents.
String[] numbers ={"One","Two","Three","Four","Five","Six","Seven","Eight","Nine","Ten"};
System.out.println("It is being processed the UNweighted Word Graph Classification of "+inputFileName);
ReadDocuments(inputFileName,documents);                                             //The documents from the file are read and assigned in the ArrayList documents.
String[] categoriesOfDocuments = new String[numberOfCategories+1];                  //The categories which contain the "documents" with which we will make the graph categories.
String testDocuments;                                                               //Here we have all the documents which will be tested-classified.
HashMap<String,Boolean>[] nGramGraphsOfCategories=new HashMap[numberOfCategories+1];//
ArrayList<String> documentsForClassificationSimilarities = new ArrayList<>(); //

for (int nFold=0; nFold<=9; nFold++){                                               
System.out.println("*** Fold-"+Integer.toString(nFold+1)+" is in process***");  //One fold of the ten folds of 10-Fold-Cross-Validation
for (int i=0;i<=numberOfCategories;i++){categoriesOfDocuments[i]="";}           //In the begining of the fold each category is Initialized not to include any text.
testDocuments="";                                                               //In the begining of the fold there are not any documents for testing
documentsForClassificationSimilarities.clear();                                 //In the begining of the fold there are not any document similarities values

//==== Assiging |10% of Documents for testing|, |45% for Graph Representation|, |45% for Values Similarities Document-20Categories| ====
Boolean document_for_the_CaetgoryRepresentation=true;
for (int i=0; i<documents.size(); i++){
    if ( ((i) % 10) == nFold)
        {testDocuments+=Integer.toString(i+1)+documents.get(i)+"\n";}           //10% of Documents for testing.
    else{
        //documentsForClassificationSimilarities.add(documents.get(i));//All the Documents of the 90% portion will be included. Changed
        int categoryOfTheDocument;
        String document= documents.get(i);
        if(document_for_the_CaetgoryRepresentation){                            //45% of Documents for Graph Representation.
            categoryOfTheDocument=Integer.parseInt(document.substring(document.indexOf('B')+1, document.indexOf('E')));
            categoriesOfDocuments[categoryOfTheDocument]+=document.substring(document.indexOf("E")+1)+"\n";
            document_for_the_CaetgoryRepresentation=false;}
        else{                                                                   //45% of Documents for Values Similarities Document-20Categories
            document_for_the_CaetgoryRepresentation=true;
            documentsForClassificationSimilarities.add(documents.get(i));}
    }
}//==== End of Assiging |10% of Documents for testing|, |45% for Graph Representation|, |45% for Values Similarities Document-20Categories| ====

//==== Categories WordGraphs construction. ====
for (int i=1; i<=numberOfCategories;i++){nGramGraphsOfCategories[i]=FromDataTo_UNweightedGraph(categoriesOfDocuments[i],frame);}
//==== End of Categories WordGraphs construction. ====

StringBuilder training_Documents_Similarities = new StringBuilder();
StringBuilder training_Documents_Categories   = new StringBuilder();
StringBuilder testing_Documents_Similarities  = new StringBuilder();
StringBuilder testing_Document_Categories     = new StringBuilder();

Float result; //The value of comparison result between a category graph to a document graph.
//==== *Similarity Values* between the |45% of Training Documents| to |WordGraph Categories|. ====
for(String one_documentForClassificationSimilarities: documentsForClassificationSimilarities){
    training_Documents_Categories.append(one_documentForClassificationSimilarities.substring(one_documentForClassificationSimilarities.indexOf('B')+1, one_documentForClassificationSimilarities.indexOf('E'))).append("\n");
    HashMap graphRepresentation_of_one_documentForClassificationSimilarities =FromDataTo_UNweightedGraph(one_documentForClassificationSimilarities.substring(one_documentForClassificationSimilarities.lastIndexOf("E") + 1),frame);
    for(int j=1; j<=numberOfCategories; j++){
        result=GraphComparator.Compare_UNweightedGraphs_Using_MCSN(graphRepresentation_of_one_documentForClassificationSimilarities,nGramGraphsOfCategories[j]);
        if(result.isNaN()) result=0.0f; //System.out.println("Under the limited amount of words the text: "+one_documentForClassificationSimilarities);
            training_Documents_Similarities.append(result).append(" ");
    }training_Documents_Similarities.setLength(training_Documents_Similarities.length()-1);
    training_Documents_Similarities.append("\n");
}//==== End of *Similarity Values* between the |45% of Training Documents| to |WordGraph Categories|. ====

//==== *Similarity Values* between the |10% of Testing Documents| to |WordGraph Categories|. ====
String[] arrayOfTestingDocuments = testDocuments.split("\n");

for (String arrayOfTestingDocument : arrayOfTestingDocuments) { // i=Document. All testingDocuments are iterrated.
    HashMap current_TestingDocument_NGramGraph;
    current_TestingDocument_NGramGraph = FromDataTo_UNweightedGraph(arrayOfTestingDocument.substring(arrayOfTestingDocument.lastIndexOf("E") + 1),frame);
    testing_Document_Categories.append(arrayOfTestingDocument.substring(arrayOfTestingDocument.indexOf('B') + 1, arrayOfTestingDocument.lastIndexOf("E"))).append("\n"); //====1_TestingDocument-all_Category Comparison.====
    for(int j=1; j<=numberOfCategories; j++){       //j=Category. All the NGramGraph of Categories are iterrated. //System.out.print("\n\nThe Document-" + arrayOfTestingDocument.substring(0, arrayOfTestingDocument.indexOf('B'))); System.out.print(" really belongs in the categories: " + arrayOfTestingDocument.substring(arrayOfTestingDocument.indexOf('B') + 1, arrayOfTestingDocument.lastIndexOf("E")).replaceAll("E", " ")); System.out.println(" and compared with the category "+Integer.toString(j));
        result=GraphComparator.Compare_UNweightedGraphs_Using_MCSN(current_TestingDocument_NGramGraph,nGramGraphsOfCategories[j]);
        if (result.isNaN()) result=0.0f; //System.out.println("Under the limited amount of words the text: "+arrayOfTestingDocument);
        testing_Documents_Similarities.append(result).append(" ");
    }testing_Documents_Similarities.setLength(testing_Documents_Similarities.length()-1);
    testing_Documents_Similarities.append("\n");
}//==== End of *Similarity Values* between the |10% of Testing Documents| to |WordGraph Categories|. ====

training_Documents_Similarities.setLength(training_Documents_Similarities.length()-1);
training_Documents_Categories.setLength(training_Documents_Categories.length()-1);
testing_Documents_Similarities.setLength(testing_Documents_Similarities.length()-1);
testing_Document_Categories.setLength(testing_Document_Categories.length()-1);
WriteIntoFilesTrainingTestingData("Fold_"+numbers[nFold],training_Documents_Similarities,training_Documents_Categories,testing_Documents_Similarities,testing_Document_Categories);
for (int i=1; i<=numberOfCategories; i++){nGramGraphsOfCategories[i].clear();} // The hash map is cleaned.
} //End of One fold of the ten folds of 10-Fold-Cross-Validation
}//====================================== End of CategoriesAndDocumentsNumericalRepresentations_MCSN() ======================================    



//=============================================CategoriesAndDocumentsNumericalRepresentations_MCSDE()============================================== 
/**A text file is read, 45% of Documents are for Word-Graph representation, 45% for the Value Similarities N-vectors,
 * and 10% for testing. The Similarity metric will be used is the --|the Number of Directed Edges of the Maximum Common Subgraph|--.
 * 10-fold cross validation is applied. Each fold involves separate representation word-graphs,Value similarities  N-Vector and
 * testing documents.
 * The method outputs 4 text files for each fold. 
 * training_Documents_Similarities.txt : The N-Vector value similarities of 45% of Training-Documents.
 * training_Documents_Categories.txt   : The Category in which belong each document of the aforementioned Training-Documents.
 * testing_Documents_Similarities.txt  : The N-Vector value similarities of 10% of Testing-Documents.
 * testing_Document_Categories.txt     : The Category in which belong each document of the aforementioned Testing-Documents.
 * @param inputFileName text file that contains all the (training & testing) documents and their categories.
 * @param numberOfCategories the number of distinct categories.
 * @param frame the number of edges will be added for each word. Each word will be a source node and the following words, 
 * contained in a frame, will be terminal nodes. The last words of a paragraph will create less edges than the frame number.*/ 
//============================================================================================================================== 
public static void CategoriesAndDocumentsNumericalRepresentations_MCSDE(String inputFileName, int numberOfCategories, int frame) throws IOException {
ArrayList<String> documents = new ArrayList<>();//The list of Documents.
String[] numbers ={"One","Two","Three","Four","Five","Six","Seven","Eight","Nine","Ten"};
System.out.println("It is being processed the UNweighted Word Graph Classification of "+inputFileName);
ReadDocuments(inputFileName,documents);                                             //The documents from the file are read and assigned in the ArrayList documents.
String[] categoriesOfDocuments = new String[numberOfCategories+1];                  //The categories which contain the "documents" with which we will make the graph categories.
String testDocuments;                                                               //Here we have all the documents which will be tested-classified.
HashMap<String,Boolean>[] nGramGraphsOfCategories=new HashMap[numberOfCategories+1];//
ArrayList<String> documentsForClassificationSimilarities = new ArrayList<>(); //

for (int nFold=0; nFold<=9; nFold++){                                               
System.out.println("*** Fold-"+Integer.toString(nFold+1)+" is in process***");  //One fold of the ten folds of 10-Fold-Cross-Validation
for (int i=0;i<=numberOfCategories;i++){categoriesOfDocuments[i]="";}           //In the begining of the fold each category is Initialized not to include any text.
testDocuments="";                                                               //In the begining of the fold there are not any documents for testing
documentsForClassificationSimilarities.clear();                                 //In the begining of the fold there are not any document similarities values

//==== Assiging |10% of Documents for testing|, |45% for Graph Representation|, |45% for Values Similarities Document-20Categories| ====
Boolean document_for_the_CaetgoryRepresentation=true;
for (int i=0; i<documents.size(); i++){
    if ( ((i) % 10) == nFold)
        {testDocuments+=Integer.toString(i+1)+documents.get(i)+"\n";}           //10% of Documents for testing.
    else{
        //documentsForClassificationSimilarities.add(documents.get(i));//All the Documents of the 90% portion will be included. Changed
        int categoryOfTheDocument;
        String document= documents.get(i);
        if(document_for_the_CaetgoryRepresentation){                            //45% of Documents for Graph Representation.
            categoryOfTheDocument=Integer.parseInt(document.substring(document.indexOf('B')+1, document.indexOf('E')));
            categoriesOfDocuments[categoryOfTheDocument]+=document.substring(document.indexOf("E")+1)+"\n";
            document_for_the_CaetgoryRepresentation=false;}
        else{                                                                   //45% of Documents for Values Similarities Document-20Categories
            document_for_the_CaetgoryRepresentation=true;
            documentsForClassificationSimilarities.add(documents.get(i));}
    }
}//==== End of Assiging |10% of Documents for testing|, |45% for Graph Representation|, |45% for Values Similarities Document-20Categories| ====

//==== Categories WordGraphs construction. ====
for (int i=1; i<=numberOfCategories;i++){nGramGraphsOfCategories[i]=FromDataTo_UNweightedGraph(categoriesOfDocuments[i],frame);}
//==== End of Categories WordGraphs construction. ====

StringBuilder training_Documents_Similarities = new StringBuilder();
StringBuilder training_Documents_Categories   = new StringBuilder();
StringBuilder testing_Documents_Similarities  = new StringBuilder();
StringBuilder testing_Document_Categories     = new StringBuilder();

Float result; //The value of comparison result between a category graph to a document graph.
//==== *Similarity Values* between the |45% of Training Documents| to |WordGraph Categories|. ====
for(String one_documentForClassificationSimilarities: documentsForClassificationSimilarities){
    training_Documents_Categories.append(one_documentForClassificationSimilarities.substring(one_documentForClassificationSimilarities.indexOf('B')+1, one_documentForClassificationSimilarities.indexOf('E'))).append("\n");
    HashMap graphRepresentation_of_one_documentForClassificationSimilarities =FromDataTo_UNweightedGraph(one_documentForClassificationSimilarities.substring(one_documentForClassificationSimilarities.lastIndexOf("E") + 1),frame);
    for(int j=1; j<=numberOfCategories; j++){
        result= GraphComparator.Compare_UNweightedGraphs_Using_MCSDE(graphRepresentation_of_one_documentForClassificationSimilarities,nGramGraphsOfCategories[j]);
        if(result.isNaN()) result=0.0f; //System.out.println("Under the limited amount of words the text: "+one_documentForClassificationSimilarities);
            training_Documents_Similarities.append(result).append(" ");
    }training_Documents_Similarities.setLength(training_Documents_Similarities.length()-1);
    training_Documents_Similarities.append("\n");
}//==== End of *Similarity Values* between the |45% of Training Documents| to |WordGraph Categories|. ====

//==== *Similarity Values* between the |10% of Testing Documents| to |WordGraph Categories|. ====
String[] arrayOfTestingDocuments = testDocuments.split("\n");

for (String arrayOfTestingDocument : arrayOfTestingDocuments) { // i=Document. All testingDocuments are iterrated.
    HashMap current_TestingDocument_NGramGraph;
    current_TestingDocument_NGramGraph = FromDataTo_UNweightedGraph(arrayOfTestingDocument.substring(arrayOfTestingDocument.lastIndexOf("E") + 1),frame);
    testing_Document_Categories.append(arrayOfTestingDocument.substring(arrayOfTestingDocument.indexOf('B') + 1, arrayOfTestingDocument.lastIndexOf("E"))).append("\n"); //====1_TestingDocument-all_Category Comparison.====
    for(int j=1; j<=numberOfCategories; j++){       //j=Category. All the NGramGraph of Categories are iterrated. //System.out.print("\n\nThe Document-" + arrayOfTestingDocument.substring(0, arrayOfTestingDocument.indexOf('B'))); System.out.print(" really belongs in the categories: " + arrayOfTestingDocument.substring(arrayOfTestingDocument.indexOf('B') + 1, arrayOfTestingDocument.lastIndexOf("E")).replaceAll("E", " ")); System.out.println(" and compared with the category "+Integer.toString(j));
        result= GraphComparator.Compare_UNweightedGraphs_Using_MCSDE(current_TestingDocument_NGramGraph,nGramGraphsOfCategories[j]);
        if (result.isNaN())result=0.0f;//System.out.println("Under the limited amount of words the text: "+arrayOfTestingDocument);
        testing_Documents_Similarities.append(result).append(" ");
    }testing_Documents_Similarities.setLength(testing_Documents_Similarities.length()-1);
    testing_Documents_Similarities.append("\n");
}//==== End of *Similarity Values* between the |10% of Testing Documents| to |WordGraph Categories|. ====

training_Documents_Similarities.setLength(training_Documents_Similarities.length()-1);
training_Documents_Categories.setLength(training_Documents_Categories.length()-1);
testing_Documents_Similarities.setLength(testing_Documents_Similarities.length()-1);
testing_Document_Categories.setLength(testing_Document_Categories.length()-1);
WriteIntoFilesTrainingTestingData("Fold_"+numbers[nFold],training_Documents_Similarities,training_Documents_Categories,testing_Documents_Similarities,testing_Document_Categories);
for (int i=1; i<=numberOfCategories; i++){nGramGraphsOfCategories[i].clear();} // The hash map is cleaned.
} //End of One fold of the ten folds of 10-Fold-Cross-Validation
}//====================================== End of CategoriesAndDocumentsNumericalRepresentations_MCSDE() ======================================    



//=============================================CategoriesAndDocumentsNumericalRepresentations_MCSUE()================================================ 
/**A text file is read, 45% of Documents are for Word-Graph representation, 45% for the Value Similarities N-vectors,
 * and 10% for testing. The Similarity metric will be used is the --|the Number of UNdirected Edges of the Maximum Common Subgraph|--.
 * 10-fold cross validation is applied. Each fold involves separate representation word-graphs,Value similarities  N-Vector and
 * testing documents.
 * The method outputs 4 text files for each fold. 
 * training_Documents_Similarities.txt : The N-Vector value similarities of 45% of Training-Documents.
 * training_Documents_Categories.txt   : The Category in which belong each document of the aforementioned Training-Documents.
 * testing_Documents_Similarities.txt  : The N-Vector value similarities of 10% of Testing-Documents.
 * testing_Document_Categories.txt     : The Category in which belong each document of the aforementioned Testing-Documents.
 * @param inputFileName text file that contains all the (training & testing) documents and their categories.
 * @param numberOfCategories the number of distinct categories.
 * @param frame the number of edges will be added for each word. Each word will be a source node and the following words, 
 * contained in a frame, will be terminal nodes. The last words of a paragraph will create less edges than the frame number.*/ 
//================================================================================================================================ 
public static void CategoriesAndDocumentsNumericalRepresentations_MCSUE(String inputFileName, int numberOfCategories, int frame) throws IOException  {
ArrayList<String> documents = new ArrayList<>();//The list of Documents.
String[] numbers ={"One","Two","Three","Four","Five","Six","Seven","Eight","Nine","Ten"};
System.out.println("It is being processed the UNweighted Word Graph Classification of "+inputFileName);
ReadDocuments(inputFileName,documents);                                             //The documents from the file are read and assigned in the ArrayList documents.
String[] categoriesOfDocuments = new String[numberOfCategories+1];                  //The categories which contain the "documents" with which we will make the graph categories.
String testDocuments;                                                               //Here we have all the documents which will be tested-classified.
HashMap<String,Boolean>[] nGramGraphsOfCategories=new HashMap[numberOfCategories+1];//
ArrayList<String> documentsForClassificationSimilarities = new ArrayList<>(); //

for (int nFold=0; nFold<=9; nFold++){                                               
System.out.println("*** Fold-"+Integer.toString(nFold+1)+" is in process***");  //One fold of the ten folds of 10-Fold-Cross-Validation
for (int i=0;i<=numberOfCategories;i++){categoriesOfDocuments[i]="";}           //In the begining of the fold each category is Initialized not to include any text.
testDocuments="";                                                               //In the begining of the fold there are not any documents for testing
documentsForClassificationSimilarities.clear();                                 //In the begining of the fold there are not any document similarities values

//==== Assiging |10% of Documents for testing|, |45% for Graph Representation|, |45% for Values Similarities Document-20Categories| ====
Boolean document_for_the_CaetgoryRepresentation=true;
for (int i=0; i<documents.size(); i++){
    if ( ((i) % 10) == nFold)
        {testDocuments+=Integer.toString(i+1)+documents.get(i)+"\n";}           //10% of Documents for testing.
    else{
        //documentsForClassificationSimilarities.add(documents.get(i));//All the Documents of the 90% portion will be included. Changed
        int categoryOfTheDocument;
        String document= documents.get(i);
        if(document_for_the_CaetgoryRepresentation){                            //45% of Documents for Graph Representation.
            categoryOfTheDocument=Integer.parseInt(document.substring(document.indexOf('B')+1, document.indexOf('E')));
            categoriesOfDocuments[categoryOfTheDocument]+=document.substring(document.indexOf("E")+1)+"\n";
            document_for_the_CaetgoryRepresentation=false;}
        else{                                                                   //45% of Documents for Values Similarities Document-20Categories
            document_for_the_CaetgoryRepresentation=true;
            documentsForClassificationSimilarities.add(documents.get(i));}
    }
}//==== End of Assiging |10% of Documents for testing|, |45% for Graph Representation|, |45% for Values Similarities Document-20Categories| ====

//==== Categories WordGraphs construction. ====
for (int i=1; i<=numberOfCategories;i++){nGramGraphsOfCategories[i]=FromDataTo_UNweightedGraph(categoriesOfDocuments[i],frame);}
//==== End of Categories WordGraphs construction. ====

StringBuilder training_Documents_Similarities = new StringBuilder();
StringBuilder training_Documents_Categories   = new StringBuilder();
StringBuilder testing_Documents_Similarities  = new StringBuilder();
StringBuilder testing_Document_Categories     = new StringBuilder();

Float result; //The value of comparison result between a category graph to a document graph.
//==== *Similarity Values* between the |45% of Training Documents| to |WordGraph Categories|. ====
for(String one_documentForClassificationSimilarities: documentsForClassificationSimilarities){
    training_Documents_Categories.append(one_documentForClassificationSimilarities.substring(one_documentForClassificationSimilarities.indexOf('B')+1, one_documentForClassificationSimilarities.indexOf('E'))).append("\n");
    HashMap graphRepresentation_of_one_documentForClassificationSimilarities =FromDataTo_UNweightedGraph(one_documentForClassificationSimilarities.substring(one_documentForClassificationSimilarities.lastIndexOf("E") + 1),frame);
    for(int j=1; j<=numberOfCategories; j++){
        result= GraphComparator.Compare_UNweightedGraphs_Using_MCSUE(graphRepresentation_of_one_documentForClassificationSimilarities,nGramGraphsOfCategories[j]);
        if(result.isNaN())result=0.0f; //System.out.println("Under the limited amount of words the text: "+one_documentForClassificationSimilarities);
            training_Documents_Similarities.append(result).append(" ");
    }training_Documents_Similarities.setLength(training_Documents_Similarities.length()-1);
    training_Documents_Similarities.append("\n");
}//==== End of *Similarity Values* between the |45% of Training Documents| to |WordGraph Categories|. ====

//==== *Similarity Values* between the |10% of Testing Documents| to |WordGraph Categories|. ====
String[] arrayOfTestingDocuments = testDocuments.split("\n");

for (String arrayOfTestingDocument : arrayOfTestingDocuments) { // i=Document. All testingDocuments are iterrated.
    HashMap current_TestingDocument_NGramGraph;
    current_TestingDocument_NGramGraph = FromDataTo_UNweightedGraph(arrayOfTestingDocument.substring(arrayOfTestingDocument.lastIndexOf("E") + 1),frame);
    testing_Document_Categories.append(arrayOfTestingDocument.substring(arrayOfTestingDocument.indexOf('B') + 1, arrayOfTestingDocument.lastIndexOf("E"))).append("\n"); //====1_TestingDocument-all_Category Comparison.====
    for(int j=1; j<=numberOfCategories; j++){       //j=Category. All the NGramGraph of Categories are iterrated. //System.out.print("\n\nThe Document-" + arrayOfTestingDocument.substring(0, arrayOfTestingDocument.indexOf('B'))); System.out.print(" really belongs in the categories: " + arrayOfTestingDocument.substring(arrayOfTestingDocument.indexOf('B') + 1, arrayOfTestingDocument.lastIndexOf("E")).replaceAll("E", " ")); System.out.println(" and compared with the category "+Integer.toString(j));
        result=GraphComparator.Compare_UNweightedGraphs_Using_MCSUE(current_TestingDocument_NGramGraph,nGramGraphsOfCategories[j]);
        if (result.isNaN()) result=0.0f; //System.out.println("Under the limited amount of words the text: "+arrayOfTestingDocument);
        testing_Documents_Similarities.append(result).append(" ");
    }testing_Documents_Similarities.setLength(testing_Documents_Similarities.length()-1);
    testing_Documents_Similarities.append("\n");
}//==== End of *Similarity Values* between the |10% of Testing Documents| to |WordGraph Categories|. ====

training_Documents_Similarities.setLength(training_Documents_Similarities.length()-1);
training_Documents_Categories.setLength(training_Documents_Categories.length()-1);
testing_Documents_Similarities.setLength(testing_Documents_Similarities.length()-1);
testing_Document_Categories.setLength(testing_Document_Categories.length()-1);
WriteIntoFilesTrainingTestingData("Fold_"+numbers[nFold],training_Documents_Similarities,training_Documents_Categories,testing_Documents_Similarities,testing_Document_Categories);
for (int i=1; i<=numberOfCategories; i++){nGramGraphsOfCategories[i].clear();} // The hash map is cleaned.
} //End of One fold of the ten folds of 10-Fold-Cross-Validation
}//====================================== End of CategoriesAndDocumentsNumericalRepresentations_MCSUE() ======================================    




//=============================================CategoriesAndDocumentsNumericalRepresentations_CS_MI()============================================== 
/**A text file is read, 45% of Documents are for Word-Graph representation, 45% for the Value Similarities N-vectors,
 * and 10% for testing. The Similarity metric will be used is the --|Containment Similarity|--.
 * 10-fold cross validation is applied. Each fold involves separate representation word-graphs,Value similarities  N-Vector and
 * testing documents.
 * The Mutual Information Feature Selection method has been applied to filter out an amount of Edges.
 * The method outputs 4 text files for each fold. 
 * training_Documents_Similarities.txt : The N-Vector value similarities of 45% of Training-Documents.
 * training_Documents_Categories.txt   : The Category in which belong each document of the aforementioned Training-Documents.
 * testing_Documents_Similarities.txt  : The N-Vector value similarities of 10% of Testing-Documents.
 * testing_Document_Categories.txt     : The Category in which belong each document of the aforementioned Testing-Documents.
 * @param inputFileName text file that contains all the (training & testing) documents and their categories.
 * @param numberOfCategories the number of distinct categories.
 * @param frame the number of edges will be added for each word. Each word will be a source node and the following words, 
 * contained in a frame, will be terminal nodes. The last words of a paragraph will create less edges than the frame number.*/ 
//============================================================================================================================== 
public static void CategoriesAndDocumentsNumericalRepresentations_CS_MI(String inputFileName, int numberOfCategories, int frame) throws IOException{
ArrayList<String> documents = new ArrayList<>();                                    //The list of all the Documents.
String[] numbers ={"One","Two","Three","Four","Five","Six","Seven","Eight","Nine","Ten"};
System.out.println("It is being processed the UNweighted Word Graph Classification of "+inputFileName+" with Mutual Information method as Feature Selection for Edges.");
ReadDocuments(inputFileName,documents);                                             //The documents from the file are read and assigned in the ArrayList documents.
String[] categoriesOfDocuments = new String[numberOfCategories+1];                  //The categories which contain the "documents" with which we will make the graph categories.
String testDocuments;                                                               //Here we have all the documents which will be tested-classified.
HashMap<String,Boolean>[] nGramGraphsOfCategories=new HashMap[numberOfCategories+1];//
ArrayList<String> documentsForClassificationSimilarities = new ArrayList<>(); //

for (int nFold=0; nFold<=9; nFold++){                                               
System.out.println("*** Fold-"+Integer.toString(nFold+1)+" is in process***");  //One fold of the ten folds of 10-Fold-Cross-Validation
HashSet FilteredOutEdges =FeatureSelectionTechniques.MutualInformation_Avg(documents, numberOfCategories, frame, 0.125f, nFold); //The set of Words, which will be filtered out. SOS TODO to threshold =0.08f
System.out.println("The edges that will be discarded: "+FilteredOutEdges);
for (int i=0;i<=numberOfCategories;i++){categoriesOfDocuments[i]="";}           //In the begining of the fold each category is Initialized not to include any text.
testDocuments="";                                                               //In the begining of the fold there are not any documents for testing
documentsForClassificationSimilarities.clear();                                 //In the begining of the fold there are not any document similarities values

//==== Assiging |10% of Documents for testing|, |45% for Graph Representation|, |45% for Values Similarities Document-20Categories| ====
Boolean document_for_the_CaetgoryRepresentation=true;
for (int i=0; i<documents.size(); i++){
    if ( ((i) % 10) == nFold)
        {testDocuments+=Integer.toString(i+1)+documents.get(i)+"\n";}           //10% of Documents for testing.
    else{
        //documentsForClassificationSimilarities.add(documents.get(i));//All the Documents of the 90% portion will be included. Changed
        int categoryOfTheDocument;
        String document= documents.get(i);
        if(document_for_the_CaetgoryRepresentation){                            //45% of Documents for Graph Representation.
            categoryOfTheDocument=Integer.parseInt(document.substring(document.indexOf('B')+1, document.indexOf('E')));
            categoriesOfDocuments[categoryOfTheDocument]+=document.substring(document.indexOf("E")+1)+"\n";
            document_for_the_CaetgoryRepresentation=false;}
        else{                                                                   //45% of Documents for Values Similarities Document-20Categories
            document_for_the_CaetgoryRepresentation=true;
            documentsForClassificationSimilarities.add(documents.get(i));}
    }
}//==== End of Assiging |10% of Documents for testing|, |45% for Graph Representation|, |45% for Values Similarities Document-20Categories| ====

//==== Categories WordGraphs construction. ====
for (int i=1; i<=numberOfCategories;i++){nGramGraphsOfCategories[i]=FromDataTo_UNweightedGraph(categoriesOfDocuments[i],frame,FilteredOutEdges);}//New The set of edges that will be filtered out.
//==== End of Categories WordGraphs construction. ====

StringBuilder training_Documents_Similarities = new StringBuilder();
StringBuilder training_Documents_Categories   = new StringBuilder();
StringBuilder testing_Documents_Similarities  = new StringBuilder();
StringBuilder testing_Document_Categories     = new StringBuilder();

Float result; //The value of comparison result between a category graph to a document graph.
//==== *Similarity Values* between the |45% of Training Documents| to |WordGraph Categories|. ====
for(String one_documentForClassificationSimilarities: documentsForClassificationSimilarities){
    training_Documents_Categories.append(one_documentForClassificationSimilarities.substring(one_documentForClassificationSimilarities.indexOf('B')+1, one_documentForClassificationSimilarities.indexOf('E'))).append("\n");
    HashMap graphRepresentation_of_one_documentForClassificationSimilarities =FromDataTo_UNweightedGraph(one_documentForClassificationSimilarities.substring(one_documentForClassificationSimilarities.lastIndexOf("E") + 1),frame,FilteredOutEdges);
    for(int j=1; j<=numberOfCategories; j++){
        result=GraphComparator.Compare_UNweightedGraphs_Using_CS(graphRepresentation_of_one_documentForClassificationSimilarities,nGramGraphsOfCategories[j]);
        if(result.isNaN()) { result=0.0f;} //System.out.println("No common Edges for the text: "+one_documentForClassificationSimilarities);  result=0.0f;}
            training_Documents_Similarities.append(result).append(" ");
    }training_Documents_Similarities.setLength(training_Documents_Similarities.length()-1);
    training_Documents_Similarities.append("\n");
}//==== End of *Similarity Values* between the |45% of Training Documents| to |WordGraph Categories|. ====

//==== *Similarity Values* between the |10% of Testing Documents| to |WordGraph Categories|. ====
String[] arrayOfTestingDocuments = testDocuments.split("\n");

for (String arrayOfTestingDocument : arrayOfTestingDocuments) { // All testingDocuments are iterrated.
    HashMap current_TestingDocument_NGramGraph;
    current_TestingDocument_NGramGraph = FromDataTo_UNweightedGraph(arrayOfTestingDocument.substring(arrayOfTestingDocument.lastIndexOf("E") + 1),frame,FilteredOutEdges);//New The set of edges that will be filtered out.
    testing_Document_Categories.append(arrayOfTestingDocument.substring(arrayOfTestingDocument.indexOf('B') + 1, arrayOfTestingDocument.lastIndexOf("E"))).append("\n"); //====1_TestingDocument-all_Category Comparison.====
    for(int j=1; j<=numberOfCategories; j++){       //j=Category. All the NGramGraph of Categories are iterrated. //System.out.print("\n\nThe Document-" + arrayOfTestingDocument.substring(0, arrayOfTestingDocument.indexOf('B'))); System.out.print(" really belongs in the categories: " + arrayOfTestingDocument.substring(arrayOfTestingDocument.indexOf('B') + 1, arrayOfTestingDocument.lastIndexOf("E")).replaceAll("E", " ")); System.out.println(" and compared with the category "+Integer.toString(j));
        result= GraphComparator.Compare_UNweightedGraphs_Using_CS(current_TestingDocument_NGramGraph,nGramGraphsOfCategories[j]);
        if (result.isNaN()){result=0.0f;}//System.out.println("No common Edges for the text: "+arrayOfTestingDocument);  result=0.0f;}
        testing_Documents_Similarities.append(result).append(" ");
    }testing_Documents_Similarities.setLength(testing_Documents_Similarities.length()-1);
    testing_Documents_Similarities.append("\n");
}//==== End of *Similarity Values* between the |10% of Testing Documents| to |WordGraph Categories|. ====

training_Documents_Similarities.setLength(training_Documents_Similarities.length()-1);
training_Documents_Categories.setLength(training_Documents_Categories.length()-1);
testing_Documents_Similarities.setLength(testing_Documents_Similarities.length()-1);
testing_Document_Categories.setLength(testing_Document_Categories.length()-1);
WriteIntoFilesTrainingTestingData("Fold_"+numbers[nFold],training_Documents_Similarities,training_Documents_Categories,testing_Documents_Similarities,testing_Document_Categories);
for (int i=1; i<=numberOfCategories; i++){nGramGraphsOfCategories[i].clear();} // The hash map is cleaned.
} //End of One fold of the ten folds of 10-Fold-Cross-Validation
}//====================================== End of CategoriesAndDocumentsNumericalRepresentations_CS_MI() ======================================   

}//======================================== End of Class DimensionalRepresentation ========================================
