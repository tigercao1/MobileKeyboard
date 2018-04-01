package com.example.yizhangcao.mobilekeyboard;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainKeyboardActivity extends AppCompatActivity {

    public static final String SERVER_URL_STRING = "ws://thehiddentent.com:3000";
    private static final String TAG = "MainActivity";
    private static final int NORMAL_CLOSURE_STATUS = 1000;
    private OkHttpClient client;
    private Request request;
    private EchoWebSocketListener listener;
    private WebSocket ws;

    private boolean isConnected;


    private Button connectButton;
    private Button disconnectButton;
    private static final String SECRET_CODE = "Simple_Key";

    private LinearLayout verticalLayout;

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
        setContentView(R.layout.activity_main);

        isConnected = false;

        connectButton = (Button) findViewById(R.id.connect);
        disconnectButton = (Button) findViewById(R.id.disconnect);

        verticalLayout = (LinearLayout) findViewById(R.id.vLayout);

        client = new OkHttpClient();

        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Connect button pressed");
                disconnectButton.setEnabled(true);
                request = new Request.Builder().url(SERVER_URL_STRING).build();
                listener = new EchoWebSocketListener();
                ws = client.newWebSocket(request, listener);
                isConnected = true;
                ws.send(SECRET_CODE);
                connectButton.setEnabled(false);
            }
        });

        disconnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "Disconnect button pressed");
                connectButton.setEnabled(true);
                ws.close(NORMAL_CLOSURE_STATUS, "Disconnected");
                isConnected = false;
                disconnectButton.setEnabled(false);
            }
        });

        try {
            InputStream iStream = getResources().openRawResource(R.raw.keylayout);
            BufferedReader bReader = new BufferedReader(new InputStreamReader(iStream));
            loadKeyBoardFrom(bReader);
            bReader.close();
        }
        catch (java.io.IOException e){
            e.printStackTrace();

        }

//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
//        params.setMargins(1,1,1,1);
//        LinearLayout.LayoutParams paramsS = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.6f);
//        paramsS.setMargins(1,1,1,1);
//        // Setting up Horizontal Layout
//        LinearLayout horizontalLayout = new LinearLayout(this);
//        horizontalLayout.setOrientation(LinearLayout.HORIZONTAL);
//        horizontalLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
//        Button buttonOne = new Button(this);
//        buttonOne.setBackgroundColor(Color.GRAY);
//        buttonOne.setText("`");
//        buttonOne.setLayoutParams(params);
//        buttonOne.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (isConnected)
//                    ws.send("" + (192));
//            }
//        });
//        horizontalLayout.addView(buttonOne);
//        for (int j = 0; j < 9; j++){
//            final int jVal = j;
//            Button button = new Button(this);
//            button.setBackgroundColor(Color.GRAY);
////                button.setTextSize(7);
//            button.setText(String.valueOf(j+1));
//            button.setLayoutParams(params);
//            button.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (isConnected)
//                        ws.send("" + (jVal + 49));
//                }
//            });
//            horizontalLayout.addView(button);
//        }
//        Button button = new Button(this);
//        button.setBackgroundColor(Color.GRAY);
////            button.setTextSize(7);
//        button.setText(String.valueOf(0));
//        button.setLayoutParams(params);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (isConnected)
//                    ws.send("48");
//            }
//        });
//        horizontalLayout.addView(button);
//        Button buttonDash = new Button(this);
//        buttonDash.setBackgroundColor(Color.GRAY);
////            button.setTextSize(7);
//        buttonDash.setText("-");
//        buttonDash.setLayoutParams(params);
//        buttonDash.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (isConnected)
//                    ws.send("189");
//            }
//        });
//        horizontalLayout.addView(buttonDash);
//        Button buttonPlus = new Button(this);
//        buttonPlus.setBackgroundColor(Color.GRAY);
////            button.setTextSize(7);
//        buttonPlus.setText("=");
//        buttonPlus.setLayoutParams(params);
//        buttonPlus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (isConnected)
//                    ws.send("187");
//            }
//        });
//        horizontalLayout.addView(buttonPlus);
//        Button buttonDelete = new Button(this);
//        buttonDelete.setBackgroundColor(Color.GRAY);
////            button.setTextSize(7);
//        buttonDelete.setText("Delete");
//        buttonDelete.setLayoutParams(paramsS);
//        buttonDelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (isConnected)
//                    ws.send("8");
//            }
//        });
//        horizontalLayout.addView(buttonDelete);
//        verticalLayout.addView(horizontalLayout);
//
//        LinearLayout horizontalLayoutTwo = new LinearLayout(this);
//        horizontalLayoutTwo.setOrientation(LinearLayout.HORIZONTAL);
//        horizontalLayoutTwo.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
//        Button buttonTab = new Button(this);
//        buttonTab.setBackgroundColor(Color.GRAY);
//        buttonTab.setText("Tab");
//        buttonTab.setLayoutParams(paramsS);
//        buttonTab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (isConnected)
//                    ws.send("" + 9);
//            }
//        });
//        horizontalLayoutTwo.addView(buttonTab);
//
//
//
//        verticalLayout.addView(horizontalLayoutTwo);
    }

    public void loadKeyBoardFrom (BufferedReader reader){
        XmlPullParserFactory factory = null;

        verticalLayout = (LinearLayout) findViewById(R.id.vLayout);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        params.setMargins(1,1,1,1);
        LinearLayout.LayoutParams paramsS = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.6f);
        paramsS.setMargins(1,1,1,1);
        // Setting up Horizontal Layout

        final String XML_ROW = "Row";
        final String XML_KEY = "Key";

        try {
            factory = XmlPullParserFactory.newInstance();
            final XmlPullParser xpp = factory.newPullParser();


            xpp.setInput(reader); // set input file for parser
            int eventType = xpp.getEventType(); // get initial eventType

            String currentText = ""; //current text between XML tags being parsed

            LinearLayout horizontalLayout = new LinearLayout(this);;

            // Loop through pull events until we reach END_DOCUMENT
            while (eventType != XmlPullParser.END_DOCUMENT) {
                // Get the current tag
                String tagname = xpp.getName();

                // handle the various xml tags encounted
                switch (eventType) {
                    case XmlPullParser.START_TAG: //XML opening tags
                        if (tagname.equalsIgnoreCase(XML_KEY)) {
                            Log.i(TAG, xpp.getAttributeValue(1));
                            final KeyButton button = new KeyButton(this, xpp.getAttributeValue(0));
                            button.setBackgroundColor(Color.GRAY);
                    //            button.setTextSize(7);
                            button.setText(xpp.getAttributeValue(1));
                            button.setLayoutParams(params);
                            button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (isConnected)
                                        ws.send(button.getKeyCode());
                                }
                            });
                            horizontalLayout.addView(button);

                        } else if (tagname.equalsIgnoreCase(XML_ROW)){
                            Log.i(TAG, "Next Row!");
                            horizontalLayout = new LinearLayout(this);
                            horizontalLayout.setOrientation(LinearLayout.HORIZONTAL);
                            horizontalLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
                        }
                        break;

                    case XmlPullParser.END_TAG: //XML closing tags
                        if (tagname.equalsIgnoreCase(XML_ROW)) {
                            Log.i(TAG, "Ending Row");
                            verticalLayout.addView(horizontalLayout);
                        }
                        break;

                    default:
                        break;
                }
                //iterate
                eventType = xpp.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }


}
