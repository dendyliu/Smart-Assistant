package org.kdadev.smartassistant;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;
import org.kdadev.smartassistant.model.ChatMessage;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ChatRoomActivity extends AppCompatActivity {
    private ListView listOfMessages;
    private FirebaseListAdapter<ChatMessage> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        FirebaseMessaging.getInstance().subscribeToTopic("news");
        listOfMessages = (ListView)findViewById(R.id.list_of_messages);
        FloatingActionButton fab =
                (FloatingActionButton)findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseMessaging.getInstance().unsubscribeFromTopic("news");
                EditText input = (EditText)findViewById(R.id.input);
                FirebaseDatabase.getInstance()
                        .getReference().child("messages").push()
                        .setValue(new ChatMessage(input.getText().toString(),
                                FirebaseAuth.getInstance()
                                        .getCurrentUser()
                                        .getDisplayName())
                        );
                input.setText("");
                sendNotification();
                listOfMessages.post(new Runnable() {
                    public void run() {
                        listOfMessages.smoothScrollToPosition(0, listOfMessages.getBottom());
                    }
                });

            }
        });

        adapter = new FirebaseListAdapter<ChatMessage>(this, ChatMessage.class,
                R.layout.message, FirebaseDatabase.getInstance().getReference().child("messages") ){
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                TextView messageText = (TextView)v.findViewById(R.id.message_text);
                TextView messageUser = (TextView)v.findViewById(R.id.message_user);
                TextView messageTime = (TextView)v.findViewById(R.id.message_time);
                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());
                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                        model.getMessageTime()));
            }
        };

        listOfMessages.setAdapter(adapter);

    }

    public void sendMessage(){


    }

    public void sendNotification(){
        new PostClass().execute();
    }

    private class PostClass extends AsyncTask<String, Void, Void> {

        public PostClass() {
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                URL url = new URL("https://fcm.googleapis.com/fcm/send");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                String authKey = "key=AIzaSyBDMnMBJt4xe9IM0oNbIxMxKYH9W2Cblm8";
                String contentType = "application/json";
                String sender = "me";
                String receiver = "you";
                String message = "hello";
                String toToken = "/topics/news";
                JSONObject objData = new JSONObject();
                objData.put("sender", sender);
                objData.put("receiver", receiver);
                objData.put("body", message);
                JSONObject objNotif = new JSONObject();
                objNotif.put("icon", "asd");
                objNotif.put("title", "tes");
                objNotif.put("body", "Hey Whats Up!");
                JSONObject arrayObj = new JSONObject();
                arrayObj.put("to", toToken);
                arrayObj.put("data", objData);
                arrayObj.put("notification", objNotif);
                String urlParameters = arrayObj.toString();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
                connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");
                connection.setRequestProperty("Content-Type", contentType);
                connection.setRequestProperty("Authorization", authKey);
                connection.setDoOutput(true);
                DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
                dStream.writeBytes(urlParameters);
                dStream.flush();
                dStream.close();
                int responseCode = connection.getResponseCode();

                final StringBuilder output = new StringBuilder("Request URL " + url);
                output.append(System.getProperty("line.separator") + "Request Parameters " + urlParameters);
                output.append(System.getProperty("line.separator") + "Response Code " + responseCode);
                output.append(System.getProperty("line.separator") + "Type " + "POST");
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = "";
                StringBuilder responseOutput = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    responseOutput.append(line);
                }
                br.close();

                output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());
                Log.d("aSDSADAS",output.toString());
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }


}
