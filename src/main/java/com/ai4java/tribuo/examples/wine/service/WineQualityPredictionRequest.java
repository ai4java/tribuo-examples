package com.ai4java.tribuo.examples.wine.service;

public class WineQualityPredictionRequest {

	final static String[] featureNames = {"fixed acidity", "volatile acidity", "citric acid", "residual sugar", "chlorides", "free sulfur dioxide", "total sulfur dioxide", "density", "pH", "sulphates", "alcohol"};///TODO

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

	// 'all-args' constructor:
	public WineQualityPredictionRequest(double fixedAcidity, double volatileAcidity, double citricAcid,
			double residualSugar, double chlorides, double freeSulfurDioxide, double totalSulfurDioxide, double density,
			double pH, double sulphates, double alcohol) {

		this.fixedAcidity = fixedAcidity;
		this.volatileAcidity = volatileAcidity;
		this.citricAcid = citricAcid;
		this.residualSugar = residualSugar;
		this.chlorides = chlorides;
		this.freeSulfurDioxide = freeSulfurDioxide;
		this.totalSulfurDioxide = totalSulfurDioxide;
		this.density = density;
		this.pH = pH;
		this.sulphates = sulphates;
		this.alcohol = alcohol;
	}

	public double[] getFeatureValues() {
		return new double[] {getFixedAcidity(), getVolatileAcidity(), getCitricAcid(), getResidualSugar(), getChlorides(),
				getFreeSulfurDioxide(), getTotalSulfurDioxide(), getDensity(), getpH(), getSulphates(), getAlcohol()};
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