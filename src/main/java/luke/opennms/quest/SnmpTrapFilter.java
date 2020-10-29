package luke.opennms.quest;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class SnmpTrapFilter {

    static private SortedMap<String, String> oidPrefixes = new TreeMap<>();
    static private Integer minPrefixLength = Integer.MAX_VALUE;

    public SnmpTrapFilter() {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("snmp.yaml");
        Map<String, Object> obj = new Yaml().load(inputStream);
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
