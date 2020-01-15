package word.graph_classification;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import static java.lang.System.exit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

/** The Construction of Graphs that represent a document or a category of documents. Methods for Weighted and Unweighted Graphs included.
* @since 12-15-2015
* @version 1.2
* @author John Violos */
public class GraphCreator {
//==========================================================FromDataTo_UNweightedGraph()==============================================================================
/** A set of documents or a single document will be represented by an Unweighted Graph. The method provides the assortment of the Edges the Graph involves.
* @param documentsInput A string that includes all the documents from which the representation Graph will be made. 
* The reserved character "N" works as delimiter between paragraphs and documents.
* @param frame The frame determines the number of edges will be created to join each Word-Node to the following Words-Nodes. 
* There is no restriction about the size of the frame.
* @return The edges comprised by the Graph. The data structure of HashMap is used. 
* The key denotes two Nodes that are adjacent in the form Word1_Word2 and the value returns True.*/
//=========================================================================================================================================================================
public static HashMap FromDataTo_UNweightedGraph(String documentsInput, int frame){
HashMap<String, Boolean>  categoryGraph = new HashMap<>();
documentsInput=documentsInput.replaceAll("(\\W*)N(\\W*)","\n");
documentsInput=documentsInput.replaceAll(" \\w{1,2} ", " ");         //The "words" that have less than 2 characters are ommited.
documentsInput=documentsInput.replaceAll(" +"," ");                 //Many spaces "   " are replaced by one space " ".
int currentWord;
int position_of_word;
String[] linesOfdataInput = documentsInput.split("\n");             // Each line-cell of the array linesOfdataInput is a line-String from dataInput. This line after processing will be part of the three gram graph.
int numberOfLines = linesOfdataInput.length;                        // We split the String dataInput into the cells of the array linesOfdataInput.
for (int currentLine=0;currentLine<numberOfLines;currentLine++){    // We iterate the cell-lines of the array linesOfdataInput one by one in order to add the corresponded edges in the graph.
    String[] words= linesOfdataInput[currentLine].split(" ");
        for(currentWord=0;currentWord<words.length ;currentWord++){ // The words of the line are iterrated one by one.
            for(position_of_word=currentWord+1; position_of_word<=currentWord + frame ;position_of_word++){
                if(position_of_word<words.length)
                categoryGraph.put(words[currentWord]+"_"+words[position_of_word],Boolean.TRUE);
                }
        }
    }
//System.out.println("The document has the form: "+documentsInput);
return categoryGraph;
}//===================================================== End ofFromDataTo_UNweightedGraph() =============================================================================  

//==========================================================FromDataTo_UNweightedGraph()==============================================================================
/** A set of documents or a single document will be represented by an Unweighted Graph. The method provides the assortment of the Edges the Graph involves.
* It also filters out a set of Edges according to a Feature Selection Method.
* @param documentsInput A string that includes all the documents from which the representation Graph will be made. 
* The reserved character "N" works as delimiter between paragraphs and documents.
* @param frame The frame determines the number of edges will be created to join each Word-Node to the following Words-Nodes. 
* There is no restriction about the size of the frame.
* @param filterOutEdges The Edges which will be filtered out and will not be included in the Word Graph.
* @return The edges comprised by the Graph. The data structure of HashMap is used. 
* The key denotes two Nodes that are adjacent in the form Word1_Word2 and the value returns True.*/
//=========================================================================================================================================================================
public static HashMap FromDataTo_UNweightedGraph(String documentsInput, int frame, HashSet filterOutEdges){
HashMap<String, Boolean>  categoryGraph = new HashMap<>();
documentsInput=documentsInput.replaceAll("(\\W*)N(\\W*)","\n");
documentsInput=documentsInput.replaceAll(" \\w{1,2} ", " ");         //The "words" that have less than 2 characters are ommited.
documentsInput=documentsInput.replaceAll(" +"," ");                 //Many spaces "   " are replaced by one space " ".
int currentWord;
int position_of_word;
String[] linesOfdataInput = documentsInput.split("\n");             // Each line-cell of the array linesOfdataInput is a line-String from dataInput. This line after processing will be part of the three gram graph.
int numberOfLines = linesOfdataInput.length;                        // We split the String dataInput into the cells of the array linesOfdataInput.
for (int currentLine=0;currentLine<numberOfLines;currentLine++){    // We iterate the cell-lines of the array linesOfdataInput one by one in order to add the corresponded edges in the graph.
    String[] words= linesOfdataInput[currentLine].split(" ");
        for(currentWord=0;currentWord<words.length ;currentWord++){ // The words of the line are iterrated one by one.
            for(position_of_word=currentWord+1; position_of_word<=currentWord + frame ;position_of_word++){
                if(position_of_word<words.length){
                    if(!filterOutEdges.contains(words[currentWord]+"_"+words[position_of_word]))
                        categoryGraph.put(words[currentWord]+"_"+words[position_of_word],Boolean.TRUE);
                    //else System.out.println("The edge "+words[currentWord]+"_"+words[position_of_word]+" is filtered out.");
                }
            }
        }
    }
/*if (categoryGraph.isEmpty())
System.out.println("The document: "+documentsInput+" can not be represented.");*/

//System.out.println("The document has the form: "+documentsInput);
return categoryGraph;
}//===================================================== End ofFromDataTo_UNweightedGraph() ============================================================================= 


//==========================================================FromDataTo_WeightedGraph()===================================================================================
/** A set of documents or a single document will be represented by a Weighted Graph. The method provides the assortment of the Edges and Weights that the Graph involves.
* @param documentsInput A string that includes all the documents from which the representation Graph will be made. 
* The reserved character "N" works as delimiter between paragraphs and documents.
* @param frame The frame determines the number of edges will be created to join each Word-Node to the following Words-Nodes.
* @return The edges comprised by the Graph with their corresponding weights. The data structure of HashMap is used. 
* The key denotes two Nodes that are adjacent in the form Word1_Word2 and the value is returned the Weight of the aforementioned Edge.*/
//=======================================================================================================================================================================
public static HashMap FromDataTo_WeightedGraph(String documentsInput, int frame){
System.out.println("Weighted Graphs are not supported yet.");
exit(0);
HashMap<String, Integer>  categoryGraph = new HashMap<>();
documentsInput=documentsInput.replaceAll("N","\n");
int currentWord, sumOfWeights=0;
int position_of_word;
String[] linesOfdataInput = documentsInput.split("\n");  //Each line-cell of the array linesOfdataInput is a line-String from dataInput. This line after processing will be part of the three gram graph.
int numberOfLines = linesOfdataInput.length;        //We split the String dataInput into the cells of the array linesOfdataInput.
for (int currentLine=0;currentLine<numberOfLines;currentLine++){ //We iterate the cell-lines of the array linesOfdataInput one by one in order to add the corresponded edges in the graph.
    String[] words= linesOfdataInput[currentLine].split(" ");
        for(currentWord=0;currentWord<words.length ;currentWord++){             //We iterate the words of the line one by one.
            for(position_of_word=currentWord+1; position_of_word<=currentWord + frame ;position_of_word++){
                if(position_of_word<words.length){
                    sumOfWeights++;
                    if(categoryGraph.containsKey(words[currentWord]+"_"+words[position_of_word])){
                        categoryGraph.put(words[currentWord]+"_"+words[position_of_word], categoryGraph.get(words[currentWord]+"_"+words[position_of_word])+1);}
                    else 
                        categoryGraph.put(words[currentWord]+"_"+words[position_of_word], 1);
                }
                    //categoryGraph.put(words[currentWord]+"_"+words[position_of_word],Boolean.TRUE);
                }
        }
    }


HashMap <String, Float>  categoryNormalizedGraph = new HashMap<>();        

Iterator it = categoryGraph.entrySet().iterator();
    while (it.hasNext()) {
        Map.Entry pairs = (Map.Entry)it.next();
        categoryNormalizedGraph.put((String) pairs.getKey(), (float) pairs.getValue().hashCode()/sumOfWeights);
        it.remove(); // avoids a ConcurrentModificationException
    }
return categoryNormalizedGraph;
//return categoryGraph;
}//=============================================================== End of FromDataTo_WeightedGraph() ===============================================================


//======================================================= FromAllDocumentsTo_One_UNweightedGraph() =================================================================
/**
 * @param documents All the Documents of the Corpus from which the Word Graph will be constructed.
 * @param frame The Number "N" of the edges will be created having as Source Node each Word and as Terminal Nodes the "N" following Words. */
//==================================================================================================================================================================

public static void FromAllDocumentsTo_One_UNweightedGraph(ArrayList documents, int frame) throws IOException{
HashMap <String,Integer> nodes_NodeWord_to_NodeNumber = new HashMap<>();        //
HashMap <String,Boolean> edges_NodeNumber_to_NodeNumber = new HashMap<>();      //
HashMap <String,Boolean> labels_NodeNumber_to_LabelNumber = new HashMap<>();    //
String documentsInput, labelNumber;
int nodeNumberCounter=1;
int first_NodeNumber,second_NodeNumber;
for(int i=0; i<documents.size();i++){
    documentsInput = (String)documents.get(i);
    labelNumber= documentsInput.substring(documentsInput.indexOf('B') + 1, documentsInput.lastIndexOf("E")).replaceAll("E", " "); // to E isos den xriazete.
 
    //HashMap<String, Boolean>  categoryGraph = new HashMap<>();
documentsInput=documentsInput.replaceAll("(\\W*)N(\\W*)","\n");
documentsInput=documentsInput.replaceAll(" \\w{1,2} ", "");         //The "words" that have less than 2 characters are ommited.
documentsInput=documentsInput.replaceAll(" +"," ");                 //Many spaces "   " are replaced by one space " ".
int currentWord;
int position_of_word;
String[] linesOfdataInput = documentsInput.split("\n");             // Each line-cell of the array linesOfdataInput is a line-String from dataInput. This line after processing will be part of the three gram graph.
int numberOfLines = linesOfdataInput.length;                        // We split the String dataInput into the cells of the array linesOfdataInput.
for (int currentLine=0;currentLine<numberOfLines;currentLine++){    // We iterate the cell-lines of the array linesOfdataInput one by one in order to add the corresponded edges in the graph.
    String[] words= linesOfdataInput[currentLine].split(" ");
        for(currentWord=0;currentWord<words.length ;currentWord++){ // The words of the line are iterrated one by one.
            for(position_of_word=currentWord+1; position_of_word<=currentWord + frame ;position_of_word++){
                if(position_of_word<words.length){
                    

                    
                    
                    if(nodes_NodeWord_to_NodeNumber.containsKey(words[currentWord]))
                        first_NodeNumber=nodes_NodeWord_to_NodeNumber.get(words[currentWord]);
                    else{
                        nodes_NodeWord_to_NodeNumber.put(words[currentWord], nodeNumberCounter++);
                        first_NodeNumber=nodeNumberCounter;} 
                    labels_NodeNumber_to_LabelNumber.put(Integer.toString(first_NodeNumber)+","+labelNumber, Boolean.TRUE);
                    
                    if(nodes_NodeWord_to_NodeNumber.containsKey(words[position_of_word]))
                        second_NodeNumber=nodes_NodeWord_to_NodeNumber.get(words[position_of_word]);
                    else{
                        nodes_NodeWord_to_NodeNumber.put(words[position_of_word], nodeNumberCounter++);
                        second_NodeNumber=nodeNumberCounter;} 
                    
                    labels_NodeNumber_to_LabelNumber.put(Integer.toString(second_NodeNumber)+","+labelNumber, Boolean.TRUE);
                    
                    
                    edges_NodeNumber_to_NodeNumber.put(Integer.toString(first_NodeNumber)+","+Integer.toString(second_NodeNumber) , Boolean.TRUE);  //edges_NodeNumber_to_NodeNumber
                    
                    //categoryGraph.put(words[currentWord]+"_"+words[position_of_word],Boolean.TRUE);
                
                }
                }
        }
    }   
}
    
//System.out.println("The nodes_NodeWord_to_NodeNumber: "+nodes_NodeWord_to_NodeNumber);
//System.out.println("The edges_NodeNumber_to_NodeNumber: "+edges_NodeNumber_to_NodeNumber);
//System.out.println("The labels_NodeNumber_to_LabelNumber: "+labels_NodeNumber_to_LabelNumber);

System.out.println("There are "+nodes_NodeWord_to_NodeNumber.size()+" nodes.");
BufferedWriter bwr1 = new BufferedWriter(new FileWriter(new File("GraphRepresentation\\edges.csv")));
for (String edge : edges_NodeNumber_to_NodeNumber.keySet() )
bwr1.write(edge.toString()+"\n");
bwr1.flush();
bwr1.close();

BufferedWriter bwr2 = new BufferedWriter(new FileWriter(new File("GraphRepresentation\\nodeLabels.csv")));
for (String nodeLabel :labels_NodeNumber_to_LabelNumber.keySet() )
bwr2.write(nodeLabel.toString()+"\n");
bwr2.flush();
bwr2.close();

}//================================================== End of FromAllDocumentsTo_One_UNweightedGraph() ==============================================================









}//=============================================================== End of Class GraphCreator =======================================================================









