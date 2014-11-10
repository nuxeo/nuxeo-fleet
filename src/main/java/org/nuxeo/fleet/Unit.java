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

package org.nuxeo.fleet;

import java.util.List;

/**
 * @author <a href="mailto:ak@nuxeo.com">Arnaud Kervern</a>
 * @since 0.1
 */
public class Unit {
    protected String name;

    protected List<UnitOption> options;

    protected String desiredState;

    protected String currentState;

    protected String machineId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<UnitOption> getOptions() {
        return options;
    }

    public void setOptions(List<UnitOption> options) {
        this.options = options;
    }

    public String getDesiredState() {
        return desiredState;
    }

    public void setDesiredState(String desiredState) {
        this.desiredState = desiredState;
    }

    public String getCurrentState() {
        return currentState;
    }

    public void setCurrentState(String currentState) {
        this.currentState = currentState;
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Unit unit = (Unit) o;

        if (currentState != null ? !currentState.equals(unit.currentState) : unit.currentState != null) return false;
        if (desiredState != null ? !desiredState.equals(unit.desiredState) : unit.desiredState != null) return false;
        if (machineId != null ? !machineId.equals(unit.machineId) : unit.machineId != null) return false;
        if (name != null ? !name.equals(unit.name) : unit.name != null) return false;
        if (options != null ? !options.equals(unit.options) : unit.options != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (options != null ? options.hashCode() : 0);
        result = 31 * result + (desiredState != null ? desiredState.hashCode() : 0);
        result = 31 * result + (currentState != null ? currentState.hashCode() : 0);
        result = 31 * result + (machineId != null ? machineId.hashCode() : 0);
        return result;
    }
}
