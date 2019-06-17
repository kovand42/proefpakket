package be.vdab.proefpakket.services;

import be.vdab.proefpakket.domain.Brouwer;
import be.vdab.proefpakket.repositories.BrouwerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
public class DefaultBrouwerService implements BrouwerService{
    private final BrouwerRepository brouwerRepository;

    public DefaultBrouwerService(BrouwerRepository brouwerRepository) {
        this.brouwerRepository = brouwerRepository;
    }
    @Override
    public List<Brouwer> findByBeginNaam(String beginNaam){
        return brouwerRepository.findByNaamStartingWithOrderByNaam(beginNaam);
    }
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED)
    public void update(Brouwer brouwer) {
        brouwerRepository.save(brouwer);
    }
}
