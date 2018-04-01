package com.example.yizhangcao.mobilekeyboard;

import android.content.Context;
import android.widget.Button;

/**
 * Created by yizhangcao on 2018-04-01.
 */

public class KeyButton extends android.support.v7.widget.AppCompatButton {
    String keyCode;

    public KeyButton(Context context, String keyCode){
        super(context);
        this.keyCode = keyCode;
    }

    public String getKeyCode() {
        return keyCode;
    }
}
