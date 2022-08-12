package first.app.morseapp;
/**
 * A class to represent the converted input text. This activity is based on the tutorial
 * of the official android documentation.
 * 10.08.2022
 */

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.method.ScrollingMovementMethod;
import android.view.ContextMenu;
import android.view.View;
import android.widget.TextView;

public class DisplayMessageActivity extends AppCompatActivity {
    /**
     * Creates the text views where the output of the conversation ought to appear
     * @param savedInstanceState default param for super onCreate
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        // Get the Intent that started this activity and extract the string
        Intent output = getIntent();

        startService(output);

        TextView upperText = findViewById(R.id.textView);
        TextView lowerText = findViewById(R.id.textView2);
        // check whether something went wrong in the message receiving part
        if(output.hasExtra("error"))
            upperText.setText(output.getStringExtra("error"));

        else{
            String message = output.getStringExtra("copy");

            // Capture the layout's TextView and set the string as its text
            String content = output.getStringExtra("id");
            String first = "Your text: \n\n".concat(content).concat("\n\n your translation:");
            upperText.setText(first);
            lowerText.setText(message);
        }

        upperText.setMovementMethod(new ScrollingMovementMethod());
        lowerText.setMovementMethod(new ScrollingMovementMethod());

        registerForContextMenu(upperText);
        registerForContextMenu(lowerText);

    }

    /**
     * Creates a copy menu to copy the output
     * @param menu the contextmenu of android
     * @param v the window for the context menu
     * @param mf menu info
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo mf){

        menu.add(0, v.getId(),0, "Copy");

        menu.setHeaderTitle("Copy text"); //setting header title for menu
        TextView textView = (TextView) v; //calling our textView
        ClipboardManager manager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        ClipData clipData = ClipData.newPlainText("text", textView.getText());

        manager.setPrimaryClip(clipData);

    }
}