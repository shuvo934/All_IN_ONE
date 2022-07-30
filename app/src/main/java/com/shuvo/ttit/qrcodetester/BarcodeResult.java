package com.shuvo.ttit.qrcodetester;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.shuvo.ttit.qrcodetester.Model.QRGEOModel;
import com.shuvo.ttit.qrcodetester.Model.QRMECARDModel;
import com.shuvo.ttit.qrcodetester.Model.QRURLModel;
import com.shuvo.ttit.qrcodetester.Model.QRVCARDModel;

public class BarcodeResult extends AppCompatActivity {

    TextView textView;
    TextView textViewType;
    Button copy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_result);

        textView = findViewById(R.id.bar_result);
        textViewType = findViewById(R.id.bar_result_type);
        copy = findViewById(R.id.copy_text_button);

        ClipboardManager clipboard = (ClipboardManager)
                getSystemService(Context.CLIPBOARD_SERVICE);

        Intent intent = getIntent();
        String text = intent.getStringExtra("RESULT");

        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipData clipData = ClipData.newPlainText("Simple text",textView.getText());
                clipboard.setPrimaryClip(clipData);
                Toast.makeText(BarcodeResult.this, "Copied to clipboard", Toast.LENGTH_SHORT).show();
            }
        });

       processRawResult(text);


    }

    private void processRawResult(String text) {
        if (text.startsWith("BEGIN:")) {

            String[] tokens = text.split("\n");
            QRVCARDModel qrvcardModel = new QRVCARDModel();

            for (int i = 0 ; i < tokens.length; i++) {

                if (tokens[i].startsWith("BEGIN:")) {

                    qrvcardModel.setType(tokens[i].substring("BEGIN:".length()));
                }
                else if (tokens[i].startsWith("N:")) {

                    qrvcardModel.setName(tokens[i].substring("N:".length()));
                }
                else if (tokens[i].startsWith("ORG:")) {

                    qrvcardModel.setOrg(tokens[i].substring("ORG:".length()));
                }
                else if (tokens[i].startsWith("TITLE:")) {

                    qrvcardModel.setTitle(tokens[i].substring("TITLE:".length()));
                }
                else if (tokens[i].startsWith("TEL:")) {

                    qrvcardModel.setTel(tokens[i].substring("TEL:".length()));
                }
                else if (tokens[i].startsWith("URL:")) {

                    qrvcardModel.setUrl(tokens[i].substring("URL:".length()));
                }
                else if (tokens[i].startsWith("EMAIL:")) {

                    qrvcardModel.setEmail(tokens[i].substring("EMAIL:".length()));
                }
                else if (tokens[i].startsWith("ADR:")) {

                    qrvcardModel.setAddress(tokens[i].substring("ADR:".length()));
                }
                else if (tokens[i].startsWith("NOTE:")) {

                    qrvcardModel.setNote(tokens[i].substring("NOTE:".length()));
                }
                else if (tokens[i].startsWith("SUMMARY:")) {

                    qrvcardModel.setSummary(tokens[i].substring("SUMMARY:".length()));
                }
                else if (tokens[i].startsWith("DTSTART:")) {

                    qrvcardModel.setDtstart(tokens[i].substring("DTSTART:".length()));
                }
                else if (tokens[i].startsWith("DTEND:")) {

                    qrvcardModel.setDtend(tokens[i].substring("DTEND:".length()));
                }
            }
            String details =
                    " Name: "+ qrvcardModel.getName()+
                    "\n Organization: "+ qrvcardModel.getOrg()+
                    "\n Title: "+ qrvcardModel.getTitle()+
                    "\n Phone: "+ qrvcardModel.getTel()+
                    "\n URL: "+ qrvcardModel.getUrl()+
                    "\n Email: "+ qrvcardModel.getEmail()+
                    "\n Address: "+ qrvcardModel.getAddress()+
                    "\n Note: "+ qrvcardModel.getNote();
            if (qrvcardModel.getType().equals("VCARD")) {
                textViewType.setText(qrvcardModel.getType());
                textView.setText(details);
            } else {
                textViewType.setText(qrvcardModel.getType());
                textView.setText(text);
            }

        }
        else if (text.startsWith("http://") || text.startsWith("https://") || text.startsWith("www.")) {

            QRURLModel qrurlModel = new QRURLModel(text);
            textView.setText(qrurlModel.getUrl());
            textViewType.setText("URL");
        }
        else if (text.startsWith("geo:")) {
            QRGEOModel qrgeoModel = new QRGEOModel();
            String  delims = "[ , ?q= ]+";
            String[] tokens = text.split(delims);

//            for (int i = 0; i < tokens.length; i++) {
//
//                if (tokens[i].startsWith("geo:")) {
//                    qrgeoModel.setLat(tokens[i].substring("geo:".length()));
//                }
//            }
            qrgeoModel.setLat(tokens[0].substring("geo:".length()));
            qrgeoModel.setLng(tokens[1]);
            qrgeoModel.setGeo_place(tokens[2]);
            textViewType.setText("GEO LOCATION");
            textView.setText("Latitude: "+qrgeoModel.getLat()+"\nLongitude: "+qrgeoModel.getLng()+"\nPlace: "+qrgeoModel.getGeo_place());
        }
        else if(text.startsWith("MECARD:")) {

            QRMECARDModel qrmecardModel = new QRMECARDModel();
            qrmecardModel.setType("MECARD");
            text = text.substring("MECARD:".length());
            String[] tokens = text.split(";");

            for (int i = 0 ; i < tokens.length; i++) {

                if (tokens[i].startsWith("N:")) {

                    qrmecardModel.setName(tokens[i].substring("N:".length()));
                }
                else if (tokens[i].startsWith("ORG:")) {

                    qrmecardModel.setOrg(tokens[i].substring("ORG:".length()));
                }
                else if (tokens[i].startsWith("TEL:")) {

                    qrmecardModel.setTel(tokens[i].substring("TEL:".length()));
                }
                else if (tokens[i].startsWith("URL:")) {

                    qrmecardModel.setUrl(tokens[i].substring("URL:".length()));
                }
                else if (tokens[i].startsWith("EMAIL:")) {

                    qrmecardModel.setEmail(tokens[i].substring("EMAIL:".length()));
                }
                else if (tokens[i].startsWith("ADR:")) {

                    qrmecardModel.setAddress(tokens[i].substring("ADR:".length()));
                }
                else if (tokens[i].startsWith("NOTE:")) {

                    qrmecardModel.setNote(tokens[i].substring("NOTE:".length()));
                }
                else if (tokens[i].startsWith("SUMMARY:")) {

                    qrmecardModel.setSummary(tokens[i].substring("SUMMARY:".length()));
                }
                else if (tokens[i].startsWith("DTSTART:")) {

                    qrmecardModel.setDtstart(tokens[i].substring("DTSTART:".length()));
                }
                else if (tokens[i].startsWith("DTEND:")) {

                    qrmecardModel.setDtend(tokens[i].substring("DTEND:".length()));
                }
            }

            String details =
                    " Name: "+ qrmecardModel.getName()+
                    "\n Organization: "+ qrmecardModel.getOrg()+
                    "\n Note: "+ qrmecardModel.getNote()+
                    "\n Phone: "+ qrmecardModel.getTel()+
                    "\n URL: "+ qrmecardModel.getUrl()+
                    "\n Email: "+ qrmecardModel.getEmail()+
                    "\n Address: "+ qrmecardModel.getAddress();

            textViewType.setText(qrmecardModel.getType());
            textView.setText(details);

        }
        else if (text.startsWith("mailto:")) {
            text = text.substring("mailto:".length());
            textView.setText(text);
            textViewType.setText("EMAIL");
        }
        else if (text.startsWith("tel:")) {
            text = text.substring("tel:".length());
            textViewType.setText("PHONE NO");
            textView.setText(text);
        }
        else if (text.startsWith("smsto:")) {
            text = text.substring("smsto:".length());

            String phone = text.substring(0,text.indexOf(":"));
            System.out.println(phone);

            String sms = text.substring(text.indexOf(":")+1);
            System.out.println(sms);

            textViewType.setText("SMS");
            textView.setText("Phone: " + phone + "\n Message: " + sms);
        }
        else if (text.startsWith("WIFI:")) {

            text = text.substring("WIFI:".length());
            String[] tokens = text.split(";");

            String name = "";
            String type = "";
            String pass = "";


            for (int i = 0; i < tokens.length; i++) {
                if (tokens[i].startsWith("S:")) {
                    name = tokens[i].substring("S:".length());
                }
                else if (tokens[i].startsWith("T:")) {
                    type = tokens[i].substring("T:".length());
                }
                else if (tokens[i].startsWith("P:")) {
                    pass = tokens[i].substring("P:".length());
                }
            }

            String details =
                    " Connection Name: "+ name+
                    "\n Password: "+ pass+
                    "\n Type: "+ type;

            textViewType.setText("WIFI");
            textView.setText(details);
        }
        else {
            textViewType.setText("TEXT");
            textView.setText(text);
        }
    }
}