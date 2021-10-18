package look.web.ws;

import com.alibaba.fastjson.JSON;
import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import org.eclipse.jetty.websocket.api.WriteCallback;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class EventSock extends WebSocketAdapter {

    String id ;

    public static Map<String, RemoteEndpoint> socketCons = new ConcurrentHashMap<>();

    public static void sendMsg(RemoteEndpoint endpoint, SockMsg msg) {
        try {
            System.out.println(JSON.toJSONString(msg));
            endpoint.sendString(JSON.toJSONString(msg), new WriteCallback() {
                @Override
                public void writeFailed(Throwable x) {
                    System.out.println("writefailed");
                }

                @Override
                public void writeSuccess() {
//                    System.out.println("writesuess");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void broadcast(SockMsg msg) {
        for (RemoteEndpoint v : socketCons.values()) {
            sendMsg(v, msg);
        }
    }

    @Override
    public void onWebSocketConnect(Session sess) {
        super.onWebSocketConnect(sess);
        id = UUID.randomUUID().toString().replace("-", "");
        socketCons.put(id, sess.getRemote());
        System.out.println("Socket Connected: " + sess);
    }

    @Override
    public void onWebSocketText(String message) {
        System.out.println("Received TEXT message: " + message);
        SockMsg msg = JSON.parseObject(message, SockMsg.class);
        switch (msg.type) {
            case BACKERROR:
                break;
            case REQ_AUTH: {
//                sendMsg(getSession().getRemote(),new SockMsg(MsgType.REQ_AUTH,"init connection ."));
//                String json = msg.getContent().toString();
//                System.out.println(json);
//                JSONObject jo = JSON.parseObject(json);
//                Rob rob = new Rob(jo.get("usr").toString(), jo.get("pwd").toString(), new LogHandle() {
//                    @Override
//                    public void sendLog(Object msg) {
//                        sendMsg(getRemote(),new SockMsg(MsgType.REQ_AUTH,msg.toString()));
//                        System.out.println(msg);
//                    }
//
//                    @Override
//                    public void finish() {
//                        sendMsg(getRemote(),new SockMsg(MsgType.TASKFINISH));
//                    }
//                });
//                rob.runStudy();
            }
            break;
            case REQ_COOKIE:
                break;
            case AUTH_SUCCESS:
                break;
            case AUTH_FAILED:
                break;
            case SERVERTIME: {
            }
            break;
            case LOGININFOS:
                break;
            case RABPAGE:
                break;
            case LOGIN:
                break;
            case REDIRECT:
                break;
        }
    }

    @Override
    public void onWebSocketClose(int statusCode, String reason) {
        super.onWebSocketClose(statusCode, reason);
        socketCons.remove(id);
        System.out.println(id+" sockremoved");
        System.out.println("Socket Closed: [" + statusCode + "] " + reason);
    }

    @Override
    public void onWebSocketError(Throwable cause) {
        super.onWebSocketError(cause);
        cause.printStackTrace(System.err);
    }
}
