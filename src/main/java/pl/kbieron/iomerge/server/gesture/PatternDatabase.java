package pl.kbieron.iomerge.server.gesture;

import org.springframework.stereotype.Component;
import pl.kbieron.iomerge.model.ClientAction;
import pl.kbieron.iomerge.server.gesture.model.Pattern;

import java.awt.Point;
import java.util.Arrays;
import java.util.List;


@Component
public class PatternDatabase {

	private List<Pattern> patterns = Arrays.asList( //
			new Pattern(Arrays.asList( // left
					new Point(0, 950), //
					new Point(0, 900), //
					new Point(0, 850), //
					new Point(0, 800), //
					new Point(0, 750), //
					new Point(0, 700), //
					new Point(0, 650), //
					new Point(0, 600), //
					new Point(0, 550), //
					new Point(0, 500), //
					new Point(0, 450), //
					new Point(0, 400), //
					new Point(0, 350), //
					new Point(0, 300), //
					new Point(0, 250), //
					new Point(0, 200), //
					new Point(0, 150), //
					new Point(0, 100), //
					new Point(0, 50), //
					new Point(0, 0)), ClientAction.HOME_BTN), //
			new Pattern(Arrays.asList( // left
					new Point(950, 0), //
					new Point(900, 0), //
					new Point(850, 0), //
					new Point(800, 0), //
					new Point(750, 0), //
					new Point(700, 0), //
					new Point(650, 0), //
					new Point(600, 0), //
					new Point(550, 0), //
					new Point(500, 0), //
					new Point(450, 0), //
					new Point(400, 0), //
					new Point(350, 0), //
					new Point(300, 0), //
					new Point(250, 0), //
					new Point(200, 0), //
					new Point(150, 0), //
					new Point(100, 0), //
					new Point(50, 0), //
					new Point(0, 0)), ClientAction.BACK_BTN), //
			new Pattern(Arrays.asList( // circle
					new Point(65, 0), //
					new Point(179, 0), //
					new Point(294, 0), //
					new Point(408, 3), //
					new Point(523, 13), //
					new Point(637, 30), //
					new Point(752, 59), //
					new Point(867, 151), //
					new Point(918, 108), //
					new Point(901, 525), //
					new Point(786, 151), //
					new Point(672, 866), //
					new Point(557, 799), //
					new Point(443, 489), //
					new Point(328, 775), //
					new Point(213, 534), //
					new Point(99, 734), //
					new Point(15, 269), //
					new Point(129, 292), //
					new Point(244, 238)), ClientAction.MENU_BTN) //
	);

	public List<Pattern> getPatterns() {
		return patterns;
	}
}
