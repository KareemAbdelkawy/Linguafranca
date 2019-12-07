package com.example.linguafranca;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    TextToSpeech t1;
    TextView input,output;
    Button translate,listen;
    private RequestQueue queue;
    String langCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        queue = Volley.newRequestQueue(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        input =findViewById(R.id.input);
        output =findViewById(R.id.output);
        translate =findViewById(R.id.translate);
        listen = findViewById(R.id.listen);
        Spinner spinnerfrom = findViewById(R.id.spinnerfrom);
        Spinner spinnerto = findViewById(R.id.spinnerto);
        ArrayList<String> arrayList = new ArrayList<>();

        arrayList.add("English");
        arrayList.add("Arabic");
        arrayList.add("Spanish");
        arrayList.add("German");




        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerfrom.setAdapter(arrayAdapter);
        spinnerfrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String tutorialsName = parent.getItemAtPosition(position).toString();
                Toast.makeText(parent.getContext(), "Selected: " + tutorialsName,Toast.LENGTH_LONG).show();
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });
        spinnerto.setAdapter(arrayAdapter);
        spinnerto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String lang = parent.getItemAtPosition(position).toString();
                switch (lang){
                    case "English" :
                        langCode = "en";
                        Toast.makeText(parent.getContext(), "code changed to: " + langCode,Toast.LENGTH_LONG).show();
                        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                            @Override
                            public void onInit(int status) {
                                if(status != TextToSpeech.ERROR) {
                                    t1.setLanguage(Locale.UK);
                                }
                            }
                        });

                        break;
                    case "Spanish":
                        langCode = "es";
                        Toast.makeText(parent.getContext(), "code changed to: " + langCode,Toast.LENGTH_LONG).show();
                        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                            @Override
                            public void onInit(int status) {
                                if(status != TextToSpeech.ERROR) {
                                    t1.setLanguage(Locale.ITALY);
                                }
                            }
                        });

                        break;
                    case "Arabic":
                        langCode = "ar";
                        Toast.makeText(parent.getContext(), "code changed to: " + langCode,Toast.LENGTH_LONG).show();
                        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                            @Override
                            public void onInit(int status) {
                                if(status != TextToSpeech.ERROR) {
                                    t1.setLanguage(Locale.GERMAN);
                                }
                            }
                        });
                        break;
                    case "German":
                        langCode = "de";
                        Toast.makeText(parent.getContext(), "code changed to: " + langCode,Toast.LENGTH_LONG).show();
                        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                            @Override
                            public void onInit(int status) {
                                if(status != TextToSpeech.ERROR) {
                                    t1.setLanguage(Locale.GERMAN);
                                }
                            }
                        });
                        break;
                    default:
                        langCode = "en";
                        Toast.makeText(parent.getContext(), "code default to: " + langCode,Toast.LENGTH_LONG).show();

                }
                Toast.makeText(parent.getContext(), "Selected: " + langCode,Toast.LENGTH_LONG).show();
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });


        listen.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // first StringRequest: getting items searched
                StringRequest stringRequest = searchNameStringRequest(output.getText().toString(),"en","ar");



                // executing the request (adding to queue)
                queue.add(stringRequest);

                String toSpeak = output.getText().toString();

                t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
            }


        });

        translate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                // first StringRequest: getting items searched
                StringRequest stringRequest = searchNameStringRequest(input.getText().toString(),"en",langCode);



                // executing the request (adding to queue)
                queue.add(stringRequest);
            }


        });
    }




    private StringRequest searchNameStringRequest(String input,String source , String dist) {

        String url = getString(R.string.header1) +input+getString(R.string.header2)+dist+getString(R.string.header3)+getString(R.string.key);

        // 1st param => type of method (GET/PUT/POST/PATCH/etc)
        // 2nd param => complete url of the API
        // 3rd param => Response.Listener -> Success procedure
        // 4th param => Response.ErrorListener -> Error procedure
        return new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    // 3rd param - method onResponse lays the code procedure of success return
                    // SUCCESS
                    @Override
                    public void onResponse(String response) {
                        // try/catch block for returned JSON data
                        // see API's documentation for returned format
                        try {
                            JSONObject result = new JSONObject(response);
                            JSONArray resultList  = result.getJSONArray("outputs");
                            JSONObject inner = resultList.getJSONObject(0);
                            String out = inner.getString("output");
                            output.setText(" "+out);

                            // catch for the JSON parsing error
                        } catch (JSONException e) {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    } // public void onResponse(String response)
                }, // Response.Listener<String>()
                new Response.ErrorListener() {
                    // 4th param - method onErrorResponse lays the code procedure of error return
                    // ERROR
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // display a simple message on the screen
                        Toast.makeText(MainActivity.this, "Code entered ErrorListener", Toast.LENGTH_LONG).show();
                        System.out.println(error);
                        System.out.println("hiiii");
                    }
                });
    }
    }

