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

    private HashMap<String, String> dict;
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
    public void conversation(){
        Scanner scan = new Scanner(System.in);
        System.out.println("Shall I translate a word or sentence? If yes, enter 1");
        if(scan.nextLine().equals("1")){
            System.out.println("Please enter a word: ");
            String word = scan.nextLine();
            if(word.isEmpty()){
                System.out.println("Goodbye");
                System.exit(0);
            }
            String conv = convert(scan.nextLine().trim());
            System.out.println("The converted word " + "is: " + conv);
            conversation();
        }
        System.out.println("Goodbye.");
        System.exit(0);
    }
    /**
     * Convert a given sequence of characters
     * @param word word or sentence to translate.
     * @return The word or sentence converted to morse code
     */
    public String convert(String word){
        int size = word.length();
        String word2 = word.toLowerCase();
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < size; i++){
            String current = word2.substring(i, i+1);
            String morse = getMorse(current);
            builder.append(morse);
            builder.append(" ");
        }
        return builder.toString();
    }

    /**
     * Helper method to convert the current index of a word or sentence
     * currently works only in one direction
     * @param current current character of string
     * @return converted character
     */
    private String getMorse(String current){
        if(dict.containsKey(current))
            return dict.get(current);

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

        this.dict = new HashMap<>();
        parseMap(nums);
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
                        if (!nums){
                            t = false;
                            break;
                        }
                        else{
                            parseLetters(reader);
                        }
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
                    this.dict.put(id, morse);
                }
            }
            // parse until the end of a alphabet type is reached
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
            System.out.print(converter.convert("test") + "\n");
            converter.conversation();
        }
        catch(IOException | XMLStreamException e){
            e.printStackTrace();
        }
    }
}
