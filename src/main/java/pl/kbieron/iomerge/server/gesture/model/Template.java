package pl.kbieron.iomerge.server.gesture.model;

import java.awt.Point;
import java.util.List;


public interface Template extends Iterable<List<Point>> {

	List<Point> getSegment(int i);

	List<Point> getFullSegment();

	int size();
}
