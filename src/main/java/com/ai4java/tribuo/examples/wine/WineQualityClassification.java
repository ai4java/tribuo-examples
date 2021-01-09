package com.ai4java.tribuo.examples.wine;


import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tribuo.Dataset;
import org.tribuo.Model;
import org.tribuo.MutableDataset;
import org.tribuo.Trainer;
import org.tribuo.classification.Label;
import org.tribuo.classification.LabelFactory;
import org.tribuo.classification.evaluation.LabelEvaluation;
import org.tribuo.classification.evaluation.LabelEvaluator;
import org.tribuo.classification.xgboost.XGBoostClassificationTrainer;
import org.tribuo.data.csv.CSVIterator;
import org.tribuo.data.csv.CSVLoader;
import org.tribuo.datasource.ListDataSource;
import org.tribuo.evaluation.TrainTestSplitter;


/** 
 * Implementing a basic classification task using Oracle Tribuo ML Library
 * For more information see: ai4java.com
 */
public class WineQualityClassification {

	private static Logger log = LoggerFactory.getLogger(WineQualityClassification.class);

	private static final String DATASET_PATH = "src/main/resources/datasets/winequality-red.csv";

	protected Trainer<Label> trainer;
	protected Dataset<Label> trainSet;
	protected Dataset<Label> testSet;

	public static void main(String[] args) throws Exception {
		WineQualityClassification example = new WineQualityClassification();

		example.createTrainer();
		example.createDatasets();
		example.trainAndEvaluate();
	}

	protected void createTrainer() {
		log.info("Creating trainer....");
		trainer = new XGBoostClassificationTrainer(50);
	}

	public void createDatasets() throws Exception {
		log.info("Creating datasets....");

		LabelFactory labelFactory = new LabelFactory();
		CSVLoader<Label> csvLoader = new CSVLoader<>(';', CSVIterator.QUOTE, labelFactory);
		ListDataSource<Label> dataSource = csvLoader.loadDataSource(Paths.get(DATASET_PATH), "quality");

		TrainTestSplitter<Label> dataSplitter = new TrainTestSplitter<>(dataSource,0.7,1L);

		trainSet = new MutableDataset<>(dataSplitter.getTrain());
		log.info(String.format("Train set size = %d, num of features = %d, classes = %s",
				trainSet.size(), trainSet.getFeatureMap().size(), trainSet.getOutputInfo().getDomain()));

		testSet = new MutableDataset<>(dataSplitter.getTest());
		log.info(String.format("Test set size = %d, num of features = %d, classes = %s",
				testSet.size(), testSet.getFeatureMap().size(), testSet.getOutputInfo().getDomain()));
	}

	public void trainAndEvaluate() throws Exception {
		log.info("Training model...");

		Model<Label> model = trainer.train(trainSet);
		evaluate(model, "trainSet", trainSet);

		log.info("Testing model...");
		evaluate(model, "testSet", testSet);
	}

	private void evaluate(Model<Label> model, String datasetName, Dataset<Label> trainData) {
		log.info("Results for " + datasetName + "---------------------");

		// Evaluate the model on the training data (this is a useful debugging tool)
		LabelEvaluator eval = new LabelEvaluator();
		LabelEvaluation evaluation = eval.evaluate(model,trainData);

		log.info("Accuracy: " + evaluation.accuracy());
		log.info("Confusion Matrix: \n" + evaluation.getConfusionMatrix());
	}
}