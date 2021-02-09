package com.ai4java.tribuo.examples.wine;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tribuo.Dataset;
import org.tribuo.Model;
import org.tribuo.MutableDataset;
import org.tribuo.Prediction;
import org.tribuo.Trainer;
import org.tribuo.common.tree.AbstractCARTTrainer;
import org.tribuo.common.tree.RandomForestTrainer;
import org.tribuo.data.csv.CSVIterator;
import org.tribuo.data.csv.CSVLoader;
import org.tribuo.datasource.ListDataSource;
import org.tribuo.evaluation.TrainTestSplitter;
import org.tribuo.regression.RegressionFactory;
import org.tribuo.regression.Regressor;
import org.tribuo.regression.ensemble.AveragingCombiner;
import org.tribuo.regression.evaluation.RegressionEvaluation;
import org.tribuo.regression.evaluation.RegressionEvaluator;
import org.tribuo.regression.rtree.CARTRegressionTrainer;
import org.tribuo.regression.rtree.impurity.MeanSquaredError;

import com.oracle.labs.mlrg.olcut.provenance.ProvenanceUtil;

/** 
 * Implementing a basic regression task using Oracle Tribuo ML Library
 * For more information see: ai4java.com
 */
public class WineQualityRegression {

	private static Logger log = LoggerFactory.getLogger(WineQualityRegression.class);

	private static final String DATASET_PATH = "src/main/resources/datasets/winequality-red.csv";
	private static final String MODEL_PATH = "src/main/resources/models/winequality-red-regressor.ser";

	protected Model<Regressor> model;
	protected Trainer<Regressor> trainer;
	protected Dataset<Regressor> trainSet;
	protected Dataset<Regressor> testSet;

	public static void main(String[] args) throws Exception {
		WineQualityRegression example = new WineQualityRegression();

		example.createTrainer();
		example.createDatasets();
		example.trainAndEvaluate();
		example.saveModel();
	}

	protected void createTrainer() {
		log.info("Creating trainer....");

		CARTRegressionTrainer subsamplingTree = new CARTRegressionTrainer(
				Integer.MAX_VALUE,                // maxDepth - the maximum depth of the tree
				AbstractCARTTrainer.MIN_EXAMPLES, // minChildWeight - the minimum node weight to consider it for a split
				0.9f,                             // fractionFeaturesInSplit - the fraction of features available in each split
				new MeanSquaredError(),           // impurity - the impurity function to use to determine split quality
				Trainer.DEFAULT_SEED              // seed - the Random Number Generator seed
				);

		trainer = new RandomForestTrainer<>(
				subsamplingTree,                  // trainer - the tree trainer
				new AveragingCombiner(),          // combiner - the combining function for the ensemble
				10                                // numMembers - the number of ensemble members to train
				);
	}

	public void createDatasets() throws Exception {
		log.info("Creating datasets....");

		RegressionFactory regressionFactory = new RegressionFactory();
		CSVLoader<Regressor> csvLoader = new CSVLoader<>(';', CSVIterator.QUOTE, regressionFactory);
		ListDataSource<Regressor> dataSource = csvLoader.loadDataSource(Paths.get(DATASET_PATH), "quality");

		TrainTestSplitter<Regressor> dataSplitter = new TrainTestSplitter<>(dataSource,0.7,1L);
		
		trainSet = new MutableDataset<>(dataSplitter.getTrain());
		log.info(String.format("Train set size = %d, num of features = %d",
				trainSet.size(), trainSet.getFeatureMap().size()));

		testSet = new MutableDataset<>(dataSplitter.getTest());
		log.info(String.format("Test set size = %d, num of features = %d",
				testSet.size(), testSet.getFeatureMap().size()));
	}

	public void trainAndEvaluate() throws Exception {
		log.info("Training model...");

		model = trainer.train(trainSet);
		evaluate(model, "trainSet", trainSet);

		log.info("Testing model...");
		evaluate(model, "testSet", testSet);

		log.info("Dataset Provenance: --------------------");
		log.info(ProvenanceUtil.formattedProvenanceString(model.getProvenance().getDatasetProvenance()));
		log.info("Trainer Provenance: --------------------");
		log.info(ProvenanceUtil.formattedProvenanceString(model.getProvenance().getTrainerProvenance()));
	}

	private void evaluate(Model<Regressor> model, String datasetName, Dataset<Regressor> dataset) {
		log.info("Results for " + datasetName + "---------------------");

		RegressionEvaluator evaluator = new RegressionEvaluator();
		RegressionEvaluation evaluation = evaluator.evaluate(model,dataset);

		Regressor dimension0 = new Regressor("DIM-0",Double.NaN);
		
		log.info("MAE: " + evaluation.mae(dimension0));
		log.info("RMSE: " + evaluation.rmse(dimension0));
		log.info("R^2: " + evaluation.r2(dimension0));
	}

	private void saveModel() throws Exception {
		File modelFile = new File(MODEL_PATH);
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(modelFile))) {
			oos.writeObject(model);
		}
	}
}