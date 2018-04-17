package cn.cntv.app.ipanda.data.exception;

/**
 * Created by yuerq on 16/7/4.
 */
public class PandaException extends Exception {

    private String msg;

    public PandaException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public static PandaException convert(String msg) {
        return new PandaException(msg);
    }
}
