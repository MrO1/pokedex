package hub;

import gui.GUIHandler;

public class Dex {
	GUIHandler handler;
	public Dex(){
		handler = new GUIHandler();
	}
	
	public void mainLogic(){
		handler.setUpGUI();
	}

}
