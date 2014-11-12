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

import org.apache.commons.lang.StringUtils;
import org.nuxeo.fleet.service.FleetService;
import org.nuxeo.runtime.api.Framework;

/**
 * @author <a href="mailto:ak@nuxeo.com">Arnaud Kervern</a>
 * @since 0.1
 */
public abstract class PaginableResult<T> {

    protected abstract String getResource();

    protected abstract List<T> getContent();

    protected String nextPageToken;

    protected <T extends PaginableResult> T getNextPage(Class<T> clazz) {
        FleetService service = Framework.getLocalService(FleetService.class);
        return service.getNextPage(nextPageToken, getResource(), clazz);
    }

    public String getNextPageToken() {
        return nextPageToken;
    }

    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }

    public boolean hasNextPage() {
        return StringUtils.isEmpty(nextPageToken);
    }
}
