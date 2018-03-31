package com.example.yizhangcao.mobilekeyboard;

import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class MainKeyboardActivity extends AppCompatActivity {

    public static final String SERVER_URL_STRING = "ws://172.17.78.125:3000";
    private static final String TAG = "MainActivity";
    private static final int NORMAL_CLOSURE_STATUS = 1000;
    private OkHttpClient client;
    private Request request;
    private EchoWebSocketListener listener;
    private WebSocket ws;
    private Button buttonW;
    private Button connectButton;
    private Button disconnectButton;
    private static final String SECRET_CODE = "12UYIUmkjkjhlj";

    private final class EchoWebSocketListener extends WebSocketListener {
        private static final int NORMAL_CLOSURE_STATUS = 1000;
        @Override
        public void onMessage(WebSocket webSocket, String text) {
            output("Receiving : " + text);
        }
        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            output("Receiving bytes : " + bytes.hex());
        }
        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            output("Closing : " + code + " / " + reason);
        }
        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            output("Error : " + t.getMessage());
        }
    }

    private void output(final String txt) {
        /*
          This method will output contents on the mTextViewMsgOutput but run the request
          on the UIThread. This can be called from with a WebSocketClient which runs
          on its own thread.
         */
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, txt);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_keyboard);

        buttonW = (Button) findViewById(R.id.buttonW);
        connectButton = (Button) findViewById(R.id.connect);
        disconnectButton = (Button) findViewById(R.id.disconnect);

        client = new OkHttpClient();

        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Disconnect");
                request = new Request.Builder().url(SERVER_URL_STRING).build();
                listener = new EchoWebSocketListener();
                ws = client.newWebSocket(request, listener);
                ws.send(SECRET_CODE);
            }
        });

//        disconnectButton.setOn

    }
}
