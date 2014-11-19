Nuxeo fleet API client
======================

A Nuxeo bundle providing a service to access [Fleet HTTP API](https://github.com/coreos/fleet/blob/master/Documentation/api-v1.md).

It's currently bound to `/v1-alpha` endpoint.

Deployment
----------

To deploy this bundle in your Nuxeo

    $ git clone https://github.com/nuxeo/nuxeo-fleet
    $ cd nuxeo-fleet
    $ mvn -DskipTests package
    $ cp target/nuxeo-fleet-*.jar $NUXEO_SERVER/nxserver/bundles

Configuration
-------------

Endpoint `configuration` sample:

    <extension target="org.nuxeo.fleet.service.FleetService" point="configuration">
      <configuration>
        <endpoint>http://localhost:4002</endpoint>
      </configuration>
    </extension>

About Nuxeo
-----------

Nuxeo provides a modular, extensible Java-based
[open source software platform for enterprise content management](http://www.nuxeo.com/en/products/ep),
and packaged applications for [document management](http://www.nuxeo.com/en/products/document-management),
[digital asset management](http://www.nuxeo.com/en/products/dam) and
[case management](http://www.nuxeo.com/en/products/case-management).

Designed by developers for developers, the Nuxeo platform offers a modern
architecture, a powerful plug-in model and extensive packaging
capabilities for building content applications.

More information on: <http://www.nuxeo.com/>