package org.kalbinvv.tsclient.layout;

import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class TestsLayout extends Layout{

	public TestsLayout(VBox vBox) {
		super(vBox);
	}

	@Override
	public void draw() {
		clearNodes();
		Text text = new Text("Тесты: ");
		addNode(text);
	}

}
