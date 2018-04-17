package cn.cntv.app.ipanda.custom;

/**
 * 广告响应处理器
 *
 * @author
 */
public interface ResponseHandler {

    /**
     * 处理响应内容
     *
     * @param response 响应内容
     */
    public void processResponse(String response);


    /**
     * 处理错误
     *
     * @param msg 错误消息
     * @param t   异常对象
     */
    public void processError(String msg, Throwable t);

}
