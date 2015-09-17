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
package net.segoia.util.parser.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.segoia.commons.exceptions.ContextAwareException;
import net.segoia.util.data.NameValue;
import net.segoia.util.data.reflection.ReflectionUtility;
import net.segoia.util.parser.event.AssociationEvent;
import net.segoia.util.parser.event.GroupEvent;
import net.segoia.util.parser.event.ParseEventHandler;

public class CollectionMapParseEventHandler implements ParseEventHandler {
    private MultivaluedTypeDefinition<?, ?> collectionTypeDef = new MultivaluedTypeDefinition<ArrayList, String>(
	    ArrayList.class, String.class);
    private MapTypeDefinition<?, ?, ?> mapTypeDef = new MapTypeDefinition<HashMap, String, String>(HashMap.class,
	    String.class, String.class);

    public CollectionMapParseEventHandler() {
	super();
    }

    public CollectionMapParseEventHandler(MultivaluedTypeDefinition<?, ?> collectionTypeDef,
	    MapTypeDefinition<?, ?, ?> mapTypeDef) {
	super();
	this.collectionTypeDef = collectionTypeDef;
	this.mapTypeDef = mapTypeDef;
    }

    public Object handleAssociationEvent(AssociationEvent event) {
	return new NameValue<Object>(event.getPrefixValue().toString(), event.getPostfixValue());
    }

    public Object handleEmptyString(String content) {
	return content.trim();
    }

    public Object handleGroupEvent(GroupEvent event) throws ContextAwareException {
	String prefix = event.getStartSymbol().getSequence();
	List<?> objects = event.getObjects();
	if ("{".equals(prefix)) {
	    Map<Object, Object> map = null;
	    try {
		// map = mapImplType.newInstance();
		map = (Map<Object, Object>) mapTypeDef.getMultivaluedType().newInstance();
	    } catch (Exception e) {

	    }
	    for (Object o : objects) {
		NameValue<Object> nv = (NameValue<Object>) o;

		Object key = ReflectionUtility.createObjectByTypeAndValue(mapTypeDef.getKeyType(), nv.getName());
		Object value = nv.getValue();
		if (value instanceof String) {
		    value = ReflectionUtility.createObjectByTypeAndValue(mapTypeDef.getNestedType(), (String) value);
		}
		map.put(key, value);
	    }
	    return map;
	} else if ("[".equals(prefix)) {
	    Collection<Object> collection = null;
	    try {
		// collection = collectionImplType.newInstance();
		collection = (Collection<Object>) collectionTypeDef.getMultivaluedType().newInstance();
	    } catch (Exception e) {

	    }
	    for (Object o : objects) {
		if (o instanceof String) {
		    collection.add(ReflectionUtility.createObjectByTypeAndValue(collectionTypeDef.getNestedType(),
			    (String) o));
		} else {
		    collection.add(o);
		}
	    }
	    return collection;
	} else if ("\"".equals(prefix) && objects.size() >0) {
	    return objects.get(0);
	}
	return null;
    }

    /**
     * @return the collectionTypeDef
     */
    public MultivaluedTypeDefinition<?, ?> getCollectionTypeDef() {
	return collectionTypeDef;
    }

    /**
     * @return the mapTypeDef
     */
    public MapTypeDefinition<?, ?, ?> getMapTypeDef() {
	return mapTypeDef;
    }

    /**
     * @param collectionTypeDef
     *            the collectionTypeDef to set
     */
    public void setCollectionTypeDef(MultivaluedTypeDefinition<?, ?> collectionTypeDef) {
	this.collectionTypeDef = collectionTypeDef;
    }

    /**
     * @param mapTypeDef
     *            the mapTypeDef to set
     */
    public void setMapTypeDef(MapTypeDefinition<?, ?, ?> mapTypeDef) {
	this.mapTypeDef = mapTypeDef;
    }

}
