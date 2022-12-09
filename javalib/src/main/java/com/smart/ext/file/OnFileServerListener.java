package com.smart.ext.file;

import java.net.Socket;

public interface OnFileServerListener {
    void onServerListening(int serverPort);

    void onClientConnected(Socket socket, int serverPort);

    void onClientDisconnected(Socket socket, int serverPort);

    void onClientTransferComplete(Socket socket, int serverPort);

    void onClientTransferError(Socket socket, int serverPort, Throwable throwable);

    void onServerError(int serverPort);

    void onServerShadow(int serverPort);
    class IMPL implements OnFileServerListener {

        @Override
        public void onServerListening(int serverPort) {

        }

        @Override
        public void onClientConnected(Socket socket, int serverPort) {

        }

        @Override
        public void onClientDisconnected(Socket socket, int serverPort) {

        }

        @Override
        public void onClientTransferComplete(Socket socket, int serverPort) {

        }

        @Override
        public void onClientTransferError(Socket socket, int serverPort, Throwable throwable) {

        }

        @Override
        public void onServerError(int serverPort) {

        }

        @Override
        public void onServerShadow(int serverPort) {

        }
    }
}
