package org.kalbinvv.tsclient.layout;

import java.io.IOException;

import java.util.HashSet;
import java.util.Set;

import org.kalbinvv.tsclient.Config;
import org.kalbinvv.tsclient.TsClient;
import org.kalbinvv.tscore.net.Connection;
import org.kalbinvv.tscore.net.Request;
import org.kalbinvv.tscore.net.RequestType;
import org.kalbinvv.tscore.net.Response;
import org.kalbinvv.tscore.net.ResponseType;
import org.kalbinvv.tscore.user.User;

import animatefx.animation.FadeIn;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class UsersLayout extends Layout{

	public UsersLayout(VBox vBox) {
		super(vBox);
	}

	@Override
	public void view() {
		draw();
		TsClient.setUpdateable(this);
		new FadeIn(getVBox()).play();
	}

	@Override
	public void draw() {
		clearNodes();
		try {
			Config config = TsClient.getConfig();
			Connection connection = new Connection(config.getServerAddress().toSocket());
			Response response = connection.sendRequestAndGetResponse(
					new Request(RequestType.GetOnlineUsers, 
							null, config.getUser()));
			if(response.getType() == ResponseType.Successful) {
				@SuppressWarnings("unchecked")
				Set<User> onlineUsers = (HashSet<User>) response.getObject();
				for(User user : onlineUsers) {
					addNode(new Label(user.getName() + " " + user.getAddress().toString()));
				}
			}else {
				addNode(new Label("Не удалось отобразить список участников: " 
						+ (String) response.getObject()));
			}
		} catch (IOException e) {
			Text errorText = new Text(
					"Не удалось отобразить список участников по неизвестной ошибке!"); 
			addNode(errorText);
		}
	}

	@Override
	public void update() {
		draw();
	}

}
