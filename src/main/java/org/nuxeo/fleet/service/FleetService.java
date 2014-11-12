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

import org.nuxeo.fleet.Machine;
import org.nuxeo.fleet.Unit;

/**
 * @author <a href="mailto:ak@nuxeo.com">Arnaud Kervern</a>
 * @since 0.1
 */
public interface FleetService {
    public static enum UnitSate {
        inactive, loaded, launched;
    }

    Unit submitUnit(Unit unit);

    Unit getUnit(String unitName);

    boolean startUnit(String unitName);

    boolean stopUnit(String unitName);

    boolean destroyUnit(String unitName);

    boolean setUnitState(String unitName, UnitSate state);

    List<Unit> listUnits();

    List<Machine> listMachines();
}
