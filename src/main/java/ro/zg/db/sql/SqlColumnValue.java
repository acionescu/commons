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
package ro.zg.db.sql;

public class SqlColumnValue {
    private String columnName;
    private String rawValue;
    private SqlType sqlType;
    
    public static SqlColumnValue getSqlColumnValue(String columnName,Object value,SqlType sqlType){
	String rawValue = null;
	if(value != null){
	    rawValue = value.toString();
	}
	
	SqlColumnValue w = new SqlColumnValue();
	w.setColumnName(columnName);
	w.setRawValue(rawValue);
	w.setSqlType(sqlType);
	return w;
    }
    
    public String getColumnName() {
        return columnName;
    }
    public String getRawValue() {
        return rawValue;
    }
    public SqlType getSqlType() {
        return sqlType;
    }
    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }
    public void setRawValue(String rawValue) {
        this.rawValue = rawValue;
    }
    public void setSqlType(SqlType sqlType) {
        this.sqlType = sqlType;
    }
    
    public String getValue(){
	if(rawValue == null){
	    return null;
	}
	String value = null;
	switch(sqlType){
		case NUMBER: value = rawValue;break;
		case STRING: value = "'"+rawValue+"'";break;
		/*TODO: format this */
		case DATE: value= rawValue;break;
		
	}
	return value;
    }
    
    public String getSqlFragment(){
	StringBuffer sqlf = new StringBuffer(256);
	sqlf.append(columnName);
	sqlf.append(" ");
	String value = getValue();
	if(value == null){
	    sqlf.append("is null");
	}
	else{
	    sqlf.append("= ").append(value);
	}
	return sqlf.toString();
    }
}
