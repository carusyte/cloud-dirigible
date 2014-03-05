dirigible
=========

Introduction
------------

### Overview ###

Dirigible is an open source project that provides Integrated Development Environment as a Service (IDEaaS) as well as the runtime engines integration for the running applications.
The applications created with Dirigible comply with the Dynamic Applications concepts and structure.
The environment itself runs directly in the browser, therefore does not require additional downloads and installations.
It packs all the needed components, which makes it self-contained and well integrated software bundle that can be deployed on any Java based Web Server such as Tomcat, Jetty, JBoss connected via JDBC to the RDBMS of your choice.

### Background ###

The Dirigible project came out of an internal SAP initiative to address the extension and adaptation use-cases around SOA and Enterprise Services.
On one hand in this project were implied the lessons learned from the standard tools and approaches so far and on the other hand, there were added features aligned with the most recent technologies and architectural patterns related to Web 2.0 and HTML5, which made it complete enough to be used as the only tool and environment needed for building and running on demand application in the cloud.

### Try ###

Dirigible project is deployed and tested against the [HANA Cloud Platform](https://account.hana.ondemand.com/).
You can start by creating your own unlimited free trial account at [https://account.hanatrial.ondemand.com/](https://account.hanatrial.ondemand.com/).
Sandbox instance with restricted functionality is available at: [http://sandbox.dirigible.io](http://sandbox.dirigible.io).


Build
-----

### Maven ###

1. Clone the repository 'https://github.com/SAP/dirigible.git' or [download the latest release](https://github.com/SAP/dirigible/archive/master.zip). 
2. Build the project with Maven running a "mvn clean install". The build should pass successfully.
3. The two produced WAR files dirigible-ide*.war and dirigible-runtime*.war are ready to be deployed on [HANA Cloud Platform](https://account.hana.ondemand.com/) with the [Cloud SDK](https://tools.hana.ondemand.com/#cloud)
4. Import the project as existing Maven project into your local Eclipse environment


Additional Information
----------------------

### License ###

This project is copyrighted by [SAP AG](http://www.sap.com/) and made available under the [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0.html). Please also confer to the text files "LICENSE" and "NOTICE" included with the project sources.


### Contributors ###

File an [issue](https://github.com/SAP/dirigible/issues) or send us a [pull request](https://github.com/SAP/dirigible/pulls).


References
----------

- Project Home
[http://www.dirigible.io](http://www.dirigible.io)

- Help Portal
[http://help.dirigible.io](http://help.dirigible.io)

- Simple Samples
[http://samples.dirigible.io](http://samples.dirigible.io)

- Sandbox Instance
[http://sandbox.dirigible.io](http://sandbox.dirigible.io)

- Forum
[http://forum.dirigible.io](http://forum.dirigible.io)

