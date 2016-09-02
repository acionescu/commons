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
package net.segoia.util.data;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Test;

import junit.framework.Assert;

public class SetMapTest {

    @Test
    public void test() {
	SetMap<String, String> s = new SetMap<String, String>();
	
	s.add("1","1");
	s.add("1", "2");
	
	s.add("2", "5");
	
	HashSet<String> c = new HashSet<String>(Arrays.asList(new String[] {"1","2"}));
	
	Assert.assertEquals(s.get("1"), c);
    }
    
}
