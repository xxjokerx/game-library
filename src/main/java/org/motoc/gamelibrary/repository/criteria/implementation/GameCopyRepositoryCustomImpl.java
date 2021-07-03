package org.motoc.gamelibrary.repository.criteria.implementation;

import org.motoc.gamelibrary.model.GameCopy;
import org.motoc.gamelibrary.model.Publisher;
import org.motoc.gamelibrary.model.Seller;
import org.motoc.gamelibrary.repository.criteria.GameCopyRepositoryCustom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class GameCopyRepositoryCustomImpl implements GameCopyRepositoryCustom {

    private static final Logger logger = LoggerFactory.getLogger(GameCopyRepositoryCustom.class);

    private final EntityManager entityManager;

    @Autowired
    public GameCopyRepositoryCustomImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public GameCopy addSeller(GameCopy copy, Seller seller) {
        copy.addSeller(seller);
        entityManager.persist(copy);
        return copy;
    }

    @Override
    public GameCopy removeSeller(GameCopy copy, Seller seller) {
        copy.removeSeller(seller);
        entityManager.persist(copy);
        return copy;
    }

    @Override
    public GameCopy addPublisher(GameCopy copy, Publisher publisher) {
        copy.addPublisher(publisher);
        entityManager.persist(copy);
        return copy;
    }

    @Override
    public GameCopy removePublisher(GameCopy copy, Publisher publisher) {
        copy.removePublisher(publisher);
        entityManager.persist(copy);
        return copy;
    }
}