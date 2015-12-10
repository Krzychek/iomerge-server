package pl.kbieron.iomerge.server.utilities;

public interface MovementListener {

	void move(int dx, int dy);

	default void mouseClicked() {}

	default void MousePressed() {}

	default void mouseReleased() {}

}
