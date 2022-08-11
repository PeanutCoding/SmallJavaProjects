package first.app.morseapp;
/**
 * A class to convert the input of a user, or text which is read in.
 * 10.08.2022
 */

public class MorseConverter{

    private String text;
    private int size;
    private String convert;

    //TODO: implement threading
    //TODO: implement file reading
/*
    public MorseConverter(Parcel in){
        String[] data = new String[3];
        data[0] = text;
        data[1] = Integer.parseInt(size);
        data[2] = convert;
    }
    @Override
    public int describeContents() {
    // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    // TODO Auto-generated method stub
        dest.writeStringArray(new String[]{this.UserName,this.Password,String.valueOf(this.Action)});
    }
    public static final Parcelable.Creator<MorseConverter> CREATOR= new Parcelable.Creator<MorseConverter>() {

        @Override
        public MorseConverter createFromParcel(Parcel source) {
        // TODO Auto-generated method stub
            return new MorseConverter(source);  //using parcelable constructor
        }

        @Override
        public MorseConverter[] newArray(int length) {
        // TODO Auto-generated method stub
            return new MorseConverter[length];
        }
    }; */

    /**
     * constructor for the class, no parameters
     */
    public MorseConverter(){
        text = "";
        size = 0;
        convert = "";
    }

    /**
     * set the text to convert and it's length
     * @param input text to convert
     * @param length of the text
     */
    public void setText(String input, int length){
        text = input;
        size = length;
    }

    /**
     * @return the conversion of the given text
     */
    public String getConvert() {
        return convert;
    }

    /**
     * method to convert a given morse encoding to normal alphabet representation
     * @param dict the dict containing the keys and values
     */
    public void convertMorse(Dictionary dict){
        String word = text.toLowerCase();
        StringBuilder build = new StringBuilder();
        String now = "";
        for(int i = 0; i < size; i++) {
            String tmp = word.substring(i, i+1);
            if (tmp.equals(".") || tmp.equals("-") || tmp.equals("E")) {
                now.concat(tmp);
            }
            else {
                if(!now.isEmpty()) {
                    build.append(dict.getMorse(now));
                    now = "";
                }
                if (tmp.equals("/"))
                    build.append(" ");
            }
        }
        convert = build.toString();
    }
    /**
     * Convert a given sequence of characters
     * @param dict the dictionary containing the encodings
     * @return The word or sentence converted to morse code
     */
    public void convertChars(Dictionary dict){
        String word = text.toLowerCase();
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < size; i++){
            String current = word.substring(i, i+1);
            String morse = dict.getId(current);
            builder.append(morse);
            builder.append("'");
        }
        builder.reverse().replace(0,0,"").reverse();
        convert = builder.append("/").toString();
    }
}
