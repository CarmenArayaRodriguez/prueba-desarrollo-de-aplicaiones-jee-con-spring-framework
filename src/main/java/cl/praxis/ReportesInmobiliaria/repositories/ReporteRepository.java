package cl.praxis.ReportesInmobiliaria.repositories;

import cl.praxis.ReportesInmobiliaria.models.Reporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReporteRepository extends JpaRepository<Reporte, Long> {
}
