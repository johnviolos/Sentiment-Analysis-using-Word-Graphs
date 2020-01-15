package word.graph_classification;
import static java.lang.Float.NaN;
import static java.lang.System.exit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

/**Methods for the comparison of Labeled Weighted and Unweighted Graphs are included.
* @since 10-6-2015
* @version 1.1
* @author John Violos */
public class GraphComparator {
//==============================Compare_UNweightedGraphs_Using_CS()===============================
/**The comparison of two Graphs is based on the number of Common Edges.
 * The Containment Similarity is the metric to be reckoned.
 * @param TestingGraph  The Edges of the Document-Unweighted-Graph. 
 * @param CategoryGraph The Edges of the Topic-Unweighted-Graph.
 * @return The division between the number of common Edges with the number of 
 * the Edges of the smaller Graph. */ 
//===============================================================================================
public static float Compare_UNweightedGraphs_Using_CS(HashMap TestingGraph, HashMap CategoryGraph){
int numberOfEdges1 = TestingGraph.size();
int numberOfEdges2 = CategoryGraph.size();
int numberOfCommonEdges=0;
Set edgesOfTestingGraph=TestingGraph.keySet();
for(Object i : edgesOfTestingGraph){
    if(CategoryGraph.containsKey(i))    
        numberOfCommonEdges++;
}
float minimumLines;
if(numberOfEdges1<numberOfEdges2){minimumLines=numberOfEdges1;}else minimumLines=numberOfEdges2;
if((numberOfCommonEdges/minimumLines)==NaN)System.out.println("Problem");
return (float) numberOfCommonEdges/minimumLines;    
} //========================== End of Compare_UNweightedGraphs_Using_CS() ===============================



//==================================Compare_UNweightedGraphs_Using_MCSN()===================================
/**The comparison of two Graphs is based on the Nodes that contained in the Maximum Common Subgraph (MCS).
* It detects the Common Edges. Afterwards, it calls the CommonLabelSubGraph_Sum_of_Nodes()
* to detect the MCS of the Representative Graphs (Document and Topic category) and count 
* the Number of the Common Nodes that contained in the MCS.
* @param TestingGraph  The Edges of the Document-Unweighted-Graph. 
* @param CategoryGraph The Edges of the Topic-Unweighted-Graph.
* @return The Division between the Number of the Common Nodes that contained in the MCS with
* the Edges of the Smaller Graph. */ 
//===============================================================================================
public static float Compare_UNweightedGraphs_Using_MCSN(HashMap TestingGraph, HashMap CategoryGraph){
HashMap<String,Boolean> commonDirectedEdges = new HashMap<>();
HashMap<String,Boolean> allNodes = new HashMap<>();
ArrayList<String> nodesOfCommonEdges = new ArrayList<>();
HashMap<String, TreeSet<String>>  common_Undirected_Edges = new HashMap<>();
int numberOfEdges1 = TestingGraph.size();
int numberOfEdges2 = CategoryGraph.size();
Set edgesOfTestingGraph=TestingGraph.keySet();
for(Object i : edgesOfTestingGraph){
    String commonEdge= (String) i;                                      // The Labels of the two nodes tha are connected by the common Edge
    String[] twoAdjacentNodes= commonEdge.split("_");
    String j= twoAdjacentNodes[1]+"_"+twoAdjacentNodes[0];
    allNodes.put(twoAdjacentNodes[0], Boolean.TRUE);
    allNodes.put(twoAdjacentNodes[1], Boolean.TRUE);
    if(CategoryGraph.containsKey(i))
    commonDirectedEdges.put((String)i, Boolean.TRUE);                   // A common Edge is detected and is appended to the HashMap of commonDirectedEdges.    
    if(CategoryGraph.containsKey(i)||CategoryGraph.containsKey(j)){ //the direction is destroyed.                                      // A Common edge between the two Graphs is found.
        //commonDirectedEdges.put((String)i, Boolean.TRUE);                   // A common Edge is detected and is appended to the HashMap of commonDirectedEdges.
//        String commonEdge= (String) i;                                      // The Labels of the two nodes tha are connected by the common Edge
//        String[] twoAdjacentNodes= commonEdge.split("_");                   // The common Edge is splitted into two adjacent nodes.
        if(!nodesOfCommonEdges.contains(twoAdjacentNodes[0])) nodesOfCommonEdges.add(twoAdjacentNodes[0]);  //The Source Node is added to the ArrayList of the common Nodes
        if(!nodesOfCommonEdges.contains(twoAdjacentNodes[1])) nodesOfCommonEdges.add(twoAdjacentNodes[1]);  //The Terminal Node is added to the ArrayList of the common Nodes
            
        if(common_Undirected_Edges.containsKey(twoAdjacentNodes[0])){                           // the key-||Source-Node|| already exists in the HashMap of Undirected_Edges and the Terminal-Node is appended in the value-set of its neighbour node.
            TreeSet<String> temp_SetofEdges=common_Undirected_Edges.get(twoAdjacentNodes[0]);   // A temporay set that includes all the neighbours nodes of the examined node.
            temp_SetofEdges.add(twoAdjacentNodes[1]);                                           // The new adjacent node is added in the temporary set of neighbour nodes.
            common_Undirected_Edges.put( twoAdjacentNodes[0], temp_SetofEdges );                // The updated set of edges is the value of the hashtable
        }
        else{                                                                                   // the key-||Source-Node|| doesn't exist in the HashMap of Undirected_Edges so a new value-set that includes only the Terminal-Node is constructed.
            TreeSet<String> temp_SetofEdges = new TreeSet<>();                                  
            temp_SetofEdges.add(twoAdjacentNodes[1]);
            common_Undirected_Edges.put( twoAdjacentNodes[0], temp_SetofEdges);
        }
            
            
        if(common_Undirected_Edges.containsKey(twoAdjacentNodes[1])){                           // the key-||Terminal-Node|| already exists in the HashMap of Undirected_Edges and the Source-Node is appended in the value-set of its neighbour node.
            TreeSet<String> temp_SetofEdges=common_Undirected_Edges.get(twoAdjacentNodes[1]);
            temp_SetofEdges.add(twoAdjacentNodes[0]);
            common_Undirected_Edges.put( twoAdjacentNodes[1], temp_SetofEdges );
        }
        else{                                                                                   // the key-||Terminal-Node|| doesn't exist in the HashMap of Undirected_Edges so a new set that includes only the value-Source-Node is constructed.
            TreeSet<String> temp_SetofEdges = new TreeSet<>();
            temp_SetofEdges.add(twoAdjacentNodes[0]);
            common_Undirected_Edges.put( twoAdjacentNodes[1], temp_SetofEdges);
        }
    }
}
//float size_of_common_subgraph = (float) CommonLabelSubGraph_Sum_Of_Edges(common_Undirected_Edges,nodesOfCommonEdges,commonDirectedEdges); //It keeps the direction of Edges. The number of edges between contained in the MCS. As parameter we pass the common directed edges os the two Graphs.

float size_of_common_subgraph = CommonLabelSubGraph_Sum_of_Nodes(common_Undirected_Edges,nodesOfCommonEdges);
//float size_of_common_subgraph = CommonLabelSubGraph_Sum_Of_Edges(common_Undirected_Edges,nodesOfCommonEdges);  //2nd choice It does NOT keep the direction of Edges.
//float size_of_common_subgraph = CommonLabelSubGraph_Sum_of_Nodes(common_Undirected_Edges,nodesOfCommonEdges);

float minimumLines= allNodes.size();   //The dominator is the Number of Nodes of the smaller graph (document graph, not the category graph)
//if(numberOfEdges1<numberOfEdges2){minimumLines=numberOfEdges1;}else minimumLines=numberOfEdges2; //The dominator can be the Number of Edges that exist in the smaller graph.

return (float)size_of_common_subgraph/minimumLines;    
}//==================================== End of Compare_NGram_UNweightedGraphs_Using_MCSN()===================================



//==================================Compare_UNweightedGraphs_Using_MCSDE()===================================
/**The comparison of two Graphs is based on the Directed Edges of the Maximum Common Subgraph (MCS).
* It detects the Common Edges. Afterwards, it calls the CommonLabelSubGraph_Sum_of_DirectedEdges()
* to detect the MCS of the Representative Graphs (Document and Topic category) and count 
* the Number of the Common Edges that contained in the MCS.
* The Edges should have the same Direction to be counted as Common Edges.
* @param TestingGraph  The Edges of the Document-Unweighted-Graph. 
* @param CategoryGraph The Edges of the Topic-Unweighted-Graph.
* @return The Division between the Number of Common Directed Edged that included in the MCS
* divided by the Minimum Edges of two Graphs.*/ 
//===============================================================================================
public static float Compare_UNweightedGraphs_Using_MCSDE(HashMap TestingGraph, HashMap CategoryGraph) { //Subgraph Similarity, it is based on the Edges of the MCS
HashMap<String,Boolean> commonDirectedEdges = new HashMap<>();
ArrayList<String> nodesOfCommonEdges = new ArrayList<>();
HashMap<String, TreeSet<String>>  common_Undirected_Edges = new HashMap<>();
int numberOfEdges1 = TestingGraph.size();
int numberOfEdges2 = CategoryGraph.size();
Set edgesOfTestingGraph=TestingGraph.keySet();
for(Object i : edgesOfTestingGraph){
    String commonEdge= (String) i;                                      // The Labels of the two nodes tha are connected by the common Edge
    String[] twoAdjacentNodes= commonEdge.split("_");
    String j= twoAdjacentNodes[1]+"_"+twoAdjacentNodes[0];
    if(CategoryGraph.containsKey(i))
    commonDirectedEdges.put((String)i, Boolean.TRUE);                   // A common Edge is detected and is appended to the HashMap of commonDirectedEdges.    
    if(CategoryGraph.containsKey(i)||CategoryGraph.containsKey(j)){ //the direction is destroyed.                                      // A Common edge between the two Graphs is found.
        //commonDirectedEdges.put((String)i, Boolean.TRUE);                   // A common Edge is detected and is appended to the HashMap of commonDirectedEdges.
//        String commonEdge= (String) i;                                      // The Labels of the two nodes tha are connected by the common Edge
//        String[] twoAdjacentNodes= commonEdge.split("_");                   // The common Edge is splitted into two adjacent nodes.
        if(!nodesOfCommonEdges.contains(twoAdjacentNodes[0])) nodesOfCommonEdges.add(twoAdjacentNodes[0]);  //The Source Node is added to the ArrayList of the common Nodes
        if(!nodesOfCommonEdges.contains(twoAdjacentNodes[1])) nodesOfCommonEdges.add(twoAdjacentNodes[1]);  //The Terminal Node is added to the ArrayList of the common Nodes
            
        if(common_Undirected_Edges.containsKey(twoAdjacentNodes[0])){                           // the key-||Source-Node|| already exists in the HashMap of Undirected_Edges and the Terminal-Node is appended in the value-set of its neighbour node.
            TreeSet<String> temp_SetofEdges=common_Undirected_Edges.get(twoAdjacentNodes[0]);   // A temporay set that includes all the neighbours nodes of the examined node.
            temp_SetofEdges.add(twoAdjacentNodes[1]);                                           // The new adjacent node is added in the temporary set of neighbour nodes.
            common_Undirected_Edges.put( twoAdjacentNodes[0], temp_SetofEdges );                // The updated set of edges is the value of the hashtable
        }
        else{                                                                                   // the key-||Source-Node|| doesn't exist in the HashMap of Undirected_Edges so a new value-set that includes only the Terminal-Node is constructed.
            TreeSet<String> temp_SetofEdges = new TreeSet<>();                                  
            temp_SetofEdges.add(twoAdjacentNodes[1]);
            common_Undirected_Edges.put( twoAdjacentNodes[0], temp_SetofEdges);
        }
            
            
        if(common_Undirected_Edges.containsKey(twoAdjacentNodes[1])){                           // the key-||Terminal-Node|| already exists in the HashMap of Undirected_Edges and the Source-Node is appended in the value-set of its neighbour node.
            TreeSet<String> temp_SetofEdges=common_Undirected_Edges.get(twoAdjacentNodes[1]);
            temp_SetofEdges.add(twoAdjacentNodes[0]);
            common_Undirected_Edges.put( twoAdjacentNodes[1], temp_SetofEdges );
        }
        else{                                                                                   // the key-||Terminal-Node|| doesn't exist in the HashMap of Undirected_Edges so a new set that includes only the value-Source-Node is constructed.
            TreeSet<String> temp_SetofEdges = new TreeSet<>();
            temp_SetofEdges.add(twoAdjacentNodes[0]);
            common_Undirected_Edges.put( twoAdjacentNodes[1], temp_SetofEdges);
        }
    }
}
//float size_of_common_subgraph = (float) CommonLabelSubGraph_Sum_Of_Edges(common_Undirected_Edges,nodesOfCommonEdges,commonDirectedEdges); //It keeps the direction of Edges. The number of edges between contained in the MCS. As parameter we pass the common directed edges os the two Graphs.

float size_of_common_subgraph = CommonLabelSubGraph_Sum_Of_DirectedEdges(common_Undirected_Edges,nodesOfCommonEdges,commonDirectedEdges);
//float size_of_common_subgraph = CommonLabelSubGraph_Sum_Of_Edges(common_Undirected_Edges,nodesOfCommonEdges);  //2nd choice It does NOT keep the direction of Edges.
//float size_of_common_subgraph = CommonLabelSubGraph_Sum_of_Nodes(common_Undirected_Edges,nodesOfCommonEdges);

float minimumLines;
if(numberOfEdges1<numberOfEdges2){minimumLines=numberOfEdges1;}else minimumLines=numberOfEdges2;

return (float)size_of_common_subgraph/minimumLines;    
}//==================================== End of Compare_NGram_UNweightedGraphs_Using_MCSDE()===================================



//==================================Compare_UNweightedGraphs_Using_MCSUE()===================================
/**The comparison of two Graphs is based on the Undirected Edges of the Maximum Common Subgraph (MCS).
* It detects the common Edges. Afterwards, it calls the CommonLabelSubGraph_Sum_of_UndirectedEdges()
* to detect the MCS of the Representative Graphs (Document and Topic category) and count 
* the Number of the Common Edges that contained in the MCS.
* The Edges are considered as UNdirected Edges.           
* @param TestingGraph  The Edges of the Document-Unweighted-Graph. 
* @param CategoryGraph The Edges of the Topic-Unweighted-Graph.
* @return  The division between the number of Common UNdirected Edged that included in the MCS
* divided by the Minimum Edges of two Graphs. */ 
//===============================================================================================
public static float Compare_UNweightedGraphs_Using_MCSUE(HashMap TestingGraph, HashMap CategoryGraph){
HashMap<String,Boolean> commonDirectedEdges = new HashMap<>();
ArrayList<String> nodesOfCommonEdges = new ArrayList<>();
HashMap<String, TreeSet<String>>  common_Undirected_Edges = new HashMap<>();
int numberOfEdges1 = TestingGraph.size();
int numberOfEdges2 = CategoryGraph.size();
Set edgesOfTestingGraph=TestingGraph.keySet();
for(Object i : edgesOfTestingGraph){
    String commonEdge= (String) i;                                      // The Labels of the two nodes tha are connected by the common Edge
    String[] twoAdjacentNodes= commonEdge.split("_");
    String j= twoAdjacentNodes[1]+"_"+twoAdjacentNodes[0];
    if(CategoryGraph.containsKey(i))
    commonDirectedEdges.put((String)i, Boolean.TRUE);                   // A common Edge is detected and is appended to the HashMap of commonDirectedEdges.    
    if(CategoryGraph.containsKey(i)||CategoryGraph.containsKey(j)){ //the direction is destroyed.                                      // A Common edge between the two Graphs is found.
        //commonDirectedEdges.put((String)i, Boolean.TRUE);                   // A common Edge is detected and is appended to the HashMap of commonDirectedEdges.
//        String commonEdge= (String) i;                                      // The Labels of the two nodes tha are connected by the common Edge
//        String[] twoAdjacentNodes= commonEdge.split("_");                   // The common Edge is splitted into two adjacent nodes.
        if(!nodesOfCommonEdges.contains(twoAdjacentNodes[0])) nodesOfCommonEdges.add(twoAdjacentNodes[0]);  //The Source Node is added to the ArrayList of the common Nodes
        if(!nodesOfCommonEdges.contains(twoAdjacentNodes[1])) nodesOfCommonEdges.add(twoAdjacentNodes[1]);  //The Terminal Node is added to the ArrayList of the common Nodes
            
        if(common_Undirected_Edges.containsKey(twoAdjacentNodes[0])){                           // the key-||Source-Node|| already exists in the HashMap of Undirected_Edges and the Terminal-Node is appended in the value-set of its neighbour node.
            TreeSet<String> temp_SetofEdges=common_Undirected_Edges.get(twoAdjacentNodes[0]);   // A temporay set that includes all the neighbours nodes of the examined node.
            temp_SetofEdges.add(twoAdjacentNodes[1]);                                           // The new adjacent node is added in the temporary set of neighbour nodes.
            common_Undirected_Edges.put( twoAdjacentNodes[0], temp_SetofEdges );                // The updated set of edges is the value of the hashtable
        }
        else{                                                                                   // the key-||Source-Node|| doesn't exist in the HashMap of Undirected_Edges so a new value-set that includes only the Terminal-Node is constructed.
            TreeSet<String> temp_SetofEdges = new TreeSet<>();                                  
            temp_SetofEdges.add(twoAdjacentNodes[1]);
            common_Undirected_Edges.put( twoAdjacentNodes[0], temp_SetofEdges);
        }
            
            
        if(common_Undirected_Edges.containsKey(twoAdjacentNodes[1])){                           // the key-||Terminal-Node|| already exists in the HashMap of Undirected_Edges and the Source-Node is appended in the value-set of its neighbour node.
            TreeSet<String> temp_SetofEdges=common_Undirected_Edges.get(twoAdjacentNodes[1]);
            temp_SetofEdges.add(twoAdjacentNodes[0]);
            common_Undirected_Edges.put( twoAdjacentNodes[1], temp_SetofEdges );
        }
        else{                                                                                   // the key-||Terminal-Node|| doesn't exist in the HashMap of Undirected_Edges so a new set that includes only the value-Source-Node is constructed.
            TreeSet<String> temp_SetofEdges = new TreeSet<>();
            temp_SetofEdges.add(twoAdjacentNodes[0]);
            common_Undirected_Edges.put( twoAdjacentNodes[1], temp_SetofEdges);
        }
    }
}
//float size_of_common_subgraph = (float) CommonLabelSubGraph_Sum_Of_Edges(common_Undirected_Edges,nodesOfCommonEdges,commonDirectedEdges); //It keeps the direction of Edges. The number of edges between contained in the MCS. As parameter we pass the common directed edges os the two Graphs.

float size_of_common_subgraph = CommonLabelSubGraph_Sum_Of_UndirectedEdges(common_Undirected_Edges,nodesOfCommonEdges);
//float size_of_common_subgraph = CommonLabelSubGraph_Sum_Of_Edges(common_Undirected_Edges,nodesOfCommonEdges);  //2nd choice It does NOT keep the direction of Edges.
//float size_of_common_subgraph = CommonLabelSubGraph_Sum_of_Nodes(common_Undirected_Edges,nodesOfCommonEdges);

float minimumLines;
if(numberOfEdges1<numberOfEdges2){minimumLines=numberOfEdges1;}else minimumLines=numberOfEdges2;

return (float)size_of_common_subgraph/minimumLines;    
}//==================================== End of Compare_NGram_UNweightedGraphs_Using_MCSUE()===================================



//==============================Compare_UNweightedGraphs_Using_TS()===============================
/**The comparison of two Graphs is based on the Tversky Similarity measure.
 * This method is under construction.
 * @param TestingGraph  The Edges of the Document-Unweighted-Graph. 
 * @param CategoryGraph The Edges of the Topic-Unweighted-Graph.
 * @return Tvesky Similarity is returned. */ 
//===============================================================================================
public static float Compare_UNweightedGraphs_Using_TS (HashMap TestingGraph, HashMap CategoryGraph){
System.out.println("Tversky Graph Smilarity method is under construction.");
exit(0);
return 0.0f;
}//========================== End of Compare_UNweightedGraphs_Using_TS() ===============================



//===============================CommonLabelSubGraph_Sum_of_Nodes=====================================
/** The MCS is detected, afterwards is returned the size of common Nodes that exist in the MCS.
* This method is callable by the Compare_UNweightedGraphs_Using_MCSN().
* @param allEdges A HashMap that includes all the Common edges between two Graphs. The key is a common Node, the Value is the Set of the Adjacent Nodes of the key node
* @param unexamined_Nodes The common Nodes of two Graphs will be examined to the detect all the common subgraphs.
* @return The number of common Nodes that exist in MCS.*/
//====================================================================================================
public static float CommonLabelSubGraph_Sum_of_Nodes(HashMap allEdges, ArrayList<String> unexamined_Nodes){
if (unexamined_Nodes.isEmpty()) return 0;
ArrayList<String> candidate_MCS= new ArrayList<>();                 // An ArrayList that contais only the NODES of the Candidate MCS.
ArrayList<String> maxCommonSubgraph=new ArrayList<>();              // An ArrayList that contais only the NODES of the MCS.
   
//System.out.println("The common Nodes are: "+unexamined_Nodes);      
//System.out.println("The common Undirected Edges are: "+allEdges);

while(unexamined_Nodes.size()>0){                                   // A new candidate MCS will be constructed, if there are nodes that have not been examined. // All the Common Nodes, that are not examined, are iterated to initialize a new candidate MCS.
    String examing_the_Node =unexamined_Nodes.remove(0);            // The 1st node from the set of the unexaminedNodes is assigned as examing_the_Node and removed from the unexamined_Nodes.
    candidate_MCS.add(examing_the_Node);                            // The 1st node that retrieved formerly node, will initialize the candidate_MCS.
    TreeSet<String> adjacent_nodesof_Examming_the_Node=(TreeSet<String>) allEdges.get(examing_the_Node); // adjacent_nodes is a set that includes --all the adjacent nodes of the current node--.
    for( String c : adjacent_nodesof_Examming_the_Node){            // The adjacent |Nodes| - c - of the 1st node are appended in the current MCS, if and only if they do not already exist in the currentSubgraph.
        if(!candidate_MCS.contains(c))
            candidate_MCS.add(c);}
    
    int pointer=1;                                                  // A pointer that indicate the Node of the |candidate MCS currentSubgraph| that will be examined.
    while(pointer<candidate_MCS.size()){                            // maybe the "=" should be ommited.
        examing_the_Node=candidate_MCS.get(pointer);                // The |currentNode| is a node that has not be examined yet.
        if(unexamined_Nodes.contains(examing_the_Node))             // The currentNode is removed from the set of allNodes so no to be examined again.
            unexamined_Nodes.remove(examing_the_Node);
        
        adjacent_nodesof_Examming_the_Node=(TreeSet<String>) allEdges.get(examing_the_Node);    // All the adjacent nodes of the currentNode are retrieved
        for( String c : adjacent_nodesof_Examming_the_Node){                                    // The adjacent nodes of the current node that do not already exist in the current MCS will be added.
            if(!candidate_MCS.contains(c))
                candidate_MCS.add(c);}
        pointer++;                                                  //The next node of arraylist of the candidate MCS will be pointed by the counter pointer.
    }                                                               // The construction of the current MCS has been finished.

    //System.out.println("A common subgraph is: "+candidate_MCS);     // The candidate MCS is illustrated.
    if(maxCommonSubgraph.size()<candidate_MCS.size()){
        maxCommonSubgraph=new ArrayList<>(candidate_MCS);}
    candidate_MCS.clear();                                                  
}
//System.out.println("The maximum common subgraph is: "+maxCommonSubgraph);  //Illustration of the MCS.
return maxCommonSubgraph.size();
}//=================================================== End of CommonLabelSubGraph() ===================================================



//==========================================================CommonLabelSubGraph_Sum_Of_DirectedEdges()==============================================================================
/** The MCS is detected, afterwards is returned the size of common Edges that exist in MCS. It keeps the direction of Edges.
* This method is callable by the Compare_UNweightedGraphs_Using_MCSDE(). 
* @param allEdges A HashMap that includes all the -Common edges between two Graphs. The key is a common Node, the Value is the Set of the Adjacent Nodes of the key node
* @param unexamined_Nodes The common Nodes of two Graphs will be examined to the detect all the common subgraphs.
* @param directedEdges The common Edges, the direction of the edges is preserved.
* @return The number of common Edges that exist in MCS with the Right Direction.*/
//=========================================================================================================================================================================
public static float CommonLabelSubGraph_Sum_Of_DirectedEdges(HashMap allEdges, ArrayList<String> unexamined_Nodes, HashMap directedEdges){
if (unexamined_Nodes.isEmpty()) return 0;
ArrayList<String> candidate_MCS= new ArrayList<>();                 // An ArrayList that contais only the NODES of the Candidate MCS.
ArrayList<String> maxCommonSubgraph=new ArrayList<>();              // An ArrayList that contais only the NODES of the MCS.

//System.out.println("The common Nodes are: "+unexamined_Nodes);      
//System.out.println("The common directed Edges are: "+directedEdges.keySet());

while(unexamined_Nodes.size()>0){                                   // A new candidate MCS will be constructed, if there are nodes that have not been examined. // All the Common Nodes, that are not examined, are iterated to initialize a new candidate MCS.
    String examing_the_Node =unexamined_Nodes.remove(0);            // The 1st node from the set of the unexaminedNodes is assigned as examing_the_Node and removed from the unexamined_Nodes.
    candidate_MCS.add(examing_the_Node);                            // The 1st node that retrieved formerly node, will initialize the candidate_MCS.
    TreeSet<String> adjacent_nodesof_Examming_the_Node=(TreeSet<String>) allEdges.get(examing_the_Node); // adjacent_nodes is a set that includes --all the adjacent nodes of the current node--.
    for( String c : adjacent_nodesof_Examming_the_Node){            // The adjacent |Nodes| - c - of the 1st node are appended in the current MCS, if and only if they do not already exist in the currentSubgraph.
        if(!candidate_MCS.contains(c))
            candidate_MCS.add(c);}
    
    int pointer=1;                                                  // A pointer that indicate the Node of the |candidate MCS currentSubgraph| that will be examined.
    while(pointer<candidate_MCS.size()){                            // maybe the "=" should be ommited.
        examing_the_Node=candidate_MCS.get(pointer);                // The |currentNode| is a node that has not be examined yet.
        if(unexamined_Nodes.contains(examing_the_Node))             // The currentNode is removed from the set of allNodes so no to be examined again.
            unexamined_Nodes.remove(examing_the_Node);
        
        adjacent_nodesof_Examming_the_Node=(TreeSet<String>) allEdges.get(examing_the_Node);    // All the adjacent nodes of the currentNode are retrieved
        for( String c : adjacent_nodesof_Examming_the_Node){                                    // The adjacent nodes of the current node that do not already exist in the current MCS will be added.
            if(!candidate_MCS.contains(c))
                candidate_MCS.add(c);}
        pointer++;                                                          //The next node of arraylist of the candidate MCS will be pointed by the counter pointer.
    }                                                                       // The construction of the current MCS has been finished.

    //System.out.println("A common subgraph is: "+candidate_MCS);             // The candidate MCS is illustrated.
    if(maxCommonSubgraph.size()<candidate_MCS.size()){
        maxCommonSubgraph=new ArrayList<>(candidate_MCS);}
    candidate_MCS.clear();                                                  
}
//System.out.println("The MCS is : "+maxCommonSubgraph);
//ArrayList<String> maxCommonSubgraph=new ArrayList<>(); This data structure has all the Nodes.
ArrayList<String> maxCommonSubgraphEdges=new ArrayList<>(); 
for(Object directedCommonEdge: directedEdges.keySet()){
    String[] twoNodesOfTheCommonEdge= ((String)directedCommonEdge).split("_");
    if(maxCommonSubgraph.contains(twoNodesOfTheCommonEdge[0])&&maxCommonSubgraph.contains(twoNodesOfTheCommonEdge[1]))
        maxCommonSubgraphEdges.add((String)directedCommonEdge);
//System.out.println("One common directed Edge is:"+(String)directedCommonEdge);
}
//System.out.println("The Directed Edges that contained in the MCS are: "+maxCommonSubgraphEdges);  //Illustration of the MCS.
return maxCommonSubgraphEdges.size();  
}//=============================================== End CommonLabelSubGraph_Sum_Of_DirectedEdges ==============================================



//==========================================================CommonLabelSubGraph_Sum_Of_UndirectedEdges()==============================================================================
/** The MCS is detected, afterwards is returned the the size of common Edges that exist in MCS. It does NOT keep the direction of Edges.
* This method is callable by the Compare_UNweightedGraphs_Using_MCSUE().
* @param allEdges A HashMap that includes all the -Common edges between two Graphs. The key is a common Node, the Value is the Set of the Adjacent Nodes of the key node.
* @param unexamined_Nodes The common Nodes of two Graphs will be examined to the detect all the common subgraphs.
* @return The number of Edges that have as source node one of the nodes of the MCS without taking into account the direction of the common Edges.*/
//=========================================================================================================================================================================
public static float CommonLabelSubGraph_Sum_Of_UndirectedEdges(HashMap allEdges, ArrayList<String> unexamined_Nodes){  
if (unexamined_Nodes.isEmpty()) return 0;
ArrayList<String> candidate_MCS= new ArrayList<>();
ArrayList<String> maxCommonSubgraph=new ArrayList<>();
//System.out.println("The common Nodes are: "+unexamined_Nodes);    // The common Nodes of the two graphs are compared.
//System.out.println("The common Undirected Edges are: "+allEdges); // The common Edges of the two graphs are compared.

while(unexamined_Nodes.size()>0){                                   // If there exist Nodes that are not examined, then these unexamined nodes will initilaize a new candidate MCS.
    String examing_the_Node=unexamined_Nodes.remove(0);             // The 1st Node from the arrayList of allNodes will be assigned to the currentNode and will be removed from the array of unexamined Nodes.
    candidate_MCS.add(examing_the_Node);                            // The 1st node will initialize the 1st current Subgraph
    TreeSet<String> adjacent_nodesof_Examming_the_Node=(TreeSet<String>) allEdges.get(examing_the_Node); // adjacent_nodes is a set that includes all the adjacent nodes of the current node.
    for( String c : adjacent_nodesof_Examming_the_Node){            //The adjacent Nodes of the 1st Node will be appended in the current subgraph, only if they do not already exist in the currentSubgraph.
        if(!candidate_MCS.contains(c))
            candidate_MCS.add(c);}
    
    int pointer=1;                                                  // The pointer that indicates the node of the arrayList of the candidate MCS, in order to be xamined and be added its adjacent Nodes.
    while(pointer<candidate_MCS.size()){                            // The pointer iterates all the Nodes of the Arraylist - candidate MCS.
        examing_the_Node=candidate_MCS.get(pointer);                // The Node of the candidate MCS that is pointed by the pointer is assigned to the currentNode.
        if(unexamined_Nodes.contains(examing_the_Node))             // The Node to be eaxmined is removed from the list.
            unexamined_Nodes.remove(examing_the_Node);
        adjacent_nodesof_Examming_the_Node=(TreeSet<String>) allEdges.get(examing_the_Node);            // The adjacent Nodes of the examingNode will be appended in the candidate MCS
        for( String candidateNodeToBeAdded : adjacent_nodesof_Examming_the_Node){                       // We add one by one the neighbour nodes of the examing_the_Node --> in the current subgraph.
        if(!candidate_MCS.contains(candidateNodeToBeAdded))                                             // The addition of a node on the Candidate_MCS occurs if this node does not already exist in the Candidate_MCS.
            candidate_MCS.add(candidateNodeToBeAdded);}
    pointer++;
    }

    //System.out.println("A common subgraph is: "+candidate_MCS);
    if(maxCommonSubgraph.size()<candidate_MCS.size()){              //If the Candidate_MCS is bigger than the MCS then the MCS is the Candidate_MCS.
        maxCommonSubgraph=new ArrayList<>(candidate_MCS);}
    candidate_MCS.clear();
}
//System.out.println("The maximum common subgraph is: "+maxCommonSubgraph);                               //The MCS is illustrated.
                                                                                                        //Now on the subgraph with the undirected edges is being constructed.
ArrayList<String> maxSubgraphWithEdges=new ArrayList<>();
for(String aNodeOfTheMCS: maxCommonSubgraph){
    TreeSet<String> adjacentNodes_OfaNodeByMCS=(TreeSet<String>) allEdges.get(aNodeOfTheMCS);
    for( String an_AdjacentNode_OfaNodeByMCS : adjacentNodes_OfaNodeByMCS){                             //The neighbour Nodes of a Node of the MCS are added.
        maxSubgraphWithEdges.add(aNodeOfTheMCS+"_"+an_AdjacentNode_OfaNodeByMCS);
        if(aNodeOfTheMCS.equals(an_AdjacentNode_OfaNodeByMCS))                                          //A selfloop edge will be added twice because it should exist two times for the two directions.
            maxSubgraphWithEdges.add(aNodeOfTheMCS+"_"+an_AdjacentNode_OfaNodeByMCS);
    }
}
//System.out.println("The UNdirected Edges that contained in the MCS are: "+maxSubgraphWithEdges);
return 0.5f*maxSubgraphWithEdges.size();
}//=================================================== End of CommonLabelSubGraph_Sum_Of_UndirectedEdges() ===================================================



//============================================Compare_WeightedGraphs()=======================================
/**The comparison of two Graphs is based on the amount of Common Edges and the corresponded edge weights.
 * The Value Similarity (VS) and Normalized Value Similarity (NVS) are metrics to be reckoned.
 * The VS expressed the division between the number of common Edges with the number of 
 * the Edges of the smaller Graph taking into account the edge weights.
 * @param TestingGraph  The Edges of the Document-Weighted-Graph. 
 * @param CategoryGraph The Edges of the topic-Weighted-Graph.
 * @return A float array of two cells. The cell 0 is the VS figure. The cell 1 is the NVS figure.  */
//============================================================================================================
public static float[] Compare_NGram_WeightedGraphs(HashMap TestingGraph, HashMap CategoryGraph){
System.out.println("This method is not supported for word graphs");
exit(0);
float[] VS_NVS= new float[2];
int numberOfEdges1 = TestingGraph.size();
int numberOfEdges2 = CategoryGraph.size();
float sum_OfCommonEdges=0, min_WeightOf2Edges, max_WeightOf2Edges;
Set edgesOfTestingGraph=TestingGraph.keySet();
for(Object i : edgesOfTestingGraph){
    if(CategoryGraph.containsKey(i)){    
        if((float)CategoryGraph.get(i)>=(float)TestingGraph.get(i)){max_WeightOf2Edges=(float)CategoryGraph.get(i);min_WeightOf2Edges=(float)TestingGraph.get(i);}
        else{max_WeightOf2Edges=(float)TestingGraph.get(i); min_WeightOf2Edges=(float)CategoryGraph.get(i);}
        sum_OfCommonEdges+=min_WeightOf2Edges/max_WeightOf2Edges;
    }
}
float maximum_EdgesOf_2Graphs,minimum_EdgesOf_2Graphs;
if(numberOfEdges1>numberOfEdges2){maximum_EdgesOf_2Graphs=numberOfEdges1; minimum_EdgesOf_2Graphs=numberOfEdges2;}
else {maximum_EdgesOf_2Graphs=numberOfEdges2; minimum_EdgesOf_2Graphs=numberOfEdges1;}

VS_NVS[0]= sum_OfCommonEdges/maximum_EdgesOf_2Graphs;
VS_NVS[1]=VS_NVS[0]*maximum_EdgesOf_2Graphs/minimum_EdgesOf_2Graphs;
return VS_NVS;    
}//==================================== End of Compare_NGram_WeightedGraphs ===================================

}//==================================== End of Class GraphComparator ==========================================

