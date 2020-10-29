Part 2 - Java Development

We have a series of SNMP traps which we wish to filter based on the trap type
(or enterprise) OID. If the OID start with any of the given prefixes, we will
perform further processing on the SNMP trap, otherwise it will be discarded.

For this project we want to build a solution that is capable of evaluating
whether or not a given trap type OID starts with any of the configured prefixes
and perform this filtering in an efficient fashion. The list of prefixes for
which to filter will be fixed for a given run and will normally contain
several hundred entries.

Configuration will be provided via a YAML file, for example snmp.yaml
may contain:

trap-type-oid-prefix:
- .1.3.6.1.6.3.1.1.5
- .1.3.6.1.2.1.10.166.3
- .1.3.6.1.4.1.9.9.117.2
- .1.3.6.1.2.1.10.32.0.1
- .1.3.6.1.2.1.14.16.2.2
- .1.3.6.1.4.1.9.10.137.0.1

The application should read the trap type OID from stdin, evaluate whether
or not it matches any of the given prefixes and output the result via stdout:

Input: .1.3.6.1.4.1.9.9.117.2.0.1
Expected output: .1.3.6.1.4.1.9.9.117.2.0.1: true

Input: .1.3.6.1.4.1.9.9.117
Expected output: .1.3.6.1.4.1.9.9.117: true

Input: .1.3.6.1.4.1.9.9.118.2.0.1
Expected output: .1.3.6.1.4.1.9.9.118.2.0.1: false


The solution must be written in Java and should contain unit tests to verify the functionality.
Documentation should be provided for compiling and running the solution
Evaluation will be based on accuracy, efficiency and completeness.

Download Source
---------------
```git clone https://github.com/lhsamad/snmp-trap-filter.git```

Config
---------------
Edit the `src/main/resources/snmp.yaml` or pass in from command line `-Dsnmp_trap_config=/Users/luqmansamad/projects/snmp.yaml`

Run With Maven
---------------
```mvn compile exec:java -Dsnmp_trap_config=/Users/luqmansamad/projects/snmp.yaml```

Build and run jar
---------------
```
mvn clean install
mvn clean compile assembly:single
java -jar target/snmp-trap-filter-1.0-SNAPSHOT-jar-with-dependencies.jar -Dsnmp_trap_config=/Users/luqmansamad/projects/snmp.yaml
```



