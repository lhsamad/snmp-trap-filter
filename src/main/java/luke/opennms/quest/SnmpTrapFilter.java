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
    static private SortedMap<String, String> oidPrefixes = new TreeMap<>();
    static private Integer minPrefixLength = Integer.MAX_VALUE;

    public SnmpTrapFilter() {

        Map<String, Object> obj = new Yaml().load(getSnmpTrapConfigFile());
        List<String> prefixes = (List<String>) obj.get("trap-type-oid-prefix");
        for (String prefix : prefixes) {
            oidPrefixes.put(prefix, prefix);
            int parts = prefix.split("\\.").length;
            if (parts < minPrefixLength) {
                minPrefixLength = parts;
            }
        }
        System.out.println("\n\n------------------ snmp.yaml configuration contains ------------------");
        oidPrefixes.keySet().stream().forEach(System.out::println);
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
        //default resource folder
        return this.getClass().getClassLoader().getResourceAsStream("snmp.yaml");
    }

    public boolean hasPrefix(String input) {
        if (!oidPrefixes.isEmpty()) {
            Map<String, String> tailMap = oidPrefixes.tailMap(input);
            if ((!tailMap.isEmpty() && ((SortedMap<String, String>) tailMap).firstKey().startsWith(input))) {
                return true;
            } else {
                int count = input.split("\\.").length - minPrefixLength;
                while (count > 0) {
                    if (oidPrefixes.containsKey(input)) {
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
