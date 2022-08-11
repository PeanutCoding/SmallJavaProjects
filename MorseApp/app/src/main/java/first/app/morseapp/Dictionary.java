package first.app.morseapp;
/**
 * A class, that reads in the morse.xml file and stores the values in a hashmap.
 * One way from a normal character, i.e. 'a', to it's respective morse encoding
 * and the other way around.
 *
 * 10.08.2022
 */

import android.util.Xml;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Dictionary {

    private HashMap<String, String> dictAToM;
    private HashMap<String, String> dictMToA;


    public Dictionary(){
        dictAToM = new HashMap<>();
        dictMToA = new HashMap<>();
    }
    /**
     * Initializes the dictionary for the morse code
     * @param in the morse.xml to read in with an input stream
     * @throws IOException if file not found
     */
    public void initializeDic(InputStream in) throws IOException, XmlPullParserException{
        parseMaps(in);
    }

    /**
     * Get the value of an alphabet key. i.e. a : .-
     * @param key the character to look for
     * @return the respective value, if not inside dictionary, return not found
     */
    public String getId(String key){
        if(dictAToM.containsKey(key.toLowerCase()))
            return dictAToM.get(key.toLowerCase());

        return "[Not found]";
    }

    /**
     * Get the value of a morse encoding. i.e. .- : a
     * @param key the morse endocing to look for
     * @return the rsspective decription, or not found if not in dicitonary
     */
    public String getMorse(String key){
        if(dictMToA.containsKey(key))
            return dictMToA.get(key);

        return "[Not found]";
    }
    /**
     * helper method to parse the dictionary
     */
    private void parseMaps(InputStream in) throws IOException, XmlPullParserException {
        try {
            XmlPullParser reader = Xml.newPullParser();
            reader.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            reader.setInput(in, null);
            reader.nextTag();
            parseMorse(reader);
        } finally {
            in.close();
        }
    }
    // a message from my girlfriend
    //hallooooooooooooooo


    /**
     * helper method to parse the id and morse of a token
     * @param parser the xml event parser parsing the document
     */
    private void parseMorse(XmlPullParser parser) throws XmlPullParserException, IOException {
        while(parser.next() != XmlPullParser.END_DOCUMENT) {
            if (parser.getEventType() == XmlPullParser.START_TAG) {
                String name = parser.getName();
                if (name.equals("letters"))
                    readLetters(parser);

                else if (name.equals("numbers"))
                    readNumbers(parser);
            }
        }
    }

    /**
     * helper method to parse the letters in the xml file
     * @param parser the xml pull parser
     * @throws XmlPullParserException if something goes wrong while parsing
     * @throws IOException if something goes while parsing
     */
    private void readLetters(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, "", "letters");
        // boolean to check whether the end of the letters tag is reached
        boolean looper = true;
        while(looper){
            if(parser.getEventType() == XmlPullParser.START_TAG && parser.getName().equals("letter"))
                readLetter(parser);

            if(parser.getEventType() == XmlPullParser.END_TAG && parser.getName().equals("letters"))
                looper = false;

            parser.next();
        }
    }

    /**
     * helper method to read in a single letter in the letters tag
     * @param parser the xml pull parser
     * @throws XmlPullParserException if something goes wrong while parsing
     * @throws IOException if something goes wrong while parsing
     */
    private void readLetter(XmlPullParser parser) throws XmlPullParserException, IOException {
        if(parser.getEventType() == XmlPullParser.START_TAG && parser.getName().equals("letter"))
            readToken(parser);
    }

    /**
     * helper method to read in the numbers part of the xml file
     * @param parser the xml pull parser
     * @throws XmlPullParserException if something goes wrong while parsing
     * @throws IOException if something goes wrong while parsing
     */
    private void readNumbers(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, "", "numbers");
        // boolean to check, whether the end of the numbers tag is reached
        boolean looper = true;
        while(looper){
            if(parser.getEventType() == XmlPullParser.START_TAG && parser.getName().equals("number"))
                readNumber(parser);
            if(parser.getEventType() == XmlPullParser.END_TAG && parser.getName().equals("numbers"))
                looper = false;

            parser.next();
        }
    }

    /**
     * helper method to read in the number part of numbers
     * @param parser the xml pull parser
     * @throws XmlPullParserException if something goes wrong while parsing
     * @throws IOException if something goes wrong while parsing
     */
    private void readNumber(XmlPullParser parser) throws XmlPullParserException, IOException {
        if(parser.getEventType() == XmlPullParser.START_TAG && parser.getName().equals("number"))
            readToken(parser);
    }

    /**
     * the helper method to read in each token of the tags
     * @param parser the xml pull parser
     * @throws XmlPullParserException if something goes wrong while parsing
     * @throws IOException if something goes wrong while parsing
     */
    private void readToken(XmlPullParser parser) throws XmlPullParserException, IOException {
        // boolean to determine end of token.
        // for reasons which must be cleared yet, to get into the actual token part.
        boolean go = true;
        while(go){
            if (parser.getEventType() == XmlPullParser.START_TAG && parser.getName().equals("token")) {
                String id = parser.getAttributeValue(null, "id");
                String morse = parser.getAttributeValue(null, "morse");
                dictAToM.put(id, morse);
                dictMToA.put(morse, id);
            }
            parser.next();
            if(parser.getEventType() == XmlPullParser.END_TAG && parser.getName().equals("token"))
                go = false;
        }
    }
}
