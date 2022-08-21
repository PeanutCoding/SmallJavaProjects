package ap.PhilippHellwig.morseconvertkt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import java.io.BufferedReader
import java.io.InputStream

class MainActivity : AppCompatActivity() {
    private var dict = Dictionary()
    private var convert = MorseConverter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val information: TextView = findViewById(R.id.information)
        val input: InputStream = resources.openRawResource(R.raw.information)
        dict.initializeDic(resources.openRawResource(R.raw.morse))

        val buffReader = BufferedReader(input.reader())
        try{
            information.text = buffReader.readText()
        }
        finally{buffReader.close()}
    }

    fun onClick(v: View) {
        var editText = findViewById<EditText>(R.id.editText)
        when(v.id){
            R.id.morse -> convertMessage(editText, "m")
            else -> convertMessage(editText, "a")
        }
    }

    private fun convertMessage(editText: EditText, s: String) {
        var message = editText.text.toString()

        convert.set(message, message.length)
        when(s){
            "m" -> convert.convertMorse(dict)
            else -> convert.convertChars(dict)
        }
        val input = Intent(this, ShowConversionActivity::class.java)
        input.putExtra("id", convert.getConvert())
        input.putExtra("copy", message)
        startActivity(input)
    }
}