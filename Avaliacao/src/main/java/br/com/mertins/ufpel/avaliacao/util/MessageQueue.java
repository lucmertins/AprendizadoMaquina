package br.com.mertins.ufpel.avaliacao.util;

import java.io.Serializable;

/**
 *
 * @author mertins
 */
public class MessageQueue implements Serializable{
    private String msg;
    private boolean finished=false;

    public MessageQueue() {
    }

    public MessageQueue(String msg,boolean finished) {
        this.msg = msg;
        this.finished=finished;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }
    
    
}
