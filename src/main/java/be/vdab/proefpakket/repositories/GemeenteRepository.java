package be.vdab.proefpakket.repositories;

import be.vdab.proefpakket.domain.Gemeente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GemeenteRepository extends JpaRepository<Gemeente, Long> {
}
