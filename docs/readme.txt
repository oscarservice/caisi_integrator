This is the integrator project (formerly known as the caisi_integrator2 project)

--------
Overview
--------

The original intent of the integrator project was to allow data sharing between multiple
independent oscar/caisi installations. i.e. sharing demographic information like a persons first 
and last name or picture or heath card number etc.

The data sharing functionality of this project is provided via web services. In theory this means 
the system is capable of sharing data between any web service capable system, not just oscar/caisi.
This portion will be referred to as the WebServices portion.

A secondary feature of the integrator project is to allow consolidated statistics (de-identified)
style reporting across the participating community. As an exmaple, to be able to track instances
of flu and to see any patters that may arise across the population. The reporting portion of this
project is provided through a standard web browser interface. This portion will be referred to as
the Site portion. 

-----------------
Technical details
-----------------

The integrator server is a standard maven2 project, just run 'mvn package' and it should spit out
something like caisi_integrator-0.0-SNAPSHOT.war in the target directory.

Running the maven tests will create the database tables for you.
Before the tests work it expects a blank schema is made with the name as configured in
the config.properties, i.e. caisi_integrator, with associated user/password. The test scripts
will drop all tables and create new tables with test data upon initialisation, the created
tables can be the basis for a running system. The purpose of this is mainly for testing.
This will help ensure the database is at an expected and reproducable state upon each 
junit test. If you want to create the database manually, you should be able to find
a create_tables.sql file in the target directory but you will not have
a default user/password set. The database is currently assumed to be Mysql
and using INNODB as the default table, you will need to configure this in your
my.cnf file. There is another way to create a blank database, there's a script
in the root called ./reset_blank_database.sh that should create a blank
database with a default user/password for you.

When you start tomcat you may pass it a java system property (a.k.a. -D property)
caisi_integrator_properties=<property_file> which overrides any values found in
the config.properties file. You can also pass in 
log4j.override.configuration=<path to log4j.xml> which will configure those settings
on top of the ones in the distributed log4j.xml file. The purpose of this is to allow you to 
customise your configuration with out touching things with in the war archive / directory
(which results is easier upgrades, or refreshes of the code base). 

Out of the box, the default configuration has 
https_endpoint_url_base=https://127.0.0.1:8086/caisi_integrator/ws
this is most likely incorrect for everyone, you must change the endpoint to be what ever
the client systems (oscar/caisi servers) will use to resolve to that location,
i.e. the ip address as seen from the oscar/caisi servers and the contextPath you've
deployed to. This is a CXF oddity in that we need to explicitly configure it. It maybe
resolved in a future version of CXF. 

There are utility scripts in the utils directory, they're not required, they just help 
with a few commonly run tasks. The build_client_stubs.xml has 2 targets of interest.
By default it runs build_client_stubs which builds the client stubs for the 
web services and produces a jar with everything in it in the target directory.
There's also a run_test_client target which will build and run a few simple 
client tests against the server, the server must already be running for both the above to work.

If you're using an IDE to work with this code base, you're going to want to
"ignore missing serialVersionUID". We do not support compatibility between
compiled class files except with in the exact same build instance. Beyond
the above change, there should generally be no errors or warnings of any
types in the source code. Maven checkstyle will be used to try and enforce some semblance 
of code-style-format.

It is technically feasible to run the integrator and just use the WebServices portion.
To configure new Facilities, you need to manually add entries into the "Facility" table.

If you intend to run the Site portion you need to manually add an entry into the SiteUser table.

There's an option of using recaptcha to prevent automated login attempts, for this
you need to at least configure 2 properties either in your config.xml or
your config override properties file. 

<server>
	<recaptcha>
		<!-- DO NOT CHECK IN RECAPTCHA KEYS INTO THE SOURCE REPOSITORIES -->
		<!-- Leaving the values blank will disable recaptcha on the login page -->
		<public_key></public_key>
		<private_key></private_key>
	</recaptcha>
<server>

You may need to sign up for recaptcha or if you are part of the oscar/caisi
developer community, you may be able to use our shared key. Ask the current 
maintainer of this project for the shared key.

-----
Url's
-----

Once the system is running, you should be able to go to http://127.0.0.1:8085/caisi_integrator
to see both the WSDL's as well as the reporting site. The default user/password for the system should be admin/admin.

--------
Upgrades
--------
A little history :
Previously there were no upgrades, this was because the system was to be wiped clean upon reinstalls as all data was considered cached and volatile.

This has since changed. As a result we need an upgrade path. The upgrades from now on will be done via the web site, example url : http://127.0.0.1:8085/caisi_integrator/site/upgrade.jsf

This leaves a 1 time anomalie for people upgrading from the no-upgrades version to the auto-upgrades version. For this one migration, people should do the following :
1) do the schema dump / diff as previously did to upgrade the schema structure itself.
2) go to the upgrade page it should ask you to upgrade from version 0 to version 1, i.e. http://127.0.0.1:8085/caisi_integrator/site/upgrade.jsf 