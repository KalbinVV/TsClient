package org.kalbinvv.tsclient.layout;

import org.kalbinvv.tsclient.EmptyUpdateable;
import org.kalbinvv.tsclient.TsClient;

import animatefx.animation.FadeIn;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class SettingsLayout extends Layout{

	public SettingsLayout(VBox vBox) {
		super(vBox);
	}

	@Override
	public void view() {
		draw();
		TsClient.setUpdateable(new EmptyUpdateable());
		new FadeIn(getVBox()).play();
	}
	
	@Override
	public void draw() {
		clearNodes();
		Text text = new Text("Настройки не готовы");
		addNode(text);
	}

}
