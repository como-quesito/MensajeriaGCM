package org.campitos.gcm;

/**
 * Created by campitos on 5/27/15.
 */
public class RegistroMensajeria {
    String registroId;

    public RegistroMensajeria() {
    }

    public RegistroMensajeria(String registroId) {
        this.registroId = registroId;
    }

    public String getRegistroId() {
        return registroId;
    }

    public void setRegistroId(String registroId) {
        this.registroId = registroId;
    }
}
