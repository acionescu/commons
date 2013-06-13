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

import ro.zg.util.calculator.operators.Operator;
import ro.zg.util.calculator.operators.OperatorInput;


public class DoubleOperandExpression extends SimpleExpression{
    private Expression firstOperand;
    private Expression secondOperand;
    
    public DoubleOperandExpression(){
	
    }
    
    public DoubleOperandExpression(Expression op1,Expression op2, String operator){
	super(operator);
	firstOperand = op1;
	secondOperand = op2;
    }
    
    public String asString() {
	return firstOperand.asString()+getOperator()+secondOperand.asString();
    }
    public ExpressionResult calculate(Map<String, Operator> operators, Map<String, Number> values) {
	Operator operator = operators.get(getOperator());
	ExpressionResult res1 = firstOperand.calculate(operators, values);
	ExpressionResult res2 = secondOperand.calculate(operators, values);
	OperatorInput input = null;
	if(res1.isNumber() && res2.isNumber()){
	    input = new OperatorInput(res1.getNumber(),res2.getNumber());
	}
	 
	return new ExpressionResult(operator.doOperation(input).getResult());
    }
    public Expression getFirstOperand() {
        return firstOperand;
    }
    public Expression getSecondOperand() {
        return secondOperand;
    }
    public void setFirstOperand(Expression firstOperand) {
        this.firstOperand = firstOperand;
    }
    public void setSecondOperand(Expression secondOperand) {
        this.secondOperand = secondOperand;
    }
    

}
