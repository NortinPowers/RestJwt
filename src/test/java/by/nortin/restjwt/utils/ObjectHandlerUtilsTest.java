package by.nortin.restjwt.utils;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.Test;

class ObjectHandlerUtilsTest {

    private TestIncludeObject testIncludeObject;
    private TestObject testObject;

    @Test
    void test_getIgnoreProperties_noInclude_allField() {
        testIncludeObject = new TestIncludeObject(1L, "testName");
        testObject = new TestObject(1L, "name", 18, true, testIncludeObject);

        assertArrayEquals(new String[] {}, ObjectHandlerUtils.getIgnoreProperties(testObject));
    }

    @Test
    void test_getIgnoreProperties_noInclude_semiEmptyIncludeTestObject() {
        testIncludeObject = new TestIncludeObject(1L, null);
        testObject = new TestObject(1L, "name", 18, true, testIncludeObject);

        assertArrayEquals(new String[] {}, ObjectHandlerUtils.getIgnoreProperties(testObject));
    }

    @Test
    void test_getIgnoreProperties_include_nullIncludeTestObject() {
        testIncludeObject = null;
        testObject = new TestObject(1L, "name", 18, true, testIncludeObject);

        assertArrayEquals(new String[] {"includeObject"}, ObjectHandlerUtils.getIgnoreProperties(testObject));
    }

    @Test
    void test_getIgnoreProperties_include_nullFieldsTestObject() {
        testIncludeObject = new TestIncludeObject(1L, "testName");

        testObject = new TestObject(1L, null, null, true, testIncludeObject);

        assertArrayEquals(new String[] {"name", "age"}, ObjectHandlerUtils.getIgnoreProperties(testObject));
    }

    @Test
    void test_getIgnoreProperties_include_nullBooleanFieldTestObject() {
        testIncludeObject = new TestIncludeObject(1L, "testName");
        testObject = new TestObject(1L, "name", 18, null, testIncludeObject);

        assertArrayEquals(new String[] {"trust"}, ObjectHandlerUtils.getIgnoreProperties(testObject));
    }

    record TestObject(Long id, String name, Integer age, Boolean trust, TestIncludeObject includeObject) {

    }

    record TestIncludeObject(Long id, String name) {

    }

}
