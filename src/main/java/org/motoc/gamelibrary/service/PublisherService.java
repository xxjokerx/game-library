package org.motoc.gamelibrary.service;

import org.motoc.gamelibrary.domain.dto.PublisherNameDto;
import org.motoc.gamelibrary.domain.model.Publisher;
import org.motoc.gamelibrary.repository.jpa.PublisherRepository;
import org.motoc.gamelibrary.service.refactor.SimpleCrudMethodsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Perform service logic on the entity Publisher
 */
@Service
@Transactional
public class PublisherService extends SimpleCrudMethodsImpl<Publisher, JpaRepository<Publisher, Long>> {

    private static final Logger logger = LoggerFactory.getLogger(PublisherService.class);

    final private PublisherRepository publisherRepository;


    @Autowired
    public PublisherService(JpaRepository<Publisher, Long> genericRepository,
                            PublisherRepository publisherRepository) {
        super(genericRepository, Publisher.class);
        this.publisherRepository = publisherRepository;
    }

    /**
     * Edits a publisher by id
     */
    public Publisher edit(Publisher publisher, Long id) {
        return publisherRepository.findById(id)
                .map(publisherFromPersistence -> {
                    publisherFromPersistence.setName(publisher.getName());
                    publisherFromPersistence.setContact(publisher.getContact());
                    logger.debug("Found publisher of id={} : {}", id, publisherFromPersistence);
                    return publisherRepository.save(publisherFromPersistence);
                })
                .orElseGet(() -> {
                    publisher.setId(id);
                    logger.debug("No publisher of id={} found. Set mechanism : {}", id, publisher);
                    return publisherRepository.save(publisher);
                });
    }

    /**
     * Calls the DAO to delete a publisher by id
     */
    public void remove(Long id) {
        logger.debug("Deleting (if exist) publisher of id=" + id);
        publisherRepository.remove(id);
    }

    public Page<Publisher> quickSearch(String keyword, Pageable pageable) {
        logger.debug("Find paged publishers that contains : " + keyword);
        return publisherRepository.findByLowerCaseNameContaining(keyword, pageable);
    }

    public void removeContact(Long pId) {
        logger.debug("Deleting contact from publisher of id=" + pId);
        Publisher p = publisherRepository.removeContact(pId);
        logger.debug("Successfully removed contact for Publisher of id={} : {}", pId, p);
    }

    public List<PublisherNameDto> findNames() {
        logger.debug("Find all publishers' name");
        return publisherRepository.findNames();
    }

    public Publisher save(Publisher p) {
        logger.debug(" publishers' name");
        if (p.getId() == null) {
            logger.debug("Trying to save new p={}", p.getName());
        } else {
            logger.debug("Trying to save new p={} of id={}", p.getName(), p.getId());
        }
        return publisherRepository.savePublisher(p);
    }
}
