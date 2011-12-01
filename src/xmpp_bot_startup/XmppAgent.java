package xmpp_bot_startup;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

public class XmppAgent {

	private static final String SERVER_LOGIN_CREDENTIALS_PROPERTIES_PATH = "server-login-credentials.xml";

	String buddies[] = { "Buddy 1", "Buddy 2", "Buddy 3" };

	public static void main(String[] args) {
		new XmppAgent().connectToMessageService();
	}

	private void connectToMessageService() {
		Properties agentCredentials = new Properties();
		try {
			FileInputStream fis = new FileInputStream(
					SERVER_LOGIN_CREDENTIALS_PROPERTIES_PATH);
			agentCredentials.loadFromXML(fis);
			fis.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		String username, password, host, serviceName;
		username = password = host = serviceName = null;
		int port = 0;

		try {
			username = agentCredentials.getProperty("agent.username");
			username = agentCredentials.getProperty("agent.password");

			host = agentCredentials.getProperty("server.host");
			port = Integer.parseInt(agentCredentials.getProperty("server.port"));
			serviceName = agentCredentials.getProperty("server.serviceName");
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}

		ConnectionConfiguration config = new ConnectionConfiguration(host,
				port, serviceName);
		XMPPConnection connection = new XMPPConnection(config);

		try {
			connection.connect();
		} catch (XMPPException canNotConnectError) {
			canNotConnectError.printStackTrace();
		}

		try {
			connection.login(username, password);
		} catch (XMPPException invalidCredentials) {
			invalidCredentials.printStackTrace();
		}

		System.out.println(username + " bot has started");

		ChatManager chatManager = connection.getChatManager();
		MessageListener listener = new MessageListener() {
			public void processMessage(Chat chat, Message message) {
				System.out.println("I got a message!");
			}
		};

		System.out.println("Message listener is created");

		for (int i = 0; i < buddies.length; i++) {
			Chat newChat = chatManager.createChat(buddies[i], listener);
			try {
				newChat.sendMessage("Hello, " + buddies[i] + " !");
			} catch (XMPPException messageNotRetrieved) {
				// TODO Auto-generated catch block
				messageNotRetrieved.printStackTrace();
			}
		}
	}
}
