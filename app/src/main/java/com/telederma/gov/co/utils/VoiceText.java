package com.telederma.gov.co.utils;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

//import net.gotev.speech.Speech;
import com.telederma.gov.co.R;
import com.telederma.gov.co.utils.Speech;
import com.bumptech.glide.Glide;

import net.gotev.speech.SpeechDelegate;

import java.util.List;

public class VoiceText implements SpeechDelegate {

    Context context;
    EditText ed_field;
    Button btn_speech;
    String previes_text=null;
    View fl_microphone;
    LinearLayout linearLayout;
    public VoiceText(Context context, View view) {
        this.context = context;
        this.ed_field = view.findViewById(R.id.ed_text);
        this.fl_microphone = view.findViewById(R.id.fl_microphone);
        this.linearLayout = view.findViewById(R.id.linearLayout);
        previes_text = this.ed_field.getText().toString();
        Speech.init(context, context.getPackageName());


    }


    private void viewsRecording(){
        this.fl_microphone.setVisibility(View.GONE);
        this.linearLayout.setVisibility(View.VISIBLE);

    }
    private void viewsStopRecording(){
        this.fl_microphone.setVisibility(View.VISIBLE);
        this.linearLayout.setVisibility(View.GONE);
    }

    @Override
    public void onStartOfSpeech() {
        viewsRecording();
    }

    @Override
    public void onSpeechRmsChanged(float value) {

    }

    @Override
    public void onSpeechPartialResults(List<String> results) {
        StringBuilder str = new StringBuilder();
        for (String res : results) {
            Log.i("speech", "partial result in for: " + res);
            str.append(res).append(" ");
            /*if(res.toLowerCase().equals("punto a parte")){
                str.append("\n");
            }
            if(res.toLowerCase().equals("punto y coma")){
                str.append("; ");
            }
            if(res.toLowerCase().equals("punto seguido")){
                str.append(". ");
            }*/

            if(str.toString().toLowerCase().contains("borrar texto")){
                str = new StringBuilder(" ");
                previes_text = "";
            }
        }

        Log.i("speech", "partial result definitivo : "+previes_text +" · "+ str.toString().trim());
        this.ed_field.setText(previes_text+" "+str.toString().trim());

    }

    @Override
    public void onSpeechResult(String result) {
        viewsStopRecording();
    }
}
