package ap.PhilippHellwig.morseconvertkt

import android.content.ClipData
import android.content.ClipboardManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.ContextMenu
import android.view.View
import android.widget.TextView

class ShowConversionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_conversion)
        val message = intent.getStringExtra("copy")
        val input = "Your text: \n\n"
        val upperText = findViewById<TextView>(R.id.inputText).apply {
            text = input + message + "\n\n your translation:"
            movementMethod = ScrollingMovementMethod()
        }

        val lowerText = findViewById<TextView>(R.id.outputText).apply {
            text = intent.getStringExtra("id")
            movementMethod = ScrollingMovementMethod()
        }

        registerForContextMenu(upperText)
        registerForContextMenu(lowerText)
    }
    override fun onCreateContextMenu(menu: ContextMenu, v: View, mf: ContextMenu.ContextMenuInfo){
        menu.apply {
            add(0, v.id, 0, "Copy")
            setHeaderTitle("Copy Text")
        }
        val textView = v as TextView
        val clipData = ClipData.newPlainText("text", textView.text)
        val manager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        manager.setPrimaryClip(clipData)
    }
}