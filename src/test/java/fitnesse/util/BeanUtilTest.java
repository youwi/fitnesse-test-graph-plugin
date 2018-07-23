package fitnesse.util;

import org.junit.Test;

import static fitnesse.util.BeanUtil.getObjectByPath;
import static org.junit.Assert.*;

/**
 * pluginTestGraph
 * Created by yu on 2018/7/19.
 */
public class BeanUtilTest {
    String haha="xxx";
    ExampleTest ex=new ExampleTest();

    @Test
    public void testGetObjectByPath() throws Exception {
        assert "xxx".equals( getObjectByPath(this,"haha"));
        assert "SSS".equals(getObjectByPath(this,"ex.name"));
    }
}

class ExampleTest{
    String name="SSS";
}