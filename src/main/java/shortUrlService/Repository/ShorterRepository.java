package shortUrlService.Repository;

import org.springframework.data.repository.CrudRepository;
import shortUrlService.Entity.Shorter;

public interface ShorterRepository extends CrudRepository<Shorter, Long> {
    Shorter findByHash(String hash);
}