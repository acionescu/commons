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
