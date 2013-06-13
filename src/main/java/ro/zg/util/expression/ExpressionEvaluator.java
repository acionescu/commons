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
package ro.zg.util.expression;

import java.math.BigDecimal;

public class ExpressionEvaluator {
    
    public BigDecimal add(String a, String b){
	return new BigDecimal(a).add(new BigDecimal(b));
    }
    
    public BigDecimal subtract(String a, String b){
	return new BigDecimal(a).subtract(new BigDecimal(b));
    }
    
    public BigDecimal multiply(String a, String b){
	return new BigDecimal(a).multiply(new BigDecimal(b));
    }
    
    public BigDecimal divide(String a, String b){
	return new BigDecimal(a).divide(new BigDecimal(b));
    }
}
