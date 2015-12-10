package pl.kbieron.iomerge.server.gesture.model;

import java.awt.Point;
import java.io.Serializable;
import java.util.List;


public interface Template extends Serializable {

	List<Point> getSegment(int i);

	List<Point> getFullSegment();
}
