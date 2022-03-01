package org.kalbinvv.tsclient.layout;

import java.io.IOException;
import java.util.List;

import org.kalbinvv.tsclient.Config;
import org.kalbinvv.tsclient.TsClient;
import org.kalbinvv.tscore.net.Connection;
import org.kalbinvv.tscore.net.Request;
import org.kalbinvv.tscore.net.RequestType;
import org.kalbinvv.tscore.net.Response;
import org.kalbinvv.tscore.net.ResponseType;
import org.kalbinvv.tscore.user.User;

import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class UsersLayout extends Layout{

	public UsersLayout(VBox vBox) {
		super(vBox);
	}

	@Override
	public void draw() {
		clearNodes();
		try {
			Config config = TsClient.getConfig();
			Connection connection = new Connection(config.getServerAddress().toSocket());
			Response response = connection.sendRequestAndGetResponse(
					new Request(RequestType.GetOnlineUsersList, 
							null, config.getUser()));
			if(response.getType() == ResponseType.Successful) {
				List<User> onlineUsers = (List<User>) response.getObject();
				for(User user : onlineUsers) {
					Text userText = new Text(user.getName() + " " + user.getAddress().toString());
					addNode(userText);
				}
			}else {
				Text errorText = new Text("Не удалось отобразить список участников: " 
						+ (String) response.getObject()); 
				addNode(errorText);
			}
		} catch (IOException e) {
			Text errorText = new Text("Не удалось отобразить список участников по неизвестной ошибке!"); 
			addNode(errorText);
		}
	}

}
