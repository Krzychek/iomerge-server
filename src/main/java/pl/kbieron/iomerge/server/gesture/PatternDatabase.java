package pl.kbieron.iomerge.server.gesture;

import org.springframework.stereotype.Component;
import pl.kbieron.iomerge.model.message.keyboard.BackBtnClick;
import pl.kbieron.iomerge.model.message.keyboard.HomeBtnClick;
import pl.kbieron.iomerge.model.message.keyboard.MenuBtnClick;

import java.awt.Point;
import java.util.Arrays;
import java.util.List;


@Component
class PatternDatabase {

	private final List<Pattern> patterns = Arrays.asList( //
			new Pattern(Arrays.asList( // up
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
					new Point(0, 0)), new HomeBtnClick(), "up"), //
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
					new Point(0, 0)), new BackBtnClick(), "left"), //
			new Pattern(Arrays.asList( // circle
					new Point(195, 0),  //
					new Point(286, 20),  //
					new Point(378, 1),  //
					new Point(470, 0),  //
					new Point(562, 0),  //
					new Point(654, 0),  //
					new Point(746, 2),  //
					new Point(838, 34),  //
					new Point(930, 7),  //
					new Point(977, 284),  //
					new Point(886, 343),  //
					new Point(794, 265),  //
					new Point(702, 455),  //
					new Point(610, 591),  //
					new Point(518, 50),  //
					new Point(426, 53),  //
					new Point(334, 237),  //
					new Point(242, 349),  //
					new Point(150, 28),  //
					new Point(58, 1)), new MenuBtnClick(), "circle") //
	);

	public List<Pattern> getPatterns() {
		return patterns;
	}
}
