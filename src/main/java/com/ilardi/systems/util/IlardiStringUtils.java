/*
 * Created on Mar 23, 2004
 *
 */

/*
 * Copyright 2007 Robert C. Ilardi
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.ilardi.systems.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

public class IlardiStringUtils {

  /**
   * This class contains some useful string utility methods
   */

  public IlardiStringUtils() {}

  public static String[] trimAllElements(String[] arr) {
    String[] newArr = null;

    if (arr != null) {
      newArr = new String[arr.length];

      for (int i = 0; i < arr.length; i++) {
        newArr[i] = arr[i].trim();
      }
    }

    return newArr;
  }

  public static String filterNonUtf8Characters(String input) {
    StringBuilder result = new StringBuilder();
    for (int i = 0; i < input.length(); i++) {
      char c = input.charAt(i);
      if (isUtf8Character(c)) {
        result.append(c);
      }
    }
    return result.toString();
  }

  public static boolean isUtf8Character(char c) {
    // We can use Character.isValidCodePoint to check if the code point is valid
    // and Character.isBmpCodePoint to check if it's in the Basic Multilingual Plane
    // (BMP).
    int codePoint = (int) c;
    return Character.isValidCodePoint(codePoint) && Character.isBmpCodePoint(codePoint);
  }

  public static boolean startsWithQuoteChar(String text) {
    return startsWith(text, true, "\"", "'");
  }

  public static boolean startsWith(String text, boolean trim, String... prefixLst) {
    boolean found = false;

    if (text != null && prefixLst != null) {
      if (trim) {
        text = text.trim();
        prefixLst = trimAllElements(prefixLst);
      }

      for (int i = 0; i < prefixLst.length; i++) {
        if (text.startsWith(prefixLst[i])) {
          found = true;
          break;
        }
      }
    }

    return found;
  }

  public static boolean charListContainsChar(char c, char... charLst) {
    boolean found = false;

    if (charLst != null) {
      for (int i = 0; i < charLst.length; i++) {
        if (c == charLst[i]) {
          found = true;
          break;
        }
      }
    }

    return found;
  }

  public static String[] splitOnAnyWhitespace(String text, boolean trim) {
    String[] tmpArr = null;
    ArrayList<String> al;
    StringBuilder sb;
    char c;

    if (text != null) {
      if (trim) {
        text = text.trim();
      }

      al = new ArrayList<>();
      sb = new StringBuilder();

      for (int i = 0; i < text.length(); i++) {
        c = text.charAt(i);

        if (charListContainsChar(c, ' ', '\t')) {
          al.add(sb.toString());
          sb = new StringBuilder();
        }
        else {
          sb.append(c);
        }
      }

      if (sb.length() > 0) {
        al.add(sb.toString());
      }

      tmpArr = new String[al.size()];
      tmpArr = al.toArray(tmpArr);
      al.clear();
    }

    return tmpArr;
  }

  private static Character findFirstQuoteTypeChar(String text) {
    char c;
    Character quoteChar = null;

    if (text != null) {
      text = text.trim();

      for (int i = 0; i < text.length(); i++) {
        c = text.charAt(i);

        if (c == '\'' || c == '\"') {
          quoteChar = c;
          break;
        }
      }
    }

    return quoteChar;
  }

  public static String[] smartQuoteSplit(String text, boolean trim, char... splitChars) {
    String[] tmpArr = null;
    ArrayList<String> al;
    StringBuilder sb;
    char c;
    boolean inQuotes;
    Character quoteChar;

    if (text != null) {
      if (trim) {
        text = text.trim();
      }

      al = new ArrayList<>();
      sb = new StringBuilder();
      inQuotes = false;

      quoteChar = findFirstQuoteTypeChar(text);

      for (int i = 0; i < text.length(); i++) {
        c = text.charAt(i);

        if (quoteChar != null && c == quoteChar.charValue()) {
          inQuotes = !inQuotes;
        }
        else if (!inQuotes && charListContainsChar(c, splitChars)) {
          al.add(sb.toString());
          sb = new StringBuilder();
        }
        else {
          sb.append(c);
        }
      }

      if (sb.length() > 0) {
        al.add(sb.toString());
      }

      tmpArr = new String[al.size()];
      tmpArr = al.toArray(tmpArr);
      al.clear();
    }

    return tmpArr;
  }

  public static Properties getPrefixedPropNames(Properties props, String prefix) {
    Properties prefixedProps = null;
    Iterator<Object> iter;
    String name, value;

    if (props != null) {
      prefixedProps = new Properties();

      iter = props.keySet().iterator();

      while (iter.hasNext()) {
        name = (String) iter.next();
        value = props.getProperty(name);

        if (name.startsWith(prefix)) {
          prefixedProps.setProperty(name, value);
        }
      }
    }

    return prefixedProps;
  }

  public static Properties getPropertiesUsingPropNamePrefix(Properties props, String prefix) {
    Properties prefixedProps = null;
    Iterator<Object> iter;
    String name, value;

    if (props != null) {
      prefixedProps = new Properties();

      iter = props.keySet().iterator();

      while (iter.hasNext()) {
        name = (String) iter.next();
        value = props.getProperty(name);

        if (name.startsWith(prefix)) {
          prefixedProps.setProperty(name, value);
        }
      }
    }

    return prefixedProps;
  }

  public static Properties removePrefixFromPropNames(Properties props, String prefix) {
    Properties unprefixedProps = null;
    Iterator<Object> iter;
    String name, value;
    int beginIndex;

    if (props != null) {
      unprefixedProps = new Properties();

      iter = props.keySet().iterator();

      while (iter.hasNext()) {
        name = (String) iter.next();
        value = props.getProperty(name);

        if (name.startsWith(prefix)) {
          beginIndex = prefix.length();
          name = name.substring(beginIndex);
          unprefixedProps.setProperty(name, value);
        }
      }
    }

    return unprefixedProps;
  }

  public static List<String> getValueListUsingPropNamePrefix(Properties props, String prefix) {
    ArrayList<String> valueLst = null;
    Iterator<Object> iter;
    String name, value;

    if (props != null) {
      valueLst = new ArrayList<String>();

      iter = props.keySet().iterator();

      while (iter.hasNext()) {
        name = (String) iter.next();
        value = props.getProperty(name);

        if (name.startsWith(prefix)) {
          valueLst.add(value);
        }
      }
    }

    return valueLst;
  }

}
