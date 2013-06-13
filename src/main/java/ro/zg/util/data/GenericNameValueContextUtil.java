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
package ro.zg.util.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.Map;

import ro.zg.commons.exceptions.ContextAwareException;
import ro.zg.commons.exceptions.ExceptionContext;
import ro.zg.util.parser.Parser;
import ro.zg.util.parser.ParserHandlerFactory;
import ro.zg.util.parser.Symbol;
import ro.zg.util.parser.SymbolFlag;
import ro.zg.util.parser.SymbolType;
import ro.zg.util.parser.event.AssociationEvent;
import ro.zg.util.parser.event.GroupEvent;
import ro.zg.util.parser.event.ParseEventHandler;

public class GenericNameValueContextUtil {
    private static Parser parser;

    static {
	ContextParserHandler handler = new ContextParserHandler();
	parser = new Parser();
	parser.setHandlerFactory(new ParserHandlerFactory(handler));

	Symbol commaSymbol = new Symbol(",", SymbolType.SEPARATE);

	Symbol listSymbol = new Symbol("[", SymbolType.GROUP_START);
	listSymbol.addNestedSymbol(new Symbol("]", SymbolType.GROUP_END));
	listSymbol.addNestedSymbol(listSymbol);
	listSymbol.addNestedSymbol(commaSymbol);
	listSymbol.addFlag(SymbolFlag.IGNORE_EMPTY);

	Symbol mapSymbol = new Symbol("{", SymbolType.GROUP_START);
	mapSymbol.addNestedSymbol(new Symbol("}", SymbolType.GROUP_END));
	mapSymbol.addNestedSymbol(commaSymbol);
	mapSymbol.addNestedSymbol(new Symbol("=", SymbolType.ASSOCIATE));
	mapSymbol.addNestedSymbol(mapSymbol);
	mapSymbol.addFlag(SymbolFlag.IGNORE_EMPTY);

	mapSymbol.addNestedSymbol(listSymbol);
	listSymbol.addNestedSymbol(mapSymbol);

	parser.addSymbol(listSymbol);
	parser.addSymbol(mapSymbol);
	parser.setUseEscapeCharacterOn(true);
    }

    public static GenericNameValueContext parse(String input) {
	try {
	    Deque<Object> objects = parser.parse(input).getObjects();
	    if (objects.size() > 0) {
		return (GenericNameValueContext) objects.pop();
	    } else {
		// String in = input.trim();
		// GenericNameValueList c = new GenericNameValueList();
		// if (!"".equals(in)) {
		// c.addValue(in);
		// return c;
		// }
		// return c;
		return new GenericNameValueContext();
	    }
	} catch (Exception e) {
	    throw new RuntimeException(e);
	}
    }

    /**
     * Basically this will convert lists and maps to {@link GenericNameValueContext} and will leave other types of
     * objects alone
     * 
     * @param target
     * @return
     * @throws ContextAwareException
     */
    public static Object convertToKnownType(Object target, boolean parseString) throws ContextAwareException {
	if (target instanceof List<?>) {
	    return listToContext((List<?>) target);
	} else if (target instanceof Map<?, ?>) {
	    return mapToContext((Map<?, ?>) target);
	} else if (target instanceof GenericNameValue) {
	    GenericNameValueContext mapContext = new GenericNameValueContext();
	    mapContext.put((GenericNameValue) target);
	    return mapContext;
	} else if (target instanceof Object[]) {
	    return listToContext(Arrays.asList((Object[]) target));
	} else if (target instanceof String) {
	    if (parseString) {
		GenericNameValueContext c = parse(target.toString());
		if (c.size() > 0) {
		    return c;
		}
	    }
	    else {
		return target;
	    }
	}
	return target;
    }

    public static Object convertToKnownType(Object target) throws ContextAwareException {
	return convertToKnownType(target,true);
    }

    public static GenericNameValueContext listToContext(List list) throws ContextAwareException {

	GenericNameValueList context = new GenericNameValueList();
	if (list != null && list.size() > 0) {
	    /*
	     * check to see the content of the list, if the elements are of type GenericNameValue then return a
	     * GenericNameValueContext not a GenericNameValueList
	     */
	    if (list.get(0) instanceof GenericNameValue) {
		GenericNameValueContext mapContext = new GenericNameValueContext();
		try {
		    mapContext.putAll(list);
		    return mapContext;
		} catch (ClassCastException e) {
		    /* do nothing just fallback to the default */
		} catch (Exception e) {

		    ExceptionContext ec = new ExceptionContext();
		    ec.put("list", list);
		    throw new ContextAwareException("CONVERSION_ERROR", e, ec);
		}
	    }
	    for (Object o : list) {
		context.addValue(convertToKnownType(o,false));
	    }
	}
	return context;
    }

    public static GenericNameValueContext mapToContext(Map<?, ?> map) throws ContextAwareException {
	GenericNameValueContext context = new GenericNameValueContext();
	for (Object key : map.keySet()) {
	    context.put(key.toString(), convertToKnownType(map.get(key),false));
	}
	return context;
    }

    public static Object getValueFromContext(GenericNameValueContext context, String param) {
	List<String> list = new ArrayList(Arrays.asList(param.split("\\.")));
	return getValueFromContext(context, list);
    }

    public static Object getValueFromContext(GenericNameValueContext context, List<String> names) {
	if (context == null) {
	    System.out.println("fuck");
	}

	String currentName = names.remove(0);
	Object value = context.getValue(currentName);
	if (names.size() == 0) {
	    return value;
	} else {
	    return getValueFromContext((GenericNameValueContext) value, names);
	}
    }

    public static Object convertToType(Object source, String type) {
	if (source instanceof GenericNameValueList && type.equals("java.util.List")) {
	    return ((GenericNameValueList) source).getValues();
	}
	return source;
    }

    public static void main(String[] args) {
	String s = "[[rss, version String \"2.0\", xmlns:content String \"http://purl.org/rss/1.0/modules/content/\", xmlns:wfw String \"http://wellformedweb.org/CommentAPI/\", xmlns:dc String \"http://purl.org/dc/elements/1.1/\", xmlns:atom String \"http://www.w3.org/2005/Atom\", xmlns:sy String \"http://purl.org/rss/1.0/modules/syndication/\", xmlns:slash String \"http://purl.org/rss/1.0/modules/slash/\", [channel, [title, Ars Technica], [atom:link, href String \"http://arstechnica.com/feed/\", rel String \"self\", type String \"application/rss+xml\"], [link, http://arstechnica.com], [description, The Art of Technology], [lastBuildDate, Fri, 22 Mar 2013 19:48:54 +0000], [language, en-US], [sy:updatePeriod, hourly], [sy:updateFrequency, 1], [generator, http://wordpress.org/?v=3.5.1], [item, [title, Android app roundup: Google&#8217;s had a very busy week], [link, http://arstechnica.com/gadgets/2013/03/this-week-in-android-apps-googles-had-a-very-busy-month/], [comments, http://arstechnica.com/gadgets/2013/03/this-week-in-android-apps-googles-had-a-very-busy-month/#comments], [pubDate, Fri, 22 Mar 2013 19:45:26 +0000], [dc:creator, Florence Ion], [category, Gear & Gadgets], [category, app roundup], [category, apps], [category, Coupons], [category, currents], [category, Evernote], [category, google play], [category, keep], [category, neatly], [category, snipsnap], [category, twitter clients], [guid, isPermaLink String \"false\", http://arstechnica.com/?p=231317], [description, And so have Neatly and SnipSnap, for that matter. ], [content:encoded, <div id=\"rss-wrap\"> <p>It’s been a busy week in the Android world, especially for Google. But they’re not the only ones debuting and updating apps in the Google Play store this week. Remember <a href=\"http://arstechnica.com/gadgets/2013/02/android-app-party-get-the-ubuntu-phone-os-sidebar-on-android/\">Neatly</a> from an app roundup last month? It’s now in “final beta” and available for download on the Google Play store (it was previously a side-loaded-only application). Coupon clippers will also be happy to know that SnipSnap has finally made its way over to Android from iOS.</p>\n"
		+ "<p><strong>Google Keep, <a href=\"https://play.google.com/store/apps/details?id=com.google.android.keep&amp;feature=search_result#?t=W251bGwsMSwxLDEsImNvbS5nb29nbGUuYW5kcm9pZC5rZWVwIl0.\">Free</a></strong></p>\n"
		+ "<p></p>\n"
		+ "</div><p><a href=\"http://arstechnica.com/gadgets/2013/03/this-week-in-android-apps-googles-had-a-very-busy-month/#p3n\">Read 10 remaining paragraphs</a> | <a href=\"http://arstechnica.com/gadgets/2013/03/this-week-in-android-apps-googles-had-a-very-busy-month/?comments=1\">Comments</a></p>], [wfw:commentRss, http://arstechnica.com/gadgets/2013/03/this-week-in-android-apps-googles-had-a-very-busy-month/feed/], [slash:comments, 0]], [item, [title, Bug in EA&#8217;s Battlefield Play4Free allows attackers to hijack players&#8217; PCs], [link, http://arstechnica.com/security/2013/03/bug-in-eas-battlefield-play4free-allows-attackers-to-hijack-players-pcs/], [comments, http://arstechnica.com/security/2013/03/bug-in-eas-battlefield-play4free-allows-attackers-to-hijack-players-pcs/#comments], [pubDate, Fri, 22 Mar 2013 19:00:35 +0000], [dc:creator, Dan Goodin], [category, Opposable Thumbs], [category, Risk Assessment], [category, Technology Lab], [category, computer security], [category, malware], [category, PC games], [guid, isPermaLink String \"false\", http://arstechnica.com/?p=231339], [description, Remote-code exploit works against PCs running older versions of Windows.], [content:encoded, <div id=\"rss-wrap\">\n"
		+ "<div>\n"
		+ "      <img src=\"http://cdn.arstechnica.net/wp-content/uploads/2013/03/battlefield_p4f_exploit-640x404.jpg\"><div class=\"caption\" style=\"font-size:0.8em\">\n"
		+ "			<div class=\"caption-text\">A frame from a video demonstrating an attack that allows attackers to execute malicious code on older Windows systems that have Play4Free installed.</div>\n"
		+ "	\n"
		+ "			<div class=\"caption-byline\">\n"
		+ "							<a href=\"http://vimeo.com/61364094\">ReVuln</a>\n"
		+ "				</div>\n"
		+ "	  </div>\n"
		+ "  </div>\n"
		+ " <p>If you play EA's popular <em>Battlefield Play4Free</em> game on an older version of Windows, a pair of researchers say they can hijack your system by luring you to a booby-trapped website.</p>\n"
		+ "<p>The proof-of-concept exploit, <a href=\"https://www.blackhat.com/eu-13/briefings.html#Ferrante\">demonstrated last week</a> at the Black Hat security conference in Amsterdam, allows attackers to surreptitiously execute malicious code on default systems running Windows XP or Windows 2003 that have the <em>Play4Free</em> title installed. There are <a href=\"http://battlefield.play4free.com/en/forum/showthread.php?tid=115716\">close to 1 million players</a> of the first-person shooter game, and about <a href=\"https://en.wikipedia.org/wiki/Microsoft_Windows#Usage_share\">39 percent of Windows users are still on XP</a>.</p>\n"
		+ "<p>The webpage used in the exploit opens the game on a victim's computer and instructs it to load a malicious \"MOD\" file used to customize game settings and features, according to a <a href=\"http://revuln.com/files/ReVuln_Battlefield_play4free.pdf\">document the researchers published Friday</a>. Using some nonstandard behavior of a programming interface version found only in older versions of Windows, the MOD file is able to upload a malicious <a href=\"https://en.wikipedia.org/wiki/Batch_file\">batch file</a> that will be executed the next time the computer is restarted. The technique is successful because it overrides a whitelist that's supposed to restrict the sites that are permitted to load the <em>Play4Free</em> game.</p>\n"
		+ "</div><p><a href=\"http://arstechnica.com/security/2013/03/bug-in-eas-battlefield-play4free-allows-attackers-to-hijack-players-pcs/#p3n\">Read 4 remaining paragraphs</a> | <a href=\"http://arstechnica.com/security/2013/03/bug-in-eas-battlefield-play4free-allows-attackers-to-hijack-players-pcs/?comments=1\">Comments</a></p>], [wfw:commentRss, http://arstechnica.com/security/2013/03/bug-in-eas-battlefield-play4free-allows-attackers-to-hijack-players-pcs/feed/], [slash:comments, 0]], [item, [title, &#8220;Stop the Cyborgs&#8221; launches public campaign against Google Glass], [link, http://arstechnica.com/tech-policy/2013/03/stop-the-cyborgs-launches-public-campaign-against-google-glass/], [comments, http://arstechnica.com/tech-policy/2013/03/stop-the-cyborgs-launches-public-campaign-against-google-glass/#comments], [pubDate, Fri, 22 Mar 2013 18:50:28 +0000], [dc:creator, Cyrus Farivar], [category, Gear & Gadgets], [category, Law & Disorder], [category, adam], [category, Google Glass], [category, privacy], [category, stop the cyborgs], [guid, isPermaLink String \"false\", http://arstechnica.com/?p=231267], [description, \"It destroys having multiple identities, and I find that quite a scary concept.\"], [content:encoded, <div id=\"rss-wrap\">\n"
		+ "<div>\n"
		+ "      <img src=\"http://cdn.arstechnica.net/wp-content/uploads/2013/03/8500184427_ce0e3d0afe_o.jpg\"><div class=\"caption\" style=\"font-size:0.8em\">\n"
		+ "			<div class=\"caption-text\">Sharon Ron-McKellar, whose husband (Ian McKellar) works on Google Glass, was seen wearing these at an event in Oakland in February 2013.</div>\n"
		+ "	\n"
		+ "			<div class=\"caption-byline\">\n"
		+ "							<a href=\"https://secure.flickr.com/photos/dearanxiety/8500184427\">Sharon Ron-McKellar</a>\n"
		+ "				</div>\n"
		+ "	  </div>\n"
		+ "  </div>\n"
		+ " <p dir=\"ltr\" id=\"internal-source-marker_0.8933989481832836\">Less than two weeks ago, Seattle’s <a href=\"http://the5pointcafe.com/\">5 Point Cafe</a> became the <a href=\"http://arstechnica.com/gadgets/2013/03/seattle-bar-bans-google-glass-still-loves-beer-goggles/\">first known establishment</a> in the United States (and possibly the world) to publicly ban <a href=\"http://google.com/glass\">Google Glass</a>, the highly anticipated <a href=\"http://arstechnica.com/gadgets/2013/02/google-glass-will-speak-to-you-by-vibrating-the-bones-in-your-head/\">augmented reality device</a> set to be released later this year.</p>\n"
		+ "<p dir=\"ltr\">The “No Glass” logo that the café published on its website was developed and released (under a Creative Commons license) by a new London-based group called “<a href=\"http://www.stopthecyborgs.org\">Stop the Cyborgs</a>.” The group is composed of three young Londoners who decided to make a public case against Google Glass and other similar devices.</p>\n"
		+ "<p dir=\"ltr\">“If it's just a few geeks wearing it, it's a niche tool \\[and] I don't think it's a problem,” said Adam, 27, who prefers only to be identified by his first name. He communicated with Ars via Skype and an encrypted Hushmail e-mail account.</p>\n"
		+ "</div><p><a href=\"http://arstechnica.com/tech-policy/2013/03/stop-the-cyborgs-launches-public-campaign-against-google-glass/#p3n\">Read 14 remaining paragraphs</a> | <a href=\"http://arstechnica.com/tech-policy/2013/03/stop-the-cyborgs-launches-public-campaign-against-google-glass/?comments=1\">Comments</a></p>], [wfw:commentRss, http://arstechnica.com/tech-policy/2013/03/stop-the-cyborgs-launches-public-campaign-against-google-glass/feed/], [slash:comments, 0]], [item, [title, So long, Julius: FCC chair lauded by industry, blasted by activists], [link, http://arstechnica.com/tech-policy/2013/03/so-long-julius-fcc-chair-lauded-by-industry-blasted-by-activists/], [comments, http://arstechnica.com/tech-policy/2013/03/so-long-julius-fcc-chair-lauded-by-industry-blasted-by-activists/#comments], [pubDate, Fri, 22 Mar 2013 18:26:59 +0000], [dc:creator, Jon Brodkin], [category, Law & Disorder], [category, Ministry of Innovation], [category, broadband competition], [category, FCC], [category, julius genachowksi], [category, Net Neutrality], [guid, isPermaLink String \"false\", http://arstechnica.com/?p=231205], [description, Genachowski let corporate interests override public ones, nonprofit groups say.], [content:encoded, <div id=\"rss-wrap\">\n"
		+ "<div>\n"
		+ "      <img src=\"http://cdn.arstechnica.net/wp-content/uploads/2013/03/julius-genachowski.jpg\"><div class=\"caption\" style=\"font-size:0.8em\">\n"
		+ "			<div class=\"caption-text\">FCC Chairman Julius Genachowski in November 2010 at the Web 2.0 Summit in San Francisco.</div>\n"
		+ "	\n"
		+ "			<div class=\"caption-byline\">\n"
		+ "							<a href=\"http://www.flickr.com/photos/jdlasica/5186915606/\">JD Lasica</a>\n"
		+ "				</div>\n"
		+ "	  </div>\n"
		+ "  </div>\n"
		+ " <p>Federal Communications Commission Chairman Julius Genachowski <a href=\"http://arstechnica.com/tech-policy/2013/03/fcc-chairman-julius-genachowski-expected-to-announce-resignation-tomorrow/%20\">today said</a> he will leave the agency in the coming weeks, four years after being appointed by President Obama. At an all-hands meeting of FCC staff, Genachowski listed all his accomplishments, demonstrating how proud he is of his record:</p>\n"
		+ "<blockquote>\n"
		+ "<p>Over the past four years, we’ve focused the FCC on broadband, wired and wireless, working to drive economic growth and improve the lives of all Americans.</p>\n"
		+ "<p>And thanks to you, the Commission’s employees, we’ve taken big steps to build a future where broadband is ubiquitous and bandwidth is abundant, where innovation and investment are flourishing.</p>\n"
		+ "<p>To connect all Americans to high-speed Internet, we adopted a landmark overhaul of multi-billion dollar universal service programs, modernizing them from telephone to broadband and creating the Connect America Fund and the Mobility Fund, an unprecedented investment in broadband infrastructure.</p>\n"
		+ "<p>To unleash the enormous opportunities of mobile, we pioneered incentive auctions and other cutting-edge spectrum policies. To fuel America’s innovation economy, we put in place the first rules to preserve Internet freedom and openness.</p>\n"
		+ "<p>To drive competition and empower consumers, we opposed and modified transactions where necessary, deployed technology to drive transparency, and took unprecedented enforcement actions.</p>\n"
		+ "</blockquote>\n"
		+ "<p>It's true that Genachowski occasionally annoyed corporate interests, presiding over the <a href=\"http://arstechnica.com/tech-policy/2011/12/att-admits-defeat-on-t-mobile-takeover-will-pay-4-billion-breakup-fee/\">scuttling</a> of AT&amp;T's attempted takeover of T-Mobile. Genachowski's FCC also <a href=\"http://arstechnica.com/tech-policy/2012/02/why-lightsquared-failed/\">blocked</a> LightSquared's proposed cellular network, which would have led to interference with GPS devices (despite pressure from LightSquared's <a href=\"http://arstechnica.com/tech-policy/2012/09/lightsquared-redux-fcc-blamed-by-lawmakers-who-lack-tech-expertise/\">friends in Congress</a>). Angry at the FCC for asserting its authority, the <a href=\"http://arstechnica.com/tech-policy/2012/03/house-votes-to-limit-fccs-power-to-make-rules-set-conditions-on-mergers/\">Republican-controlled House of Representatives</a> tried to limit the FCC's power to make rules and set conditions on mergers.</p>\n"
		+ "<p>But according to nonprofit public interest groups, Genachowski's tenure was characterized more by allowing corporate interests to override public ones.</p>\n"
		+ "</div><p><a href=\"http://arstechnica.com/tech-policy/2013/03/so-long-julius-fcc-chair-lauded-by-industry-blasted-by-activists/#p3n\">Read 15 remaining paragraphs</a> | <a href=\"http://arstechnica.com/tech-policy/2013/03/so-long-julius-fcc-chair-lauded-by-industry-blasted-by-activists/?comments=1\">Comments</a></p>], [wfw:commentRss, http://arstechnica.com/tech-policy/2013/03/so-long-julius-fcc-chair-lauded-by-industry-blasted-by-activists/feed/], [slash:comments, 0]], [item, [title, Capcom reviving classic NES DuckTales with modern HD remake], [link, http://arstechnica.com/gaming/2013/03/capcom-reviving-classic-nes-ducktales-with-modern-hd-remake/], [comments, http://arstechnica.com/gaming/2013/03/capcom-reviving-classic-nes-ducktales-with-modern-hd-remake/#comments], [pubDate, Fri, 22 Mar 2013 17:37:45 +0000], [dc:creator, Kyle Orland], [category, Opposable Thumbs], [category, capcom], [category, Disney], [category, ducktales], [category, hd remake], [category, WayForward], [guid, isPermaLink String \"false\", http://arstechnica.com/?p=231265], [description, Redrawn graphics, 3D backgrounds, and a chance to \"jump in the money bin.\"], [content:encoded, <div id=\"rss-wrap\"> <p>Capcom's first ever PAX East panel had been droning on for 50 minutes with unexciting announcements like new Mega Man merchandise, trailers for well-known games, and the reveal of America's favorite Street Fighter character (it was Ryu) before the announcement that sent the gathered crowd of hundreds into a sing-along frenzy:</p>\n"
		+ "<blockquote><p><i>Life is like a hurricane<br>\n"
		+ "Here in Duckburg…</i></p></blockquote>\n"
		+ "<p>Yes, Capcom is finally bringing back the classic NES-era <i>DuckTales</i> in an HD remake for modern consoles, with Disney's blessing. The conversion is being handled by WayForward, best known at the moment for its <i>Double Dragon Neon</i> and the Nintendo DS <i>Adventure Time</i> game. All the graphics are being redrawn in HD, with 3D backgrounds drawn by one of the original <i>DuckTales</i> cartoon artists. Overall, the trailer evoked a visual feel similar to that of the <i>Paper Mario</i> games. The classic soundtrack is being remastered, too, by WayForward's Jay Kaufman.</p>\n"
		+ "<p>The game is based heavily on the NES classic—about 70 percent of the content will be exactly the same, while 30 percent will be slightly re-tuned, a producer said. The similarities could actually be considered a feature rather than a problem, though, considering that the original game was <a href=\"http://arstechnica.com/gaming/2011/04/masterpiece-duck-tales-on-the-nes/\">a bona fide masterpiece created by some of Capcom's best talent</a>. There will be a few new side features, too, such as a tutorial stage, cutscene introductions for characters, a museum, and a section that lets you do the \"classic jump into the money bin,\" which is apparently very popular in play testing.</p>\n"
		+ "</div><p><a href=\"http://arstechnica.com/gaming/2013/03/capcom-reviving-classic-nes-ducktales-with-modern-hd-remake/#p3n\">Read 1 remaining paragraphs</a> | <a href=\"http://arstechnica.com/gaming/2013/03/capcom-reviving-classic-nes-ducktales-with-modern-hd-remake/?comments=1\">Comments</a></p>], [wfw:commentRss, http://arstechnica.com/gaming/2013/03/capcom-reviving-classic-nes-ducktales-with-modern-hd-remake/feed/], [slash:comments, 0]], [item, [title, Patent shows Google is, or was, thinking about smart watches], [link, http://arstechnica.com/gadgets/2013/03/patent-shows-google-is-or-was-thinking-about-smart-watches/], [comments, http://arstechnica.com/gadgets/2013/03/patent-shows-google-is-or-was-thinking-about-smart-watches/#comments], [pubDate, Fri, 22 Mar 2013 16:54:25 +0000], [dc:creator, Casey Johnston], [category, Gear & Gadgets], [category, android], [category, apple], [category, Google Glass], [category, iWatch], [category, Samsung], [category, smart watch], [category, wearable computing], [guid, isPermaLink String \"false\", http://arstechnica.com/?p=231253], [description, A flip-up display and augmented reality are proposed features.], [content:encoded, <div id=\"rss-wrap\"> <p><span style=\"font-size: 14px; line-height: 19px;\">According to the <em><a href=\"http://blogs.ft.com/tech-blog/2013/03/google-smart-watch/\">Financial Times</a>, </em>Google may be working on yet another wearable computer in addition to Google Glass: a smart watch similar to the ones Samsung and Apple apparently have in the works</span><span style=\"font-size: 14px; line-height: 19px;\">. A </span><a style=\"font-size: 14px; line-height: 19px;\" href=\"http://patft.uspto.gov/netacgi/nph-Parser?Sect1=PTO2&amp;Sect2=HITOFF&amp;u=/netahtml/PTO/search-adv.htm&amp;r=23&amp;p=1&amp;f=G&amp;l=50&amp;d=PTXT&amp;S1=(20121002.PD.%20AND%20Google.ASNM.)&amp;OS=ISD/20121002%20AND%20AN/Google&amp;RS=(ISD/20121002%20AND%20AN/Google)\">patent application</a><span style=\"font-size: 14px; line-height: 19px;\"> filed by Google in 2011 describes a watch with a “flip up portion” that includes a top display when open that acts as a supplement to the base of the watch, which presumably also includes a screen.</span></p>\n"
		+ "<p>In addition to the flip-up portion, the watch would also include a touchscreen display and a camera as well as the typical mobile device drivers like a processor and “wireless transceiver.” Google makes specific note that the flip-up display would be concealed when the watch is closed.</p>\n"
		+ "<p>The watch would be able to display e-mail messages, geographical location, and direction information, and it sounds like the camera would be able to effect some kind of augmented reality: “the processor is configured to activate an image retrieval system that generates information related to an image captured by the camera when the flip up portion is in the open position.”</p>\n"
		+ "</div><p><a href=\"http://arstechnica.com/gadgets/2013/03/patent-shows-google-is-or-was-thinking-about-smart-watches/#p3n\">Read 1 remaining paragraphs</a> | <a href=\"http://arstechnica.com/gadgets/2013/03/patent-shows-google-is-or-was-thinking-about-smart-watches/?comments=1\">Comments</a></p>], [wfw:commentRss, http://arstechnica.com/gadgets/2013/03/patent-shows-google-is-or-was-thinking-about-smart-watches/feed/], [slash:comments, 0]], [item, [title, Action-packed video games may help dyslexic kids learn to read], [link, http://arstechnica.com/science/2013/03/action-packed-video-games-may-help-dyslexic-kids-learn-to-read/], [comments, http://arstechnica.com/science/2013/03/action-packed-video-games-may-help-dyslexic-kids-learn-to-read/#comments], [pubDate, Fri, 22 Mar 2013 16:50:35 +0000], [dc:creator, Kate Shaw], [category, Opposable Thumbs], [category, Scientific Method], [category, behavior], [category, Biology], [category, brain], [category, dyslexia], [category, video games], [guid, isPermaLink String \"false\", http://arstechnica.com/?p=230019], [description, Nine days on the Wii equalled a year of traditional therapy.], [content:encoded, <div id=\"rss-wrap\">\n"
		+ "<div>\n"
		+ "      <img src=\"http://cdn.arstechnica.net/wp-content/uploads/2013/03/6605818845_da3b224a3f_b-640x425.jpg\"><div class=\"caption\" style=\"font-size:0.8em\">\n"
		+ "	\n"
		+ "			<div class=\"caption-byline\">\n"
		+ "							<a href=\"http://www.flickr.com/photos/kingf/6605818845/sizes/l/in/photostream/\">Flickr user: king.f</a>\n"
		+ "				</div>\n"
		+ "	  </div>\n"
		+ "  </div>\n"
		+ " <p>For children with dyslexia, learning to read can be a nightmare: to them, it's a jumble of words, letters, and sounds that is impossible to make sense of. Studies show that dyslexia is a disorder of the brain (rather than of the visual system), but since scientists still don’t know the root cause, there’s no simple way to combat the disorder. Traditional treatments and therapies for the dyslexia are time-consuming, expensive, and don’t necessarily bring huge improvements.</p>\n"
		+ "<p>One of the hallmarks of dyslexia is what researchers call \"attentional dysfunction;\" this deficit makes it hard for dyslexics to focus their attention and pick out important information in a cluttered environment. To attack this deficit head-on, a group of Italian researchers wondered whether children with dyslexia would benefit from intense immersion in an activity that forced them to practice these skills. Specifically, would playing active video games help dyslexic kids learn to focus their attention, making it easier for them to learn to read?</p>\n"
		+ "<p>The answer was a resounding yes, according to the research detailed in <em>Current Biology </em>this week.</p>\n"
		+ "</div><p><a href=\"http://arstechnica.com/science/2013/03/action-packed-video-games-may-help-dyslexic-kids-learn-to-read/#p3n\">Read 7 remaining paragraphs</a> | <a href=\"http://arstechnica.com/science/2013/03/action-packed-video-games-may-help-dyslexic-kids-learn-to-read/?comments=1\">Comments</a></p>], [wfw:commentRss, http://arstechnica.com/science/2013/03/action-packed-video-games-may-help-dyslexic-kids-learn-to-read/feed/], [slash:comments, 0]], [item, [title, Smaller and more focused: the Nvidia GPU Technology Conference in images], [link, http://arstechnica.com/gadgets/2013/03/smaller-and-more-focused-the-nvidia-gpu-technology-conference-in-images/], [comments, http://arstechnica.com/gadgets/2013/03/smaller-and-more-focused-the-nvidia-gpu-technology-conference-in-images/#comments], [pubDate, Fri, 22 Mar 2013 16:20:55 +0000], [dc:creator, Andrew Cunningham], [category, Gear & Gadgets], [category, Opposable Thumbs], [category, gallery], [guid, isPermaLink String \"false\", http://arstechnica.com/?p=230945], [description, GTC is far removed from the madness that is CES and Mobile World Congress.], [content:encoded, <div id=\"rss-wrap\"> <p>SAN JOSE, CA—Nvidia's GPU Technology Conference is a relatively small show. Sure, its meeting rooms and exhibition halls span most of the San Jose Convention Center and run over into some of the adjoining hotels. There were also plenty of buzzworthy names in attendance—gaming darlings like Valve, <a href=\"http://arstechnica.com/gaming/2012/09/virtual-realitys-time-to-shine-hands-on-with-the-oculus-rift/\">Oculus Rift</a>, and <a href=\"http://arstechnica.com/gaming/2013/03/ouyas-founder-if-we-dont-nail-this-nothing-else-really-matters/\">Ouya</a> were all there, as were plenty of heavyweights in the professional hardware and software businesses.</p>\n"
		+ "<p>Nonetheless, the show spent most of its time focused on one company, and so by necessity it was smaller and less sprawling than either CES or Mobile World Congress. While there was a bit less to see, smaller shows like this one do afford the opportunity to cover subjects in a little more depth—in our case, Nvidia's <a href=\"http://arstechnica.com/gadgets/2013/03/nvidias-next-tegra-chips-will-get-a-big-boost-from-new-geforce-gpus/\">GeForce and Tegra product roadmaps</a>, its <a href=\"http://arstechnica.com/gadgets/2013/03/from-smartphone-to-server-room-nvidias-kayla-shows-the-future-of-tegra/\">\"Kayla\" motherboard</a>, and its <a href=\"http://arstechnica.com/information-technology/2013/03/nvidia-plans-to-turn-ultrabooks-into-workstations-with-grid-vca-server/\">ongoing efforts</a> to get graphics up into the cloud. To give you a taste of all that and more, check out our photo tour below.</p>\n"
		+ "  <div class=\"gallery\">\n"
		+ "    <div class=\"gallery-main-image\" style=\"width:auto\">\n"
		+ "      <div class=\"gallery-image-container\" style=\"height:auto\">\n"
		+ "        <div class=\"gallery-image-wrap\">\n"
		+ "        <a class=\"enlarge\" href=\"http://cdn.arstechnica.net/wp-content/uploads/2013/03/011.jpg\" data-width=\"1280\" data-height=\"853\">\n"
		+ "          <img src=\"http://cdn.arstechnica.net/wp-content/uploads/2013/03/011-980x653.jpg\" style=\"max-width:auto;max-height:auto\"></a>\n"
		+ "        </div>\n"
		+ "      </div>\n"
		+ "    </div>\n"
		+ "\n"
		+ "        <div class=\"gallery-image-description\">\n"
		+ "\n"
		+ "            <h2>Ignore our mess</h2>\n"
		+ "                <p>GTC was held in the San Jose Convention Center, made slightly less picturesque by ongoing construction on its massive extension.</p>\n"
		+ "      \n"
		+ "          <p class=\"gallery-image-credit\">\n"
		+ "              <em>Andrew Cunningham</em>\n"
		+ "            </p>\n"
		+ "    \n"
		+ "    </div>\n"
		+ "    \n"
		+ "       \n"
		+ "          <p><strong>14 more images in gallery</strong></p>\n"
		+ "      </div>\n"
		+ "    <style type=\"text/css\"> .related-stories \\{ display: none !important; } </style>\n"
		+ "</div><p><a href=\"http://arstechnica.com/gadgets/2013/03/smaller-and-more-focused-the-nvidia-gpu-technology-conference-in-images/\">Read on Ars Technica</a> | <a href=\"http://arstechnica.com/gadgets/2013/03/smaller-and-more-focused-the-nvidia-gpu-technology-conference-in-images/?comments=1\">Comments</a></p>], [wfw:commentRss, http://arstechnica.com/gadgets/2013/03/smaller-and-more-focused-the-nvidia-gpu-technology-conference-in-images/feed/], [slash:comments, 0]], [item, [title, Vintage videos show Woz speaking on &#8220;computer abuse,&#8221; Steve Jobs, and more], [link, http://arstechnica.com/apple/2013/03/vintage-videos-show-woz-speaking-on-computer-abuse-steve-jobs-and-more/], [comments, http://arstechnica.com/apple/2013/03/vintage-videos-show-woz-speaking-on-computer-abuse-steve-jobs-and-more/#comments], [pubDate, Fri, 22 Mar 2013 15:30:52 +0000], [dc:creator, Jacqui Cheng], [category, Infinite Loop], [category, 1984], [category, apple], [category, Steve Jobs], [category, Steve Wozniak], [category, video], [category, Woz], [category, YouTube], [guid, isPermaLink String \"false\", http://arstechnica.com/?p=231141], [description, The videos come from a 1984 VHS tape and were recently uploaded to YouTube.], [content:encoded, <div id=\"rss-wrap\"> <p>Tales from the olden days of Apple have always been popular among the geek crowd, especially when they come from people like Steve Wozniak. That's why a series of <a href=\"http://www.youtube.com/user/PDXtvVince?feature=watch\">rare, vintage Woz videos</a> uploaded to YouTube early Friday has sparked some interest. YouTube user Vince Patton told <a href=\"http://appleinsider.com/articles/13/03/22/apple-cofounder-steve-wozniak-talks-apple-i-jobs-and-more-in-video-from-1984\">AppleInsider</a> that the videos came from a VHS recording of Woz speaking at the Denver Apple Pi club on October 4, 1984, with Woz covering an entire range of topics. There's also a bonus appearance from Apple's sixth employee, Randy Wigginton.</p>\n"
		+ "<p>Below are a handful of the videos uploaded by Patton (check out the rest on Patton's YouTube page):</p>\n"
		+ "<p>Woz on being put on probation for \"computer abuse\":</p>\n"
		+ "</div><p><a href=\"http://arstechnica.com/apple/2013/03/vintage-videos-show-woz-speaking-on-computer-abuse-steve-jobs-and-more/#p3n\">Read 5 remaining paragraphs</a> | <a href=\"http://arstechnica.com/apple/2013/03/vintage-videos-show-woz-speaking-on-computer-abuse-steve-jobs-and-more/?comments=1\">Comments</a></p>], [wfw:commentRss, http://arstechnica.com/apple/2013/03/vintage-videos-show-woz-speaking-on-computer-abuse-steve-jobs-and-more/feed/], [slash:comments, 0]], [item, [title, Doctors track stem cells with nanoparticles during cardiac therapy], [link, http://arstechnica.com/science/2013/03/doctors-track-stem-cells-with-nanoparticles-during-cardiac-therapy/], [comments, http://arstechnica.com/science/2013/03/doctors-track-stem-cells-with-nanoparticles-during-cardiac-therapy/#comments], [pubDate, Fri, 22 Mar 2013 15:25:17 +0000], [dc:creator, Ars Staff], [category, Scientific Method], [category, Biology], [category, medicine], [category, nanoparticles], [category, nanotechnology], [category, stem cells], [guid, isPermaLink String \"false\", http://arstechnica.com/?p=230835], [description, Cells can be tracked in the heart with a simple ultrasound scan.], [content:encoded, <div id=\"rss-wrap\">\n"
		+ "<div>\n"
		+ "      <img src=\"http://cdn.arstechnica.net/wp-content/uploads/2013/03/Screen-Shot-2013-03-21-at-8.36.30-PM-640x449.png\">\n"
		+ "</div>\n"
		+ " <p>Heart-related diseases are the leading cause of death in the industrialized world. Cardiac stem cell therapy is a promising new way of reducing those numbers, but its application has proven to be less effective than hoped. Now researchers at Stanford University have developed nanoparticles that can be used to image stem cells implanted into the heart. They claim this will help improve the efficiency of these transplants drastically.</p>\n"
		+ "<p>Stem cell therapy uses cells that have the ability to transform into a wide variety of mature cell types. When implanted in the heart, for instance, they can transform into heart cells. This ability can be used to repair injured or diseased parts of the heart. Sadly, current methods of introducing stem cells rely on trial and error.</p>\n"
		+ "<p>Let’s say, for instance, that a patient suffers a heart attack, which leaves some of his heart cells injured. To help the heart heal, the patient is first put into an MRI scanner to locate the areas of the heart that need repair. Once those are determined, doctors use the scan to implant new stem cells into these regions. After implantation, the patient is returned to the MRI to determine the location and number of implanted cells—if they’re not where they need to be, the patient is returned to surgery. This is exhaustively repeated.</p>\n"
		+ "</div><p><a href=\"http://arstechnica.com/science/2013/03/doctors-track-stem-cells-with-nanoparticles-during-cardiac-therapy/#p3n\">Read 6 remaining paragraphs</a> | <a href=\"http://arstechnica.com/science/2013/03/doctors-track-stem-cells-with-nanoparticles-during-cardiac-therapy/?comments=1\">Comments</a></p>], [wfw:commentRss, http://arstechnica.com/science/2013/03/doctors-track-stem-cells-with-nanoparticles-during-cardiac-therapy/feed/], [slash:comments, 0]], [item, [title, Nvidia plans to turn Ultrabooks into workstations with Grid VCA server], [link, http://arstechnica.com/information-technology/2013/03/nvidia-plans-to-turn-ultrabooks-into-workstations-with-grid-vca-server/], [comments, http://arstechnica.com/information-technology/2013/03/nvidia-plans-to-turn-ultrabooks-into-workstations-with-grid-vca-server/#comments], [pubDate, Fri, 22 Mar 2013 15:10:51 +0000], [dc:creator, Andrew Cunningham], [category, Gear & Gadgets], [category, Technology Lab], [category, grid], [category, NVIDIA], [guid, isPermaLink String \"false\", http://arstechnica.com/?p=231029], [description, Nvidia continues to push GPUs in the direction of the cloud.], [content:encoded, <div id=\"rss-wrap\">\n"
		+ "<div>\n"
		+ "      <img src=\"http://cdn.arstechnica.net/wp-content/uploads/2013/03/IMG_3984-640x426.jpg\"><div class=\"caption\" style=\"font-size:0.8em\">\n"
		+ "			<div class=\"caption-text\">Nvidia CEO Jen-Hsun Huang directs a demo of the Grid Visual Computing Appliance (VCA) during his GTC 2013 keynote.</div>\n"
		+ "	\n"
		+ "			<div class=\"caption-byline\">\n"
		+ "							Andrew Cunningham				</div>\n"
		+ "	  </div>\n"
		+ "  </div>\n"
		+ " <p>SAN JOSE, CA—One of the announcements embedded in Nvidia CEO Jen-Hsun Huang's opening keynote for the company's GPU Technology Conference Tuesday was a brand new server product, something that Nvidia is calling the Grid Visual Computing Appliance, or VCA.</p>\n"
		+ "<p>The VCA is a buttoned-down, business-focused cousin to the <a href=\"http://arstechnica.com/gaming/2013/01/but-can-it-stream-crysis-nvidias-new-cloud-gaming-server-explained/\">Nvidia Grid cloud gaming server</a> that the company unveiled at CES in January. It's a 4U rack-mountable box that uses Intel Xeon CPUs and Nvidia's Grid graphics cards (<a href=\"http://arstechnica.com/information-technology/2012/10/nvidias-vgx-cards-bring-big-graphics-performance-to-virtual-machines/\">née VGX</a>), and like the Grid gaming server, it takes the GPU in your computer and puts it into a server room. The VCA serves up 64-bit Windows VMs to users, but unlike most traditional VMs, you've theoretically got the same amount of graphical processing power at your disposal as you would in a high-end workstation.</p>\n"
		+ "<p>However, while the two share a lot of underlying technology, both Grid servers have very different use cases and audiences. We met with Nvidia to learn more about just who this server is for and what it's like to use and administer one.</p>\n"
		+ "</div><p><a href=\"http://arstechnica.com/information-technology/2013/03/nvidia-plans-to-turn-ultrabooks-into-workstations-with-grid-vca-server/#p3n\">Read 19 remaining paragraphs</a> | <a href=\"http://arstechnica.com/information-technology/2013/03/nvidia-plans-to-turn-ultrabooks-into-workstations-with-grid-vca-server/?comments=1\">Comments</a></p>], [wfw:commentRss, http://arstechnica.com/information-technology/2013/03/nvidia-plans-to-turn-ultrabooks-into-workstations-with-grid-vca-server/feed/], [slash:comments, 0]], [item, [title, Apple forcing developers to ditch unique device IDs], [link, http://arstechnica.com/apple/2013/03/apple-forcing-developers-to-ditch-unique-device-ids/], [comments, http://arstechnica.com/apple/2013/03/apple-forcing-developers-to-ditch-unique-device-ids/#comments], [pubDate, Fri, 22 Mar 2013 14:20:48 +0000], [dc:creator, Jacqui Cheng], [category, Infinite Loop], [category, App Store], [category, apple], [category, developers], [category, iOS], [category, iPhone 5], [category, retina], [category, UDID], [guid, isPermaLink String \"false\", http://arstechnica.com/?p=231077], [description, As of May 1, Apple's also cracking down on apps that don't support iPhone 5.], [content:encoded, <div id=\"rss-wrap\">\n"
		+ "<div>\n"
		+ "      <img src=\"http://cdn.arstechnica.net/wp-content/uploads/2012/09/udid_identity_listing_3d-640x360.jpg\"><div class=\"caption\" style=\"font-size:0.8em\">\n"
		+ "			<div class=\"caption-text\">Data like location, Facebook profile, and other information could be correlated from multiple databases to piece together an individual's identity using the UDID.</div>\n"
		+ "	\n"
		+ "	  </div>\n"
		+ "  </div>\n"
		+ " <p>Apple will officially start rejecting iOS apps that make use of the unique device ID or UDID, in order to track users. The company <a href=\"https://developer.apple.com/news/index.php?id=3212013a\">informed</a> its developer community of the policy late Thursday (followed by a confirmation to <em><a href=\"http://www.macworld.com/article/2031573/apple-sets-may-1-deadline-for-udid-iphone-5-app-changes.html\">Macworld</a></em>) that it would no longer accept UDID-utilizing apps as of May 1. Instead, the company instructed developers to make use of other identifiers, such as Apple's new Identifier for Advertising, or IDFA, which was introduced in iOS 6 last fall.</p>\n"
		+ "<p>Apple had already warned developers in early last year that it would begin deprecating the UDID, and it had been <a href=\"http://arstechnica.com/apple/2012/03/after-warning-from-apple-apps-using-udids-now-being-rejected/\">rejecting apps that make use of it</a> as early as May 2012. That didn't stop <em>every</em> developer from using the UDID, though, and a high-profile leak in September (originally <a href=\"http://arstechnica.com/security/2012/09/1-million-ios-device-ids-leaked-after-alleged-fbi-laptop-hack/\">suspected to have come from the FBI</a> but <a href=\"http://arstechnica.com/apple/2012/09/publishing-firm-ios-udid-leak-came-from-us-not-the-fbi/\">ultimately revealed to have come from an iOS app publisher</a>) highlighted the need for a more privacy-conscious system to track users and offer targeted advertising.</p>\n"
		+ "<p>That's part of why <a href=\"http://arstechnica.com/apple/2012/10/ask-ars-whats-the-difference-between-the-old-and-new-tracking-systems-on-ios/\">Apple introduced its IDFA when it rolled out iOS 6</a>. Not only do users have more control over whether they are being tracked; the IDFA also allows developers to follow iOS users across devices. With the UDID, users had no way to turn off the ad tracking, and since the UDID was tied to a specific device, the data could be cross-contaminated if a new user took control of it.</p>\n"
		+ "</div><p><a href=\"http://arstechnica.com/apple/2013/03/apple-forcing-developers-to-ditch-unique-device-ids/#p3n\">Read 1 remaining paragraphs</a> | <a href=\"http://arstechnica.com/apple/2013/03/apple-forcing-developers-to-ditch-unique-device-ids/?comments=1\">Comments</a></p>], [wfw:commentRss, http://arstechnica.com/apple/2013/03/apple-forcing-developers-to-ditch-unique-device-ids/feed/], [slash:comments, 0]], [item, [title, Blizzard announces &#8220;freemium&#8221; online trading card game], [link, http://arstechnica.com/gaming/2013/03/blizzard-announces-work-on-an-online-trading-card-game/], [comments, http://arstechnica.com/gaming/2013/03/blizzard-announces-work-on-an-online-trading-card-game/#comments], [pubDate, Fri, 22 Mar 2013 14:20:34 +0000], [dc:creator, Casey Johnston], [category, Opposable Thumbs], [category, Blizzard], [category, hearthstone: heroes of warcraft], [category, trading card game], [category, Warcraft], [guid, isPermaLink String \"false\", http://arstechnica.com/?p=231067], [description, <i>HearthStone: Heroes of Warcraft</i> is Blizzard's first \"freemium\" model game.], [content:encoded, <div id=\"rss-wrap\">\n"
		+ "<div>\n"
		+ "      <img src=\"http://cdn.arstechnica.net/wp-content/uploads/2013/03/hearthstone-heroes-of-warcraft.png\"><div class=\"caption\" style=\"font-size:0.8em\">\n"
		+ "	\n"
		+ "			<div class=\"caption-byline\">\n"
		+ "							<a href=\"http://www.twitch.tv/\">Twitch TV</a>\n"
		+ "				</div>\n"
		+ "	  </div>\n"
		+ "  </div>\n"
		+ " <p>BOSTON—Blizzard confirmed that it is working on a new small-scale game titled <em>HearthStone: Heroes of Warcraft</em> at a conference at PAX Friday. The game is a collectible-card-style game played online and will be Blizzard's \"first free-to-play\" title.</p>\n"
		+ "<p>The model is \"something we've wanted to experiment with for a while... A lot of people at Blizzard have played CCGs for a really long time,\" said Rob Pardo, chief creative officer at Blizzard. But the company wanted to focus on making a trading card game that's more accessible and \"not just something that us hardcore nerds would play.\"</p>\n"
		+ "<p>The game is based on <em>Warcraft</em> lore and will have 300 cards at launch, with five cards per pack. Blizzard has yet to determine pricing but estimates that each five-card pack will cost \"around a dollar.\" The game will also employ a freemimum model: players can earn packs by playing, but their progress will be slower than someone who simply buys packs outright.<br></p>\n"
		+ "</div><p><a href=\"http://arstechnica.com/gaming/2013/03/blizzard-announces-work-on-an-online-trading-card-game/#p3n\">Read 3 remaining paragraphs</a> | <a href=\"http://arstechnica.com/gaming/2013/03/blizzard-announces-work-on-an-online-trading-card-game/?comments=1\">Comments</a></p>], [wfw:commentRss, http://arstechnica.com/gaming/2013/03/blizzard-announces-work-on-an-online-trading-card-game/feed/], [slash:comments, 0]], [item, [title, Cop accused of putting webcam in boys&#8217; bathroom at Maryland HS], [link, http://arstechnica.com/tech-policy/2013/03/cop-accused-of-putting-webcam-in-boys-bathroom-at-maryland-hs/], [comments, http://arstechnica.com/tech-policy/2013/03/cop-accused-of-putting-webcam-in-boys-bathroom-at-maryland-hs/#comments], [pubDate, Fri, 22 Mar 2013 14:00:48 +0000], [dc:creator, Sean Gallagher], [category, Law & Disorder], [category, surveillance], [category, webcam], [guid, isPermaLink String \"false\", http://arstechnica.com/?p=231001], [description, Device was planted without permission, cop under investigation.], [content:encoded, <div id=\"rss-wrap\"> <p>Around noon on March 20, a student at Glen Burnie High School in Glen Burnie, Maryland went to an assistant principal at the school to tell him he felt uncomfortable using the bathroom. Why? Because he had discovered a <a href=\"http://www.wbaltv.com/news/maryland/anne-arundel-county/Police-Officer-put-camera-in-Glen-Burnie-school-bathroom/-/10137088/19413010/-/ut8qccz/-/index.html\">surveillance camera</a> in it an hour and a half earlier.</p>\n"
		+ "<p>That triggered an investigation by police and resulted in a 14-year veteran of the Anne Arundel County Police Department going on paid administrative leave when it was discovered that he had purchased the wireless camera and allegedly installed it without the knowledge of the police department or school.</p>\n"
		+ "<p>The officer has not been named, but it was indicated that he had been assigned to the police department's Special Services Bureau, which provides \"resource\" officers for the county's school system. A police spokesperson said that no images or video were found stored on the device, but the device was capable of recording video and had been mounted to the ceiling in plain sight. No other cameras have been found at Glen Burnie H.S. or other county schools.</p>\n"
		+ "</div><p><a href=\"http://arstechnica.com/tech-policy/2013/03/cop-accused-of-putting-webcam-in-boys-bathroom-at-maryland-hs/#p3n\">Read 1 remaining paragraphs</a> | <a href=\"http://arstechnica.com/tech-policy/2013/03/cop-accused-of-putting-webcam-in-boys-bathroom-at-maryland-hs/?comments=1\">Comments</a></p>], [wfw:commentRss, http://arstechnica.com/tech-policy/2013/03/cop-accused-of-putting-webcam-in-boys-bathroom-at-maryland-hs/feed/], [slash:comments, 0]], [item, [title, Goodbye Windows: China to create home-grown OS based on Ubuntu], [link, http://arstechnica.com/information-technology/2013/03/goodbye-windows-china-to-create-home-grown-os-based-on-ubuntu/], [comments, http://arstechnica.com/information-technology/2013/03/goodbye-windows-china-to-create-home-grown-os-based-on-ubuntu/#comments], [pubDate, Fri, 22 Mar 2013 13:50:27 +0000], [dc:creator, Jon Brodkin], [category, Technology Lab], [category, Canonical], [category, china], [category, Ubuntu], [guid, isPermaLink String \"false\", http://arstechnica.com/?p=231011], [description, Canonical strikes deal with China to expand open source software usage.], [content:encoded, <div id=\"rss-wrap\"> <p>Ubuntu maker Canonical has <a href=\"http://www.canonical.com/content/canonical-and-chinese-standards-body-announce-ubuntu-collaboration\">signed a deal</a> with the Chinese government to create a new version of Ubuntu. For China, this is widely seen as an attempt \"to wean its IT sector off Western software in favour of more home-grown alternatives,\" the <a href=\"http://www.bbc.co.uk/news/technology-21895723\">BBC reported</a>.</p>\n"
		+ "<p>In other words, it's an attempt to move from Windows to Linux. According to <a href=\"http://netmarketshare.com/\">NetMarketshare statistics</a>, Windows has 91.62 percent market share on the desktop in China, compared to 1.21 percent for Linux. The other 7.17 percent is OS X.</p>\n"
		+ "<p>China is developing a new reference architecture for operating systems, based on Ubuntu. The Chinese version of Ubuntu—called Ubuntu Kylin—will be released next month in conjunction with Ubuntu's regular release cycle.</p>\n"
		+ "</div><p><a href=\"http://arstechnica.com/information-technology/2013/03/goodbye-windows-china-to-create-home-grown-os-based-on-ubuntu/#p3n\">Read 2 remaining paragraphs</a> | <a href=\"http://arstechnica.com/information-technology/2013/03/goodbye-windows-china-to-create-home-grown-os-based-on-ubuntu/?comments=1\">Comments</a></p>], [wfw:commentRss, http://arstechnica.com/information-technology/2013/03/goodbye-windows-china-to-create-home-grown-os-based-on-ubuntu/feed/], [slash:comments, 0]], [item, [title, Roku 3 review: A set-top box to trump all other set-top boxes], [link, http://arstechnica.com/gadgets/2013/03/roku-3-review-a-set-top-box-to-trump-all-other-set-top-boxes/], [comments, http://arstechnica.com/gadgets/2013/03/roku-3-review-a-set-top-box-to-trump-all-other-set-top-boxes/#comments], [pubDate, Fri, 22 Mar 2013 13:00:01 +0000], [dc:creator, Florence Ion], [category, Gear & Gadgets], [category, HBO], [category, Hulu], [category, Netflix], [category, review], [category, reviews], [category, roku], [category, roku 3], [category, Set-top boxes], [category, streaming], [guid, isPermaLink String \"false\", http://arstechnica.com/?p=230259], [description, With over 750 channels available and a brand new interface, what's not to love?], [content:encoded, <div id=\"rss-wrap\">\n"
		+ "<div>\n"
		+ "      <img src=\"http://cdn.arstechnica.net/wp-content/uploads/2013/03/IMG_33901-640x398.jpg\"><div class=\"caption\" style=\"font-size:0.8em\">\n"
		+ "	\n"
		+ "			<div class=\"caption-byline\">\n"
		+ "							Florence Ion				</div>\n"
		+ "	  </div>\n"
		+ "  </div>\n"
		+ " <p>Roku is no stranger to set-top boxes—we’d even go so far as to say that the company has managed to set the standard for what these little streaming devices should offer. It's the content partnerships that have made the devices so successful—consumers have so much choice when it comes to streaming, something that the Apple TV and even the WDTV Play are still catching up on.</p>\n"
		+ "<p>To be fair, Roku, Apple, and Western Digital all offer slightly different things. Roku is especially made for those who want to stream a variety of content from third-parties, while the other two act more as a mediator for users to play content they already own while still having access to some of the more widely used third parties. Regardless, the Roku has been successful in its model, and the Roku 3 continues in the company's tradition of delivering an affordable, feature-filled streaming device.</p>\n"
		+ "<h2>New look, same great offerings</h2>\n"
		+ "<p>The Roku 3, available now for $99.99, calls itself the “most powerful, responsive streaming box” that Roku has <a href=\"http://www.roku.com/why-its-cool\">ever</a> built. It features an ARM-based processor and other components all wrapped inside a shiny, hockey-puck shaped chassis. At 3.5 x 3.5 x 1 inches, it’s a bit smaller than the second- and third-generation Apple TV, and Western Digital's Play set-top box. It also feels a bit like a paperweight, but that helps keep the puck from sliding around in your entertainment center.</p>\n"
		+ "</div><p><a href=\"http://arstechnica.com/gadgets/2013/03/roku-3-review-a-set-top-box-to-trump-all-other-set-top-boxes/#p3n\">Read 17 remaining paragraphs</a> | <a href=\"http://arstechnica.com/gadgets/2013/03/roku-3-review-a-set-top-box-to-trump-all-other-set-top-boxes/?comments=1\">Comments</a></p>], [wfw:commentRss, http://arstechnica.com/gadgets/2013/03/roku-3-review-a-set-top-box-to-trump-all-other-set-top-boxes/feed/], [slash:comments, 0]], [item, [title, FCC confirms resignation of Chairman Julius Genachowski (Updated)], [link, http://arstechnica.com/tech-policy/2013/03/fcc-chairman-julius-genachowski-expected-to-announce-resignation-tomorrow/], [comments, http://arstechnica.com/tech-policy/2013/03/fcc-chairman-julius-genachowski-expected-to-announce-resignation-tomorrow/#comments], [pubDate, Fri, 22 Mar 2013 01:10:51 +0000], [dc:creator, Megan Geuss], [category, Law & Disorder], [category, FCC], [category, Genachowski], [guid, isPermaLink String \"false\", http://arstechnica.com/?p=230855], [description, <em>Wall Street Journal</em> reports that the Chairman will call it quits after four years.], [content:encoded, <div id=\"rss-wrap\"> <p><strong>UPDATE</strong>: The FCC this morning <a href=\"http://www.fcc.gov/events/chairman-genachowski-announces-plans-step-down\">confirmed</a> that Chairman Julius Genachowski will be leaving his position \"in the coming weeks.\" The FCC is holding a planning meeting, and Ars will have more detailed coverage later today.</p>\n"
		+ "<p>The <em>Wall Street Journal</em> <a href=\"http://online.wsj.com/article/SB10001424127887324557804578375023144095806.html\">reported</a> tonight that the Chairman of the Federal Communications Commission, Julius Genachowski, will announce Friday that he is stepping down from his position. Genachowski was appointed by President Barack Obama in 2009. It's not clear yet when his final day will be.</p>\n"
		+ "<p>Genachowski was at the helm through an interesting period in the history of the FCC—as connection to the Internet became more of a rule than an exception, Genachowski faced the daunting task of <a href=\"http://arstechnica.com/tech-policy/2012/06/lawmakers-want-fcc-to-bail-out-lightsquared-with-military-spectrum/\">meting out spectrum</a> between telecom providers and public entities, dividing wavelengths and assigning importance to things like <a href=\"http://arstechnica.com/tech-policy/2013/01/fccs-genachowski-govt-will-open-up-radio-spectrum-to-improve-wi-fi/\">wireless networks</a>, <a href=\"http://arstechnica.com/tech-policy/2012/02/911-broadband-network-brought-to-you-by-tv-spectrum-selloff/\">TV</a>, <a href=\"http://arstechnica.com/tech-policy/2012/02/why-lightsquared-failed/\">GPS devices</a>, “responder” networks for things like 911 calls, and broadcast radio. Genachowski also chaired the FCC as it heard debates over <a href=\"http://arstechnica.com/tech-policy/2012/05/harvard-prof-to-chair-fccs-net-neutrality-advisory-committee/\">net neutrality</a>, and during the infamous <a href=\"http://arstechnica.com/tech-policy/2011/12/fcc-to-probe-san-francisco-subway-cell-phone-interruption-policy/\">interruption of cell phone service</a> in San Francisco's BART subway during protests in 2011.</p>\n"
		+ "</div><p><a href=\"http://arstechnica.com/tech-policy/2013/03/fcc-chairman-julius-genachowski-expected-to-announce-resignation-tomorrow/#p3n\">Read 2 remaining paragraphs</a> | <a href=\"http://arstechnica.com/tech-policy/2013/03/fcc-chairman-julius-genachowski-expected-to-announce-resignation-tomorrow/?comments=1\">Comments</a></p>], [wfw:commentRss, http://arstechnica.com/tech-policy/2013/03/fcc-chairman-julius-genachowski-expected-to-announce-resignation-tomorrow/feed/], [slash:comments, 0]], [item, [title, US law enforcement biggest recipient of Microsoft customer data], [link, http://arstechnica.com/tech-policy/2013/03/us-law-enforcement-gets-most-customer-data-from-microsoft/], [comments, http://arstechnica.com/tech-policy/2013/03/us-law-enforcement-gets-most-customer-data-from-microsoft/#comments], [pubDate, Fri, 22 Mar 2013 00:00:40 +0000], [dc:creator, Peter Bright], [category, Law & Disorder], [category, google], [category, law enforcement], [category, microsoft], [category, transparency], [guid, isPermaLink String \"false\", http://arstechnica.com/?p=230807], [description, Microsoft joins Google and Twitter with new transparency report.], [content:encoded, <div id=\"rss-wrap\">\n"
		+ "<div>\n"
		+ "      <img src=\"http://cdn.arstechnica.net/wp-content/uploads/2013/03/3575476402_6aeac00e90_z.jpg\"><div class=\"caption\" style=\"font-size:0.8em\">\n"
		+ "			<div class=\"caption-text\">Transparency.</div>\n"
		+ "	\n"
		+ "			<div class=\"caption-byline\">\n"
		+ "							<a href=\"http://www.flickr.com/photos/ollily/3575476402/\">ollily</a>\n"
		+ "				</div>\n"
		+ "	  </div>\n"
		+ "  </div>\n"
		+ " <p>Following the lead set by <a href=\"http://arstechnica.com/tech-policy/2012/11/us-gets-more-google-user-data-than-all-other-countries-combined/\">Google</a> and <a href=\"http://arstechnica.com/tech-policy/2013/01/us-law-enforcement-puts-pressure-on-twitter-with-more-intensity-frequency/\">Twitter</a>, Microsoft has <a href=\"http://blogs.technet.com/b/microsoft_on_the_issues/archive/2013/03/21/microsoft-releases-2012-law-enforcement-requests-report.aspx\">published</a> its first <a href=\"http://www.microsoft.com/about/corporatecitizenship/en-us/reporting/transparency/\">transparency report</a>, tabulating the number of requests for customer data made by law enforcement around the world, the number of responses given, and what kind of information was included in those responses.</p>\n"
		+ "<p>Microsoft responds to requests for data in 46 countries, those where it says it can properly verify the legitimacy of the requests. In 2012, a total of 70,665 requests were made. The country making the most demands for data was Turkey, with 11,434. The US was in second place, with 11,073. Each request could concern multiple users, with a total of 122,015 users covered by requests.</p>\n"
		+ "<p>When it comes to the number of requests that returned customer data, however, the US was the clear leader. Of 1,558 requests that resulted in disclosure of some customer content, such as the subject or body of an e-mail or a photograph on SkyDrive, 1,544 were made in the US. The other 14 were split between Brazil (7), Canada (1), Ireland (5), and New Zealand (1).</p>\n"
		+ "</div><p><a href=\"http://arstechnica.com/tech-policy/2013/03/us-law-enforcement-gets-most-customer-data-from-microsoft/#p3n\">Read 12 remaining paragraphs</a> | <a href=\"http://arstechnica.com/tech-policy/2013/03/us-law-enforcement-gets-most-customer-data-from-microsoft/?comments=1\">Comments</a></p>], [wfw:commentRss, http://arstechnica.com/tech-policy/2013/03/us-law-enforcement-gets-most-customer-data-from-microsoft/feed/], [slash:comments, 0]], [item, [title, HBO Go &#8220;maybe,&#8221; &#8220;could&#8221; evolve into a broadband-based subscription], [link, http://arstechnica.com/business/2013/03/hbo-go-maybe-could-evolve-into-a-broadband-based-subscription/], [comments, http://arstechnica.com/business/2013/03/hbo-go-maybe-could-evolve-into-a-broadband-based-subscription/#comments], [pubDate, Thu, 21 Mar 2013 23:12:28 +0000], [dc:creator, Casey Johnston], [category, Ministry of Innovation], [category, cable], [category, cable providers], [category, HBO], [category, hbo go], [category, Internet], [category, ISP], [category, television], [category, TV], [guid, isPermaLink String \"false\", http://arstechnica.com/?p=230789], [description, <em>Game of Thrones</em> on your tablet without the cable subscription? It's possible.], [content:encoded, <div id=\"rss-wrap\"> <p>HBO has given people who listen forlornly to their friends talking about new episodes of <em>Girls</em> or <em>Boardwalk Empire</em> the barest of hopeful tidbits to snack on: HBO Go “maybe,” “could” be bundled as a service from broadband providers, in place of or addition to cable provider partnerships. Reuters <a href=\"http://www.reuters.com/article/2013/03/21/hbo-streaming-idUSL1N0CD7WP20130321\">reports</a> that HBO has yet to work out the numbers, but to us the company sounds about 40 percent sure that it’s certain that this could, indeed, happen.</p>\n"
		+ "<p>HBO shows are highly inaccessible given the digital age we live in. <em>Game of Thrones</em> in particular <a href=\"http://www.forbes.com/sites/erikkain/2012/05/09/hbo-has-only-itself-to-blame-for-record-game-of-thrones-piracy/\">suffers from a high rate of piracy</a>, which some indignant viewers and lookers-on attribute to the lack of availability of HBO’s programs. HBO does not participate in services like iTunes Season Pass or Hulu, and unless one subscribes to a full cable service package and pays an extra fee for HBO’s channels, the only other legitimate avenue to an HBO show is waiting for it to be released on DVD. HBO Go is the service’s digital extension that makes shows available on tablets, smartphones, and the Web, but only to cable subscribers.</p>\n"
		+ "<p>But HBO chief executive Richard Plepler shined a laser pointer’s worth of light on the network's future today: “Maybe HBO Go, with our broadband partners, could evolve,” Plepler told Reuters. “We have to make the math work.”</p>\n"
		+ "</div><p><a href=\"http://arstechnica.com/business/2013/03/hbo-go-maybe-could-evolve-into-a-broadband-based-subscription/#p3n\">Read 1 remaining paragraphs</a> | <a href=\"http://arstechnica.com/business/2013/03/hbo-go-maybe-could-evolve-into-a-broadband-based-subscription/?comments=1\">Comments</a></p>], [wfw:commentRss, http://arstechnica.com/business/2013/03/hbo-go-maybe-could-evolve-into-a-broadband-based-subscription/feed/], [slash:comments, 0]], [item, [title, South Korean banks and broadcasters took phish bait in cyberattack], [link, http://arstechnica.com/security/2013/03/south-korean-banks-and-broadcasters-took-phish-bait-in-cyber-attack/], [comments, http://arstechnica.com/security/2013/03/south-korean-banks-and-broadcasters-took-phish-bait-in-cyber-attack/#comments], [pubDate, Thu, 21 Mar 2013 22:45:04 +0000], [dc:creator, Sean Gallagher], [category, Law & Disorder], [category, Risk Assessment], [category, hack], [category, phishing], [category, South Korea], [guid, isPermaLink String \"false\", http://arstechnica.com/?p=230743], [description, Spam message posing as message from bank carried malware that wiped drives.], [content:encoded, <div id=\"rss-wrap\"> <p>More details of the cyberattack on multiple banks and media companies in South Korea on Wednesday have emerged, suggesting that at least part of the attack was launched through a phishing campaign against employees of the companies. According to a report from <a href=\"http://blog.trendmicro.com/trendlabs-security-intelligence/how-deep-discovery-protected-against-the-korean-mbr-wiper/\">Trend Micro's security lab</a>, the \"wiper\" malware that struck at least six different companies was delivered disguised as a document in an e-mail.</p>\n"
		+ "<p>The attachment was first noticed by e-mail scanners on March 18, the day before the attack was triggered. The e-mail was purportedly from a bank; Trend Micro's Deep Discovery threat scanning software recognized the message as coming from a host that had been used to distribute malware in the past.</p>\n"
		+ "<p>The attachment, disguised as a document, was actually the installer for the \"wiper\" malware. It also carried PuTTY SSH and SCP clients, and a bash script designed to be used in an attack against Unix servers that the target machines had connection profiles for. When activated, the dropper attempted to create SSH sessions to Unix hosts with root privileges and erase key directories, as Ars reported yesterday.</p>\n"
		+ "</div><p><a href=\"http://arstechnica.com/security/2013/03/south-korean-banks-and-broadcasters-took-phish-bait-in-cyber-attack/#p3n\">Read 1 remaining paragraphs</a> | <a href=\"http://arstechnica.com/security/2013/03/south-korean-banks-and-broadcasters-took-phish-bait-in-cyber-attack/?comments=1\">Comments</a></p>], [wfw:commentRss, http://arstechnica.com/security/2013/03/south-korean-banks-and-broadcasters-took-phish-bait-in-cyber-attack/feed/], [slash:comments, 0]], [item, [title, Apple follows Google, Facebook, and others with two-step authentication], [link, http://arstechnica.com/apple/2013/03/apple-follows-google-facebook-and-others-with-two-step-authentication/], [comments, http://arstechnica.com/apple/2013/03/apple-follows-google-facebook-and-others-with-two-step-authentication/#comments], [pubDate, Thu, 21 Mar 2013 22:15:11 +0000], [dc:creator, Jacqui Cheng], [category, Infinite Loop], [category, Risk Assessment], [category, apple], [category, authentication], [category, icloud], [category, iOS], [category, security], [category, two-factor], [guid, isPermaLink String \"false\", http://arstechnica.com/?p=230663], [description, iCloud gains an extra layer of security in wake of increasing security threats.], [content:encoded, <div id=\"rss-wrap\"> <p>Apple has finally responded to increasing online security threats by <a href=\"http://support.apple.com/kb/HT5631?viewlocale=en_US&amp;locale=en_US\">introducing</a> two-step authentication for iCloud. Like Google and other companies that already employ two-step authentication, Apple's system would provide an extra layer of security on top of the existing iCloud passwords when users try to access their accounts from unrecognized devices. iCloud users can set up two-step authentication on Apple IDs today by going to the <a href=\"https://appleid.apple.com/\">Apple ID website</a> and clicking the \"Password and Security\" tab.</p>\n"
		+ "<div class=\"image center full\">\n"
		+ "<img src=\"http://cdn.arstechnica.net/wp-content/uploads/2013/03/1r.png\"><div class=\"caption\" style=\"font-size:0.8em\">\n"
		+ "<div class=\"caption-text\">Apple walks you through the process on its Apple ID management site.</div> </div>\n"
		+ "</div>\n"
		+ "<p>For Apple, this means an authentication code is either sent via SMS to a phone number or found within the Find My iPhone app (if you have it installed) whenever you try to log in from somewhere new. This means that a potential attacker will have a harder time getting into your iCloud account without having physical access to your \"trusted\" device receiving the code. (Users are prompted to set up at least one trusted device when they turn on two-step authentication, though you can have more than one if you like.) Currently, two-step authentication is available to iCloud users in the US, UK, Australia, Ireland, and New Zealand.</p>\n"
		+ "<p>One of the benefits to setting this up on your iCloud account is that you'll no longer have to rely on security questions—which are inherently insecure—in order to gain access to your account if you lose your password. The downside (if you consider it that) is that once you set up two-step authentication, Apple will no longer be able to reset your password for you should you lose or forget it. This is what ended up biting <em>Wired</em> editor Mat Honan in the behind <a href=\"http://arstechnica.com/security/2012/08/apple-freezes-over-the-phone-password-resets-in-response-to-honan-hack/\">when his various accounts were compromised</a>—hackers were able to gather enough personal information from Honan's e-mail and Amazon accounts to trick Apple support into resetting his iCloud password, giving them free reign to remotely wipe his iPhone, iPad, and MacBook.</p>\n"
		+ "</div><p><a href=\"http://arstechnica.com/apple/2013/03/apple-follows-google-facebook-and-others-with-two-step-authentication/#p3n\">Read 1 remaining paragraphs</a> | <a href=\"http://arstechnica.com/apple/2013/03/apple-follows-google-facebook-and-others-with-two-step-authentication/?comments=1\">Comments</a></p>], [wfw:commentRss, http://arstechnica.com/apple/2013/03/apple-follows-google-facebook-and-others-with-two-step-authentication/feed/], [slash:comments, 0]], [item, [title, “Donglegate” is classic overreaction—and everyone pays], [link, http://arstechnica.com/staff/2013/03/donglegate-is-classic-overreaction-and-everyone-pays/], [comments, http://arstechnica.com/staff/2013/03/donglegate-is-classic-overreaction-and-everyone-pays/#comments], [pubDate, Thu, 21 Mar 2013 22:00:45 +0000], [dc:creator, Ken Fisher], [category, Staff], [category, Donglegate], [guid, isPermaLink String \"false\", http://arstechnica.com/?p=230643], [description, Or, how not to deal with difficult social issues.], [content:encoded, <div id=\"rss-wrap\">\n"
		+ "<div>\n"
		+ "      <img src=\"http://cdn.arstechnica.net/wp-content/uploads/2013/03/donglegate.jpg\"><div class=\"caption\" style=\"font-size:0.8em\">\n"
		+ "	\n"
		+ "			<div class=\"caption-byline\">\n"
		+ "							Aurich Lawson				</div>\n"
		+ "	  </div>\n"
		+ "  </div>\n"
		+ " <p>Watching \"Donglegate\" unfold over the past few days has been like watching a comedy of errors slowly metastasize into a tragedy of thoughtlessness. News coverage of what unfolded at (and after) this year's PyCon developer conference has <a href=\"http://arstechnica.com/tech-policy/2013/03/how-dongle-jokes-got-two-people-fired-and-led-to-ddos-attacks/\">already been written</a>; I'll assume that you’re up to speed. What follows is straight opinion about a silly situation.</p>\n"
		+ "<p>As events unfolded from Sunday until today, partisans quickly formed to weigh in on some key questions. Was SendGrid evangelist Adria Richards right or wrong to take offense at the jokes in question? Were the two male developers out of bounds with their \"dongle\" comments? Did they even say the things they were accused of? Was taking the matter right to Twitter the wrong way to go? Was the termination of two people—including Richards herself—a preferred outcome? How did DDoS vigilantes get involved in a complaint over some genital jokes? Finally: how long until the lawsuits?</p>\n"
		+ "<p>Let's start by spreading the blame where it's deserved: on nearly everyone involved. The \"Boy’s Club” mentality is thankfully no longer acceptable in tech, but it's still common—some people have actually described tech to me as \"men's work.\" The jokes appear to run afoul of PyCon's code of conduct, which strives to create a welcoming atmosphere for everyone, and their unfunny-ness is equaled only by their lameness. “Forking a repo” and “big dongles” must rank somewhere around \"0.5: classless brospeak\" on the seismic scale of harassing/menacing behavior toward women. While such sexually inappropriate comments are completely unnacceptable in professional settings (to many men as well as women), neither merits firing unless someone had a history of making unwelcome comments. A teaching opportunity should not generally be turned into a termination event. (PlayHaven, which employed both developers, <a href=\"http://blog.playhaven.com/addressing-pycon/\">says</a> that it will not comment \"on all the factors that contributed to our parting ways\" with one developer, so it's not clear what the exact situation here was.)</p>\n"
		+ "</div><p><a href=\"http://arstechnica.com/staff/2013/03/donglegate-is-classic-overreaction-and-everyone-pays/#p3n\">Read 5 remaining paragraphs</a> | <a href=\"http://arstechnica.com/staff/2013/03/donglegate-is-classic-overreaction-and-everyone-pays/?comments=1\">Comments</a></p>], [wfw:commentRss, http://arstechnica.com/staff/2013/03/donglegate-is-classic-overreaction-and-everyone-pays/feed/], [slash:comments, 0]], [item, [title, Lawmakers re-introduce GPS protection bill against government spying], [link, http://arstechnica.com/tech-policy/2013/03/lawmakers-re-introduce-gps-protection-bill-against-government-spying/], [comments, http://arstechnica.com/tech-policy/2013/03/lawmakers-re-introduce-gps-protection-bill-against-government-spying/#comments], [pubDate, Thu, 21 Mar 2013 20:45:15 +0000], [dc:creator, Cyrus Farivar], [category, Law & Disorder], [category, geolocation], [category, gps act], [category, privacy], [guid, isPermaLink String \"false\", http://arstechnica.com/?p=230545], [description, GPS Act brought back from 2011—hopefully it will pass both House and Senate.], [content:encoded, <div id=\"rss-wrap\"> <p dir=\"ltr\" id=\"internal-source-marker_0.8933989481832836\">Just two days after new legislative reform on e-mail privacy was <a href=\"http://www.wyden.senate.gov/news/press-releases/bipartisan-legislation-institutes-warrant-requirements-to-track-americans-with-gps-data\">re-introduced</a> in Congress, another privacy bill was brought back from years past.</p>\n"
		+ "<p dir=\"ltr\">On Thursday, three members of the House (two Republicans and a Democrat) and two bipartisan senators <a href=\"http://thehill.com/blogs/hillicon-valley/technology/289575-lawmakers-push-bill-to-limit-gps-tracking\">introduced</a> the GPS Act, which would require law enforcement to obtain a probable cause-driven warrant <a href=\"http://arstechnica.com/tech-policy/2012/08/federal-court-rules-cops-can-warantlessly-track-suspects-via-cellphone/\">before accessing</a> a suspect’s <a href=\"http://arstechnica.com/business/2012/02/location-tracking-of-gsm-cellphones-now-easier-and-cheaper-than-ever/\">geolocation</a> information. The bill had originally been <a href=\"http://arstechnica.com/tech-policy/2011/06/bipartisan-bill-would-end-governments-warrantless-gps-tracking/\">introduced nearly two years ago</a> by the same group of legislators.</p>\n"
		+ "<p dir=\"ltr\">As <a href=\"http://arstechnica.com/tech-policy/2012/03/obama-admin-wants-warrantless-access-to-cell-phone-location-data/\">Ars’ Tim Lee wrote in 2012</a>, the Obama Administration laid out its position in a <a href=\"http://volokh.com/wp-content/uploads/2012/02/DOJ-cell-site.pdf\">legal brief</a> last year, arguing that customers have \"no privacy interest\" in cell-site location records (CSLR) held by a network provider. Under a legal principle known as the \"third-party doctrine,\" information voluntarily disclosed to a third party ceases to enjoy Fourth Amendment protection. The government contends that this rule applies to cell phone location data collected by a network provider.</p>\n"
		+ "</div><p><a href=\"http://arstechnica.com/tech-policy/2013/03/lawmakers-re-introduce-gps-protection-bill-against-government-spying/#p3n\">Read 3 remaining paragraphs</a> | <a href=\"http://arstechnica.com/tech-policy/2013/03/lawmakers-re-introduce-gps-protection-bill-against-government-spying/?comments=1\">Comments</a></p>], [wfw:commentRss, http://arstechnica.com/tech-policy/2013/03/lawmakers-re-introduce-gps-protection-bill-against-government-spying/feed/], [slash:comments, 0]], [item, [title, IsoHunt&#8217;s Fung helped users infringe, blew off &#8220;red flags,&#8221; say judges], [link, http://arstechnica.com/tech-policy/2013/03/appeals-court-deals-a-major-blow-to-torrent-site-isohunt/], [comments, http://arstechnica.com/tech-policy/2013/03/appeals-court-deals-a-major-blow-to-torrent-site-isohunt/#comments], [pubDate, Thu, 21 Mar 2013 20:35:43 +0000], [dc:creator, Joe Mullin], [category, Law & Disorder], [category, BItTorrent], [category, IsoHunt], [guid, isPermaLink String \"false\", http://arstechnica.com/?p=230415], [description, File-sharing services face a practically unbroken string of court losses.], [content:encoded, <div id=\"rss-wrap\">\n"
		+ "<div>\n"
		+ "      <img src=\"http://cdn.arstechnica.net/wp-content/uploads/2013/03/6926226756_feb3104201_z.jpg\"><div class=\"caption\" style=\"font-size:0.8em\">\n"
		+ "			<div class=\"caption-text\">A mosaic of Lady Justice at the 9th Circuit in San Francisco.</div>\n"
		+ "	\n"
		+ "			<div class=\"caption-byline\">\n"
		+ "							<a href=\"http://www.flickr.com/photos/thomashawk/6926226756/sizes/l/in/photostream/\"> Thomas Hawk</a>\n"
		+ "				</div>\n"
		+ "	  </div>\n"
		+ "  </div>\n"
		+ " <p>A major Hollywood court win against the IsoHunt torrent site has been upheld on appeal. In a <a href=\"http://cdn.ca9.uscourts.gov/datastore/opinions/2013/03/21/10-55946.pdf\">ruling</a> (PDF) issued this morning, a three-judge panel found that IsoHunt and its founder Gary Fung had illegally induced users to swap copyrighted files.</p>\n"
		+ "<p>After seven years of litigation, the site may finally have to adopt some serious filtering. Fung was first sued by Columbia Pictures in 2006, <a href=\"http://arstechnica.com/tech-policy/2009/12/judge-slams-isohunt-infringement-old-wine-in-a-new-bottle/\">lost</a> his case in 2009, and was slapped with a strongly worded <a href=\"http://arstechnica.com/tech-policy/2010/05/1-down-5-to-go-isohunt-neutered-by-us-judge/\">injunction</a> in 2010. That injunction allowed content companies to submit long lists of search terms to Fung that he was supposed to filter for. However, despite Fung's court losses, IsoHunt today doesn't appear to be functionally very different from the site that the entertainment industry objected to back in 2006. <a href=\"http://isohunt.com/torrents/?ihq=game+of+thrones\">Searches</a> for copyrighted <a href=\"http://isohunt.com/torrents/?iht=-1&amp;ihq=daily+show\">material</a> readily turn up lists of files with names that strongly suggest they are infringing.</p>\n"
		+ "<p>Even if you interpret the data in a light favorable to IsoHunt, there's no question that the site's main use was to trade copyrighted material, the judges wrote. Columbia's expert found that between 90 and 96 percent of content on the site was confirmed or \"highly likely\" to infringe copyright. And while Fung \"takes issue\" with some aspects of the methodology, \"he does not attempt to rebut the factual assertion that his services were widely used to infringe copyrights.\" Even tripling the margin of error on the Columbia survey would mean that the overwhelming use of IsoHunt was to violate copyright.</p>\n"
		+ "</div><p><a href=\"http://arstechnica.com/tech-policy/2013/03/appeals-court-deals-a-major-blow-to-torrent-site-isohunt/#p3n\">Read 11 remaining paragraphs</a> | <a href=\"http://arstechnica.com/tech-policy/2013/03/appeals-court-deals-a-major-blow-to-torrent-site-isohunt/?comments=1\">Comments</a></p>], [wfw:commentRss, http://arstechnica.com/tech-policy/2013/03/appeals-court-deals-a-major-blow-to-torrent-site-isohunt/feed/], [slash:comments, 0]], [item, [title, Extinction that paved way for dinosaurs definitively linked to volcanism], [link, http://arstechnica.com/science/2013/03/extinction-that-paved-way-for-dinosaurs-definitively-linked-to-volcanism/], [comments, http://arstechnica.com/science/2013/03/extinction-that-paved-way-for-dinosaurs-definitively-linked-to-volcanism/#comments], [pubDate, Thu, 21 Mar 2013 20:15:00 +0000], [dc:creator, John Timmer], [category, Scientific Method], [category, Biology], [category, dinosaurs], [category, Earth sciences], [category, evolution], [category, flood basalt], [category, mass extinction], [category, volcanism], [guid, isPermaLink String \"false\", http://arstechnica.com/?p=230443], [description, New dates also show life started recovering while eruptions were in progress.], [content:encoded, <div id=\"rss-wrap\">\n"
		+ "<div>\n"
		+ "      <img src=\"http://cdn.arstechnica.net/wp-content/uploads/2013/03/hrvt24-640x425.jpg\"><div class=\"caption\" style=\"font-size:0.8em\">\n"
		+ "			<div class=\"caption-text\">The formation of these innocent looking rocks killed off most of the species alive at the time, but worked out well for the dinosaurs.</div>\n"
		+ "	\n"
		+ "			<div class=\"caption-byline\">\n"
		+ "							<a href=\"http://www.dec.ny.gov/lands/66605.html\">NY DEC</a>\n"
		+ "				</div>\n"
		+ "	  </div>\n"
		+ "  </div>\n"
		+ " <p>Although we tend to think of the Earth as an amazingly hospitable planet, at several times in the past it seems to have done its best to kill us all—or at least all of our ancestors. Several of the Earth's mass extinctions occurred around the time of elevated volcanic activity, but the timing has been notoriously difficult to work out; the fossil beds that track the extinction rarely preserve the evidence of volcanic activity and vice-versa.</p>\n"
		+ "<p>A study that will appear in today's issue of <em>Science</em> provides a new window into the end-Triassic mass extinction, the event that ushered in the start of the era of the dinosaurs. The study provides a precise timing of events of the extinction through a combination of new dating work and a link to the Earth's orbital cycles preserved in rocks near Newark, New Jersey (because when you think end-Triassic, you think New Jersey, right?). The timing of events shows that the extinction occurred at the very onset of the volcanic activity that signaled the breakup of the supercontinent Pangea, but that life began to recover even as the later eruptions were taking place.</p>\n"
		+ "<h2>Thinking big</h2>\n"
		+ "<p>Volcanic activity takes place all the time, and while it can be devastating for the local environment (and you can use a very large definition of \"local\" for supervolcanoes), this isn't enough to set off a global extinction. For that, you need what are termed \"flood basalt eruptions.\" These events are just what the name implies: molten rock comes flooding out of a rift and covers thousands of square kilometers in rock, often at depths of hundreds of meters. Then, before the Earth recovers, you do it all over again. The largest of these eruptions, which formed the Siberian Traps, has had the total volume of rock that erupted estimated at above a million cubic kilometers.</p>\n"
		+ "</div><p><a href=\"http://arstechnica.com/science/2013/03/extinction-that-paved-way-for-dinosaurs-definitively-linked-to-volcanism/#p3n\">Read 10 remaining paragraphs</a> | <a href=\"http://arstechnica.com/science/2013/03/extinction-that-paved-way-for-dinosaurs-definitively-linked-to-volcanism/?comments=1\">Comments</a></p>], [wfw:commentRss, http://arstechnica.com/science/2013/03/extinction-that-paved-way-for-dinosaurs-definitively-linked-to-volcanism/feed/], [slash:comments, 0]]]]]\n"
		+ "";
	System.out.println(GenericNameValueContextUtil.parse(s));
    }

}

class ContextParserHandler implements ParseEventHandler {

    public Object handleAssociationEvent(AssociationEvent event) {
	return new GenericNameValue(event.getPrefixValue().toString(), event.getPostfixValue());
    }

    public Object handleEmptyString(String content) {
	return content;
    }

    public Object handleGroupEvent(GroupEvent event) {
	String closeSequence = event.getEndSymbol().getSequence();

	/* we have a list */
	if (closeSequence.equals("]")) {
	    GenericNameValueList newContext = new GenericNameValueList();
	    newContext = new GenericNameValueList();
	    List<?> objects = event.getObjects();
	    for (Object o : objects) {
		newContext.addValue(o);
	    }
	    return newContext;
	} else if (closeSequence.equals("}")) {
	    GenericNameValueContext newContext = new GenericNameValueContext();
	    newContext.putAll((List) event.getObjects());
	    return newContext;
	}
	return null;
    }

}
