package luke.opennms.quest;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class SnmpTrapFilter {

    static SortedMap<String, String> oidPrefixes = new TreeMap<>();
    static Integer minPrefixLength = Integer.MAX_VALUE;

    public void loadOidPrefixes() {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("snmp.yaml");
        Map<String, Object> obj = new Yaml().load(inputStream);
        List<String> prefixes = (List<String>) obj.get("trap-type-oid-prefix");
        for (String prefix : prefixes) {
            oidPrefixes.put(prefix, prefix);
            int totalParts = prefix.split("\\.").length - 1;
            if (totalParts < minPrefixLength) {
                minPrefixLength = totalParts;
            }
        }
        oidPrefixes.keySet().stream().forEach(System.out::println);
        System.out.println("min: "+minPrefixLength);
    }

    public boolean hasPrefix(String input) {
        System.out.println("Testing : "+input);
        Map<String,String> tailMap = oidPrefixes.tailMap(input);
        if((!tailMap.isEmpty() && ((SortedMap<String, String>) tailMap).firstKey().startsWith(input))){
            return true;
        }else{
            String [] parts = input.split("\\.");
            int totalParts = Math.abs(parts.length - minPrefixLength);
            for (int i = 0; i < totalParts; i++) {
                System.out.println("Checking Index: "+input);
                if (oidPrefixes.containsKey(input)) {
                    return true;
                }
                input = input.substring(0, input.lastIndexOf('.'));
            }
        }
        return false;
    }

    public static void main(String[] args) {
        SnmpTrapFilter snmpTrapFilter = new SnmpTrapFilter();
        snmpTrapFilter.loadOidPrefixes();

        String[] inputs = {
            ".1.3.6.1.4.1.9.9.117.2.0.1",
            ".1.3.6.1.4.1.9.9.117",
            ".1.3.6.1.4.1.9.9.118.2.0.1"
        };
        for (String input : inputs) {
            System.out.println(snmpTrapFilter.hasPrefix(input));
            System.out.println("---------------------------");
        }
    }
}
