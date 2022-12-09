package com.smart.ext.file;

public interface OnFileClientListener {
    /**
     * 当Socket断开后<br>
     *
     * @param hostName 服务端的host地址
     * @param port     服务端的端口
     */
    void onSocketDisconnection(String hostName, int port);

    /**
     * 当Socket连接建立成功后<br>
     *
     * @param hostName 服务端的host地址
     * @param port     服务端的端口
     */
    void onSocketConnectionSuccess(String hostName, int port);

    /**
     * 当Socket连接失败时会进行回调<br>
     * 建立Socket连接,如果服务器出现故障,网络出现异常都将导致该方法被回调<br>
     *
     * @param e 连接未成功建立的错误原因
     */
    void onSocketConnectionFailed(String hostName, int port, Throwable e);

    class IMPL implements OnFileClientListener{

        @Override
        public void onSocketDisconnection(String hostName, int port) {

        }

        @Override
        public void onSocketConnectionSuccess(String hostName, int port) {

        }

        @Override
        public void onSocketConnectionFailed(String hostName, int port, Throwable e) {

        }
    }
}
