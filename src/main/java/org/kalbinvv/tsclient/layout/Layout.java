package org.kalbinvv.tsclient.layout;

import org.kalbinvv.tsclient.Updateable;

import javafx.scene.Node;
import javafx.scene.layout.VBox;

public abstract class Layout implements Updateable{

	private final VBox vBox;
	
	public Layout(VBox vBox){
		this.vBox = vBox;
	}
	
	void clearNodes() {
		getVBox().getChildren().clear();
	}
	
	void addNode(Node node) {
		getVBox().getChildren().add(node);
	}
	
	public VBox getVBox() {
		return vBox;
	}
	
	public abstract void draw();
	
	public void view() {
		draw();
	}
	
	public void update() {}
	
}
