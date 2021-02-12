package com.ai4java.tribuo.examples.wine.service;

public class WineQualityPredictionResult {

	final private double quality;

	public WineQualityPredictionResult(double quality) {
		this.quality = quality;
	}

	public double getQuality() {
		return quality;
	}

}