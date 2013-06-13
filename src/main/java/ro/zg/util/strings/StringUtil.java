/*******************************************************************************
 * Copyright 2011 Adrian Cristian Ionescu
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package ro.zg.util.strings;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

import ro.zg.util.parser.ParseEventHandlerConfig;
import ro.zg.util.parser.ParseResponse;
import ro.zg.util.parser.Parser;
import ro.zg.util.parser.ParserException;
import ro.zg.util.parser.ParserHandlerFactory;
import ro.zg.util.parser.event.ConfigurableParseEventHandler;
import ro.zg.util.parser.event.DefaultParseEventHandler;

public class StringUtil {
    private static Parser unescapeHtmlParser;
    private static Parser htmlSanitizer;

    static {
	unescapeHtmlParser = new Parser();
	try {
	    unescapeHtmlParser
		    .addSymbols("[,DOC_START:IGNORE_EMPTY,doc_start,true],[&,GROUP_START:SEPARATE,html_escape_start,true,html_escape_end,html_escaper],[;,GROUP_END:UNGROUP,html_escape_end],['{}',WORKER:STRING_CONCAT,string_concat],['{lt=<,gt=>,amp=&,quot=\\\\\\\",apos=\\'}',WORKER:MAPPER,html_escaper]");
	} catch (ParserException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	unescapeHtmlParser.setHandlerFactory(new ParserHandlerFactory(new DefaultParseEventHandler()));
	unescapeHtmlParser.getParseContextConfig().getNestedSymbols().setCaseInsensitive(true);

	htmlSanitizer = new Parser();
	try {
	    htmlSanitizer
		    .addSymbols("[,DOC_START:IGNORE_EMPTY,doc_start,true],[<script>,GROUP_START:SEPARATE:IGNORE,ss1,true,es1,es2,es3,es4],[<script ,GROUP_START:SEPARATE:IGNORE,ss2,true,es1,es2,es3,es4],[<script\t,GROUP_START:SEPARATE:IGNORE,ss3,true,es1,es2,es3,es4],[<script\n,GROUP_START:SEPARATE:IGNORE,ss4,true,es1,es2,es3,es4],[</script>,GROUP_END,es1],[</script ,GROUP_END_START:IGNORE,es2,false,end_tag],[</script\t,GROUP_END_START:IGNORE,es3,false,end_tag],[</script\n,GROUP_END_START:IGNORE,es4,false,end_tag],[>,GROUP_END,end_tag]");
	} catch (ParserException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	htmlSanitizer.setHandlerFactory(new ParserHandlerFactory(new DefaultParseEventHandler()));
	htmlSanitizer.getParseContextConfig().getNestedSymbols().setCaseInsensitive(true);
    }

    /**
     * <p>
     * Escapes the characters in a <code>String</code> using JavaScript String rules to a <code>Writer</code>.
     * </p>
     * 
     * <p>
     * A <code>null</code> string input has no effect.
     * </p>
     * 
     * @see #escapeJavaScript(java.lang.String)
     * @param out
     *            Writer to write escaped string into
     * @param str
     *            String to escape values in, may be null
     * @throws IllegalArgumentException
     *             if the Writer is <code>null</code>
     * @throws IOException
     *             if error occurs on underlying Writer
     **/
    public static void escapeJavaScript(Writer out, String str) throws IOException {
	escapeJavaStyleString(out, str, true);
    }

    /**
     * <p>
     * Worker method for the {@link #escapeJavaScript(String)} method.
     * </p>
     * 
     * @param str
     *            String to escape values in, may be null
     * @param escapeSingleQuotes
     *            escapes single quotes if <code>true</code>
     * @return the escaped string
     */
    private static String escapeJavaStyleString(String str, boolean escapeSingleQuotes) {
	if (str == null) {
	    return null;
	}
	try {
	    StringWriter writer = new StringWriter(str.length() * 2);
	    escapeJavaStyleString(writer, str, escapeSingleQuotes);
	    return writer.toString();
	} catch (IOException ioe) {
	    // this should never ever happen while writing to a StringWriter
	    ioe.printStackTrace();
	    return null;
	}
    }

    /**
     * <p>
     * Worker method for the {@link #escapeJavaScript(String)} method.
     * </p>
     * 
     * @param out
     *            write to receieve the escaped string
     * @param str
     *            String to escape values in, may be null
     * @param escapeSingleQuote
     *            escapes single quotes if <code>true</code>
     * @throws IOException
     *             if an IOException occurs
     */
    private static void escapeJavaStyleString(Writer out, String str, boolean escapeSingleQuote) throws IOException {
	if (out == null) {
	    throw new IllegalArgumentException("The Writer must not be null");
	}
	if (str == null) {
	    return;
	}
	int sz;
	sz = str.length();
	for (int i = 0; i < sz; i++) {
	    char ch = str.charAt(i);

	    // handle unicode
	    if (ch > 0xfff) {
		out.write("\\u" + hex(ch));
	    } else if (ch > 0xff) {
		out.write("\\u0" + hex(ch));
	    } else if (ch > 0x7f) {
		out.write("\\u00" + hex(ch));
	    } else if (ch < 32) {
		switch (ch) {
		case '\b':
		    out.write('\\');
		    out.write('b');
		    break;
		case '\n':
		    out.write('\\');
		    out.write('n');
		    break;
		case '\t':
		    out.write('\\');
		    out.write('t');
		    break;
		case '\f':
		    out.write('\\');
		    out.write('f');
		    break;
		case '\r':
		    out.write('\\');
		    out.write('r');
		    break;
		default:
		    if (ch > 0xf) {
			out.write("\\u00" + hex(ch));
		    } else {
			out.write("\\u000" + hex(ch));
		    }
		    break;
		}
	    } else {
		switch (ch) {
		case '\'':
		    if (escapeSingleQuote) {
			out.write('\\');
		    }
		    out.write('\'');
		    break;
		case '"':
		    out.write('\\');
		    out.write('"');
		    break;
		case '\\':
		    out.write('\\');
		    out.write('\\');
		    break;
		case '/':
		    out.write('\\');
		    out.write('/');
		    break;
		default:
		    out.write(ch);
		    break;
		}
	    }
	}
    }

    /**
     * <p>
     * Unescapes any Java literals found in the <code>String</code>. For example, it will turn a sequence of
     * <code>'\'</code> and <code>'n'</code> into a newline character, unless the <code>'\'</code> is preceded by
     * another <code>'\'</code>.
     * </p>
     * 
     * @param str
     *            the <code>String</code> to unescape, may be null
     * @return a new unescaped <code>String</code>, <code>null</code> if null string input
     */
    public static String unescapeJava(String str) {
	if (str == null) {
	    return null;
	}
	try {
	    StringWriter writer = new StringWriter(str.length());
	    unescapeJava(writer, str);
	    return writer.toString();
	} catch (IOException ioe) {
	    // this should never ever happen while writing to a StringWriter
	    ioe.printStackTrace();
	    return null;
	}
    }

    /**
     * <p>
     * Unescapes any Java literals found in the <code>String</code> to a <code>Writer</code>.
     * </p>
     * 
     * <p>
     * For example, it will turn a sequence of <code>'\'</code> and <code>'n'</code> into a newline character, unless
     * the <code>'\'</code> is preceded by another <code>'\'</code>.
     * </p>
     * 
     * <p>
     * A <code>null</code> string input has no effect.
     * </p>
     * 
     * @param out
     *            the <code>Writer</code> used to output unescaped characters
     * @param str
     *            the <code>String</code> to unescape, may be null
     * @throws IllegalArgumentException
     *             if the Writer is <code>null</code>
     * @throws IOException
     *             if error occurs on underlying Writer
     */
    public static void unescapeJava(Writer out, String str) throws IOException {
	if (out == null) {
	    throw new IllegalArgumentException("The Writer must not be null");
	}
	if (str == null) {
	    return;
	}
	int sz = str.length();
	StringBuffer unicode = new StringBuffer(4);
	boolean hadSlash = false;
	boolean inUnicode = false;
	for (int i = 0; i < sz; i++) {
	    char ch = str.charAt(i);
	    if (inUnicode) {
		// if in unicode, then we're reading unicode
		// values in somehow
		unicode.append(ch);
		if (unicode.length() == 4) {
		    // unicode now contains the four hex digits
		    // which represents our unicode character
		    try {
			int value = Integer.parseInt(unicode.toString(), 16);
			out.write((char) value);
			unicode.setLength(0);
			inUnicode = false;
			hadSlash = false;
		    } catch (NumberFormatException nfe) {
			throw new IOException("Unable to parse unicode value: " + unicode, nfe);
		    }
		}
		continue;
	    }
	    if (hadSlash) {
		// handle an escaped value
		hadSlash = false;
		switch (ch) {
		case '\\':
		    out.write('\\');
		    break;
		case '\'':
		    out.write('\'');
		    break;
		case '\"':
		    out.write('"');
		    break;
		case 'r':
		    out.write('\r');
		    break;
		case 'f':
		    out.write('\f');
		    break;
		case 't':
		    out.write('\t');
		    break;
		case 'n':
		    out.write('\n');
		    break;
		case 'b':
		    out.write('\b');
		    break;
		case 'u': {
		    // uh-oh, we're in unicode country....
		    inUnicode = true;
		    break;
		}
		default:
		    out.write(ch);
		    break;
		}
		continue;
	    } else if (ch == '\\') {
		hadSlash = true;
		continue;
	    }
	    out.write(ch);
	}
	if (hadSlash) {
	    // then we're in the weird case of a \ at the end of the
	    // string, let's output it anyway.
	    out.write('\\');
	}
    }

    /**
     * <p>
     * Returns an upper case hexadecimal <code>String</code> for the given character.
     * </p>
     * 
     * @param ch
     *            The character to convert.
     * @return An upper case hexadecimal <code>String</code>
     */
    private static String hex(char ch) {
	return Integer.toHexString(ch).toUpperCase();
    }

    public static String escapeString(String input, String outputCharsToBeEscaped, String outputEscapeChar) {

	StringBuffer in = new StringBuffer(input);
	for (int i = 0; i < outputCharsToBeEscaped.length(); i++) {
	    char c = outputCharsToBeEscaped.charAt(i);
	    StringBuffer out = new StringBuffer();
	    replace(in, c, outputEscapeChar + c, out);
	    in = out;
	}
	return in.toString();
    }

    public static void replace(StringBuffer input, char what, String with, StringBuffer sb) {
	for (int i = 0; i < input.length(); i++) {
	    char c = input.charAt(i);
	    if (c == what) {
		sb.append(with);
	    } else {
		sb.append(c);
	    }
	}
    }

    public static String escapeHtml(String input) {
	StringBuffer sb = new StringBuffer();
	for (int i = 0; i < input.length(); i++) {
	    char c = input.charAt(i);
	    switch (c) {

	    case '<':
		sb.append("&lt;");
		break;
	    case '>':
		sb.append("&gt;");
		break;
	    case '&':
		sb.append("&amp;");
		break;
	    default:
		sb.append(c);
		break;
	    }
	}
	return sb.toString();
    }

    public static String unescapeHtml(String input) {
	try {
	    ParseResponse result = unescapeHtmlParser.parse(input);
	    StringBuffer resp = new StringBuffer();
	    List<Object> objectsList = result.getObjectsList();
	    for (Object o : objectsList) {
		resp.append(o.toString());
	    }

	    return resp.toString();
	} catch (ParserException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	    return null;
	}
    }

    public static String sanitizeHtml(String input) {
	if (input == null) {
	    return null;
	}
	try {
	    ParseResponse result = htmlSanitizer.parse(input);
	    StringBuffer resp = new StringBuffer();
	    List<Object> objectsList = result.getObjectsList();
	    for (Object o : objectsList) {
		resp.append(o.toString());
	    }

	    return resp.toString();
	} catch (ParserException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	    return null;
	}
    }

    public static void main(String[] args) {
	// System.out.println("end="+unescapeHtml("&lt;script&gt;alert(\"hopa\")&lt;/script&gt;\""));
	System.out.println(sanitizeHtml("<html>\n" + "deci : <script	c >alert(\"hopa\")</script>hhfd\n" + "</html>"));
    }

}
