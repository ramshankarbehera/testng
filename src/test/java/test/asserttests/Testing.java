package test.asserttests;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.internal.MethodInvocationHelper;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class Testing {

    public static final Object[][] DATA1 = { new Object[] { "first" },
            new Object[] { "second" } };

    Map<String,String> allTcData = new HashMap<>();
    StringBuffer allException = new StringBuffer();
    int count = 0;
    boolean test = true;

    public static final Object[][] DATA2 = { new Object[] { "first" }};
    @BeforeMethod(alwaysRun = true)
    public void before(Method m, ITestContext ctx,Object[] dpdata) throws NoSuchMethodException {

        int hid=0;
        Test a = m.getAnnotation(Test.class);
        boolean isMerge = a.isMerge();
        System.out.println(isMerge);
        String[] data = a.hodorId();
        for(String idata:data)
        {
            //hid = hid+  idata.split("#").length;
            hid++;
        }
       if (!isMerge)
            Assert.assertEquals(hid,MethodInvocationHelper.size,"The number of data provider is not matching with number of hodor ID");
    }
    @Test(isMerge=false,hodorId = {"ANM-001#ANM=005","ANM-002"},dataProvider="data1")
    public void testingMymethod(String data)
    {
        if(data.equalsIgnoreCase("first"))
            Assert.fail("exceptiontion is tested");
    }
    @Test(isMerge=true,hodorId = {"ANM-004"},dataProvider="data1")
    public void testingMymethodtest(String data)
    {
       if(data.equalsIgnoreCase("first"))
           Assert.fail("exceptiontion is tested");
    }
    @Test(hodorId = {"ANM-003"})
    public void te()
    {
        System.out.println();
    }
    @DataProvider
    public Object[][] data1() {
        return DATA1;
    }

    @AfterMethod(alwaysRun = true)
    public void afterMethod(Method m,ITestContext tc, ITestResult result)
    {
        Test a = m.getAnnotation(Test.class);
        String[] data = a.hodorId();
        boolean isMerge = a.isMerge();
        int size=0;
        if(MethodInvocationHelper.size==0)
            size=0;
        else
            size = MethodInvocationHelper.size-1;

        if(result.getStatus() == ITestResult.FAILURE) {
            allException.append(result.getThrowable());
        }
        if(isMerge) {
            if(!result.isSuccess() && test)
            {
              test = false;
            }

            if (count==size) {
                updateResult(data[0],test,allException);
                count=0;
                test=true;
                allException = new StringBuffer();
                return;
            }
            count++;
        }

        else
        {
            if (count<=size)
            {

                String[] eachDpData = data[count].split("#");

                for(String updateDp: eachDpData)
                    updateResult(updateDp,result.isSuccess(),allException);
                if(count == size)
                {
                    count = 0;
                    allException = new StringBuffer();
                }
            }
            count++;
        }
    }

    public void updateResult(String tcId, boolean status,StringBuffer exception)
    {
         if(status)
             System.out.println(tcId+ " result has updated as PASS");
         else
         {
             if(exception.length()!=0)
                 System.out.println(tcId+ " result has updated as FAIL with reason:- "+exception.toString());
             else
                 System.out.println(tcId+ " result has updated as FAIL");
         }


    }

}
