package com.arpaul.gcm_firebase;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;

import com.arpaul.gcm_firebase.adapter.ChatMessageAdapter;
import com.arpaul.gcm_firebase.common.AppConstants;
import com.arpaul.gcm_firebase.dataObjects.MessageDO;
import com.arpaul.utilitieslib.UnCaughtException;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

import java.util.ArrayList;

/**
 * Created by Aritra on 21-10-2016.
 */

public class ChatActivity extends AppCompatActivity {

    //https://firebase.google.com/docs/cloud-messaging/android/upstream?authuser=2

    private RecyclerView rvChatMessages;
    private EditText edtMessage;
    private ImageView btnSend;
    private ChatMessageAdapter adapter;
    private ArrayList<MessageDO> arrChat = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Thread.setDefaultUncaughtExceptionHandler(new UnCaughtException(ChatActivity.this,"aritra1704@gmail.com",getString(R.string.app_name)));
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_chat);

        initialiseControls();

        bindControls();
    }

    private void bindControls(){
        arrChat = new ArrayList<>();

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        try {
            registerReceiver(br_REFRESH, new IntentFilter(AppConstants.ACTION_REFRESH));
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    private BroadcastReceiver br_REFRESH = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            MessageDO objMessage = new MessageDO();
            objMessage.messageSender = intent.getStringExtra(MessageDO.SENDER);
            objMessage.messageBody = intent.getStringExtra(MessageDO.BODY);

            arrChat.add(objMessage);
            adapter.refresh(arrChat);
        }
    };

    @Override
    protected void onStop() {
        super.onStop();

        try {
            unregisterReceiver(br_REFRESH);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void initialiseControls(){
        rvChatMessages = (RecyclerView) findViewById(R.id.rvChatMessages);
        adapter = new ChatMessageAdapter(ChatActivity.this, new ArrayList<MessageDO>());
        rvChatMessages.setAdapter(adapter);

        edtMessage = (EditText) findViewById(R.id.edtMessage);
        btnSend = (ImageView) findViewById(R.id.btnSend);
    }
}
