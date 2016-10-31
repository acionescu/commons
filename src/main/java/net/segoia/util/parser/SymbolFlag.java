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


public interface SymbolFlag {
    /* used to ignore a group */
    public static final String IGNORE="IGNORE";
    /* used to output the content of a group as plain text not as a list of objects */
    public static final String SIMPLE="SIMPLE";
    /* used when a symbol used as a group_end is also used as a group_start symbol in the parent group */
    public static final String MULTIPLE="MULTIPLE";
    /* specifies that any character from the symbol string can be used for matching */
    public static final String ANYCHAR="ANYCHAR";
    /* 
     * when used, the parser will not stop at the first occurence of the searched symbol, but will 
     * continue, as long as the symbol is found
     */
    public static final String REPEATABLE="REPEATABLE";
    /* ignore an empty content */
    public static final String IGNORE_EMPTY="IGNORE_EMPTY";
    /* will cause the search sequence to be converted lowercase and also the symbol sequence */
    public static final String CASE_INSENSITIVE="CASE_INSENSITIVE";
    /* makes a GROUP_START act also as separate */
    public static final String SEPARATE="SEPARATE";
    /* pushes each element of the group on the stack rather then the group itself. use it on GROUP_END */
    public static final String UNGROUP="UNGROUP";
    /* added to the GROUP_START will make the parse handler to create a set with the types of the objects in the present group */
    public static final String FOLLOW_TYPES="FOLLOW_TYPES";
    
    /* use this flag on DOC_START to start a group when starting parsing */
    public static final String GROUP_START="GROUP_START";
}
