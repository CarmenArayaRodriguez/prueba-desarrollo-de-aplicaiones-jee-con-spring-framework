package cl.praxis.ReportesInmobiliaria.controllers;

import cl.praxis.ReportesInmobiliaria.models.Reporte;
import cl.praxis.ReportesInmobiliaria.services.ReporteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/reportes")
public class ReporteController {

    private final ReporteService reporteService;

    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @GetMapping
    public List<Reporte> listAll() {
        return reporteService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reporte> get(@PathVariable Long id) {
        try {
            Reporte reporte = reporteService.findById(id).get();
            return new ResponseEntity<>(reporte, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<Reporte> add(@RequestBody Reporte reporte) {
        return new ResponseEntity<>(reporteService.save(reporte), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody Reporte reporte, @PathVariable Long id) {
        try {
            Reporte existReporte = reporteService.findById(id).orElseThrow(() -> new NoSuchElementException("Reporte no encontrado con id: " + id));

            // Solo actualiza los campos que no sean nulos en la solicitud
            if (reporte.getNombreProyecto() != null) {
                existReporte.setNombreProyecto(reporte.getNombreProyecto());
            }
            if (reporte.getUbicacion() != null) {
                existReporte.setUbicacion(reporte.getUbicacion());
            }
            if (reporte.getEstado() != null) {
                existReporte.setEstado(reporte.getEstado());
            }

            reporteService.save(existReporte);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            reporteService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
