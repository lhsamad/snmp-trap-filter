package luke.opennms.quest;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class SnmpTrapFilterTest {

    private static SnmpTrapFilter snmpTrapFilter = null;

    @BeforeClass
    public static void init(){
        snmpTrapFilter = new SnmpTrapFilter();
    }

    @Test
    public void testValid1() {
        Assert.assertEquals(true, snmpTrapFilter.hasPrefix(".1.3.6.1.4.1.9.9.117.2.0.1"));
    }

    @Test
    public void testValid2() {
        Assert.assertEquals(true, snmpTrapFilter.hasPrefix(".1.3.6.1.4.1.9.9.117"));
    }

    @Test
    public void testInValid() {
        Assert.assertEquals(false, snmpTrapFilter.hasPrefix(".1.3.6.1.4.1.9.9.118.2.0.1"));
    }

}
