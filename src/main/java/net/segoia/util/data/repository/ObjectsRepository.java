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
package net.segoia.util.data.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import net.segoia.util.data.ObjectsUtil;
import net.segoia.util.data.SetMap;
import net.segoia.util.data.reflection.ReflectionUtility;

/**
 * Implements a repository for a certain type of object (all the objects should share a common interface) this can also
 * be used as a repository of repositories
 * 
 * @author adi
 * 
 * @param <O>
 */
public class ObjectsRepository<O> implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -2852851812850022179L;
    /**
     * keeps a map with all the maps used to search objects by certain properties </br>
     * key - the name of the property used to search </br>
     * value - the map with all the mapped values for the property
     */
    private Map<String, SetMap<Object, O>> indexMap = new TreeMap<String, SetMap<Object, O>>();
    /**
     * the list of searchable properties
     */
    private List<String> indexProperties = new ArrayList<String>();

    private Set<O> allObjects = new HashSet<O>();

    public ObjectsRepository() {

    }

    /**
     * @return the indexProperties
     */
    public List<String> getIndexProperties() {
	return indexProperties;
    }

    /**
     * @param indexProperties
     *            the indexProperties to set
     */
    public void setIndexProperties(List<String> indexProperties) {
	this.indexProperties = indexProperties;
    }

    public void addIndexProperty(String property) {
	if (!indexProperties.contains(property)) {
	    indexProperties.add(property);
	}
    }

    public boolean addObject(O obj) {
	boolean added = allObjects.add(obj);
	if (added) {
	    buildIndexesForObject(obj);
	}
	return added;
    }

    public int addObjects(Collection<O> c) {
	int addedCount = 0;
	for (O o : c) {
	    boolean added = addObject(o);
	    if (added) {
		addedCount++;
	    }
	}
	return addedCount;
    }

    /**
     * adds the current object to the indexes according to the specified index properties
     * 
     * @param obj
     */
    private void buildIndexesForObject(O obj) {
	for (String prop : indexProperties) {
	    try {
		Object value = ReflectionUtility.getValueForField(obj, prop);
		/* get the map for the current property */
		SetMap<Object, O> listMap = indexMap.get(prop);
		if (listMap == null) {
		    listMap = SetMap.createTreeMapWithTreeSet();
		    indexMap.put(prop, listMap);
		}
		listMap.add(value, obj);
	    } catch (Exception e) {
		System.out.println("Error building index for prop " + prop + " on object " + obj);
		e.printStackTrace();
		/* do nothing */
	    }
	}
    }

    /**
     * Returns the list of objects that have the specified value for the property
     * 
     * @param propName
     * @param propValue
     * @return
     */
    public Collection<O> getObjectsForProperty(String propName, Object propValue) {
	SetMap<Object, O> valuesMap = indexMap.get(propName);
	if (valuesMap == null) {
	    return null;
	}
	return valuesMap.get(propValue);
    }

    /**
     * Will return the first object from the list found for the specified search criteria
     * 
     * @param propName
     * @param propValue
     * @return
     */
    public O getSingleObjectForProperty(String propName, Object propValue) {
	Collection<O> valCol = getObjectsForProperty(propName, propValue);
	if (valCol == null || valCol.size() == 0) {
	    return null;
	}
//	List<O> valList = new ArrayList<O>(valCol);
//	return valList.get(0);
	return valCol.iterator().next();
    }

    public Map<Object, O> getObjectsMapForProperty(String searchPropName, Object searchValue, String keyPropName) {
	Collection<O> list = getObjectsForProperty(searchPropName, searchValue);
	return (Map) ObjectsUtil.createMapFromCollection(list, keyPropName);
    }

    /**
     * Returns a map with the keys representing the possible values for the specified property and the value
     * representing the list with the objects that have the property value equal with the key
     * 
     * @param propName
     * @return
     */
    public Map<Object, Set<O>> getObjectsByProperty(String propName) {
	return indexMap.get(propName).getAll();
    }

    public Collection<Object> getPossibleValuesForPropertyAsList(String propName) {
	SetMap<Object, O> listVal = indexMap.get(propName);
	if (listVal == null) {
	    return null;
	}
	return listVal.keySet();
    }

    public List<O> getAllObjectsList() {
	return new ArrayList<O>(allObjects);
    }

    public boolean containsObjectWithProperty(String propName, String propValue) {
	SetMap<Object, O> valuesMap = indexMap.get(propName);
	if (valuesMap == null) {
	    return false;
	}
	Collection<O> objectsList = valuesMap.get(propValue);
	return (objectsList != null && objectsList.size() > 0);
    }

    private void removeIndexesForObject(O obj) {
	for (String prop : indexProperties) {
	    try {
		Object value = ReflectionUtility.getValueForField(obj, prop);
		/* get the map for the current property */
		SetMap<Object, O> listMap = indexMap.get(prop);
		if (listMap != null) {
		    listMap.removeValueForKey(value, obj);
		}
	    } catch (Exception e) {
		System.out.println("Error building index for prop " + prop + " on object " + obj);
		e.printStackTrace();
		/* do nothing */
	    }
	}
    }
    
    public boolean removeObject(O obj) {
	boolean removed = allObjects.remove(obj);
	if (removed) {
	    removeIndexesForObject(obj);
	}
	return removed;
    }
    
    public ObjectsRepositoryResponse<O> removeObjectForProperty(String propName, Object propValue) {
	Collection<O> objectsForProperty = getObjectsForProperty(propName, propValue);
	List<Exception> errors=null;
	Collection<O> results = new HashSet<>();
	if(objectsForProperty != null) {
//	    System.out.println(this+" removing "+objectsForProperty.size()+" objects for prop "+propName+"->"+propValue);
	    for(O obj : objectsForProperty) {
		boolean removed = removeObject(obj);
		if(!removed) {
		    if(errors == null) {
			errors=new ArrayList<>();
		    }
		    errors.add(new RuntimeException("Failed to remove object "+obj+" but indexed for "+propName+"->"+propValue));
		}
		else {
		    results.add(obj);
		}
	    }
	}
	
	return new ObjectsRepositoryResponse<>(results, errors);
    }
}
