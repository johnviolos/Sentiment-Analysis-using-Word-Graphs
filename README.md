# Sentiment Analysis using Word-Graphs

This repository contains the code, an example dataset and the evaluation method for the method Sentiment Analysis using Word-Graphs. The Word-Graph Sentiment Analysis Method is proposed to identify the sentiment that expressed in a microblog document using the sequence of the words that contains. The sequence of the words can be represented using graphs in which graph similarity metrics and classification algorithms can be applied to produce sentiment predictions. Experiments that were carried out with this method in a Twitter dataset validate the proposed model and allow us to further understand the metrics and the criteria that can be applied in words-graphs to predict the sentiment disposition of short, microblog documents.

## Getting Started

The experimental evaluation has two parts. The first part is a java project that involves the representation of documents with word-graphs and a representation of the relations among the word-graphs as vectors.
The second part involves a python script that takes as input the vector representation of the documents and their label, it trains a common base classifier (such as Decision trees, Bayes classifier, SVM, etc) using the ten fold cross validation approach and it evaluates the results with the micro/macro precision,recall and f-measure metrics.


### Prerequisites and Installing

The graph and the vector representation is a java eclipse project that can be import as general project from folder.
It has only one dependency: [org.apache.commons.lang3.StringUtils](https://commons.apache.org/proper/commons-lang/apidocs/org/apache/commons/lang3/StringUtils.html)

The python script uses the modules: 
* [scikit-learn](https://scikit-learn.org/stable/) 
* [NumPy](https://numpy.org) 

The Java project reads a document file in the following format and outputs a vector representation for each document.

```
B1E example text1 camera sony alpha dslra digital cameragb kit new price usd online order http 
B2E example text2 blog update cc user audi s http 
...
```

Where the capital letters B and E declare the begin and the end of the label number and each line has one lower cased document.

The output paths of the eclipse project should be declared according to the input paths of the python script.
In the method Utilities.WriteIntoFilesTrainingTestingData() you should hard code the paths.


## Running the Experiments

The word-graph document representations and the relation-comparison of the graphs can be parameterized with various configuration.
We can define the rank of the word vicinity from two to ten (frame size)

We can apply one of the following graph comparison metrics: 
* Containment Similarity
* Maximum Common Subgraph taking into account the common nodes
* Maximum Common Subgraph taking into account the undirected edges
* Maximum Common Subgraph taking into account the directed edges
* Tversky Similarity
* Containment Similarity with Mutual information as feature engineering technique

The sentiment analysis problem in this study has been reduced to a supervised ML problem with three labels positive, negative and neutral disposition.
So the number of categories is three. 

## Acknowledgments

If you find this code useful in your research, please consider citing.

Violos, J., Tserpes, K., Psomakelis, E., Psychas, K., Varvarigou, T., 2016. Sentiment Analysis Using Word-Graphs, in: Proceedings of the 6th International Conference on Web Intelligence, Mining and Semantics, WIMS ’16. ACM, New York, NY, USA, pp. 22:1–22:9. https://doi.org/10.1145/2912845.2912863
