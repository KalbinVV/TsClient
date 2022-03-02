package org.kalbinvv.tsclient.layout;

import javafx.scene.Node;
import javafx.scene.layout.VBox;

public abstract class Layout {

	private final VBox vBox;
	
	public Layout(VBox vBox){
		this.vBox = vBox;
	}
	
	void clearNodes() {
		vBox.getChildren().clear();
	}
	
	void addNode(Node node) {
		vBox.getChildren().add(node);
	}
	
	public abstract void draw();
	
}
