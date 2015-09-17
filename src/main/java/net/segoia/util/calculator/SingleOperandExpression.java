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
package net.segoia.util.calculator;

import java.util.Map;

import net.segoia.util.calculator.operators.Operator;
import net.segoia.util.calculator.operators.OperatorInput;

public class SingleOperandExpression extends SimpleExpression{
    private Expression operand;
    
    public SingleOperandExpression(){
	
    }
    
    public SingleOperandExpression(String operator, Expression argument){
	super(operator);
	this.operand = argument;
    }
    
    public String asString() {
	return getOperator()+CalculatorSymbols.OPEN_GROUP+operand+CalculatorSymbols.CLOSE_GROUP;
    }
    

    public ExpressionResult calculate(Map<String, Operator> operators, Map<String, Number> values) {
	Operator operator = operators.get(getOperator());
	ExpressionResult res = operand.calculate(operators, values);
	OperatorInput input = null;
	if(res.isNumber()){
	    input = new OperatorInput(res.getNumber(),res.getString());
	}
	else{
	    input = new OperatorInput(res.getString());
	}
	return new ExpressionResult(operator.doOperation(input).getResult());
    }

    public Expression getOperand() {
        return operand;
    }

    public void setOperand(Expression operand) {
        this.operand = operand;
    }

}
