package pl.kbieron.iomerge.server.gesture.alghorithm;

import pl.kbieron.iomerge.server.gesture.model.Template;


public class MatchResult {

	private Template template;

	private double prob;

	private int mostLikelySegment;

	public MatchResult(Template template, double prob, int mostLikeliSegment) {
		this.template = template;
		this.prob = prob;
		this.mostLikelySegment = mostLikeliSegment;
	}
}

