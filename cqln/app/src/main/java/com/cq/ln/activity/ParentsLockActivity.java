package com.cq.ln.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cq.ln.R;

/**
 * Created by admin on 2016/7/12.
 */
public class ParentsLockActivity extends BaseActivity{

    public static final String KEY_INPUT_CONTENT = "key_input_conent";
    private EditText inputServer;
    private String password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        showPlaying = false;
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_parents_lock);
        getWindow().setBackgroundDrawable(null);
        password = getIntent().getStringExtra("PASSWORD");

        inputServer = (EditText) findViewById(R.id.edit_nickname);
        Button leftButton = (Button) findViewById(R.id.left);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView errTip = (TextView)findViewById(R.id.txtErrorTip);
                String inputStr = inputServer.getText().toString();
                if (!inputStr.equals(password)) {
                    errTip.setText(getResources().getString(R.string.string_error_tip));
                    return;
                }
                errTip.setText("");
                Intent intent = new Intent();
                intent.putExtra(KEY_INPUT_CONTENT, inputServer.getText().toString());
                setResult(RESULT_OK, intent); //intent为A传来的带有Bundle的intent，当然也可以自己定义新的Bundle
                finish();//此处一定要调用finish()方法
            }
        });
        Button rightButton = (Button) findViewById(R.id.right);
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        leftButton.requestFocus();

        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = metric.widthPixels;
        getWindow().setAttributes(p);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        String input = inputServer.getText().toString();

        if(keyCode == KeyEvent.KEYCODE_ESCAPE && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (input.length() > 0) {
                input = input.substring(0, input.length() - 1);
                inputServer.setText(input);
                return true;
            }
        }

        if (input.length()>5){
            return super.onKeyDown(keyCode, event);
        }
        switch (keyCode){
            case KeyEvent.KEYCODE_0:
                input = input+"0";
                break;
            case KeyEvent.KEYCODE_1:
                input = input+"1";
                break;
            case KeyEvent.KEYCODE_2:
                input = input+"2";
                break;
            case KeyEvent.KEYCODE_3:
                input = input+"3";
                break;
            case KeyEvent.KEYCODE_4:
                input = input+"4";
                break;
            case KeyEvent.KEYCODE_5:
                input = input+"5";
                break;
            case KeyEvent.KEYCODE_6:
                input = input+"6";
                break;
            case KeyEvent.KEYCODE_7:
                input = input+"7";
                break;
            case KeyEvent.KEYCODE_8:
                input = input+"8";
                break;
            case KeyEvent.KEYCODE_9:
                input = input+"9";
                break;

        }
        inputServer.setText(input);
        return super.onKeyDown(keyCode, event);
    }
}
