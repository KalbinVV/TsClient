package org.kalbinvv.tsclient;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.kalbinvv.tscore.net.Connection;
import org.kalbinvv.tscore.net.Request;
import org.kalbinvv.tscore.net.RequestType;
import org.kalbinvv.tscore.test.TestResult;
import org.kalbinvv.tscore.user.User;

public class Utils {
	@SuppressWarnings("unchecked")
	public static ArrayList<TestResult> getUsersTestsResult(User user) 
			throws UnknownHostException, IOException{
		Config config = TsClient.getConfig();
		Connection connection = 
				new Connection(config.getServerAddress().toSocket());
		List<TestResult> testResults = (List<TestResult>) connection.sendRequestAndGetResponse(
				new Request(RequestType.GetTestsResults, null, config.getUser())).getObject();
		return new ArrayList<TestResult>(testResults.stream().filter((TestResult testResult) -> {
						return testResult.getUser().getName().equals(user.getName());
					}).collect(Collectors.toList()));
	}
}
