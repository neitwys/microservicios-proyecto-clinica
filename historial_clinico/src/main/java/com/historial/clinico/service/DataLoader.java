package com.historial.clinico.service;

import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.historial.clinico.model.HistorialClinico;
import com.historial.clinico.repository.HistorialClinicoRepository;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner init(HistorialClinicoRepository repository) {
        return args -> {

            if (repository.count() == 0) {
                repository.save(new HistorialClinico(null, 1L, "Juan Perez", LocalDate.now(), "Gripe",
                        "Paracetamol", "Reposo por 5 dias", "Dr. Martinez"));

                repository.save(new HistorialClinico(null, 2L, "Maria Gonzalez", LocalDate.now().minusDays(2),
                        "Migraña", "Ibuprofeno", "Control en una semana", "Dra. Soto"));

                repository.save(new HistorialClinico(null, 3L, "Pedro Ramirez", LocalDate.now().minusDays(5),
                        "Diabetes", "Metformina", "Control mensual", "Dr. Herrera"));
            }
        };
    }
}
