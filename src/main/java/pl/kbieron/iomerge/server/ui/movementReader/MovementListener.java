package pl.kbieron.iomerge.server.ui.movementReader;

public interface MovementListener {

	void moveMouse(int dx, int dy);

	default void mouseClicked() {}

	default void mousePressed() {}

	default void mouseReleased() {}

}
