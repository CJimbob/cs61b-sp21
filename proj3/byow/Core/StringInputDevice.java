package byow.Core;

public class StringInputDevice {

    private int index;
    private String input;

    private String output;

    public StringInputDevice(String s) {
        s = s.toUpperCase();
        input = s;
        index = 0;
    }

    public boolean hasNextInput() {
        return input.length() > index;
    }

    public char getNextKey() {
        return input.charAt(index++);
    }

    public int getSeed() {
        String seedString = null;
        if (input.charAt(0) == 'N') {
            while (hasNextInput() && input.charAt(index) != 'S') {
                index++;
            }
            seedString = input.substring(1, index);
        }
        return Integer.parseInt(seedString);
    }

}
