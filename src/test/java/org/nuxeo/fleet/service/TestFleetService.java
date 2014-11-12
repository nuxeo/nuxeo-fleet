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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.core.test.annotations.Granularity;
import org.nuxeo.ecm.core.test.annotations.RepositoryConfig;
import org.nuxeo.fleet.Machines;
import org.nuxeo.fleet.Unit;
import org.nuxeo.fleet.Units;
import org.nuxeo.fleet.service.FleetService.UnitSate;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;
import org.nuxeo.runtime.test.runner.LocalDeploy;
import org.nuxeo.runtime.test.runner.RuntimeFeature;

import com.google.inject.Inject;

/**
 * @author <a href="mailto:ak@nuxeo.com">Arnaud Kervern</a>
 * @since 0.1
 */
@RunWith(FeaturesRunner.class)
@Features({RuntimeFeature.class})
@Deploy({"org.nuxeo.fleet"})
@LocalDeploy({"org.nuxeo.fleet:fleet-config.xml"})
@RepositoryConfig(cleanup = Granularity.METHOD)
public class TestFleetService {

    @Inject
    FleetService fleetService;

    @Test
    public void testServiceRegistration() {
        assertNotNull(fleetService);
        // Check contribution registration
        assertEquals("http://localhost:14002", ((FleetComponent) fleetService).configuration.getEndpoint());
    }

    @Test
    public void testGetUnit() {
        assertNull(fleetService.getUnit("unknown_unit.service"));

        Unit unit = fleetService.getUnit("nxio.nxio_000094.1.service");
        assertNotNull(unit);
        assertEquals("nxio.nxio_000094.1.service", unit.getName());
    }

    @Test
    public void testGetUnits() {
        Units units = fleetService.listUnits();
        assertNotNull(units);
        assertNotNull(units.getUnits());

        units = units.getNextPage();
        assertNotNull(units);
        assertNotNull(units.getUnits());
    }

    @Test
    public void testMachines() {
        Machines machines = fleetService.listMachines();
        assertNotNull(machines);
    }

    @Test
    public void testStartService() {
        Unit unit = fleetService.getUnit("nxio.nxio_000094.1.service");
        fleetService.stopUnit(unit.getName());
        assertEquals(UnitSate.loaded, unit.getCurrentState());

        assertTrue(fleetService.startUnit(unit.getName()));
        unit = fleetService.getUnit(unit.getName());

        assertEquals(UnitSate.launched, unit.getDesiredState());
        assertTrue(fleetService.stopUnit(unit.getName()));

        unit = fleetService.getUnit(unit.getName());
        assertEquals(UnitSate.loaded, unit.getDesiredState());
    }
}
