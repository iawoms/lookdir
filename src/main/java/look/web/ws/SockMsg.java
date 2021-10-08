package look.web.ws;

import look.web.ws.MsgType;

import java.util.Date;

/**
 * Created by iawom on 17-8-30.
 */
public class SockMsg {
    /**
     * ÏûÏ¢ÀàÐÍ
     */
    public MsgType type;
    /**
     * ÏûÏ¢id
     */
    public long id;
    /**
     * Ô´ip
     */
    public String from;
    /**
     * Ä¿±êip
     */
    public String to;
    /**
     * Ä¿±êÒµÎñ¶ÔÏóid
     */
    public String target;
    public Date sendTime;
    public Date receiveTime;
    public Object content;



    public SockMsg()
    {

    }
    public SockMsg(MsgType type){
        this.type = type;
    }
    public SockMsg(MsgType type, Object content){
        this.type = type;
        this.content = content;
    }
}
