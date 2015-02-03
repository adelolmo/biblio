package org.ado.biblio.update;

/**
 * @author Andoni del Olmo,
 * @since 25.01.15
 */
public enum ComponentEnum {
    DESKTOP_CLIENT("biblio-desktop-client"),
    ANDROID_APP("biblio-android"),
    SERVER_WAR("biblio-server-war");

    private String codeName;

    ComponentEnum(String codeName) {
        this.codeName = codeName;
    }

    public String getCodeName() {
        return codeName;
    }
}
