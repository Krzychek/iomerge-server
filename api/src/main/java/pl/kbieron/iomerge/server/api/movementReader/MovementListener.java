package pl.kbieron.iomerge.server.api.movementReader;

/**
 * Receives mouse events
 */
public interface MovementListener {

	void move(int dx, int dy);

	default void mousePressed() {}

	default void mouseReleased() {}

	default void mouseWheelMoved(int wheelRotation) {}

}
