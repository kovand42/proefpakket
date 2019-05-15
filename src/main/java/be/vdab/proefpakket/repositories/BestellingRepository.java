package be.vdab.proefpakket.repositories;

import be.vdab.proefpakket.domain.Bestelling;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BestellingRepository extends JpaRepository<Bestelling, Long> {
}
