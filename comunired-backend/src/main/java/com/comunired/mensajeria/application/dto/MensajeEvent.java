package com.comunired.mensajeria.application.dto;

public class MensajeEvent {
    private MensajeDTO mensaje;
    private String destinatarioId;

    public MensajeEvent(MensajeDTO mensaje, String destinatarioId) {
        this.mensaje = mensaje;
        this.destinatarioId = destinatarioId;
    }

    public MensajeDTO getMensaje() { return mensaje; }
    public String getDestinatarioId() { return destinatarioId; }
}
