/**
 * Created May 30, 2017
 */
package com.roguelogic.toys;

import java.util.HashMap;

/**
 * @author rilari
 *
 */

public class NATOPhonetic {

  private static final HashMap<String, String> phoneticMap = new HashMap<String, String>();

  static {
    phoneticMap.put("A", "Alfa");
    phoneticMap.put("B", "Bravo");
    phoneticMap.put("C", "Charlie");
    phoneticMap.put("D", "Delta");
    phoneticMap.put("E", "Echo");
    phoneticMap.put("F", "Foxtrot");
    phoneticMap.put("G", "Golf");
    phoneticMap.put("H", "Hotel");
    phoneticMap.put("I", "India");
    phoneticMap.put("J", "Juliett");
    phoneticMap.put("K", "Kilo");
    phoneticMap.put("L", "Lima");
    phoneticMap.put("M", "Mike");
    phoneticMap.put("N", "November");
    phoneticMap.put("O", "Oscar");
    phoneticMap.put("P", "Papa");
    phoneticMap.put("Q", "Quebec");
    phoneticMap.put("R", "Romeo");
    phoneticMap.put("S", "Sierra");
    phoneticMap.put("T", "Tango");
    phoneticMap.put("U", "Uniform");
    phoneticMap.put("V", "Victor");
    phoneticMap.put("W", "Whiskey");
    phoneticMap.put("X", "Xray");
    phoneticMap.put("Y", "Yankee");
    phoneticMap.put("Z", "Zulu");
  }

  public NATOPhonetic() {}

  public String getCodeWord(String letter) {
    String codeWord;

    codeWord = phoneticMap.get(letter.toUpperCase());

    return codeWord;
  }

  public String getCodeWord(char letter) {
    return getCodeWord(String.valueOf(letter));
  }

  public String getCodedText(String text) {
    char letter;
    String codeWord;
    StringBuilder codedText;
    boolean wasWs = false;

    codedText = new StringBuilder();

    for (int i = 0; i < text.length(); i++) {
      letter = text.charAt(i);

      if (letter == ' ') {
        wasWs = true;
        codedText.append(" ");
      }
      else {
        if (i > 0 && !wasWs) {
          codedText.append("-");
        }

        wasWs = false;
        codeWord = getCodeWord(letter);
        codedText.append(codeWord);
      }

    }

    return codedText.toString();
  }

  public static void main(String[] args) {
    NATOPhonetic np;
    int exitCd = 0;
    StringBuilder sb;
    String codedText;

    np = new NATOPhonetic();

    sb = new StringBuilder();

    for (int i = 0; i < args.length; i++) {
      if (i > 0) {
        sb.append(" ");
      }

      sb.append(args[i]);
    }

    codedText = np.getCodedText(sb.toString());

    System.out.println(codedText);

    System.exit(exitCd);
  }

}
