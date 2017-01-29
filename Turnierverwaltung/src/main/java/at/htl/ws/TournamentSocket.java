package at.htl.ws;

import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

/**
 * Created by Lokal on 09.01.2017.
 */
@ServerEndpoint("/refresh")
public class TournamentSocket {
    @OnMessage
    public void message(String message, Session client){
        for (Session session :client.getOpenSessions()) {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(message);
        }
    }
}
