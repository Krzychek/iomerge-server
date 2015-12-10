package pl.kbieron.iomerge.server.gesture.alghorithm;/*
 * Continuous Recognition and Visualization of Pen Strokes and Touch-Screen Gestures
 * Version: 2.0
 *
 * If you use this code for your research then please remember to cite our paper:
 * 
 * Kristensson, P.O. and Denby, L.C. 2011. Continuous recognition and visualization
 * of pen strokes and touch-screen gestures. In Procceedings of the 8th Eurographics
 * Symposium on Sketch-Based Interfaces and Modeling (SBIM 2011). ACM Press: 95-102.
 * 
 * Copyright (C) 2011 by Per Ola Kristensson, University of St Andrews, UK.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

import pl.kbieron.iomerge.server.gesture.model.Template;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * A continuous gesture recognizer. Outputs a probability distribution over
 * a set of template gestures as a function of received sampling points.
 * <p/>
 * History:
 * Version 1.0 (August 12, 2011)   - Initial public release
 * Version 2.0 (September 6, 2011) - Simplified the public interface, simplified
 * internal implementation.
 * <p/>
 * For details of its operation, see the paper referenced below.
 * <p/>
 * Documentation is here: http://pokristensson.com/increc.html
 * <p/>
 * Copyright (C) 2011 Per Ola Kristensson, University of St Andrews, UK.
 * <p/>
 * If you use this code for your research then please remember to cite our paper:
 * <p/>
 * Kristensson, P.O. and Denby, L.C. 2011. Continuous recognition and visualization
 * of pen strokes and touch-screen gestures. In Procceedings of the 8th Eurographics
 * Symposium on Sketch-Based Interfaces and Modeling (SBIM 2011). ACM Press: 95-102.
 *
 * @author Per Ola Kristensson
 * @author Leif Denby
 */
public class ContinuousGestureRecognizer {
	
	/* Beginning of public interface */

	private static final int MAX_RESAMPLING_PTS = 1000;

	private static double DEFAULT_E_SIGMA = 200.0;

	private static double DEFAULT_BETA = 400.0;

	private static double DEFAULT_LAMBDA = 0.4;

	private static double DEFAULT_KAPPA = 1.0;
	
	/* End of public interface */

	private static Rect normalizedSpace = new Rect(0, 0, 1000, 1000);

	private List<Pattern> patterns = new ArrayList<>();

	private static double getEuclideanDistance(List<Point> pts1, List<Point> pts2) {
		// XXX done
		return 0;
	}

	private static double getTurningAngleDistance(List<Point> pts1, List<Point> pts2) {
		// XXX done
		return 0;
	}

	private static double getEuclideanDistance(Point pt1, Point pt2) {
		// XXX done
		return 0;
	}

	private static double getSquaredEuclidenDistance(Point pt1, Point pt2) {
		// XXX done
		return 0;
	}

	private static double getTurningAngleDistance(Point ptA1, Point ptA2, Point ptB1, Point ptB2) {
		// xxx done
		return 0;
	}

	private static List<List<Point>> generateEquiDistantProgressiveSubSequences(List<Point> pts, int ptSpacing) {
		List<List<Point>> sequences = new ArrayList<>();
		int nSamplePoints = getResamplingPointCount(pts, ptSpacing);
		List<Point> resampledPts = resample(pts, nSamplePoints);
		for ( int i = 1, n = resampledPts.size(); i < n; i++ ) {
			List<Point> seq = deepCopyPts(resampledPts.subList(0, i + 1));
			sequences.add(seq);
		}
		return sequences;
	}

	private static int getResamplingPointCount(List<Point> pts, int samplePointDistance) {
		double len = getSpatialLength(pts);
		return (int) (len / samplePointDistance) + 1;
	}

	private static List<Point> resample(List<Point> points, int numTargetPoints) {
		// xxx done
		return null;
	}

	private static double getSpatialLength(List<Point> pts) {
		double len = 0.0d;
		Iterator<Point> i = pts.iterator();
		if ( i.hasNext() ) {
			Point p0 = i.next();
			while ( i.hasNext() ) {
				Point p1 = i.next();
				len += distance(p0, p1);
				p0 = p1;
			}
		}
		return len;
	}

	private static double distance(Point p1, Point p2) {
		// XXX done
		return 0;
	}

	private static int[] toArray(List<Point> points) {
		int[] out = new int[points.size() * 2];
		for ( int i = 0, n = points.size() * 2; i < n; i += 2 ) {
			out[i] = points.get(i / 2).x;
			out[i + 1] = points.get(i / 2).y;
		}
		return out;
	}

	private static void resample(int[] template, int[] buffer, int n, int numTargetPoints) {
		// XXX done
	}

	private static int getSpatialLength(int[] pat, int n) {
		int l;
		int i, m;
		int x1, y1, x2, y2;

		l = 0;
		m = 2 * n;
		if ( m > 2 ) {
			x1 = pat[0];
			y1 = pat[1];
			for ( i = 2; i < m; i += 2 ) {
				x2 = pat[i];
				y2 = pat[i + 1];
				l += distance(x1, y1, x2, y2);
				x1 = x2;
				y1 = y2;
			}
			return l;
		} else {
			return 0;
		}
	}

	private static int distance(int x1, int y1, int x2, int y2) {
		// xxx done
		return 0;
	}

	/**
	 * Sets the set of templates this recognizer will recognize.
	 *
	 * @param templates the set of templates this recognizer will recognize
	 */
	public void setTemplateSet(List<Template> templates) {
		// TODO a zrób przykładowy set
	}

	/**
	 * Outputs a list of templates and their associated probabilities for the given input.
	 *
	 * @param input a list of input points
	 * @return a list of templates and their associated probabilities
	 */
	public List<Result> recognize(List<Point> input) {
		return recognize(input, DEFAULT_BETA, DEFAULT_LAMBDA, DEFAULT_KAPPA, DEFAULT_E_SIGMA);
	}

	/**
	 * Outputs a list of templates and their associated probabilities for the given input.
	 *
	 * @param input   a list of input points
	 * @param beta    a parameter, see the paper for details
	 * @param lambda  a parameter, see the paper for details
	 * @param kappa   a parameter, see the paper for details
	 * @param e_sigma a parameter, see the paper for details
	 * @return a list of templates and their associated probabilities
	 */
	public List<Result> recognize(List<Point> input, double beta, double lambda, double kappa, double e_sigma) {
		List<IncrementalResult> incResults = getIncrementalResults(input, beta, lambda, kappa, e_sigma);
		List<Result> results = getResults(incResults);
		//		Collections.sort(results);
		return results;
	}

	private List<IncrementalResult> getIncrementalResults(List<Point> input, double beta, double lambda, double kappa, double e_sigma) {
		List<IncrementalResult> results = new ArrayList<>();
		List<Point> unkPts = deepCopyPts(input);
		normalize(unkPts);
		for ( Pattern pattern : patterns ) {
			IncrementalResult result = getIncrementalResult(unkPts, pattern, beta, lambda, e_sigma);
			List<Point> lastSegmentPts = pattern.segments.get(pattern.segments.size() - 1);
			double completeProb = getLikelihoodOfMatch(resample(unkPts, lastSegmentPts
					.size()), lastSegmentPts, e_sigma, e_sigma / beta, lambda);
			double x = 1 - completeProb;
			result.prob *= (1 + kappa * Math.exp(-x * x));
			results.add(result);
		}
		marginalizeIncrementalResults(results);
		return results;
	}

	private List<Result> getResults(List<IncrementalResult> incrementalResults) {
		List<Result> results = new ArrayList<>(incrementalResults.size());
		for ( IncrementalResult ir : incrementalResults ) {
			Result r = new Result(ir.pattern.template, ir.prob, ir.pattern.segments.get(ir.indexOfMostLikelySegment));
			results.add(r);
		}
		return results;
	}

	private static void normalize(List<Point> pts) {
		// XXX done
	}

	private static IncrementalResult getIncrementalResult(List<Point> unkPts, Pattern pattern, double beta, double lambda, double e_sigma) {
		// xxx done
		return null;
	}

	private static double getLikelihoodOfMatch(List<Point> pts1, List<Point> pts2, double eSigma, double aSigma, double lambda) {
		// xxx done
		return 0;
	}

	private static void marginalizeIncrementalResults(List<IncrementalResult> results) {
		double totalMass = 0.0d;
		for ( IncrementalResult r : results ) {
			totalMass += r.prob;
		}
		for ( IncrementalResult r : results ) {
			r.prob /= totalMass;
		}
	}

	/**
	 * Holds a recognition result.
	 *
	 * @author Per Ola Kristensson
	 */
	public static class Result {

		/**
		 * The template associated with this recognition result.
		 */
		public Template template;

		/**
		 * The probability associated with this recognition result.
		 */
		public double prob;

		/**
		 * The point sequence associated with this recognition result.
		 */
		public List<Point> pts;

		private Result(Template template, double prob, List<Point> pts) {
			this.template = template;
			this.prob = prob;
			this.pts = pts;
		}
	}


	private static class Pattern {

		Template template;

		private List<List<Point>> segments;

		private Pattern(Template template, List<List<Point>> segments) {
			this.template = template;
			this.segments = segments;
		}
	}


	private static class Rect {

		private int x;

		private int y;

		private int width;

		private int height;

		private Rect(int x, int y, int width, int height) {
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
		}
	}


	private static class Centroid {

		private double x;

		private double y;

		private Centroid(double x, double y) {
			this.x = x;
			this.y = y;
		}
	}


	private static class IncrementalResult {

		private Pattern pattern;

		private double prob;

		private int indexOfMostLikelySegment;

		private IncrementalResult(Pattern pattern, double prob, int indexOfMostLikelySegment) {
			this.pattern = pattern;
			this.prob = prob;
			this.indexOfMostLikelySegment = indexOfMostLikelySegment;
		}
	}

}