package com.duoc.resumen_compra_consumer.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.duoc.resumen_compra_consumer.dto.ResumenInscripcionMessage;
import com.duoc.resumen_compra_consumer.model.ResumenCompra;
import com.duoc.resumen_compra_consumer.repository.ResumenCompraRepository;

import java.time.LocalDateTime;

@Component
public class ResumenCompraListener {

    private final ResumenCompraRepository resumenCompraRepository;

    public ResumenCompraListener(ResumenCompraRepository resumenCompraRepository) {
        this.resumenCompraRepository = resumenCompraRepository;
    }

    @RabbitListener(queues = "${app.rabbitmq.queue}")
    public void recibirResumen(ResumenInscripcionMessage mensaje) {
        ResumenCompra resumenCompra = new ResumenCompra();
        resumenCompra.setInscripcionId(mensaje.getInscripcionId());
        resumenCompra.setNombreUsuario(mensaje.getNombreUsuario());
        resumenCompra.setEmailUsuario(mensaje.getEmailUsuario());
        resumenCompra.setCursos(String.join(", ", mensaje.getCursos()));
        resumenCompra.setTotalPagar(mensaje.getTotalPagar());
        resumenCompra.setFechaInscripcion(mensaje.getFechaInscripcion());
        resumenCompra.setFechaProcesado(LocalDateTime.now());
        resumenCompra.setContenidoResumen(mensaje.getContenidoResumen());

        resumenCompraRepository.save(resumenCompra);
    }
    
}