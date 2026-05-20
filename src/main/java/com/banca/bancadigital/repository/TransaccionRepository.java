package com.banca.bancadigital.repository;

import com.banca.bancadigital.model.Cuenta;
import com.banca.bancadigital.model.Transaccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface TransaccionRepository extends JpaRepository<Transaccion, Long> {

    // Busca todas las transacciones donde la cuenta haya sido origen O destino, ordenadas por fecha
    @Query("SELECT t FROM Transaccion t WHERE t.cuentaOrigen = :cuenta OR t.cuentaDestino = :cuenta ORDER BY t.fechaHora DESC")
    List<Transaccion> findByCuenta(@Param("cuenta") Cuenta cuenta);
}