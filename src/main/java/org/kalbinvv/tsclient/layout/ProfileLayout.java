package org.kalbinvv.tsclient.layout;

import org.kalbinvv.tsclient.TsClient;
import org.kalbinvv.tscore.user.User;

import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class ProfileLayout extends Layout{

	public ProfileLayout(VBox vBox) {
		super(vBox);
	}

	@Override
	public void draw() {
		clearNodes();
		User user = TsClient.getConfig().getUser();
		Text userNameText = new Text("Имя пользователя: " + user.getName());
		Text userAddressText = new Text("Адрес пользователя: " + user.getAddress());
		Text serverAddressText = new Text("Адрес сервера: " 
				+ TsClient.getConfig().getServerAddress());
		addNode(userNameText);
		addNode(userAddressText);
		addNode(serverAddressText);
	}

}
