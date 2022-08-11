package first.app.morseapp;
/**
 * A class that sets the main layout of the app. This is based on the tutorial of the
 * official android documentation.
 * 10.08.2022
 */

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {
    public Dictionary dict;
    public MorseConverter convert;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        // create main layout and make it visible
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // intialize the dictionary and morse converter
        dict = new Dictionary();
        convert = new MorseConverter();
        // read in the alphabet
        try {
            dict.initializeDic(getResources().openRawResource(R.raw.morse));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }

    /** Called when the user taps the Send button */
    public void sendMessage(View view) throws IOException, XmlPullParserException {

        // Do something in response to button
        Intent input = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.editTextTextPersonName);

        String message = editText.getText().toString();

        convert.setText(message, message.length());
        convert.convertChars(dict);
        input.putExtra("copy", convert.getConvert());
        input.putExtra("id", message);
        startActivity(input);
    }
}