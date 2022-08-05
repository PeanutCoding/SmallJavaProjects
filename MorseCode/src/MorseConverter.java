import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import javax.xml.stream.*;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

public class MorseConverter {

    private HashMap<String, String> dictAToM;
    private HashMap<String, String> dictMToA;
    private final String fileName = "src/resources/Morse.xml";


    /**
     * Construct a morse converter
     * @param nums true if numbers are included false if not so. at the moment
     *             not changeable
     * @throws IOException if file not found
     * @throws XMLStreamException something while parsing went wrong
     */
    public MorseConverter(boolean nums) throws IOException, XMLStreamException {
        initializeDic(nums);
    }

    /**
     * Type in words or sentences to translate until no longer wished for
     */
    public void conversation(Scanner scan){
        System.out.println("Shall I translate a word or sentence? If yes, enter 1");
        if(scan.nextLine().equals("1")){
            System.out.println("Please enter a word: ");
            String word = scan.nextLine();
            if(word.isEmpty()){
                System.out.println("Goodbye");
                System.exit(0);
            }
            String line = scan.nextLine().trim();
            String conv = "";
            while(scan.hasNext(line)){
                String next = scan.next(line);
                int size = next.length();
                if(next.startsWith("-") || next.startsWith(".") &&
                        next.charAt(1) == '.' || next.charAt(1) == '-'){
                    conv += convertMorse(next, size);
                }
                else
                    conv += convertChars(next, size);
            }
            System.out.println("The converted word " + "is: " + conv);

            conversation(scan);
        }
        System.out.println("Goodbye.");
        System.exit(0);
    }

    /**
     * Method to convert morse code into human readable characters
     * @param word the String to translate
     * @param size size of the string
     * @return translation of the string
     */
    public String convertMorse(String word, int size){
        String word2 = word.toLowerCase();
        StringBuilder build = new StringBuilder();
        String now = "";
        for(int i = 0; i < size; i++) {
            char tmp = word2.charAt(i);
            if (tmp == '.' || tmp == '-' || tmp == 'E') {
                now += tmp;
            }
            else {
                if(!now.isEmpty()) {
                    build.append(getMorseMo(now));
                    now = "";
                }
                if (tmp == '/')
                    build.append(" ");
            }
        }
        return build.toString();
    }
    /**
     * Convert a given sequence of characters
     * @param word word or sentence to translate.
     * @return The word or sentence converted to morse code
     */
    public String convertChars(String word, int size){
        String word2 = word.toLowerCase();
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < size; i++){
            String current = word2.substring(i, i+1);
            String morse = getMorseCh(current);
            builder.append(morse);
            builder.append("'");
        }
        builder.reverse().replace(0,0,"").reverse();
        return builder.append("/").toString();
    }

    /**
     * Helper method to convert the current index of a word or sentence
     * currently works only in one direction
     * @param current current character of string
     * @return converted character
     */
    private String getMorseCh(String current){
        if(dictAToM.containsKey(current))
            return dictAToM.get(current);

        return "[Not found]";
    }

    /**
     * Converts a morse sequence to it's reciprocate
     * @param current morse string
     * @return respective translation
     */
    private String getMorseMo(String current){
        if(dictMToA.containsKey(current))
            return dictMToA.get(current);

        return "[Not found]";
    }

    /**
     * Initializes the dictionary for the morse code
     * @throws IOException if file not found
     * @throws XMLStreamException passed from parser
     */
    private void initializeDic(boolean nums) throws IOException, XMLStreamException {
        File f = new File(fileName);
        if(!checkFile(f))
            throw new IOException("Files does not exist!");

        this.dictAToM = new HashMap<>();
        this.dictMToA = new HashMap<>();
        parseMap(nums);
        //testMaps();
    }

    /**
     * A simple method to check, whether the dictionaries were parsed correctly
     */
    private void testMaps(){
        this.dictMToA.forEach((k, v) -> {System.out.println(k + " " + v);});
        this.dictAToM.forEach((k, v) -> {System.out.println(k + " " + v);});
    }
    /**
     * predicate to check for the file to parse from
     * @param f file to check for
     * @return true if exists
     */
    private boolean checkFile(File f){
        if(!f.isDirectory() && f.exists())
            return true;

        return false;
    }

    /**
     * helper method to parse the dictionary
     */
    private void parseMap(boolean nums) throws FileNotFoundException, XMLStreamException {
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        XMLEventReader reader = inputFactory.createXMLEventReader(new FileInputStream(fileName));
        boolean t = true;
        while(reader.hasNext() && t){
            XMLEvent event = reader.nextEvent();
            if(event.getEventType() == XMLStreamConstants.START_ELEMENT) {
                StartElement start = event.asStartElement();
                String local = start.getName().getLocalPart();
                switch(local) {
                    case "letters":
                        parseLetters(reader);
                        break;
                    case "numbers":
                        if (!nums)
                            t = false;
                        else
                            parseLetters(reader);
                        break;
                }
            }
        }
    }

    /**
     * helper method to parse the id and morse of a token
     * @param reader the xml event reader parsing the document
     * @throws XMLStreamException if something while parsing went wrong
     */
    private void parseLetters(XMLEventReader reader) throws XMLStreamException {
        while(reader.hasNext()){
            XMLEvent event = reader.nextEvent();
            if(event.getEventType() == XMLStreamConstants.START_ELEMENT) {
                StartElement start = event.asStartElement();
                String local = start.getName().getLocalPart();
                if(local.equals("token")) {
                    Iterator<Attribute> attributes = start.getAttributes();
                    String morse = attributes.next().getValue();
                    String id = attributes.next().getValue();
                    // put character as key, morse as value
                    this.dictAToM.put(id, morse);
                    // put morse as key, character as value
                    this.dictMToA.put(morse, id);
                }
            }
            // parse until the end of an alphabet type is reached
            else if(event.getEventType() == XMLStreamConstants.END_ELEMENT){
                EndElement end = event.asEndElement();
                if(!end.getName().getLocalPart().equals("token"))
                    break;
            }
        }
    }
    public static void main(String[] args){
        try {
            MorseConverter converter = new MorseConverter(false);
            String test = "testerino this sentence pliz.";
            String test2 = "-'.'...'-'.'.-.'..'-.'---'/'-'....'..'...'/'...'.'-.'-'.'-.'-.-.'.'/'.--.'.-..'..'--..'/";
            System.out.print(converter.convertChars(test.toLowerCase(), test.length()) + "\n");
            System.out.println(converter.convertMorse(test2, test2.length()));
            //converter.conversation(new Scanner(System.in));
        }
        catch(IOException | XMLStreamException e){
            e.printStackTrace();
        }
    }
}
