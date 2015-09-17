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
package net.segoia.util.calculator.event;

import net.segoia.util.calculator.Calculator;
import net.segoia.util.calculator.ConstantExpression;
import net.segoia.util.calculator.Expression;
import net.segoia.util.parser.event.AssociationEvent;
import net.segoia.util.parser.event.GroupEvent;
import net.segoia.util.parser.event.ParseEventHandler;

public class MathExpressionParserEventHandler implements ParseEventHandler{

    public Object handleAssociationEvent(AssociationEvent event) {
	return Calculator.buildExpression(event.getSymbol().getSequence(), (Expression)event.getPrefixValue(), (Expression)event.getPostfixValue());
    }

    public Object handleGroupEvent(GroupEvent event) {
	return event.getObjects().get(0);
    }

    public Object handleEmptyString(String content) {
	return new ConstantExpression(content);
    }

}
