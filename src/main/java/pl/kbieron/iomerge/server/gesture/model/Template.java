package pl.kbieron.iomerge.server.gesture.model;

import java.awt.Point;
import java.util.List;


public interface Template {

	List<Point> getPoints();

	int size();
}
