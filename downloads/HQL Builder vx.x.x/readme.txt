0.0 sources
===========

I recommend checking out the sources with Subversion and using Maven to handle dependencies
sources are included in directory sources

1.0 dependencies
================

I recommend checking out the sources with Subversion and using Maven to handle dependencies
you can use other versions than the ones below, for example use a more recent version of Hibernate and it's dependencies

run download-dependencies.bat found in directory libraries,
it will download all dependencies to the directory libraries\dependencies


1.1 service dependencies
------------------------
groovy-all version 2.1.5
lucene-highlighter version 3.6.0
spring-tx version 2.5.6
slf4j-api version 1.5.8
log4j version 1.2.16
antlr version 2.7.6
hibernate-validator version 3.1.0.GA
slf4j-log4j12 version 1.5.8
spring-orm version 2.5.6
commons-logging version 1.1.1
spring-context version 2.5.6
jakarta-regexp version 1.4
commons-lang version 2.6
lucene-analyzers version 3.6.0
commons-collections version 3.2.1
spring-jdbc version 2.5.6
spring-beans version 2.5.6
lucene-queries version 3.6.0
lucene-core version 3.6.0
dom4j version 1.6.1
spring-core version 2.5.6
groovy-engine version 20080808
hibernate-core version 3.6.10.Final
hibernate-jpa-2.0-api version 1.0.1.Final
aopalliance version 1.0
jta version 1.1
hibernate-commons-annotations version 3.2.0.Final
lucene-memory version 3.6.0

1.1 client dependencies
-----------------------
utils version 1.07.00
commons-codec version 1.5
groovy-all version 2.1.5
slf4j-api version 1.5.8
log4j version 1.2.16
joda-time version 1.6
antlr version 2.7.6
hibernate-validator version 3.1.0.GA
commons-beanutils-core version 1.8.3
stax-api version 1.0.1
slf4j-log4j12 version 1.5.8
miglayout-core version 4.2
poi-ooxml version 3.9
commons-logging version 1.1.1
itextpdf version 5.3.0
xmlbeans version 2.3.0
commons-lang version 2.6
poi-ooxml-schemas version 3.9
commons-collections version 3.2.1
flying-saucer-pdf-itext5 version 9.0.2
l2fprod-common-all version 7.3
flying-saucer-core version 9.0.2
jcalendar version 1.3.2
spring-beans version 2.5.6
swing-easy version 2.6.0.0
poi version 3.9
dom4j version 1.6.1
spring-core version 2.5.6
groovy-engine version 20080808
miglayout-swing version 4.2
hibernate-core version 3.6.10.Final
hibernate-jpa-2.0-api version 1.0.1.Final
jta version 1.1
glazedlists_java15 version 1.9.0
hibernate-commons-annotations version 3.2.0.Final
javassist version 3.18.0-GA


2.0 HQL Builder v1.0.2
======================
included in this archive

hql-builder-client-1.0.2.jar
hql-builder-common-1.0.2.jar
hql-builder-service-1.0.2.jar


3.0 sample project
==================

3.1 separate service and client
------------------------------

3.1.1 separate service
----------------------

add service dependencies
add hql-builder-common-1.0.2.jar to your dependencies
add hql-builder-client-1.0.2.jar to your dependencies
add the directory named 'sample-project/common' to your sources: contains a Hibernate entity class
add the directory named 'sample-project/service' to your sources: contains webservice configuration and the HQL Builder service config

you can change the database connection by editing the file 'hibernate.properties'
by default the standalone database Apache Derby is used wich can also be found on the Maven repository or downloaded

start as a webapp, should be available at http://localhost:PORTNUMBER/hqlbuilder/hqlbuilder

3.1.2 separate client
---------------------

add client dependencies
add hql-builder-common-1.0.2.jar to your dependencies
add hql-builder-service-1.0.2.jar to your dependencies
add the directory named 'sample-project/common' to your sources: contains a Hibernate entity class
add the directory named 'sample-project/client' to your sources: contains the HQL Builder client config

edit hqlwebclient.properties and change the url to http://localhost:PORTNUMBER/hqlbuilder/hqlbuilder
replace PORTNUMBER as configured in your IDE (most of the times 8080)

start the client: HqlBuilder.java

3.2 all in one service and client
---------------------------------

add service dependencies
add client dependencies
add hql-builder-common-1.0.2.jar to your dependencies
add hql-builder-client-1.0.2.jar to your dependencies
add hql-builder-service-1.0.2.jar to your dependencies
add the directory named 'sample-project/common' to your sources: contains a Hibernate entity class
add the directory named 'sample-project/all-in-one' to your sources: contains the HQL Builder service and HQL Builder client config

you can change the database connection by editing the file 'hibernate.properties'
by default the standalone database Apache Derby is used wich can also be found on the Maven repository or downloaded

this doesn't require a webserver

start the client: HqlBuilder.java