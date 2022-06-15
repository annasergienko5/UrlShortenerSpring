package shortUrlService.Controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shortUrlService.Constants;
import shortUrlService.Entity.Shorter;
import shortUrlService.HashGenerator;
import shortUrlService.Repository.ShorterRepository;

import java.time.ZonedDateTime;

@RestController
@RequestMapping
class ShorterController {
    Logger logger = LoggerFactory.getLogger(ShorterController.class.getSimpleName());

    private final ShorterRepository repository;

    private final HashGenerator hashGenerator;

    @Autowired
    public ShorterController(final ShorterRepository repository, HashGenerator hashGenerator) {
        this.repository = repository;
        this.hashGenerator = hashGenerator;
    }

    @PostMapping("/")
    public String createShortUrl(@RequestBody String fullUrl) {
        String hash = hashGenerator.generate(10);
        logger.info(hash);
        if(fullUrl != null) {
            Shorter shorter = new Shorter(null, hash, fullUrl, ZonedDateTime.now());
            repository.save(shorter);
            return (Constants.HOST + hash);
        } else {
            return null;
        }
    }

    @GetMapping("/{hash}")
    public ResponseEntity redirectShorter(@PathVariable("hash") String hash) {
        Shorter shorter = repository.findByHash(hash);
        if (shorter != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Location", shorter.getOriginalUrl());
            return new ResponseEntity<String>(headers, HttpStatus.FOUND);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}