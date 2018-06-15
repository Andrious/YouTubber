package com.andrioussolutions.utils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
/**
 * Copyright (C) 2018  Andrious Solutions Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Example Reference: https://javapracs.blogspot.ca/2011/06/java-ini-parser.html
 *
 * Created  27 Jan 2018
 */
public class IniProperties {

    private Properties globalProperties;
    
    private Map<String,Properties> properties;

    String current_section = null;


    enum ParseState {
        NORMAL,
        ESCAPE,
        ESC_CRNL,
        COMMENT
    }



    
    public IniProperties() {

        globalProperties = new Properties();

        properties = new HashMap<String,Properties>();
    }


    /**
     * Load ini as properties from input stream.
     */
    public void load(String file) throws IOException {

        InputStream in = new BufferedInputStream(new FileInputStream(file));

        load(in);

        in.close();
    }



    /**
     * Load ini as properties from input stream.
     */
    private void load(InputStream in) throws IOException {

        int bufSize = 4096;

        byte[] buffer = new byte[bufSize];

        int n = in.read(buffer, 0, bufSize);

        ParseState state = ParseState.NORMAL;

        boolean section_open = false;

//        String current_section = null;

        String key = null, value = null;

        StringBuilder sb = new StringBuilder();

        while (n >= 0) {

            for (int i = 0; i < n; i++) {

                char c = (char) buffer[i];

                if (state == ParseState.COMMENT) {

                    // comment, skip to end of line
                    if ((c == '\r') ||(c == '\n')) {

                        state = ParseState.NORMAL;
                    }
                    else {

                        continue;
                    }
                }

                if (state == ParseState.ESCAPE) {

                    sb.append(c);

                    if (c == '\r') {

                        // if the EOL is \r\n, \ escapes both chars
                        state = ParseState.ESC_CRNL;
                    }
                    else {

                        state = ParseState.NORMAL;
                    }
                    
                    continue;
                }

                switch (c) {

                    case '[': // start section

                        sb = new StringBuilder();

                        section_open = true;

                        break;

                    case ']': // end section

                        if (section_open) {

                            current_section = sb.toString().trim();

                            sb = new StringBuilder();

                            properties.put(current_section, new Properties());

                            section_open = false;
                        }
                        else {

                            sb.append(c);
                        }
                        break;

                    case '\\': // escape char, take the next char as is

                        state = ParseState.ESCAPE;

                        break;

                    case '#':
                    case ';':
                        state = ParseState.COMMENT;

                        break;

                    case '=': // assignment operator
                    case ':':

                        if (key == null) {

                            key = sb.toString().trim();

                            sb = new StringBuilder();
                        }
                        else {

                            sb.append(c);
                        }
                        break;

                    case '\r':
                    case '\n':

                        if ((state == ParseState.ESC_CRNL) && (c == '\n')) {

                            sb.append(c);

                            state = ParseState.NORMAL;
                        }
                        else {

                            prop.set(this, sb, key);

                            sb = new StringBuilder();

//                            if (sb.length() > 0) {
//
//                                value = sb.toString().trim();
//
//                                sb = new StringBuilder();
//
//                                if (key != null) {
//
//                                    if (current_section == null) {
//
//                                        this.setProperty(key, value);
//                                    }
//                                    else {
//
//                                        this.setProperty(current_section, key, value);
//                                    }
//                                }
//                            }

                            key = null;

                            value = null;
                        }

                        break;

                    default:

                        sb.append(c);
                }
            }

            prop.set(this, sb, key);
            
            n = in.read(buffer, 0, bufSize);
        }
    }



    
    private iniProperty prop =(IniProperties iniProp, StringBuilder sb, String key) ->{

        if (sb.length() > 0) {

            String value = sb.toString().trim();

            if (key != null) {

                if (current_section == null) {

                    iniProp.setProperty(key, value);
                }
                else {

                    iniProp.setProperty(current_section, key, value);
                }
            }
        }
    };




    @FunctionalInterface
    private interface iniProperty {

        void set(IniProperties ini, StringBuilder sb, String key);
    }



    
    /**
     * Get global property by name.
     */
    public String getProperty(String name) {

        return globalProperties.getProperty(name);
    }




    /**
     * Set global property.
     */
    public void setProperty(String name, String value) {

        globalProperties.setProperty(name, value);
    }




    /**
     * Return iterator of global properties.
     */
    @SuppressWarnings("unchecked")
    public Iterator<String> properties() {

        return new IteratorFromEnumeration<String>(
                (Enumeration<String>)globalProperties.propertyNames());
    }




    /**
     * Get property value for specified section and name. Returns null
     * if section or property does not exist.
     */
    public String getProperty(String section, String name) {

        Properties p = properties.get(section);

        return p == null ? null : p.getProperty(name);
    }




    /**
     * Set property value for specified section and name. Creates section
     * if not existing.
     */
    public void setProperty(String section, String name, String value) {

        Properties p = properties.get(section);

        if (p == null) {

            p = new Properties();

            properties.put(section, p);
        }
        
        p.setProperty(name, value);
    }




    /**
     * Return property iterator for specified section. Returns null if
     * specified section does not exist.
     */
    @SuppressWarnings("unchecked")
    public Iterator<String> properties(String section) {

        Properties p = properties.get(section);

        if (p == null) {

            return null;
        }

        return new IteratorFromEnumeration<String>(
                (Enumeration<String>)p.propertyNames());
    }




    /**
     * Return iterator of names of section.
     */
    public Iterator<String> sections() {

        return properties.keySet().iterator();
    }




    /**
     * Dumps properties to output stream.
     */
    public void dump(PrintStream out) throws IOException {

        // Global properties
        Iterator<String> props = this.properties();

        while (props.hasNext()) {

            String name = props.next();

            out.printf("%s = %s\n", name, dumpEscape(getProperty(name)));
        }

        // sections
        Iterator<String> sections = this.sections();

        while (sections.hasNext()) {

            String section = sections.next();

            out.printf("\n[%s]\n", section);

            props = this.properties(section);

            while (props.hasNext()) {

                String name = props.next();

                out.printf("%s = %s\n", name, dumpEscape(getProperty(section, name)));
            }
        }
    }




    private static String dumpEscape(String s) {

        return s.replaceAll("\\\\", "\\\\\\\\")
                .replaceAll(";", "\\\\;")
                .replaceAll("#", "\\\\#")
                .replaceAll("(\r?\n|\r)", "\\\\$1");
    }




    // private class used to coerce Enumerator to Iterator.
    private static class IteratorFromEnumeration<E> implements Iterator {

        private Enumeration<E> e;

        public IteratorFromEnumeration(Enumeration<E> e) {

            this.e = e;
        }

        public boolean hasNext() {

            return e.hasMoreElements();
        }

        public E next() {

            return e.nextElement();
        }

        public void remove() {

            throw new UnsupportedOperationException("Can't change underlying enumeration");
        }
    }
}
/**
 There are only a few simple rules:
 Leading and trailing spaces are trimmed from section names, property names and property values.
 Section names are enclosed between [ and ].
 Properties following a section header belong to that section
 Properties defined before the appearance of any section headers are considered global properties and should be set and get with no section names.
 You can use either equal sign (=) or colon (:) to assign property values
 Comments begin with either a semicolon (;), or a sharp sign (#) and extend to the end of line. It doesn't have to be the first character.
 A backslash (\) escapes the next character (e.g., \# is a literal #, \\ is a literal \).
 If the last character of a line is backslash (\), the value is continued on the next line with new line character included.
*/