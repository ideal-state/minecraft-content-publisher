/*
 *     <one line to give the program's name and a brief idea of what it does.>
 *     Copyright (C) 2024  ideal-state
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package team.idealstate.minecraft.contentpublisher.common.model.entity;

/**
 * <p>ServiceConfig</p>
 *
 * <p>创建于 2024/2/7 5:06</p>
 *
 * @author ketikai
 * @version 1.0.0
 * @since 1.0.0
 */
public class Service {

    private String host;
    private Short port;
    private Integer threads;
    private Integer timeout;
    private Integer maximumRetry;

    public Service() {
    }

    public Service(String host, Short port, Integer threads, Integer timeout, Integer maximumRetry) {
        this.host = host;
        this.port = port;
        this.threads = threads;
        this.timeout = timeout;
        this.maximumRetry = maximumRetry;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Short getPort() {
        return port;
    }

    public void setPort(Short port) {
        this.port = port;
    }

    public Integer getThreads() {
        return threads;
    }

    public void setThreads(Integer threads) {
        this.threads = threads;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public Integer getMaximumRetry() {
        return maximumRetry;
    }

    public void setMaximumRetry(Integer maximumRetry) {
        this.maximumRetry = maximumRetry;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Service)) {
            return false;
        }
        final Service service = (Service) o;

        if (getHost() != null ? !getHost().equals(service.getHost()) : service.getHost() != null) {
            return false;
        }
        if (getPort() != null ? !getPort().equals(service.getPort()) : service.getPort() != null) {
            return false;
        }
        if (getThreads() != null ? !getThreads().equals(service.getThreads()) : service.getThreads() != null) {
            return false;
        }
        if (getTimeout() != null ? !getTimeout().equals(service.getTimeout()) : service.getTimeout() != null) {
            return false;
        }
        return getMaximumRetry() != null ? getMaximumRetry().equals(service.getMaximumRetry()) : service.getMaximumRetry() == null;
    }

    @Override
    public int hashCode() {
        int result = getHost() != null ? getHost().hashCode() : 0;
        result = 31 * result + (getPort() != null ? getPort().hashCode() : 0);
        result = 31 * result + (getThreads() != null ? getThreads().hashCode() : 0);
        result = 31 * result + (getTimeout() != null ? getTimeout().hashCode() : 0);
        result = 31 * result + (getMaximumRetry() != null ? getMaximumRetry().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Service{" +
                "host='" + host + '\'' +
                ", port=" + port +
                ", threads=" + threads +
                ", timeout=" + timeout +
                ", maximumRetry=" + maximumRetry +
                '}';
    }
}
