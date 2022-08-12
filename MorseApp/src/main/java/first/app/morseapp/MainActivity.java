package first.app.morseapp;
/**
 * A class that sets the main layout of the app. This is based on the tutorial of the
 * official android documentation.
 * 10.08.2022
 *
 * Added the conversion from morse to normal alphabet
 * 12.06.2022
 */

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
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
        TextView information = findViewById(R.id.textView3);
        // read in the dictionary and the information text for how to use
        try {
            dict.initializeDic(getResources().openRawResource(R.raw.morse));
            InputStream input = getResources().openRawResource(R.raw.information);
            byte[] bytes = new byte[1000];

            StringBuilder x = new StringBuilder();

            int numRead = 0;
            while ((numRead = input.read(bytes)) >= 0) {
                x.append(new String(bytes, 0, numRead));
            }
            information.setText(x.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }

    /**
     * Check which button the user used and translate accordingly
     * @param v the view field to get
     */
    @Override
    public void onClick(View v){
        EditText editText = (EditText) findViewById(R.id.editTextTextPersonName);
        try {
            switch (v.getId()) {
                case R.id.morse:
                    convertMessage(editText, "m");
                    break;
                case R.id.alphabet:
                    convertMessage(editText, "a");
                    break;
                default:
                    break;
            }
        }
        catch (IOException | XmlPullParserException ex){
            Intent error = new Intent(this, DisplayMessageActivity.class);
            error.putExtra("error", "Something went wrong");
            startActivity(error);
        }
    }


    /** Called when the user taps the Send button */
    private void convertMessage(EditText editText, String which) throws IOException, XmlPullParserException {

        // Do something in response to button
        Intent input = new Intent(this, DisplayMessageActivity.class);
        String message = editText.getText().toString();
        convert.setText(message, message.length());
        switch(which){
            case "m":
                convert.convertMorse(dict);
                break;
            case "a":
                convert.convertChars(dict);
                break;
        }
        input.putExtra("copy", convert.getConvert());
        input.putExtra("id", message);
        startActivity(input);
    }

}