import numpy
def tenFoldEvaluation(training_Documents_Similarities,training_Document_Categories,testing_Documents_Similarities,testing_Document_Categories):
	#from sklearn.neighbors.nearest_centroid import NearestCentroid
	from sklearn.metrics import precision_recall_fscore_support
	from sklearn.naive_bayes import GaussianNB
	#from sklearn import tree
	from sklearn import svm
	#from sklearn import linear_model
	#from sklearn.linear_model import SGDClassifier
	#from sklearn.lda import LDA
	#from sklearn.kernel_ridge import KernelRidge
	#from sklearn.linear_model import SGDClassifier
	from sklearn.cross_decomposition import PLSRegression
	from sklearn.ensemble import RandomForestClassifier
	from sklearn.ensemble import ExtraTreesClassifier
	#from sklearn import gaussian_process
	from sklearn.ensemble import AdaBoostClassifier
	from sklearn.ensemble import GradientBoostingClassifier
	foldFile=["Fold_One", "Fold_Two", "Fold_Three", "Fold_Four", "Fold_Five", "Fold_Six", "Fold_Seven", "Fold_Eight", "Fold_Nine", "Fold_Ten"]
	macro_PR_RE_FM_for_10Folds = numpy.empty(shape=0,dtype=float)
	testing_PredictedCategories_for_10Folds_Micro   = numpy.empty(shape=0,dtype=int)
	testing_GroundTruthCategories_for_10Folds_Micro = numpy.empty(shape=0,dtype=int)
	for fold in foldFile:
		print('We process the',fold)
		path='Evaluation_WordGraphsClassification/'+fold+'_'

		training_DocumentSimilarities_for_1Fold_Macro=numpy.loadtxt(path+training_Documents_Similarities)		#Training Similarity-Values 	  for 1 Fold
		training_GroundTruthCategories_for_1Fold_Macro=numpy.loadtxt(path+training_Document_Categories)			#Training GroundTruth-Categories  for 1 Fold
		testing_DocumentSimilarities_for_1Fold_Macro=numpy.loadtxt(path+testing_Documents_Similarities)
		testing_GroundTruthCategories_for_1Fold_Macro = numpy.loadtxt(path+testing_Document_Categories)
		testing_PredictedCategories_for_1Fold_Macro = numpy.empty(shape=0,dtype=float)
		testing_GroundTruthCategories_for_10Folds_Micro=numpy.append(testing_GroundTruthCategories_for_10Folds_Micro,testing_GroundTruthCategories_for_1Fold_Macro)

		clf = GaussianNB()
		clf.fit(training_DocumentSimilarities_for_1Fold_Macro , training_GroundTruthCategories_for_1Fold_Macro)
		for row in testing_DocumentSimilarities_for_1Fold_Macro:
			testing_PredictedCategories_for_1Fold_Macro = numpy.append (testing_PredictedCategories_for_1Fold_Macro , int(clf.predict(row))) #The predictions for one Fold.
		testing_PredictedCategories_for_10Folds_Micro   = numpy.append(testing_PredictedCategories_for_10Folds_Micro,testing_PredictedCategories_for_1Fold_Macro)
		macro_PR_RE_FM_for_10Folds = numpy.append(macro_PR_RE_FM_for_10Folds, precision_recall_fscore_support(testing_GroundTruthCategories_for_1Fold_Macro ,testing_PredictedCategories_for_1Fold_Macro ,average='macro'))
	macro_PR_RE_FM_for_10Folds=macro_PR_RE_FM_for_10Folds.reshape(10,4)
	print('The Macro classification is: ', macro_PR_RE_FM_for_10Folds[:,0:3].mean(axis=0,dtype=float))
	print('The Micro classification is: ', precision_recall_fscore_support(testing_GroundTruthCategories_for_10Folds_Micro,testing_PredictedCategories_for_10Folds_Micro ,average='micro')[0:3])
	
tenFoldEvaluation('training_Documents_Similarities.txt','training_Documents_Categories.txt','testing_Documents_Similarities.txt','testing_Document_Categories.txt')