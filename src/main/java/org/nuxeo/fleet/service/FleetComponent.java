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
import java.util.List;

import javax.ws.rs.core.Response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.nuxeo.fleet.Error;
import org.nuxeo.fleet.Machine;
import org.nuxeo.fleet.Unit;
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
        return null;
    }

    @Override
    public Unit getUnit(String unitName) {
        ClientResponse response = getClientReponse("/units/" + unitName);

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return clientResponseToClass(response, Unit.class);
        } else {
            Error error = clientResponseToClass(response, Error.class);
            log.warn(error);
        }
        return null;
    }

    @Override
    public boolean startUnit(String unitName) {
        return false;
    }

    @Override
    public boolean stopUnit(String unitName) {
        return false;
    }

    @Override
    public boolean destroyUnit(String unitName) {
        return false;
    }

    @Override
    public boolean setUnitState(String unitName, UnitSate state) {
        return false;
    }

    @Override
    public List<Unit> listUnits() {
        return null;
    }

    @Override
    public List<Machine> listMachines() {
        return null;
    }

    protected <V> V clientResponseToClass(ClientResponse response, Class<V> clazz) {
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
