package org.kalbinvv.tsclient.layout;

import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class SettingsLayout extends Layout{

	public SettingsLayout(VBox vBox) {
		super(vBox);
	}

	@Override
	public void draw() {
		clearNodes();
		Text text = new Text("Настройки не готовы");
		addNode(text);
	}

}
