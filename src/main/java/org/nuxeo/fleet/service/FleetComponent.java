/*
 * (C) Copyright 2014 Nuxeo SA (http://nuxeo.com/) and contributors.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * Contributors:
 *     Nuxeo
 */

package org.nuxeo.fleet.service;

import java.io.IOException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.nuxeo.fleet.Error;
import org.nuxeo.fleet.Machines;
import org.nuxeo.fleet.PaginableResult;
import org.nuxeo.fleet.Unit;
import org.nuxeo.fleet.Units;
import org.nuxeo.runtime.model.ComponentContext;
import org.nuxeo.runtime.model.ComponentInstance;
import org.nuxeo.runtime.model.DefaultComponent;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

/**
 * @author <a href="mailto:ak@nuxeo.com">Arnaud Kervern</a>
 * @since 0.1
 */
public class FleetComponent extends DefaultComponent implements FleetService {

    private static final Log log = LogFactory.getLog(FleetComponent.class);

    private static final String CONFIGURATION_EP = "configuration";

    private static final String FLEET_API_FORMAT = "%s/v1-alpha";

    protected FleetConfigurationDescriptor configuration;

    protected Client client;

    protected WebResource service;

    @Override
    public void activate(ComponentContext context) throws Exception {
        ClientConfig config = new DefaultClientConfig();
        client = Client.create(config);
    }

    protected String computeUrl() {
        String endpoint = configuration.getEndpoint();
        if (endpoint.endsWith("/")) {
            endpoint = endpoint.substring(0, endpoint.length() - 1);
        }
        return String.format(FLEET_API_FORMAT, endpoint);
    }

    @Override
    public void registerContribution(Object contribution, String extensionPoint, ComponentInstance contributor) throws Exception {
        if (CONFIGURATION_EP.equals(extensionPoint)) {
            configuration = (FleetConfigurationDescriptor) contribution;
            service = client.resource(computeUrl());
        } else {
            log.warn("Trying to register a contribution on an unknown extension point: " + extensionPoint);
        }
    }

    @Override
    public Unit submitUnit(Unit unit) {
        if (!unit.getName().endsWith(".service")) {
            throw new IllegalArgumentException("Unit name must be suffixed with '.service'");
        }

        WebResource resource = service.path("/units/" + unit.getName());
        ClientResponse response = resource.type(MediaType.APPLICATION_JSON_TYPE).put(ClientResponse.class, unit.toJSON());

        // XXX Should be 201 CREATED not 204.
        if (response.getStatus() == Response.Status.NO_CONTENT.getStatusCode()) {
            return getUnit(unit.getName());
        } else {
            Error error = clientResponseToClass(response, Error.class);
            log.warn(error);
        }

        return null;
    }

    @Override
    public Unit getUnit(String unitName) {
        ClientResponse response = getClientReponse("/units/" + unitName);

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return clientResponseToClass(response, Unit.class);
        } else {
            Error error = clientResponseToClass(response, Error.class);
            log.info(error);
        }
        return null;
    }

    @Override
    public boolean startUnit(String unitName) {
        return setUnitState(unitName, UnitSate.launched);
    }

    @Override
    public boolean stopUnit(String unitName) {
        return setUnitState(unitName, UnitSate.loaded);
    }

    @Override
    public boolean unloadUnit(String unitName) {
        return setUnitState(unitName, UnitSate.inactive);
    }

    @Override
    public boolean loadUnit(String unitName) {
        return stopUnit(unitName);
    }

    @Override
    public boolean destroyUnit(String unitName) {
        ClientResponse response = service.path("/units/" + unitName).delete(ClientResponse.class);

        if (response.getStatus() == Response.Status.NO_CONTENT.getStatusCode()) {
            return true;
        } else {
            Error error = clientResponseToClass(response, Error.class);
            log.info(error);
            return false;
        }
    }

    @Override
    public boolean setUnitState(String unitName, UnitSate state) {
        WebResource resource = service.path("/units/" + unitName);
        String input = "{\"desiredState\":\"" + state.toString() + "\"}";
        ClientResponse response = resource.type(MediaType.APPLICATION_JSON_TYPE).put(ClientResponse.class, input);

        if (response.getStatus() == Response.Status.NO_CONTENT.getStatusCode()) {
            return true;
        } else {
            Error error = clientResponseToClass(response, Error.class);
            log.warn(error);
            return false;
        }
    }

    @Override
    public Units listUnits() {
        ClientResponse response = getClientReponse("/units");

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return clientResponseToClass(response, Units.class);
        }
        return null;
    }

    @Override
    public Machines listMachines() {
        ClientResponse response = getClientReponse("/machines");

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return clientResponseToClass(response, Machines.class);
        }
        return null;
    }

    @Override
    public <T extends PaginableResult> T getNextPage(String token, String resource, Class<T> clazz) {
        if (!resource.startsWith("/")) {
            resource = "/" + resource;
        }
        WebResource webResource = service.path(resource);
        webResource.queryParam("nextPageToken", token);

        ClientResponse response = webResource.get(ClientResponse.class);
        return clientResponseToClass(response, clazz);
    }

    protected <V> V clientResponseToClass(ClientResponse response, Class<V> clazz) {
        if (response.getStatus() == Response.Status.NO_CONTENT.getStatusCode()) {
            return null;
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(response.getEntityInputStream(), clazz);
        } catch (IOException e) {
            log.warn(e, e);
        }
        return null;
    }

    protected ClientResponse getClientReponse(String path) {
        WebResource resource = service.path(path);
        return resource.get(ClientResponse.class);
    }
}
