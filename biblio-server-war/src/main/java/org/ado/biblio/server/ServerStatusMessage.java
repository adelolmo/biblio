package org.ado.biblio.server;

/**
 * @author Andoni del Olmo,
 * @since 06.01.15
 */
public class ServerStatusMessage {

    private ServerStatusEnum serverStatusEnum;

    public ServerStatusMessage(ServerStatusEnum serverStatusEnum) {
        this.serverStatusEnum = serverStatusEnum;
    }

    public ServerStatusEnum getServerStatusEnum() {
        return serverStatusEnum;
    }

    public void setServerStatusEnum(ServerStatusEnum serverStatusEnum) {
        this.serverStatusEnum = serverStatusEnum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ServerStatusMessage that = (ServerStatusMessage) o;

        if (serverStatusEnum != that.serverStatusEnum) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return serverStatusEnum != null ? serverStatusEnum.hashCode() : 0;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ServerStatusMessage{");
        sb.append("serverStatusEnum=").append(serverStatusEnum);
        sb.append('}');
        return sb.toString();
    }
}