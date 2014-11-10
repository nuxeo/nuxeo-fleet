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

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.fleet.Machine;
import org.nuxeo.fleet.Unit;
import org.nuxeo.runtime.model.ComponentInstance;
import org.nuxeo.runtime.model.DefaultComponent;

/**
 * @author <a href="mailto:ak@nuxeo.com">Arnaud Kervern</a>
 * @since 0.1
 */
public class FleetComponent extends DefaultComponent implements FleetService {

    private final static Log log = LogFactory.getLog(FleetComponent.class);

    String endpoint = "http://localhost:4002/v1-alpha";

    @Override
    public void registerContribution(Object contribution, String extensionPoint, ComponentInstance contributor) throws Exception {
        if ("configuration".equals(extensionPoint)) {
            endpoint = ((FleetConfigurationDescriptor) contribution).getEndpoint();
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
    public boolean setUnitState(String unitName, String state) {
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
}
