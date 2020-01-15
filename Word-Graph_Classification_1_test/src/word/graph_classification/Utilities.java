package word.graph_classification;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import org.apache.commons.lang3.StringUtils;

/** A set of ancillary methods to choose the user the filename, the frame size, the number of categories and the type of Graphs.
* @since 19-6-2015
* @version 1.2
* @author John Violos */
public class Utilities {
//============================================ WriteIntoFilesTrainingTestingData() ==================================================
/** The method outputs to the path four text Files for a fold.
 * The method is callable by the CategoriesAndDocumentsNumericalRepresentations_CS().    
* @param fold The fold which took place is a prefix in the output file.
* @param training_Documents_Similarities : The N-Vector value similarities of 45% of Training-Documents (.txt file).
* @param training_Documents_Categories   : The Category in which belong each document of the aforementioned Training-Documents (.txt file).
* @param testing_Documents_Similarities  : The N-Vector value similarities of 10% of Testing-Documents (.txt file).
* @param testing_Document_Categories     : The Category in which belong each document of the aforementioned Testing-Documents (.txt file). */    
//===================================================================================================================================  
static public void WriteIntoFilesTrainingTestingData(String fold, StringBuilder training_Documents_Similarities, StringBuilder training_Documents_Categories, StringBuilder testing_Documents_Similarities, StringBuilder testing_Document_Categories) throws IOException{
String path="C:\\Python34\\Evaluation_WordGraphsClassification\\";
    
BufferedWriter bwr = new BufferedWriter(new FileWriter(new File(path+fold+"_training_Documents_Similarities.txt")));
bwr.write(training_Documents_Similarities.toString());
bwr.flush();
bwr.close();

BufferedWriter bwr2 = new BufferedWriter(new FileWriter(new File(path+fold+"_training_Documents_Categories.txt")));
bwr2.write(training_Documents_Categories.toString());
bwr2.flush();
bwr2.close();

BufferedWriter bwr3 = new BufferedWriter(new FileWriter(new File(path+fold+"_testing_Documents_Similarities.txt")));
bwr3.write(testing_Documents_Similarities.toString());
bwr3.flush();
bwr3.close();

BufferedWriter bwr4 = new BufferedWriter(new FileWriter(new File(path+fold+"_testing_Document_Categories.txt")));
bwr4.write(testing_Document_Categories.toString());
bwr4.flush();
bwr4.close();
}//======================================== End of WriteIntoFilesTrainingTestingData() =============================================    



//=========================================== InputFileName() ================================================
/** Prompts the User to type the filename that includes all the Documents for training and testing. 
* The method checks the existence of the filename but it does not read the Documents from the file.
* @return The filename that includes the Documents will be processed. */
//============================================================================================================   
static public String InputFileName(){
Boolean rightInput=false;
String inputFileName="";
do{
System.out.println("The file name which contains the Categories and the Documents.");
Scanner keyboard = new Scanner(System.in);
System.out.print("input: ");
inputFileName  = keyboard.next();
File f = new File(inputFileName);
if(f.exists() && !f.isDirectory()) { rightInput=true; }
else{System.out.println("Wrong input.");}
}while(!rightInput);
return inputFileName;
}//====================================== End of Class InputFileName() ======================================


//=========================================== ReadDocuments() ================================================
/** We read the documents from the file and we put them in the ArrayList documents.
 * @param fileName The filename that contains the Documents.
 * @param documents An ArrayList that contains in each cell the Document that included in the fileName.*/    
//============================================================================================================
static public void ReadDocuments(String fileName, ArrayList<String> documents) throws FileNotFoundException, IOException{
BufferedReader br = new BufferedReader(new FileReader(fileName));
String line;
int document_size;                      //The number of words contained in a document.
int discarded_documents=0;
while((line =br.readLine()) != null){
        document_size=StringUtils.countMatches(line, " ");
    if (document_size>=0)
        documents.add(line);
    else{
    System.out.println("discarded document: "+line);
    discarded_documents++;
    }
}br.close();    
System.out.println(discarded_documents+ " documents are discarded.");
}//====================================== End of Class ReadDocuments() ======================================



static public void ReadDocumentsTests(String fileName) throws FileNotFoundException, IOException{
BufferedReader br = new BufferedReader(new FileReader(fileName));
String line;
int size;
while((line =br.readLine()) != null){
    size=StringUtils.countMatches(line, " ");
    if(size==2)
        System.out.println("The document has "+size+" words: "+line);
    else if(size==3)
        System.out.println("The document has "+size+" words: "+line);
    else if(size==4)
        System.out.println("The document has "+size+" words: "+line);
    else if(size==5)
        System.out.println("The document has "+size+" words: "+line);
    else if(size==6)
        System.out.println("The document has "+size+" words: "+line);
    else if(size==7)
        System.out.println("The document has "+size+" words: "+line);
    else if(size==8)
        System.out.println("The document has "+size+" words: "+line);
    else if(size==9)
        System.out.println("The document has "+size+" words: "+line);
    else if(size==10)
        System.out.println("The document has "+size+" words: "+line);
    else if(size<=15)
        System.out.println("The document has "+size+" words: "+line);

}br.close();    
}//====================================== End of Class ReadDocumentsTests() ======================================



//=========================================== MethodChoice() =================================================
/** Prompts the User to select the type of Graphs (Weighted/Unweighted) and the Feature Selection Method Mutual Information.
 * @return The method has been chosen. */
//============================================================================================================
static public String MethodChoice(){
Boolean rightInput=true;
String methodChoice,featureSelection;
do{
System.out.print("Available Graph Similarity methods: \nCS (Containment Similarity), MCSN (Maximum Common Subgraph Nodes Sum), ");
System.out.print("MCSDE (Maximum Common Subgraph Directed Edges Sum), \nMCSUE (Maximum Common Subgraph Undirected Edges Sum), ");
System.out.println("TS (Tversky Similarity)");
System.out.print("The Graph Similarity measure will be used. \ninput: ");
Scanner keyboard = new Scanner(System.in);
methodChoice  = keyboard.next().toUpperCase();
if(methodChoice.equals("CS")||methodChoice.equals("MCSN")||methodChoice.equals("MCSDE")||methodChoice.equals("MCSUE")||methodChoice.equals("TS")) 
break ;
System.out.println("\n\n\n\nWrong input. Available methods to type: CS, MCSN, MCSDE, MCSUE, TS.");
}while(rightInput);

do{
System.out.print("For Mutual Information Feature Selection technique type MI\n");
System.out.print("For no Feature Selection techniques type NO\ninput: ");
Scanner keyboard = new Scanner(System.in);
featureSelection  = keyboard.next().toUpperCase();
if(featureSelection.equals("NO"))break;
if(featureSelection.equals("MI")){methodChoice+="_MI"; break;}
System.out.println("\n\n\n\nWrong input. Available answers MI or NO.");
}while(true);
return methodChoice;
}//====================================== End of Class MethodChoice() ======================================



//======================================== NumberOfCategories() ==============================================
/** Prompts the User to type the number of the Categories included in the dataset. 
* @return The number of the Categories in which the Documents belong. */
//============================================================================================================
static public int NumberOfCategories(){
int numberOfCategories;
Scanner keyboard = new Scanner(System.in);
System.out.print("The number of categories. \ninput: ");
numberOfCategories = keyboard.nextInt();
return numberOfCategories;
}//====================================== End of Class NumberOfCategories() ======================================



//============================================= FrameSize() ===================================================
/** Prompts the User to type the window size that defines the distance between Words that assumed as neighbors and connected by an edge.
 * @return The Number "N" of the edges will be created having as Source Node each Word and as Terminal Nodes the "N" following Words. */
//============================================================================================================
static public int FrameSize(){
int frameSize;
Scanner keyboard = new Scanner(System.in);
System.out.print("The Word frame size. \ninput: ");
frameSize = keyboard.nextInt();
return frameSize;
}//====================================== End of Class FrameSize() ======================================



//===================================== Choiceof_Classification_or_GraphRepresentation() ===============================
/** Prompts the User to choose if the Corpus will be represented using Word Graphs (for further research),
 * or the process of the Document Classification will be applied.
 * @return GR (Graph Representation) or DC (Document Classification) according to the user's choice. */
//============================================================================================================
public static String  Choiceof_Classification_or_GraphRepresentation(){
Boolean rightInput=true;
String methodChoice;
do{
System.out.print("For Document Classification type DC.\nFor Corpus Graph Representation type GR.\ninput: ");    
Scanner keyboard = new Scanner(System.in);
methodChoice  = keyboard.next().toUpperCase();
if(methodChoice.equals("GR")||methodChoice.equals("DC")) 
break ;
System.out.println("\nWrong input. Available methods to type: GR, DC.");
}while(rightInput);
return methodChoice;
}//================================= End ofChoiceof_Classification_or_GraphRepresentation() ============================

}//======================================== End of Class Utilities. ========================================
