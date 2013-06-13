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
package ro.zg.util.parser;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

import ro.zg.commons.exceptions.ContextAwareException;
import ro.zg.util.parser.event.AssociationEvent;
import ro.zg.util.parser.event.GroupEvent;
import ro.zg.util.parser.event.ParseEventHandler;

public class ParseHandler {
    private ParseEventHandler eventHandler;
    private Deque<WaitInfo> waitingStack = new ArrayDeque<WaitInfo>();
    private Deque<Object> availableStack = new ArrayDeque<Object>();
    private boolean atomicGroupStarted = false;
    private String pendingContent;
    private Symbol previousSymbol;
    private Deque<Symbol> startedGroups = new ArrayDeque<Symbol>();
    private Deque<ParseContextConfig> activeContexts = new ArrayDeque<ParseContextConfig>();

    // private Deque<Set<Class<?>>> currentGroupTypes = new ArrayDeque<Set<Class<?>>>();

    public ParseHandler() {

    }

    public ParseHandler(ParseEventHandler peh) {
	this.eventHandler = peh;
    }

    public void onNewSymbolFound(Symbol symbol, int startIndex, String content) throws ContextAwareException {
	// System.out.println("found " + symbol.getSequence() + " at " + startIndex + " : " + content);
	SymbolType symbolType = symbol.getType();
	if (atomicGroupStarted) { /* we have an atomic group already started */
	    if (!symbol.equals(previousSymbol)) { /*
						   * this is not the expected atomic group separator, it will not be
						   * processed
						   */
		/* append to pending content. append also the symbol sequence */
		appendToPendingContent(content + symbol.getSequence());
		return;
	    } else {
		/* if this is the expected symbol, append only the content */
		appendToPendingContent(content);
	    }
	}

	switch (symbolType) {
	case GROUP_START:
	    handleStartGroup(symbol, startIndex, content);
	    break;
	case GROUP_END:
	    handleEndGroup(symbol, startIndex, content);
	    break;
	case ASSOCIATE:
	    handleAssociate(symbol, startIndex, content);
	    break;
	case SEPARATE:
	    handleSeparate(symbol, startIndex, content);
	    break;
	case GROUP_END_START:
	    handleEndGroup(symbol, startIndex, content);
	    handleStartGroup(symbol, startIndex, content);
	    break;
	// case UNIQUE_GROUP_SEPARATOR:
	// handleUniqueGroupSeparator(symbol, startIndex, content);
	// break;
	}
	previousSymbol = symbol;
	// System.out.println("waiting = " + waitingStack);
	// System.out.println("available = " + availableStack);
    }

    private void appendToPendingContent(String content) {
	if (pendingContent == null) {
	    pendingContent = "";
	}
	pendingContent += content;
    }

    private String digestPendingContent() {
	String s = pendingContent;
	pendingContent = null;
	return s;
    }

    public void handleStartedGroups(Symbol currentSymbol) {
	SymbolType ctype = currentSymbol.getType();
	if (ctype.equals(SymbolType.GROUP_START)) {
	    startedGroups.push(currentSymbol);
	    // System.out.println("group started for "+currentSymbol);
	} else if (ctype.equals(SymbolType.GROUP_END) && !startedGroups.isEmpty()) {
	    startedGroups.pop();
	    // System.out.println("group closed for "+currentSymbol);
	}
    }

    /**
     * Checks if the last started group contains the specified flag
     * 
     * @param flag
     * @return
     */
    private boolean checkGroupFlag(String flag) {
	if (hasStartedGroups()) {
	    return startedGroups.peek().containsFlag(flag);
	}
	return false;
    }
    
    private boolean checkActiveContextFlag(String flag) {
	if(activeContexts.size() > 0) {
	    return activeContexts.peek().containsFlag(flag);
	}
	return false;
    }

    public boolean checkPotentialNestedSymbolMatch(String s) {
	if (!startedGroups.isEmpty()) {
	    return startedGroups.peek().checkPotentialNestedSymbolMatch(s);
	}
	return false;
    }

    public boolean hasStartedGroups() {
	return !startedGroups.isEmpty();
    }

    public ParseContextConfig getActiveContext(ParseContextConfig currentContext) {
	if (activeContexts.size() > 0) {
	    return activeContexts.peek();
	}
	return currentContext;
    }

    public SymbolSet getActiveSymbolSet() {
	if (hasStartedGroups()) {
	    return startedGroups.peek().getNestedSymbols();
	}
	return null;
    }

    public List<Symbol> getNestedSymbol(String seq) {
	if (!startedGroups.isEmpty()) {
	    return startedGroups.peek().getNestedSymbol(seq);
	}
	return new ArrayList<Symbol>();
    }

    public void onStartOfFile(ParseContextConfig context) {
	activeContexts.push(context);
    }

    public void onEndOfFile(String content) throws ParserException, ContextAwareException {
	while (waitingStack.size() > 0) {
	    List<Symbol> pairSymbols = waitingStack.pop().getSymbol().getPairSymbols();

	    /* if at least one pair symbol is multiple don't mention the error */
	    boolean allowed = false;
	    for (Symbol ps : pairSymbols) {
		if (ps.containsFlag(SymbolFlag.MULTIPLE)) {
		    allowed = true;
		    break;
		}
	    }
	    if (!allowed) {
		throw new ParserException("Expected: " + pairSymbols + "\nwaitingStack: " + waitingStack
			+ "\navailableStack: " + availableStack);
	    }
	}

	ParseContextConfig activeContext = getActiveContext(null);
	boolean add = false;
	if (activeContext != null) {
	    String trimmedContent = content.trim();
	    boolean ignoreEmpty = activeContext.containsFlag(SymbolFlag.IGNORE_EMPTY);
	    add = !ignoreEmpty || !"".equals(trimmedContent);
	    if (add && ignoreEmpty) {
		content = trimmedContent;
	    }
	} else {
	    add = (previousSymbol != null && (previousSymbol.getType().equals(SymbolType.SEPARATE) || previousSymbol
		    .getType().equals(SymbolType.ASSOCIATE)));

	}
	;
	if (add) {
	    Object handledContent = eventHandler.handleEmptyString(content);
	    makeAvailable(handledContent);
	}
    }

    private void handleStartGroup(Symbol symbol, int startIndex, String content) throws ContextAwareException {
	// System.out.println(symbol+" at:"+startIndex+" content:"+content);

	if (symbol.containsFlag(SymbolFlag.SEPARATE)) {
	    handleSeparate(symbol, startIndex, content);
	}
	WaitInfo wi = new WaitInfo(symbol, content, availableStack.size());
	wi.setFoundIndex(startIndex);
	waitingStack.push(wi);
	startedGroups.push(symbol);
	if (symbol.isOverrideSuperContextConfig()) {
	    activeContexts.push(symbol);
	}
	// if(symbol.containsFlag(SymbolFlag.FOLLOW_TYPES)) {
	// currentGroupTypes.push(new HashSet<Class<?>>());
	// }
    }

    private void handleEndGroup(Symbol symbol, int startIndex, String content) throws ContextAwareException {
	// System.out.println(symbol+" at:"+startIndex+" content:"+content);
	/* if the stack is empty it means the text is not formatted return or throw an error */
	if (waitingStack.size() > 0) {

	    // boolean isIgnored = SymbolFlags.IGNORE.equals(waitingStack.peek().getSymbol().getAction());
	    boolean isIgnored = waitingStack.peek().getSymbol().containsFlag(SymbolFlag.IGNORE);

	    if (!isIgnored) {
		/* end group also plays a separator role */
		handleSeparate(symbol, startIndex, content);
	    }
	    /*
	     * and now the next object in the waiting stack should be the one created when the group started, so let's
	     * try to make an object from it.
	     */
	    WaitInfo wi = waitingStack.pop();
	    Symbol startGroupSymbol = wi.getSymbol();
	    /* if this group is ignored, drop the content */
	    if (!isIgnored) {

		/* get the objects from the group */
		List<Object> objects = getAvailableObjectFromIndex(wi.getAvailableStackIndex());

		GroupEvent ge = new GroupEvent();
		ge.setEndSymbol(symbol);
		
		ge.setStartSymbol(startGroupSymbol);
		ge.setStartIndex(wi.getFoundIndex());
		ge.setObjects(objects);
		ge.setPrefixValue(wi.getPreviousValue());
		/* delegate to the event handler to create an object, push it to the stack */

		Object resObj = eventHandler.handleGroupEvent(ge);

		/* call workers if any */
		if (startGroupSymbol.hasWorkers()) {
		    resObj = startGroupSymbol.applyWorkers((Collection<Object>) resObj);
		}

		if (symbol.containsFlag(SymbolFlag.UNGROUP)) {
		    for (Object o : (Collection) resObj) {
			makeAvailable(o);
		    }
		} else {
		    makeAvailable(resObj);
		}

		// if(startGroupSymbol.containsFlag(SymbolFlag.FOLLOW_TYPES)) {
		// currentGroupTypes.pop();
		// }
	    }

	    if (!startedGroups.isEmpty()) {
		startedGroups.pop();
	    }
	    if (startGroupSymbol.isOverrideSuperContextConfig()) {
		activeContexts.pop();
	    }
	}

    }

    private void handleAssociate(Symbol symbol, int startIndex, String content) throws ContextAwareException {
	Object prevValue = getPreviousValue(content);
	WaitInfo prevWaiting = waitingStack.peek();
	if (prevWaiting != null) {
	    Symbol prevSymbol = prevWaiting.getSymbol();
	    /* if the previous waiting symbol has higher priority than this one, should be handled first */
	    if (prevSymbol.getType().equals(SymbolType.ASSOCIATE) && prevSymbol.getPriority() <= symbol.getPriority()) {
		/* handle the previous waiting object with higher priority */
		// handleWaitingObject(prevValue);
		makeAvailable(prevValue);
		prevValue = getNextAvailableObject();
	    }
	}
	WaitInfo wi = new WaitInfo(symbol, prevValue, availableStack.size());
	wi.setFoundIndex(startIndex);
	waitingStack.push(wi);
    }

    private void handleSeparate(Symbol symbol, int startIndex, String content) throws ContextAwareException {
	SymbolType prevSymbolType = null;
	boolean isSeparate = false;
	boolean isAssociation = false;
	if (previousSymbol != null) {
	    prevSymbolType = previousSymbol.getType();
	    isSeparate = prevSymbolType.equals(SymbolType.SEPARATE);
	    isAssociation = prevSymbolType.equals(SymbolType.ASSOCIATE);
	}

	boolean isEmptyAndIgnorable = content.trim().isEmpty() && checkActiveContextFlag(SymbolFlag.IGNORE_EMPTY);//checkGroupFlag(SymbolFlag.IGNORE_EMPTY);

	if (prevSymbolType != null && (isSeparate || isAssociation)) {
	    // /* if the last stared group ignores empty elements and this is string is empty do nothig */
	    if (isAssociation || !isEmptyAndIgnorable) {
		Object handledContent = eventHandler.handleEmptyString(content);
		makeAvailable(handledContent);
	    }
	} else if (isEmptyAndIgnorable) {
	    return;
	} else {
	    makeAvailable(getPreviousValue(content));
	}
    }

    private void handleUniqueGroupSeparator(Symbol symbol, int startIndex, String content) throws ContextAwareException {
	if (waitingStack.size() == 0) {
	    atomicGroupStarted = true;
	    handleStartGroup(symbol, startIndex, content);
	    return;
	}
	WaitInfo wi = waitingStack.peek();
	if (wi.getSymbol().equals(symbol)) {
	    /* we have an unpaired symbol of this type so this one must be closing the group */
	    atomicGroupStarted = false;
	    /* we will use all the content received until the group was started */
	    handleEndGroup(symbol, startIndex, digestPendingContent());
	} else {
	    atomicGroupStarted = true;
	    handleStartGroup(symbol, startIndex, content);
	}
    }

    /**
     * Returns the previous value for the current symbol If the parsed content is not empty or null this one is returned
     * otherwise the last object made available on the stack is returned
     * 
     * @param content
     * @return
     * @throws ContextAwareException
     */
    private Object getPreviousValue(String content) throws ContextAwareException {
	if (content != null && !"".equals(content)) {
	    return eventHandler.handleEmptyString(content);
	} else if (availableStack.size() > 0) {
	    return getNextAvailableObject();
	}
	return null;
    }

    private void handleWaitingObject() throws ContextAwareException {
	if (waitingStack.size() <= 0) {
	    return;
	}
	WaitInfo wi = waitingStack.peek();
	/* this method only handles ASSOCIATIVE symbols */
	if (!wi.getSymbol().getType().equals(SymbolType.ASSOCIATE)) {
	    return;
	}
	wi = waitingStack.pop();
	Object input = getNextAvailableObject();
	AssociationEvent ae = new AssociationEvent();
	ae.setSymbol(wi.getSymbol());
	ae.setStartIndex(wi.getFoundIndex());
	ae.setPrefixValue(wi.getPreviousValue());
	ae.setPostfixValue(input);
	makeAvailable(eventHandler.handleAssociationEvent(ae));
    }

    private void makeAvailable(Object input) throws ContextAwareException {
	if (input != null) {
	    availableStack.push(input);
	} else {
	    availableStack.push(new NullObject());
	}
	handleWaitingObject();

	// /* follow types */
	// if(!currentGroupTypes.isEmpty()) {
	// Set<Class<?>> types = currentGroupTypes.peek();
	// if(!availableStack.isEmpty()) {
	// Object obj = availableStack.peek();
	// if(!(obj instanceof NullObject)) {
	// types.add(obj.getClass());
	// }
	// }
	// }
    }

    private List<Object> getAvailableObjectFromIndex(int index) {
	List<Object> data = new ArrayList<Object>();
	while (availableStack.size() > index) {
	    data.add(getNextAvailableObject());
	}
	Collections.reverse(data);
	return data;
    }

    private Object getNextAvailableObject() {
	Object o = availableStack.pop();
	if (o instanceof NullObject) {
	    return null;
	}
	return o;
    }

    /**
     * @return the eventHandler
     */
    public ParseEventHandler getEventHandler() {
	return eventHandler;
    }

    /**
     * @param eventHandler
     *            the eventHandler to set
     */
    public void setEventHandler(ParseEventHandler eventHandler) {
	this.eventHandler = eventHandler;
    }

    public Deque<Object> getObjectsStack() {
	return availableStack;
    }

    class NullObject {
    }
}
