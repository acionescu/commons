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

import java.util.Map;
import java.util.regex.Pattern;

import ro.zg.util.calculator.operators.Operator;

public class ConstantExpression implements Expression {
    private String value;
    private static Pattern pattern = Pattern.compile("\\d*\\.*\\d*");
    private ExpressionResult cachedValue;

    public ConstantExpression() {

    }

    public ConstantExpression(String v) {
	this.value = v;
    }

    public String asString() {
	return value;
    }

    public ExpressionResult calculate(Map<String, Operator> operators, Map<String, Number> values) {
	/* if we have the result cached return it */
	if (cachedValue != null) {
	    return cachedValue;
	}
	/* check if the value is a number */
	if (pattern.matcher(value).matches()) {
	    /* if this is a plain number return it as it is */
	    cachedValue = new ExpressionResult(Double.parseDouble(value));
	    return cachedValue;
	}
	/* maybe this is a variable, so search in the variables list */
	Number result = null;
	if (values != null) {
	    result = values.get(value);
	}
	if (result != null) {
	    /*
	     * return both the number and the string, the number can be used as variable, and the value as a constant
	     */
	    return new ExpressionResult(result, value);
	}
	/* we've arrived here, and could't get a number for the specified value, return it as string */
	return new ExpressionResult(value);

    }

    public String getValue() {
	return value;
    }

    public static void main(String args[]) {
	System.out.println(".34".matches("\\d*\\.*\\d*"));
	System.out.println(Double.parseDouble(".34"));
    }

}
