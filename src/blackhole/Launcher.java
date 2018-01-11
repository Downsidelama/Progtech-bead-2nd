package blackhole;

import blackhole.model.BlackHoleEngine;
import blackhole.view.BlackHoleFrame;
import blackhole.view.StartFrame;

public class Launcher {

	/*
	 * This little fella starts the party here
	 * 
	 */
	
	public static void main(String[] args) {
		BlackHoleEngine bhe = new BlackHoleEngine();
		StartFrame start = new StartFrame(bhe);
		start.showFrame();
	}

}
