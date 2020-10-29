package luke.opennms.quest;

import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class SnmpTrapFilter {

    static String SNMP_TRAP_CONFIG = "snmp_trap_config";
    /*
    * I wanta use a simple hashMap but I'm not sure where to stop looking "dpsRTU" I think but I need more understanding
    * This should be accurate
    * */
    static private SortedMap<String, String> PREFIXES = new TreeMap<>();
    static private Integer MIN_PREFIX_LENGTH = Integer.MAX_VALUE;

    public SnmpTrapFilter() {
        Map<String, Object> obj = new Yaml().load(getSnmpTrapConfigFile());
        List<String> prefixes = (List<String>) obj.get("trap-type-oid-prefix");
        for (String prefix : prefixes) {
            PREFIXES.put(prefix, prefix);
            int parts = prefix.split("\\.").length;
            if (parts < MIN_PREFIX_LENGTH) {
                MIN_PREFIX_LENGTH = parts;
            }
        }
        print();
    }

    private void print(){
        System.out.println("\n\n------------------ snmp.yaml configuration contains ------------------");
        PREFIXES.keySet().stream().sorted().forEach(System.out::println);
        System.out.println("--------------------------------------------------------------------------");
    }

    private InputStream getSnmpTrapConfigFile() {
        String snmpTrapConfig = System.getProperty(SNMP_TRAP_CONFIG);
        try {
            if (snmpTrapConfig != null) {
                return new FileInputStream(snmpTrapConfig);
            }
        } catch (FileNotFoundException e) {
            System.err.println("Config file not found at: " + snmpTrapConfig);
        }
        System.out.println("Loading snmp.yaml from src/main/resources/snmp.yaml");
        return this.getClass().getClassLoader().getResourceAsStream("snmp.yaml");
    }

    public boolean hasPrefix(String input) {
        if (!PREFIXES.isEmpty()) {
            Map<String, String> tailMap = PREFIXES.tailMap(input);
            if ((!tailMap.isEmpty() && ((SortedMap<String, String>) tailMap).firstKey().startsWith(input))) {
                return true;
            } else {
                int count = input.split("\\.").length - MIN_PREFIX_LENGTH;
                while (count > 0) {
                    if (PREFIXES.containsKey(input)) {
                        return true;
                    }
                    input = input.substring(0, input.lastIndexOf('.'));
                    count--;
                }
            }
        }
        return false;
    }

}
