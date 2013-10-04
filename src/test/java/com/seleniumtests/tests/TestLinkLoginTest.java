package com.seleniumtests.tests;

import com.seleniumtests.controller.ContextManager;
import com.seleniumtests.controller.EasyFilter;
import com.seleniumtests.controller.SeleniumTestPlan;
import com.seleniumtests.dataobject.User;
import com.seleniumtests.util.SpreadSheetUtil;
import com.seleniumtests.util.internal.entity.TestObject;
import com.seleniumtests.webpage.TestLinkLoginPage;
import org.testng.ITestContext;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 * Login test for TestLink
 *
 * Date: 10/2/13
 * Time: 6:36 PM
 */
public class TestLinkLoginTest extends SeleniumTestPlan {

    @DataProvider(name = "loginData", parallel = true)
    public static Iterator<Object[]> getUserInfo(Method m,
                                                 ITestContext testContext) throws Exception {
        EasyFilter filter = EasyFilter.equalsIgnoreCase(TestObject.TEST_METHOD,
                m.getName());
        filter = EasyFilter.and(filter, EasyFilter.equalsIgnoreCase(
                TestObject.TEST_SITE,
                ContextManager.getTestLevelContext(testContext).getSite()));

        LinkedHashMap<String, Class<?>> classMap = new LinkedHashMap<String, Class<?>>();
        classMap.put("TestObject", TestObject.class);
        classMap.put("User", User.class);

        return SpreadSheetUtil.getEntitiesFromSpreadsheet(
                TestLinkLoginTest.class, classMap, "loginuser.csv", 0,
                null, filter);
    }

    /**
     * Logs in to TestLink as valid user
     *
     * @param testObject
     * @param user
     * @throws Exception
     */
    @Test(groups = {"loginAsValidUser"}, dataProvider = "loginData",
            description = "Logs in to TestLink as admin")
    public void loginAsValidUser(TestObject testObject, final User user)
            throws Exception {

        new TestLinkLoginPage(true)
                .loginAsValidUser(user)
                .verifyDocumentationDropDown();
    }

    /**
     * Logs in to TestLink as invalid user
     *
     * @param testObject
     * @param user
     * @throws Exception
     */
    @Test(groups = {"loginAsInvalidUser"}, dataProvider = "loginData",
            description = "Logs in to TestLink as invalid user")
    public void loginAsInvalidUser(TestObject testObject, final User user)
            throws Exception {

        new TestLinkLoginPage(true)
                .loginAsInvalidUser(user)
                .verifyLoginBoxPresence();
    }

    /**
     * A failed test
     *
     * @param testObject
     * @param user
     * @throws Exception
     */
    @Test(groups = {"testForFailure"}, dataProvider = "loginData",
            description = "This test is bound to fail")
    public void testForFailure(TestObject testObject, final User user)
            throws Exception {

        new TestLinkLoginPage(true)
                .loginAsValidUser(user)
                .verifyDocumentationDropDownFail();
    }




}
