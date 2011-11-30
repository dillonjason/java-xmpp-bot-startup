package xmpp_bot_startup;

import java.util.ArrayList;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

public class xmppAgent {

	String agentCred [] = {"username", "password"};
	String buddies [] = {"Buddy 1", "Buddy 2", "Buddy 3"};
	
	private void connectToMessageService(){
		ConnectionConfiguration config = new ConnectionConfiguration("service.name.com", 80, "service.com");
		XMPPConnection connection = new XMPPConnection(config);
		
		try {
			connection.connect();
		} catch (XMPPException canNotConnectError) {
			// TODO Auto-generated catch block
			canNotConnectError.printStackTrace();
		}
		try {
			connection.login(agentCred[0], agentCred[1]);
		} catch (XMPPException invalidCredentials) {
			// TODO Auto-generated catch block
			invalidCredentials.printStackTrace();
		}
		
		System.out.println (agentCred[0] + " has started");
		
		ChatManager chatmanager = connection.getChatManager();
		MessageListener listener = new MessageListener(){
			public void processMessage(Chat chat, Message message){
				System.out.println("I got a message!");
			}
		};
		
		System.out.println ("Message listen is created");
		
		for (int i = 0; i < buddies.length; i++){
			Chat newChat = chatmanager.createChat(buddies[i], listener);
			try {
				newChat.sendMessage("Hello, " + buddies[i] + " !");
			} catch (XMPPException messageNotRetrieved) {
				// TODO Auto-generated catch block
				messageNotRetrieved.printStackTrace();
			}
		}
	}
}

