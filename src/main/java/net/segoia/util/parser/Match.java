/**
 * commons - Various Java Utils
 * Copyright (C) 2009  Adrian Cristian Ionescu - https://github.com/acionescu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.segoia.util.parser;

public class Match {
    /* the symbol related with the found text */
    private Symbol symbol;
    /* the text in between this match and the previos match */
    private String content;
    /* the actual found text that matches the current symbol sequence */
    private String match;
    /* the position of the match in the searched string */
    private int position;
    
    public Match(Symbol symbol, String content, String match, int position) {
	this.symbol=symbol;
	this.content=content;
	this.match=match;
	this.position=position;
    }
    
    public Match(Match prevMatch, String newMatch) {
	this.symbol=prevMatch.getSymbol();
	this.content=prevMatch.getContent();
	this.match=prevMatch.getMatch()+newMatch;
	this.position=prevMatch.getPosition();
    }
    
    /**
     * @return the symbol
     */
    public Symbol getSymbol() {
        return symbol;
    }
    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }
    /**
     * @return the match
     */
    public String getMatch() {
        return match;
    }

    /**
     * @return the position
     */
    public int getPosition() {
        return position;
    }
    /**
     * 
     * @return the position in the searched string of the
     * next character following the matched sequence
     */
    public int getEndPosition() {
	return position+match.length();
    }
}
