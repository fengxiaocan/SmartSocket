package com.smart.socket;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.smart.ext.client.SmartClientSocket;
import com.smart.ext.client.impl.SmartSocketListener;
import com.smart.ext.file.DefaultFileReceive;
import com.smart.ext.file.FileSocketClient;
import com.smart.ext.file.OnFileClientListener;
import com.smart.ext.pack.StringSendPack;
import com.smart.ext.parse.SmartParseData;
import com.smart.socket.client.sdk.OkSocket;
import com.smart.socket.client.sdk.client.ConnectionInfo;
import com.smart.socket.core.iocore.interfaces.IPulseSender;
import com.smart.socket.core.iocore.interfaces.ISendPack;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private AppCompatButton btOpen;
    private AppCompatButton btSend;
    private AppCompatButton btStop;
    private AppCompatButton btReceive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        btOpen = findViewById(R.id.bt_open);
        btSend = findViewById(R.id.bt_send);
        btStop = findViewById(R.id.bt_stop);
        btReceive = findViewById(R.id.bt_receive);

        btOpen.setOnClickListener(v -> start());
        btSend.setOnClickListener(v -> OkSocket.open("192.168.2.1", 33333).send(new StringSendPack("上自佛，下至众生，无不由此而成佛道，了生死，度苦厄")));
        btStop.setOnClickListener(v -> OkSocket.open("192.168.2.1", 33333).disconnect());
        btReceive.setOnClickListener(v -> {
            File dir = getExternalFilesDir("socket");
            new FileSocketClient("192.168.2.1", 55066)
                    .setListener(new OnFileClientListener() {
                        @Override
                        public void onSocketDisconnection(String hostName, int port) {
                            Log.e("noah","onSocketDisconnection:"+hostName);
                        }

                        @Override
                        public void onSocketConnectionSuccess(String hostName, int port) {
                            Log.e("noah", "onSocketConnectionSuccess:" + hostName);
                        }

                        @Override
                        public void onSocketConnectionFailed(String hostName, int port, Throwable e) {
                            Log.e("noah", "onSocketConnectionFailed:" + hostName);
                            e.printStackTrace();
                        }
                    })
                    .setConnectTimeout(5000)
                    .receive(new DefaultFileReceive(dir));
        });
    }

    private void start() {


        SmartClientSocket.socket("192.168.2.1", 33333)
                .registerReceiver(new SmartSocketListener() {
                    @Override
                    public void onSocketDisconnection(ConnectionInfo info, String action, Exception e) {
                        Log.e("noah", String.format("onSocketDisconnection"));
                        e.printStackTrace();
                    }

                    @Override
                    public void onSocketConnectionFailed(ConnectionInfo info, String action, Exception e) {
                        Log.e("noah", String.format("onSocketConnectionFailed"));
                        e.printStackTrace();
                    }

                    @Override
                    public void onSocketReadResponse(ConnectionInfo info, String action, SmartParseData dataPack) {
                        super.onSocketReadResponse(info, action, dataPack);
                        if (dataPack.isString()) {
                            Log.e("noah", String.format("收到服务器信息: %s", dataPack.asString()));
                        }
                    }

                    @Override
                    public void onSocketConnectionSuccess(ConnectionInfo info, String action) {
                        super.onSocketConnectionSuccess(info, action);
                        Log.e("noah", String.format("onSocketConnectionSuccess"));
                    }

                    @Override
                    public void onSocketIOThreadStart(String action) {
                        super.onSocketIOThreadStart(action);
                        Log.e("noah", String.format("onSocketIOThreadStart"));
                    }

                    @Override
                    public void onSocketIOThreadShutdown(String action, Exception e) {
                        super.onSocketIOThreadShutdown(action, e);
                        Log.e("noah", String.format("onSocketIOThreadShutdown"));
                    }

                    @Override
                    public void onSocketWriteResponse(ConnectionInfo info, String action, ISendPack data) {
                        super.onSocketWriteResponse(info, action, data);
                        Log.e("noah", String.format("onSocketWriteResponse"));
                    }

                    @Override
                    public void onPulseSend(ConnectionInfo info, IPulseSender data) {
                        super.onPulseSend(info, data);
                    }
                }).connect();
    }
}