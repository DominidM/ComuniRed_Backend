package com.comunired.estados_queja.infrastructure.config;
import com.comunired.estados_queja.domain.entity.Estados_queja;
import com.comunired.estados_queja.infrastructure.repository.Estados_quejaMongoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
@Component
public class EstadoDataInitializer implements CommandLineRunner {
    private final Estados_quejaMongoRepository repository;
    public EstadoDataInitializer(Estados_quejaMongoRepository repository) {
        this.repository = repository;
    }
    @Override
    public void run(String... args) {
        if (repository.findByClave("votacion").isPresent()) return;
        repository.save(new Estados_queja(null, "votacion",   "Votación",    "En proceso de votación ciudadana",     1));
        repository.save(new Estados_queja(null, "pendiente",  "Pendiente",   "Pendiente de revisión por soporte",    2));
        repository.save(new Estados_queja(null, "clasificada","Clasificada", "Clasificada por nivel de riesgo",       3));
        repository.save(new Estados_queja(null, "aprobada",   "Aprobada",    "Aprobada para intervención",            4));
    }
}