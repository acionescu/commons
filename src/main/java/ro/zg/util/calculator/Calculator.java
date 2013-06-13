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
package ro.zg.util.calculator;

import java.util.HashMap;
import java.util.Map;

import ro.zg.util.calculator.event.MathExpressionParserEventHandler;
import ro.zg.util.calculator.operators.AddOperator;
import ro.zg.util.calculator.operators.ConstantOperator;
import ro.zg.util.calculator.operators.DecimalLogarithmOperator;
import ro.zg.util.calculator.operators.DivideOperator;
import ro.zg.util.calculator.operators.MultiplyOperator;
import ro.zg.util.calculator.operators.NaturalLogarithmOperator;
import ro.zg.util.calculator.operators.Operator;
import ro.zg.util.calculator.operators.PowerOperator;
import ro.zg.util.calculator.operators.SqrtOperator;
import ro.zg.util.calculator.operators.SubtractOperator;
import ro.zg.util.parser.Parser;
import ro.zg.util.parser.ParserException;
import ro.zg.util.parser.ParserHandlerFactory;
import ro.zg.util.parser.SymbolType;

public class Calculator {
    private static Map<String, Operator> operators;
    private static Parser parser;

    static {
	operators = new HashMap<String, Operator>();
	operators.put("+", new AddOperator());
	operators.put("-", new SubtractOperator());
	operators.put("/", new DivideOperator());
	operators.put("*", new MultiplyOperator());
	operators.put("sqrt", new SqrtOperator());
	operators.put("^", new PowerOperator());
	operators.put("ln", new NaturalLogarithmOperator());
	operators.put("lg", new DecimalLogarithmOperator());
	operators.put("const", new ConstantOperator());

	parser = new Parser();
	parser.setHandlerFactory(new ParserHandlerFactory(new MathExpressionParserEventHandler()));
	parser.addSymbol("+", SymbolType.ASSOCIATE, 5);
	parser.addSymbol("-", SymbolType.ASSOCIATE, 5);
	parser.addSymbol("*", SymbolType.ASSOCIATE, 3);
	parser.addSymbol("/", SymbolType.ASSOCIATE, 3);
	parser.addSymbol("sqrt", SymbolType.ASSOCIATE, 2);
	parser.addSymbol("^", SymbolType.ASSOCIATE, 2);
	parser.addSymbol("ln", SymbolType.ASSOCIATE, 2);
	parser.addSymbol("lg", SymbolType.ASSOCIATE, 2);
	parser.addSymbol("const", SymbolType.ASSOCIATE, 2);
	parser.addSymbol("(", SymbolType.GROUP_START, 0);
	parser.addSymbol(")", SymbolType.GROUP_END, 0);
    }

    public Calculator() {
    }

    public Number calculate(Expression expression, Map<String, Number> values) {
	return expression.calculate(operators, values).getNumber();
    }

    public Number evaluate(String string, Map<String, Number> values) throws ParserException {
	Expression expression = parseExpression(string);
	return calculate(expression, values);
    }

    public static Expression buildExpression(String operatorString, Expression exp1, Expression exp2) {
	Operator operator = operators.get(operatorString);
	if (operator.getArgumentsCount() == 2) {
	    return new DoubleOperandExpression(exp1, exp2, operatorString);
	} else if (operator.getArgumentsCount() == 1) {
	    return new SingleOperandExpression(operatorString, exp2);
	}
	return null;
    }

    public static Expression parseExpression(String string) throws ParserException {
	return (Expression) parser.parse(string).getObjects().pop();
    }

    public static void main(String[] args) throws ParserException {
	Calculator calc = new Calculator();
	// Map<String, Number> vals = new HashMap<String, Number>();
	// vals.put("a", 5);
	// Expression exp = new DoubleOperandExpression(new ConstantExpression("a"), new ConstantExpression("3"), "/");
	// System.out.println(calc.calculate(exp, vals));
	Map<String, Number> values = new HashMap<String, Number>();
//	values.put("e", 2);
//	System.out.println(calc.evaluate("(((2+8)*(3-2))+10)/a", values));
	System.out.println(calc.evaluate("const(e)+e", values));
    }

}
