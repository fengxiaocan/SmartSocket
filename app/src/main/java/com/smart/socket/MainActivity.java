package com.smart.socket;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.smart.ai.protocol.SmartReaderProtocol;
import com.smart.socket.client.sdk.OkSocket;
import com.smart.socket.client.sdk.client.ConnectionInfo;
import com.smart.socket.client.sdk.client.OkSocketOptions;
import com.smart.socket.client.sdk.client.connection.AbsReconnectionManager;
import com.smart.socket.client.sdk.client.connection.IConnectionManager;
import com.smart.socket.core.iocore.interfaces.ISendPack;
import com.smart.socket.core.pojo.OriginalData;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        OkSocketOptions.Builder builder = new OkSocketOptions.Builder(OkSocketOptions.getDefault())
                .setReaderProtocol(new SmartReaderProtocol())
                .setMaxReadDataMB(1000)
                .setWritePackageBytes(1024);

        IConnectionManager manager = OkSocket.open("192.168.2.1", 33333);
        manager.registerReceiver(new AbsReconnectionManager() {
            @Override
            public void onSocketReadResponse(ConnectionInfo info, String action, OriginalData data) {
                String s = new String(data.getBodyBytes());
                Log.e("noah", String.format("onSocketReadResponse:%s %s %s", info.getIp(), action, s));
            }

            @Override
            public void onSocketWriteResponse(ConnectionInfo info, String action, ISendPack data) {
                Log.e("noah", String.format("onSocketWriteResponse:%s %s %s", info.getIp(), action,new String(data.parse())));
            }

            @Override
            public void onSocketDisconnection(ConnectionInfo info, String action, Exception e) {
                Log.e("noah",String.format("onSocketDisconnection"));
                e.printStackTrace();
            }

            @Override
            public void onSocketConnectionSuccess(ConnectionInfo info, String action) {
                Log.e("noah", String.format("onSocketConnectionSuccess"));
            }

            @Override
            public void onSocketConnectionFailed(ConnectionInfo info, String action, Exception e) {
                Log.e("noah", String.format("onSocketConnectionFailed"));
            }
        }).option(builder.build()).connect();
    }
}