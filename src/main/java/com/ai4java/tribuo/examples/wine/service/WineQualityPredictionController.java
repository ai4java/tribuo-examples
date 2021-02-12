package com.ai4java.tribuo.examples.wine.service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tribuo.Example;
import org.tribuo.Model;
import org.tribuo.Prediction;
import org.tribuo.impl.ArrayExample;
import org.tribuo.regression.RegressionFactory;
import org.tribuo.regression.Regressor;

import com.oracle.labs.mlrg.olcut.provenance.ProvenanceUtil;

@RestController
@RequestMapping("/predict")
public class WineQualityPredictionController {

	private static Logger log = LoggerFactory.getLogger(WineQualityPredictionController.class);

	private static final String MODEL_PATH = "src/main/resources/models/winequality-red-regressor.ser";

	private final Model<Regressor> model;

	public WineQualityPredictionController() throws Exception {
		model = loadModel();
	}

	@SuppressWarnings("unchecked")
	private Model<Regressor> loadModel()  throws Exception {

		File modelFile = new File(MODEL_PATH);

		Model<Regressor> loadedModel;
		try (ObjectInputStream ois = new ObjectInputStream(
				new BufferedInputStream(new FileInputStream(modelFile)))) {
			loadedModel = (Model<Regressor>) ois.readObject();
		}

		if (!loadedModel.validate(Regressor.class)) {
			throw new Exception("The saved model is not a regressor!");
		}

		log.info("Dataset Provenance: --------------------");
		log.info(ProvenanceUtil.formattedProvenanceString(loadedModel.getProvenance().getDatasetProvenance()));
		log.info("Trainer Provenance: --------------------");
		log.info(ProvenanceUtil.formattedProvenanceString(loadedModel.getProvenance().getTrainerProvenance()));

		return loadedModel;
	}

	@PostMapping("/wineQuality")
	public WineQualityPredictionResult predictQuality(
			@RequestBody WineQualityPredictionRequest request) {

		Regressor outputPlaceHolder = RegressionFactory.UNKNOWN_REGRESSOR;

		// toExample() method within request class
		Example<Regressor> example = new ArrayExample<>(
				outputPlaceHolder, 
				WineQualityPredictionRequest.featureNames, 
				request.getFeatureValues()
				);

		Prediction<Regressor> prediction = model.predict(example);
		double result = prediction.getOutput().getValues()[0]; 

		return new WineQualityPredictionResult(result);
	}
}
