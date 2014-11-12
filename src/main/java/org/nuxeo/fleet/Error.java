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

/**
 * @author <a href="mailto:ak@nuxeo.com">Arnaud Kervern</a>
 * @since 0.1
 */

public class Error {

    protected InternalError error;

    public InternalError getError() {
        return error;
    }

    public void setError(InternalError error) {
        this.error = error;
    }

    public int getCode() {
        return error.getCode();
    }

    public String getMessage() {
        return error.getMessage();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Error error1 = (Error) o;

        if (error != null ? !error.equals(error1.error) : error1.error != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return error != null ? error.hashCode() : 0;
    }

    @Override
    public String toString() {
        return error == null ? "Empty error" : "[" + getCode() + "] " + getMessage();
    }

    public static class InternalError {
        protected int code;

        protected String message;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            InternalError that = (InternalError) o;

            if (code != that.code) return false;
            if (message != null ? !message.equals(that.message) : that.message != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = code;
            result = 31 * result + (message != null ? message.hashCode() : 0);
            return result;
        }
    }
}
