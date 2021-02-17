package com.ai4java.tribuo.examples.wine.service;

public class WineQualityPredictionRequest {

	final static String[] featureNames = {"fixed acidity", "volatile acidity", "citric acid", "residual sugar", "chlorides", "free sulfur dioxide", "total sulfur dioxide", "density", "pH", "sulphates", "alcohol"};

	private double fixedAcidity;
	private double volatileAcidity;
	private double citricAcid;
	private double residualSugar;
	private double chlorides;
	private double freeSulfurDioxide;
	private double totalSulfurDioxide;
	private double density;
	private double pH;
	private double sulphates;
	private double alcohol;

	// 'no-args' constructor:
	public WineQualityPredictionRequest() {}

	
	public double[] getFeatureValues() {
		return new double[] {fixedAcidity, volatileAcidity, citricAcid, residualSugar, chlorides, freeSulfurDioxide, totalSulfurDioxide, density, pH, sulphates, alcohol};
	}

	// getters and setters:
	public double getFixedAcidity() {
		return fixedAcidity;
	}

	public double getVolatileAcidity() {
		return volatileAcidity;
	}

	public double getCitricAcid() {
		return citricAcid;
	}

	public double getResidualSugar() {
		return residualSugar;
	}

	public double getChlorides() {
		return chlorides;
	}

	public double getFreeSulfurDioxide() {
		return freeSulfurDioxide;
	}

	public double getTotalSulfurDioxide() {
		return totalSulfurDioxide;
	}

	public double getDensity() {
		return density;
	}

	public double getpH() {
		return pH;
	}

	public double getSulphates() {
		return sulphates;
	}

	public double getAlcohol() {
		return alcohol;
	}
}