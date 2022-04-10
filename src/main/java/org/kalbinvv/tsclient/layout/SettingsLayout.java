package org.kalbinvv.tsclient.layout;

import org.kalbinvv.tsclient.EmptyUpdateable;

import org.kalbinvv.tsclient.TsClient;

import animatefx.animation.FadeIn;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

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
		FontAwesomeIconView settingsIcon = new FontAwesomeIconView();
		settingsIcon.setGlyphName("WRENCH");
		settingsIcon.setGlyphSize(50);
		Label settingsLabel = new Label("Настройки");
		CheckBox autoUpdateCheckBox = new CheckBox("Автообновление контента");
		autoUpdateCheckBox.setSelected(TsClient.isAutoUpdateEnabled());
		autoUpdateCheckBox.setOnAction((ActionEvent event) -> {
			TsClient.setAutoUpdateEnabled(autoUpdateCheckBox.isSelected());
		});
		addNode(settingsIcon);
		addNode(settingsLabel);
		addNode(autoUpdateCheckBox);
	}

}
