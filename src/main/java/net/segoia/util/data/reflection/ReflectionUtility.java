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
/**
 * $Id: ReflectionUtility.java,v 1.7 2008/07/15 13:48:30 Exp $
 */
package net.segoia.util.data.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import net.segoia.commons.exceptions.ContextAwareException;
import net.segoia.commons.exceptions.ExceptionContext;
import net.segoia.util.data.GenericNameValue;
import net.segoia.util.parser.utils.JavaCollectionMapParser;

/**
 * A reflection utility.
 * 
 * @author Adrian Ionescu
 * @version $Revision: 1.7 $
 * 
 */
public class ReflectionUtility {
    private static final String[] getterPrefixes = { "get", "is" };
    private static JavaCollectionMapParser defaultCollectionMapParser = new JavaCollectionMapParser();

    /**
     * gets the setter method for a field based on the javabeans standard
     * 
     * @param obj
     *            - target object
     * @param fieldName
     *            - field name
     * @return a Method object
     * @throws Exception
     */
    public static Method getSetterMethodForField(Object obj, String fieldName) throws Exception {
	StringBuffer methodName = new StringBuffer(64);
	Field field = null;

	methodName.append("set");
	methodName.append(fieldName.substring(0, 1).toUpperCase());
	methodName.append(fieldName.substring(1));
	field = getFieldForFieldName(obj.getClass(), fieldName);

	return obj.getClass().getMethod(methodName.toString(), new Class[] { field.getType() });

    }

    /**
     * gets the setter method for a field based on the javabeans standard
     * 
     * @param obj
     *            - target object
     * @param field
     * @return
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    public static Method getSetterMethodForField(Object obj, Field field) throws SecurityException,
	    NoSuchMethodException {
	String fieldName = field.getName();
	StringBuffer methodName = new StringBuffer(64);

	methodName.append("set");
	methodName.append(fieldName.substring(0, 1).toUpperCase());
	methodName.append(fieldName.substring(1));

	return obj.getClass().getMethod(methodName.toString(), new Class[] { field.getType() });
    }

    /**
     * gets the getter method for the field based on the javabeans standard
     * 
     * @param obj
     * @param field
     * @return
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    public static Method getGetterMethodForField(Object obj, Field field) throws SecurityException,
	    NoSuchMethodException {
	String fieldName = field.getName();
	StringBuffer methodName = new StringBuffer(64);
	String fieldType = field.getType().getSimpleName();
	if (fieldType.equals("boolean") || fieldType.equals("Boolean")) {
	    methodName.append("is");
	} else {
	    methodName.append("get");
	}

	methodName.append(fieldName.substring(0, 1).toUpperCase());
	methodName.append(fieldName.substring(1));

	return obj.getClass().getMethod(methodName.toString(), new Class[0]);
    }

    public static Method getGetterMethodForField(Object obj, String fieldName) throws SecurityException,
	    NoSuchMethodException {
	StringBuffer methodName = null;

	StringBuffer capitalMethodName = new StringBuffer(64);
	capitalMethodName.append(fieldName.substring(0, 1).toUpperCase());
	capitalMethodName.append(fieldName.substring(1));

	for (String prefix : getterPrefixes) {
	    methodName = new StringBuffer(64);
	    methodName.append(prefix).append(capitalMethodName);
	    Method getter = obj.getClass().getMethod(methodName.toString(), new Class[0]);
	    if (getter != null) {
		return getter;
	    }
	}
	return null;
    }

    public static boolean hasGetterForProperty(String propName, Object target) throws Exception {
	Field f = getFieldForFieldName(target.getClass(), propName);
	Method getter = getGetterMethodForField(target, f);
	return getter != null;
    }

    /**
     * Sets the specified value to the specified field on the specified object
     * 
     * @param obj
     *            the target object
     * @param field
     *            the target field
     * @param value
     *            the value
     * @throws Exception
     *             can throw field access exceptions
     */
    public static void setValueToField(Object obj, Field field, String value) throws Exception {
	String typeName = field.getType().getSimpleName();// gets the type of
	// the field
	Method setterMethod = null;
	try {
	    setterMethod = getSetterMethodForField(obj, field);
	} catch (Exception e) {
	    // ignore
	} // gets the
	  // actual
	  // setter
	  // method
	  // for the
	  // field
	Object[] valueObject = new Object[] { null };

	/**
	 * based on the field type tries to set the value on the field
	 */
	if (field.getType().isEnum()) {
	    Object enumVal = Class.forName(field.getType().getName())
		    .getMethod("valueOf", new Class[] { String.class }).invoke(null, new Object[] { value });
	    valueObject = new Object[] { enumVal };
	} else if (typeName.equals("int")) {
	    valueObject = new Object[] { new Integer(value) };
	} else if (typeName.equals("boolean")) {
	    valueObject = new Object[] { new Boolean(value) };
	} else if (typeName.equals("long")) {
	    valueObject = new Object[] { new Long(value) };
	} else if (typeName.equals("byte")) {
	    valueObject = new Object[] { new Byte(value) };
	} else if (typeName.equals("short")) {
	    valueObject = new Object[] { new Short(value) };
	} else if (typeName.equals("float")) {
	    valueObject = new Object[] { new Float(value) };
	} else if (typeName.equals("double")) {
	    valueObject = new Object[] { new Double(value) };
	} else if (typeName.equals("Integer")) {
	    valueObject = new Object[] { new Integer(value) };
	} else if (typeName.equals("Boolean")) {
	    valueObject = new Object[] { new Boolean(value) };
	} else if (typeName.equals("Long")) {
	    valueObject = new Object[] { new Long(value) };
	} else if (typeName.equals("Byte")) {
	    valueObject = new Object[] { new Byte(value) };
	} else if (typeName.equals("Short")) {
	    valueObject = new Object[] { new Short(value) };
	} else if (typeName.equals("Float")) {
	    valueObject = new Object[] { new Float(value) };
	} else if (typeName.equals("Double")) {
	    valueObject = new Object[] { new Double(value) };
	} else if (typeName.equals("String")) {
	    valueObject = new Object[] { value };
	} else if (typeName.equals("Date")) {
	    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
	    Date d = sdf.parse(value);
	    valueObject = new Object[] { d };
	} else if (typeName.equals("List")) {
	    List listObj = (List) getGetterMethodForField(obj, field).invoke(obj, new Object[0]);
	    if (listObj == null) {
		if (!field.getType().isInterface()) {
		    listObj = (List) field.getType().newInstance();
		} else {
		    listObj = new Vector(); // use Vector as default
					    // implementation of List
		}
		if (setterMethod != null) {
		    setterMethod.invoke(obj, new Object[] { listObj });
		} else {
		    throw new Exception("No setter method found for field " + field.getName());
		}
	    }
	    if (listObj != null) {
		listObj.add(value);
		return;
	    }
	} else { // else assume Object
	    valueObject = new Object[] { value };
	}
	if (setterMethod == null) { // no setter method found
	    throw new Exception("No setter method found for field " + field.getName());
	}
	try {
	    setterMethod.invoke(obj, valueObject);// sets the value
	} catch (Exception e) {
	    throw new Exception("Failed to set property '" + field.getName() + "' with value '" + value
		    + "' on object " + obj, e);
	}
    }

    public static boolean checkSimpleType(Object target) {

	if (target instanceof String) {
	    return true;
	} else if (target instanceof Integer) {
	    return true;
	} else if (target instanceof Boolean) {
	    return true;
	} else if (target instanceof Long) {
	    return true;
	} else if (target instanceof Float) {
	    return true;
	} else if (target instanceof Byte) {
	    return true;
	} else if (target instanceof Short) {
	    return true;
	} else if (target instanceof Double) {
	    return true;
	}
	return false;
    }

    public static boolean checkSimpleFieldType(Class<?> type) {
	String typeName = type.getName();

	if (typeName.contains(".") && !typeName.startsWith("java")) {
	    return false;
	}

	if (typeName.equals("java.lang.String")) {
	    return true;
	} else if (typeName.equals("int")) {
	    return true;
	} else if (typeName.equals("boolean")) {
	    return true;
	} else if (typeName.equals("long")) {
	    return true;
	} else if (typeName.equals("byte")) {
	    return true;
	} else if (typeName.equals("short")) {
	    return true;
	} else if (typeName.equals("float")) {
	    return true;
	} else if (typeName.equals("double")) {
	    return true;
	} else if (typeName.equals("char")) {
	    return true;
	} else if (typeName.equals("java.lang.Integer")) {
	    return true;
	} else if (typeName.equals("java.lang.Boolean")) {
	    return true;
	} else if (typeName.equals("java.lang.Long")) {
	    return true;
	} else if (typeName.equals("java.lang.Byte")) {
	    return true;
	} else if (typeName.equals("java.lang.Short")) {
	    return true;
	} else if (typeName.equals("java.lang.Float")) {
	    return true;
	} else if (typeName.equals("java.lang.Double")) {
	    return true;
	} else if (typeName.equals("java.lang.Character")) {
	    return true;
	} else if (typeName.equals("java.util.Date")) {
	    return true;
	} else if (typeName.equals("java.math.BigDecimal")) {
	    return true;
	}

	if (type.getSuperclass() != null) {
	    return checkSimpleFieldType(type.getSuperclass());
	}
	return false;
    }

    /**
     * Overloaded method for {@link #setValueToField(Object, Field, String)}
     * 
     * @param obj
     * @param fieldName
     * @param value
     * @throws Exception
     */
    public static void setValueToField(Object obj, String fieldName, String value) throws Exception {
	setValueToField(obj, getFieldForFieldName(obj.getClass(), fieldName), value);
    }

    public static Field getFieldForFieldName(Class<?> clazz, String fieldName) throws Exception {
	Field field = null;
	Class<?> currentClazz = clazz;
	if (fieldName.contains(".")) {
	    String[] names = fieldName.split("\\.");
	    for (int i = 0; i < names.length; i++) {
		field = getFieldForFieldName(currentClazz, names[i]);
		if (field != null) {
		    currentClazz = field.getType();
		}
	    }
	    return field;
	}
	while (field == null && currentClazz != null) {
	    try {
		field = currentClazz.getDeclaredField(fieldName);
	    } catch (NoSuchFieldException ex) {// if no such field try to search
					       // it on the superclass
		currentClazz = currentClazz.getSuperclass();
	    }
	}
	if (field == null) {
	    throw new NoSuchFieldException("No field with name " + fieldName + " found on class " + clazz.getName());
	}
	return field;
    }

    public static void setValueToField(Object obj, String fieldName, Object refObj) throws Exception {
	/*
	 * in case we want to set a nested object property, we use prop1.prop2.prop3
	 */
	if (fieldName.contains(".")) {
	    String[] propSequence = fieldName.split("\\.");
	    Object currentObj = obj;
	    int seqLength = propSequence.length - 1;
	    /* iterate through the nested objects */
	    for (int i = 0; i < seqLength; i++) {
		System.out.println("get value " + propSequence[i] + " from " + currentObj);
		currentObj = getValueForField(currentObj, propSequence[i]);

		if (currentObj == null) {
		    ExceptionContext ec = new ExceptionContext();
		    ec.put("reason", "NULL_TARGET_OBJECT");
		    ec.put("targetObj", obj);
		    ec.put("fieldPath", fieldName);
		    ec.put("nullObjectIndex", i);

		    throw new ContextAwareException("SET_VALUE_ERROR", ec);
		}
	    }

	    /*
	     * once the object on which we want to set the property is obtained, we do it
	     */
	    setValueToField(currentObj, getFieldForFieldName(currentObj.getClass(), propSequence[seqLength]), refObj);
	} else {
	    setValueToField(obj, getFieldForFieldName(obj.getClass(), fieldName), refObj);
	}
    }

    public static Collection<?> createCollection(Class<?> clazz, Collection<?> values) throws Exception {
	return (Collection<?>) clazz.getConstructor(Collection.class).newInstance(values);
    }

    public static Map<?, ?> createMap(Class<?> clazz, Collection<?> values, String keyProp) throws Exception {
	Map map = (Map) clazz.getConstructor().newInstance();
	for (Object v : values) {
	    map.put(getValueForField(v, keyProp), v);
	}
	return map;
    }

    /**
     * Tries to set a value of type Object for the specified field on the specified object
     * 
     * @param obj
     * @param field
     * @param refObj
     * @throws Exception
     */
    public static void setValueToField(Object obj, Field field, Object refObj) throws Exception {
	Class<?> fieldType = field.getType();
	try {
	    /* if the value type matches the field type */
	    if (refObj == null || areTypesCompatible(refObj.getClass(), fieldType)) {
		// if (refObj == null ||
		// fieldType.isAssignableFrom(refObj.getClass())) {
		getSetterMethodForField(obj, field).invoke(obj, new Object[] { refObj });// sets the value
	    } else if (refObj instanceof String) {// if string try to guess the
		// type
		setValueToField(obj, field, (String) refObj);
	    } else if (fieldType.getName().equals("java.util.List")) {
		List listObj = (List) getGetterMethodForField(obj, field).invoke(obj, new Object[0]);
		if (listObj == null) {
		    if (!field.getType().isInterface()) {
			listObj = (List) field.getType().newInstance();
		    } else {
			listObj = new Vector(); // use Vector as default
						// implementation of List
		    }
		    getSetterMethodForField(obj, field).invoke(obj, new Object[] { listObj });
		}
		listObj.add(refObj);

	    } else {
		throw new Exception("field's class type <<" + fieldType.getName() + ">> and object's class type <<"
			+ refObj.getClass().getName() + ">> don't match");
	    }
	} catch (Exception e) {
	    throw new Exception("Failed to set value " + refObj + " to field " + field.getName() + " for type "
		    + obj.getClass(), e);
	}
    }

    public static Object createObjectByTypeAndValue(Class<?> type, String value) throws ContextAwareException {
	if (value == null) {
	    return null;
	}

	if (type.isEnum()) {
	    try {
		return Class.forName(type.getName()).getMethod("valueOf", new Class[] { String.class })
			.invoke(null, new Object[] { value });
	    } catch (Exception e) {
		ExceptionContext ec = new ExceptionContext();
		ec.put(new GenericNameValue("type", type));
		ec.put(new GenericNameValue("value", value));
		throw new ContextAwareException("TYPE_CONVERSION_EXCEPTION", e, ec);
	    }
	} else if (type.isAssignableFrom(Class.class)) {
	    if ("".equals(value.trim())) {
		return null;
	    }
	    try {
		return Class.forName(value);
	    } catch (ClassNotFoundException e) {
		ExceptionContext ec = new ExceptionContext();
		ec.put(new GenericNameValue("type", value));

		throw new ContextAwareException("CLASS_NOT_FOUND", e, ec);
	    }
	}
	return createObjectByTypeAndValue(type.getSimpleName(), value);
    }

    public static Object createObjectByTypeAndValue(String typeName, String value) throws ContextAwareException {
	Object newValue = value;
	try {
	    if (typeName.equals("String")) {
		newValue = value;
	    } else if (typeName.equals("int")) {
		try {
		return new Integer(value).intValue();
		}
		catch(NumberFormatException ex) {
		    return new BigDecimal(value).intValue();
		}
	    } else if (typeName.equals("boolean")) {
		return new Boolean(value).booleanValue();
	    } else if (typeName.equals("long")) {
		return new Long(value).longValue();
	    } else if (typeName.equals("byte")) {
		return new Byte(value).byteValue();
	    } else if (typeName.equals("short")) {
		return new Short(value).shortValue();
	    } else if (typeName.equals("float")) {
		return new Float(value).floatValue();
	    } else if (typeName.equals("double")) {
		return new Double(value).doubleValue();
	    } else if (typeName.equals("char")) {
		return value.charAt(0);
	    } else if (typeName.equals("Integer")) {
		newValue = new Integer(value);
	    } else if (typeName.equals("Boolean")) {
		newValue = new Boolean(value);
	    } else if (typeName.equals("Long")) {
		newValue = new Long(value);
	    } else if (typeName.equals("Byte")) {
		newValue = new Byte(value);
	    } else if (typeName.equals("Short")) {
		newValue = new Short(value);
	    } else if (typeName.equals("Float")) {
		newValue = new Float(value);
	    } else if (typeName.equals("Double")) {
		newValue = new Double(value);
	    } else if (typeName.equals("BigDecimal")) {
		newValue = new BigDecimal(value);
	    } else if (typeName.equals("Number")) {
		newValue = new BigDecimal(value);
	    } else if (typeName.equals("Character")) {
		newValue = new Character(value.charAt(0));
	    } else if (typeName.equals("Date")) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		Date d;
		try {
		    d = sdf.parse(value);
		} catch (ParseException e) {
		    ExceptionContext ec = new ExceptionContext();
		    ec.put(new GenericNameValue("format", "dd.MM.yyyy"));
		    ec.put(new GenericNameValue("string", value));
		    throw new ContextAwareException("DATE_CONVERSION_EXCEPTION", e, ec);
		}
		newValue = d;
	    } else if (typeName.equals("Object")) {
		newValue = value;
	    } else {
		ExceptionContext ec = new ExceptionContext();
		ec.put(new GenericNameValue("type", typeName));
		ec.put(new GenericNameValue("value", value));
		throw new ContextAwareException("TYPE_CONVERSION_EXCEPTION", ec);
	    }
	} catch (Exception e) {
	    ExceptionContext ec = new ExceptionContext();
	    ec.put(new GenericNameValue("type", typeName));
	    ec.put(new GenericNameValue("value", value));
	    throw new ContextAwareException("TYPE_CONVERSION_EXCEPTION", e, ec);
	}
	return newValue;
    }

    /**
     * Call a method on an object
     * 
     * @param obj
     * @param methodName
     * @param args
     * @throws Exception
     */
    public static Object callMethod(Object obj, String methodName, Object[] args) throws Exception {
	// Class<?>[] paramTypes = null;
	// if (args != null) {
	// paramTypes = new Class[args.length];
	// for (int i = 0; i < args.length; i++) {
	// if (args[i] != null) {
	// paramTypes[i] = args[i].getClass();
	// } else {
	// paramTypes[i] = Object.class;
	// }
	// }
	// }
	return callMethod(obj, methodName, args, getTypes(args));
    }

    public static Class<?>[] getTypes(Object[] objects) {
	if (objects == null) {
	    return null;
	}
	Class<?>[] paramTypes = new Class[objects.length];
	for (int i = 0; i < objects.length; i++) {
	    if (objects[i] != null) {
		paramTypes[i] = objects[i].getClass();
	    } else {
		paramTypes[i] = Object.class;
	    }
	}
	return paramTypes;
    }

    public static Object callMethod(Object obj, String methodName, Object[] args, Class<?>[] types) throws Exception {
	Method method = null;
	try {
	    method = obj.getClass().getMethod(methodName, types);
	    return method.invoke(obj, args);
	} catch (Exception e) {

	    ExceptionContext ec = new ExceptionContext();
	    ec.put("className", obj.getClass().getName());
	    ec.put("methodName", methodName);
	    ec.put("argsTypes", Arrays.asList(types));
	    ec.put("args", Arrays.asList(args));
	    throw new ContextAwareException("METHOD_CALL_ERROR", e, ec);

	}

    }

    public static Object callStaticMetod(String className, String methodName, Object[] args, String[] types)
	    throws Exception {
	Class<?>[] ctypes = updateArgsForTypes(args, types);
	return callStaticMethod(className, methodName, args, ctypes);
    }

    public static Object callStaticMethod(String className, String methodName, Object[] args, Class<?>[] types)
	    throws Exception {
	Method method = Class.forName(className).getMethod(methodName, types);
	return method.invoke(null, args);
    }

    public static Object callStaticMethod(String className, String methodName, Object[] args) throws Exception {
	return callStaticMethod(className, methodName, args, getTypes(args));
    }

    public static Object callMethod(Object obj, String methodName, Object[] args, String[] types) throws Exception {
	// Class<?>[] ctypes = new Class<?>[types.length];
	// for (int i = 0; i < types.length; i++) {
	// String t = types[i];
	// if (t != null) {
	// if (t.startsWith("primitive:")) {
	// t = t.substring(t.indexOf(":") + 1);
	// Class clazz = Class.forName(t);
	// ctypes[i] = (Class) clazz.getField("TYPE").get(clazz);
	// args[i] = createObjectByTypeAndValue(ctypes[i].getSimpleName(),
	// args[i].toString());
	// } else {
	// ctypes[i] = Class.forName(t);
	// }
	// } else {
	// ctypes[i] = args.getClass();
	// }
	// }
	Class<?>[] ctypes = updateArgsForTypes(args, types);
	return callMethod(obj, methodName, args, ctypes);
    }

    private static Class<?>[] updateArgsForTypes(Object[] args, String[] types) throws Exception {
	Class<?>[] ctypes = new Class<?>[types.length];
	for (int i = 0; i < types.length; i++) {
	    String t = types[i];
	    if (t != null) {
		if (t.startsWith("primitive:")) {
		    t = t.substring(t.indexOf(":") + 1);
		    Class<?> clazz = Class.forName(t);
		    ctypes[i] = (Class<?>) clazz.getField("TYPE").get(clazz);
		    args[i] = createObjectByTypeAndValue(ctypes[i].getSimpleName(), args[i].toString());
		} else {
		    ctypes[i] = Class.forName(t);
		}
	    } else {
		ctypes[i] = args.getClass();
	    }
	}
	return ctypes;
    }

    /**
     * Obtains the value of a field from a specified object
     * 
     * @param obj
     * @param fieldName
     * @return
     * @throws Exception
     */
    public static Object getValueForField(Object obj, String fieldName) throws Exception {
	if (!".*".equals(fieldName) && fieldName.contains(".")) {
	    String[] props = fieldName.split("\\.");
	    Object target = obj;
	    for (int i = 0; i < props.length; i++) {
		target = getValueForField(target, props[i]);
		if (target == null) {
		    return null;
		}
	    }
	    return target;
	}
	try {
	    Field f = getFieldForFieldName(obj.getClass(), fieldName);
	    Method getterMethod = getGetterMethodForField(obj, f);
	    return getterMethod.invoke(obj, new Object[0]);
	} catch (Exception e) {
	    if (obj instanceof List) {
		return ((List) obj).get(Integer.parseInt(fieldName));
	    } else if (obj instanceof Map) {
		return ((Map) obj).get(fieldName);
	    } else if (obj.getClass().isArray()) {
		return ((Object[]) obj)[Integer.parseInt(fieldName)];
	    }
	    throw new Exception("No field '" + fieldName + "' on object " + obj.getClass());
	}
    }

    /**
     * Tests if the type of the field with the name propName matches searchClass type
     * 
     * @param object
     * @param propName
     * @param searchClass
     * @return
     * @throws Exception
     * @throws NoSuchFieldException
     */
    public static boolean checkFieldType(Object object, String propName, Class<?> searchClass) throws Exception,
	    NoSuchFieldException {

	// Field f = object.getClass().getDeclaredField(propName);
	Field f = getFieldForFieldName(object.getClass(), propName);
	Class<?> type = f.getType();

	try {
	    type.asSubclass(searchClass);
	    return true;
	} catch (ClassCastException e) {
	    return false;
	}
    }

    public static boolean areTypesCompatible(Class<?> sourceClass, Class<?> targetClass) throws Exception {
	try {

	    if (sourceClass.isPrimitive()) {
		if (!targetClass.isPrimitive()) {
		    targetClass = getPrimitiveType(targetClass);
		}
	    } else if (targetClass.isPrimitive()) {
		sourceClass = getPrimitiveType(sourceClass);
	    }

	    // subclass.asSubclass(parentClass);
	    return targetClass.isAssignableFrom(sourceClass);
	} catch (Exception e) {
	    return false;
	}
    }

    public static Class<?> getPrimitiveType(Class<?> c) throws ContextAwareException {
	try {
	    return (Class<?>) c.getField("TYPE").get(c);
	} catch (Exception e) {
	    ExceptionContext ec = new ExceptionContext();
	    ec.put(new GenericNameValue("class", c.getName()));
	    throw new ContextAwareException("GET_FIELD_ERROR", e, ec);
	}
    }

    private static String escapeString(String input) {
	return defaultCollectionMapParser.ESCAPE + input + defaultCollectionMapParser.ESCAPE;
    }

    public static String objectToString(Object obj) throws Exception {
	return objectToString(obj, false);
    }

    public static String objectToString(Object obj, boolean escapeString) throws Exception {
	if (obj == null) {
	    return null;
	}
	Class<?> propType = obj.getClass();

	if (propType.isArray()) {
	    return arrayToString((Object[]) obj);
	} else if (checkSimpleFieldType(propType)) {
	    if (obj instanceof String && escapeString) {
		return escapeString((String) obj);
	    }
	    return obj.toString();
	} else if (checkInstanceOf(propType, Map.class)) {
	    return mapToString((Map<Object, Object>) obj).toString();
	} else if (checkInstanceOf(propType, Collection.class)) {
	    return collectionToString((Collection<Object>) obj).toString();
	} else if (obj instanceof Class) {
	    return ((Class<?>) obj).getName();
	} else {
	    Map<String, Object> holder = new HashMap<String, Object>();
	    recursiveObjectToString(obj, propType, holder);
	    return holder.toString();
	}
    }

    public static void recursiveObjectToString(Object obj, Class<?> type, Map<String, Object> holder) throws Exception {
	if (type == null) {
	    if (obj == null) {
		return;
	    }
	    type = obj.getClass();
	}
	/* superclass fields */
	if (type.getSuperclass() != null) {
	    recursiveObjectToString(obj, type.getSuperclass(), holder);
	}
	/* current class fields */
	Field[] fields = type.getDeclaredFields();
	for (Field f : fields) {
	    if (Modifier.isTransient(f.getModifiers())) {
		continue;
	    }
	    String propName = f.getName();
	    Object propValue = null;
	    if (obj != null) {
		try {
		    propValue = getValueForField(obj, propName);
		} catch (Exception e) {
		    // probably ther is no getter. Skip this
		    continue;
		}
	    }
	    // Class<?> propType = f.getType();
	    // if (checkSimpleFieldType(propType)) {
	    // holder.put(propName, propValue);
	    // } else if (checkInstanceOf(propType, Map.class)) {
	    // holder.put(propName, mapToString((Map) propValue));
	    // } else if (checkInstanceOf(propType, List.class)) {
	    // holder.put(propName, listToString((List) propValue));
	    // } else {
	    // Map<String, Object> nestedObjectMap = new HashMap<String,
	    // Object>();
	    // recursiveObjectToString(propValue, propType, nestedObjectMap);
	    // holder.put(f.getName(), nestedObjectMap);
	    // }

	    holder.put(propName, objectToString(propValue));
	}
    }

    public static Map<Object, Object> mapToString(Map<Object, Object> sourceMap) throws Exception {
	if (sourceMap == null) {
	    return null;
	}
	Map<Object, Object> response = new LinkedHashMap<Object, Object>();

	for (Map.Entry<Object, Object> entry : sourceMap.entrySet()) {
	    response.put(objectToString(entry.getKey()), objectToString(entry.getValue(), true));
	}
	return response;
    }

    public static List<Object> collectionToString(Collection<Object> sourceList) throws Exception {
	if (sourceList == null) {
	    return null;
	}
	List<Object> responseList = new ArrayList<Object>();
	for (Object o : sourceList) {
	    responseList.add(objectToString(o, true));
	}
	return responseList;
    }

    public static String arrayToString(Object[] sourceArray) throws Exception {
	return collectionToString(Arrays.asList(sourceArray)).toString();
    }

    public static boolean checkInstanceOf(Class<?> target, Class<?> superclass) {
	if (target == null || superclass == null) {
	    return false;
	}
	try {
	    target.asSubclass(superclass);
	} catch (Exception e) {
	    return false;
	}
	return true;
    }

}
