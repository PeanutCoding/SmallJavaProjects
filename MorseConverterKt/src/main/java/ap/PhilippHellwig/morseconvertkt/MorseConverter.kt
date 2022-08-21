package ap.PhilippHellwig.morseconvertkt

import android.text.Editable

class MorseConverter() {

    private lateinit var convert: String
    private lateinit var text: String
    private var size: Int = 0

    public fun set(input: String, length: Int){
        text = input
        size = length
    }

    public fun getConvert() = convert;

    public fun convertMorse(dict: Dictionary){
        var word: String = text
        var build: String = ""
        var now: String = ""
        for(c in word){
            now += if(c == '.' || c == '-' || c == 'E') c
            else{
                if(!now.isEmpty()){
                    build.plus(dict.getMorse(now))
                    now = ""
                }
                build += if(c=='/') c else ""
            }
        }
        convert = build
    }

    public fun convertChars(dict: Dictionary){
        var word: String = text.lowercase()
        var build: String = ""
        for(c in word){
            build += dict.getID(c.toString()) + "'"
        }
        convert = build
    }
}