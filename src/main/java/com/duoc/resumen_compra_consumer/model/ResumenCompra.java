package com.duoc.resumen_compra_consumer.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "resumen_compra")
@Getter
@Setter
@NoArgsConstructor
public class ResumenCompra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "inscripcion_id", nullable = false)
    private Long inscripcionId;

    @Column(name = "nombre_usuario", nullable = false, length = 100)
    private String nombreUsuario;

    @Column(name = "email_usuario", nullable = false, length = 150)
    private String emailUsuario;

    @Column(name = "cursos", nullable = false, length = 500)
    private String cursos; // cursos concatenados separados por coma

    @Column(name = "total_pagar", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPagar;

    @Column(name = "fecha_inscripcion", nullable = false)
    private LocalDateTime fechaInscripcion;

    @Column(name = "fecha_procesado", nullable = false)
    private LocalDateTime fechaProcesado;

    @Lob
    @Column(name = "contenido_resumen")
    private String contenidoResumen;
}