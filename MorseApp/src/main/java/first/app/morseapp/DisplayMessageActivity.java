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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        // Get the Intent that started this activity and extract the string
        Intent output = getIntent();
        startService(output);
        String message = output.getStringExtra("copy");

        // Capture the layout's TextView and set the string as its text
        TextView textView = findViewById(R.id.textView);
        TextView textView2 = findViewById(R.id.textView2);
        String content = output.getStringExtra("id");
        String first = "Your text: \n\n".concat(content).concat("\n\n your translation:");

        textView.setText(first);
        textView.setMovementMethod(new ScrollingMovementMethod());

        textView2.setText(message);
        textView2.setMovementMethod(new ScrollingMovementMethod());
        registerForContextMenu(textView);
        registerForContextMenu(textView2);

    }

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