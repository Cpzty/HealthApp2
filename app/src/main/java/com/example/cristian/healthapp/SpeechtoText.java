package com.example.cristian.healthapp;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.AsyncTask;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ibm.watson.developer_cloud.alchemy.v1.model.Document;
import com.ibm.watson.developer_cloud.dialog.v1.model.Conversation;
import com.ibm.watson.developer_cloud.document_conversion.v1.DocumentConversion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SpeechtoText extends AppCompatActivity implements OnClickListener {

    private  TextView resultTEXT;
    private  Button buttonCall;
    private String watson;

    private class AskWatsonTask extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(final String...textsToAnalyze){
            System.out.println(resultTEXT.getText());

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    resultTEXT.setText("I dont know what is going on");
                }
            });
            Conversation service = new Conversation();
            service.setClientId(Integer.parseInt("f7b692cc-40ad-4ec5-b33d-de36030cb55d"));

            Map<String,Object>params = new HashMap<String,Object>();
            params.put(service.getInput(),resultTEXT.getText());
            List<String> conversation = service.getResponse();

            System.out.println(watson);
            return conversation.toString();
        }

        @Override
        protected void  onPostExecute(String result){
            resultTEXT.setText(result);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speechto_text);
        resultTEXT =  (TextView)findViewById(R.id.TVresult);
        buttonCall = (Button)findViewById(R.id.btnWatson);
        buttonCall.setOnClickListener(this);
    }




    public void onButtonClick(View v){


        if(v.getId() == R.id.imageButton){


            promptSpeechInput();

        }

    }

    public void promptSpeechInput(){

        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, Locale.getDefault());
        i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Di algo");

        try {
            startActivityForResult(i, 100);
        }catch (ActivityNotFoundException a){
            Toast.makeText(SpeechtoText.this,"No es posible en este dispositivo!",Toast.LENGTH_LONG).show();
        }
    }

    public void onActivityResult(int request_code, int result_code, Intent i){
        super.onActivityResult(request_code,result_code,i);

        switch (request_code){
            case 100: if(result_code== RESULT_OK && i != null){
                ArrayList<String>result = i.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                resultTEXT.setText(result.get(0));

            }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnWatson) {

            AskWatsonTask task = new AskWatsonTask();
            task.execute(new String[]{});

        }
    }
}
