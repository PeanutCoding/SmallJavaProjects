/**
 * this class creates the morse hash maps and allows to retrieve the
 * value of either the character key or the morse key.
 * i.e. .- = a
 * or a = .-
 */
package ap.PhilippHellwig.morseconvertkt

import android.util.Xml
import org.xmlpull.v1.XmlPullParser
import java.io.InputStream

class Dictionary() {

    private lateinit var dictAToM : HashMap<String, String>
    private lateinit var dictMToA : HashMap<String, String>

    /**
     * initializes the hashmaps
     * @param input the morse xml stream
     */
    fun initializeDic(input : InputStream){
        dictAToM = HashMap()
        dictMToA = HashMap()
        parseMaps(input)
    }

    /**
     * get the value of a character key
     * @param key the character key, i.e. a
     */
    fun getID(key: String) =
        dictAToM.getOrElse(key.lowercase()){"[Not found]"}


    /**
     * get the value of a morse key
     * @param key the morse sign key, i.e. .-
     */
    fun getMorse(key: String) =
        dictMToA.getOrElse(key){"[Not found]"}

    /**
     * helper function to initiate the parsing
     * @param input the morse xml stream
     */
    private fun parseMaps(input: InputStream){
        val reader = Xml.newPullParser()
        reader.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
        reader.setInput(input, null)
        reader.nextTag()
        parseMorse(reader)
    }

    /**
     * helper function to parse the morse xml
     * @param reader the parser for the morse xml
     */
    private fun parseMorse(reader: XmlPullParser) {
        while (reader.next() != XmlPullParser.END_DOCUMENT){
            if(reader.eventType == XmlPullParser.START_TAG) {
                var name: String = reader.name
                if(name.equals("letters"))
                    readLetters(reader)

                else if(name.equals("numbers"))
                    readNumbers(reader)
            }
        }
    }

    /**
     * helper function to read the letters of the xml file
     * @param reader the parser for the morse xml
     */
    private fun readLetters(reader: XmlPullParser){
        reader.require(XmlPullParser.START_TAG, "", "letters")
        var looper = true
        while(looper){
            if(reader.eventType == XmlPullParser.START_TAG && reader.name.equals("letter"))
                readLetter(reader)
            if(reader.eventType == XmlPullParser.END_TAG && reader.name.equals("letters"))
                looper = false

            reader.next()
        }
    }

    /**
     * helper function to read the letter member
     * @param reader the parser for the morse xml
     */
    private fun readLetter(reader: XmlPullParser){
        if(reader.eventType == XmlPullParser.START_TAG && reader.name.equals("letter"))
            readToken(reader)
    }

    /**
     * helper function to read the the numbers of the xml file
     * @param reader the parser for the morse xml
     */
    private fun readNumbers(reader: XmlPullParser){
        reader.require(XmlPullParser.START_TAG, "", "numbers")
        var looper = true
        while(looper){
            if(reader.eventType == XmlPullParser.START_TAG && reader.name.equals("number"))
                readNumber(reader)
            if(reader.eventType == XmlPullParser.END_TAG && reader.name.equals("numbers"))
                looper = false
            reader.next()
        }
    }

    /**
     * helper method for the number member
     * @param reader the parser for the morse xml
     */
    private fun readNumber(reader: XmlPullParser){
        if(reader.eventType == XmlPullParser.START_TAG && reader.name.equals("number"))
            readToken(reader)
    }

    /**
     * reads the token of the current member
     * @param reader the pull parser to read the morse xml
     */
    private fun readToken(reader: XmlPullParser){
        var go = true
        while(go){
            if(reader.eventType == XmlPullParser.START_TAG && reader.name.equals("token")){
                var id = reader.getAttributeValue(null, "id")
                var morse = reader.getAttributeValue(null, "morse")
                dictAToM[id] = morse
                dictMToA[morse] = id
            }
            reader.next()
            if(reader.eventType == XmlPullParser.END_TAG && reader.name.equals("token"))
                go = false
        }
    }
}