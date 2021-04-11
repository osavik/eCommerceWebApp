package com.udacity.examples.Testing;

import junit.framework.TestCase;
import org.junit.*;

import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;

import static org.junit.Assert.*;

public class HelperTest {

    @Before
    public void init() {
        System.out.println("init executed");
    }

    @After
    public void teardown() {
        System.out.println("teardown executed");
    }

    // This method must be public and static
    @BeforeClass
    public static void initClass() {
        System.out.println("init Class executed");
    }
    @AfterClass
    public static void teardownclass() {
        System.out.println("teardown Class executed");
    }

    @Test
    public void test(){
        assertEquals(3, 3);
    }

    //@Ignore
    @Test
    public void verify_getCount(){
        List<String> empNames = Arrays.asList("Viktor", "udacity");
        long countExpected = Helper.getCount(empNames);
        assertEquals(2, countExpected);
    }

    @Test
    public void verify_getStats(){
        List<Integer> yrsOfExperience = Arrays.asList(13,4,15,6,17,8,19,1,2,3);
        List<Integer> expectedList = Arrays.asList(13,4,15,6,17,8,19,1,2,3);
        IntSummaryStatistics stats = Helper.getStats(yrsOfExperience);
        assertEquals(1, stats.getMin());

        assertEquals(expectedList,yrsOfExperience);
    }

    @Test
    public void compare_arrays(){
        int [] yrs = {1, 3, 5};
        int [] expected = {1, 3, 5};
        assertArrayEquals(yrs, expected);
    }

    @Test
    public void verify_getMergedList(){
        List<String> empNames = Arrays.asList("Viktor", "udacity", "");
        String mergedString = Helper.getMergedList(empNames);
        String expectedString = "Viktor, udacity";
        assertEquals(expectedString, mergedString);
    }
	
}
