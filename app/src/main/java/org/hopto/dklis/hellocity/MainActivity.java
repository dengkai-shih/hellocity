package org.hopto.dklis.hellocity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.asus.robotframework.API.RobotCallback;
import com.asus.robotframework.API.RobotCmdState;
import com.asus.robotframework.API.RobotErrorCode;
import com.asus.robotframework.API.RobotFace;
import com.asus.robotframework.API.RobotUtil;
import com.asus.robotframework.API.SpeakConfig;
import com.robot.asus.robotactivity.RobotActivity;

import org.json.JSONObject;

// public class MainActivity extends AppCompatActivity {
public class MainActivity extends RobotActivity {
    public final static String TAG = "helloWorld";

    /**
     * 必要的 DOMAIN UUID
     */
    public final static String DOMAIN = "4015F56688D64128B60A42CD2BDCA129";

    private static TextView mTextView;
    private static TextView mTextView2;
    private static TextView mTextView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        robotAPI.robot.setExpression(RobotFace.HIDEFACE);
        mTextView = (TextView) findViewById(R.id.textview_info);
        mTextView2 = (TextView) findViewById(R.id.textview_info2);
        mTextView3 = (TextView) findViewById(R.id.textview_info3);
    }

    public static RobotCallback robotCallback = new RobotCallback() {
        @Override
        public void onResult(int cmd, int serial, RobotErrorCode err_code, Bundle result) {
            super.onResult(cmd, serial, err_code, result);
        }

        @Override
        public void onStateChange(int cmd, int serial, RobotErrorCode err_code, RobotCmdState state) {
            super.onStateChange(cmd, serial, err_code, state);
        }

        @Override
        public void initComplete() {
            super.initComplete();

        }
    };

    public static RobotCallback.Listen robotListenCallback = new RobotCallback.Listen() {
        @Override
        public void onFinishRegister() {

        }

        @Override
        public void onVoiceDetect(JSONObject jsonObject) {

        }

        @Override
        public void onSpeakComplete(String s, String s1) {

        }

        @Override
        public void onEventUserUtterance(JSONObject jsonObject) {
            String text;
            text = "onEventUserUtterance: " + jsonObject.toString();
            Log.d(TAG, text);
            mTextView.setText("onEventUserUtterance: " + jsonObject.toString());
        }

        @Override
        public void onResult(JSONObject jsonObject) {
            String text;
            text = "onResult: " + jsonObject.toString();
            Log.d(TAG, text);
            mTextView.setText("onResult: " + jsonObject.toString());
/*            mTextView2.setText("Intention Id = ");
            mTextView3.setText("Result City = ");*/


            /**
             *  讀取 IntentionId 的資訊。
             */
            String sIntentionID = RobotUtil.queryListenResultJson(jsonObject, "IntentionId");
            Log.d(TAG, "Intention Id = " + sIntentionID);
            mTextView2.setText("Intention Id = " + sIntentionID);

            if(sIntentionID.equals("helloWorld")) {
                String sSluResultCity = RobotUtil.queryListenResultJson(jsonObject, "myCity1", null);
                Log.d(TAG, "Result City = " + sSluResultCity);
                mTextView3.setText("Result City = " + sSluResultCity);

                if(sSluResultCity!= null) {
                    mTextView3.setText("You are now at " + sSluResultCity);
                }
            }
        }

        @Override
        public void onRetry(JSONObject jsonObject) {

        }
    };

    public MainActivity() {
        super(robotCallback, robotListenCallback);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // close faical
        robotAPI.robot.setExpression(RobotFace.HIDEFACE);

        // jump dialog domain
        /**
         * 指向開始的 plan 位址
         */
        robotAPI.robot.jumpToPlan(DOMAIN, "ThisPlanLaunchingThisApp");

        // listen user utterance
        robotAPI.robot.speakAndListen("Which city do you like?", new SpeakConfig().timeout(20));

        // show hint
        mTextView.setText(getResources().getString(R.string.dialog_example));
    }

    @Override
    protected void onPause() {
        super.onPause();

        //stop listen user utterance
        robotAPI.robot.stopSpeakAndListen();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
