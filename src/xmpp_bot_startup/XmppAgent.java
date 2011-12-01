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
	XMPPConnection connection;
	ChatManager chatManager;
	MessageListener listener;

	public static void main(String[] args) {
		new XmppAgent().connectToMessageService();
	}

	private void connectToMessageService() {
		Properties agentCredentials = getAgentCredentials();

		connection = createConnection(agentCredentials);

		login(agentCredentials);

		chatManager = connection.getChatManager();
		
		listener = createMessageListener();
		
		pokeBuddies();
	}

	private void pokeBuddies() {
		for (int i = 0; i < buddies.length; i++) {
			Chat newChat = chatManager.createChat(buddies[i], listener);
			try {
				newChat.sendMessage("Hello, " + buddies[i] + " !");
			} catch (XMPPException messageNotRetrieved) {
				messageNotRetrieved.printStackTrace();
			}
		}
	}

	private MessageListener createMessageListener() {
		MessageListener listener = new MessageListener() {
			public void processMessage(Chat chat, Message message) {
				System.out.println("I got a message!");
			}
		};
		System.out.println("Message listener is created");
		return listener;
	}

	private void login(Properties agentCredentials) {
		String username, password;
		// username = password = host = serviceName = null;
		username = agentCredentials.getProperty("agent.username");
		password = agentCredentials.getProperty("agent.password");
		try {
			connection.login(username, password);
		} catch (XMPPException invalidCredentials) {
			invalidCredentials.printStackTrace();
		}
		System.out.println(username + " bot has started");
	}

	private XMPPConnection createConnection(Properties agentCredentials) {
		String host, serviceName;
		int port = 0;

		host = agentCredentials.getProperty("server.host");
		port = Integer.parseInt(agentCredentials.getProperty("server.port"));
		serviceName = agentCredentials.getProperty("server.serviceName");

		ConnectionConfiguration config = new ConnectionConfiguration(host,
				port, serviceName);

		XMPPConnection connection = new XMPPConnection(config);
		try {
			connection.connect();
		} catch (XMPPException canNotConnectError) {
			canNotConnectError.printStackTrace();
		}
		return connection;
	}

	private Properties getAgentCredentials() {
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
		return agentCredentials;
	}
}
