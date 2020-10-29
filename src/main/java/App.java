import luke.opennms.quest.SnmpTrapFilter;

import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        SnmpTrapFilter snmpTrapFilter = new SnmpTrapFilter();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter Trap Type OID");

        String trapTypeOid = scanner.nextLine();
        System.out.println("Input: " + trapTypeOid);

        System.out.println("Expected output: " + trapTypeOid + ": " + snmpTrapFilter.hasPrefix(trapTypeOid));
    }
}
