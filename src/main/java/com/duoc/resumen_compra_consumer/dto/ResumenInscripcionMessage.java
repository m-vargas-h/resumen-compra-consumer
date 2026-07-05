package com.duoc.resumen_compra_consumer.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class ResumenInscripcionMessage implements Serializable {

    private Long inscripcionId;
    private String nombreUsuario;
    private String emailUsuario;
    private List<String> cursos;
    private BigDecimal totalPagar;
    private LocalDateTime fechaInscripcion;
    private String contenidoResumen;

    public ResumenInscripcionMessage() {
    }

    // Getters y setters

    public Long getInscripcionId() { 
        return inscripcionId; 
    }

    public void setInscripcionId(Long inscripcionId) { 
        this.inscripcionId = inscripcionId; 
    }

    public String getNombreUsuario() { 
        return nombreUsuario; 
    }

    public void setNombreUsuario(String nombreUsuario) { 
        this.nombreUsuario = nombreUsuario; 
    }

    public String getEmailUsuario() { 
        return emailUsuario; 
    }

    public void setEmailUsuario(String emailUsuario) { 
        this.emailUsuario = emailUsuario; 
    }

    public List<String> getCursos() { 
        return cursos; 
    }

    public void setCursos(List<String> cursos) { 
        this.cursos = cursos; 
    }

    public BigDecimal getTotalPagar() { 
        return totalPagar; 
    }

    public void setTotalPagar(BigDecimal totalPagar) { 
        this.totalPagar = totalPagar; 
    }

    public LocalDateTime getFechaInscripcion() { 
        return fechaInscripcion; 
    }

    public void setFechaInscripcion(LocalDateTime fechaInscripcion) { 
        this.fechaInscripcion = fechaInscripcion; 
    }

    public String getContenidoResumen() { 
        return contenidoResumen; 
    }

    public void setContenidoResumen(String contenidoResumen) { 
        this.contenidoResumen = contenidoResumen; 
    }

}
