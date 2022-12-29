package org.motoc.gamelibrary.repository.fragment.implementation;

import org.junit.Ignore;
import org.junit.jupiter.api.*;
import org.motoc.gamelibrary.domain.model.Theme;
import org.motoc.gamelibrary.repository.AbstractContainerBaseTest;
import org.motoc.gamelibrary.repository.jpa.ThemeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.testcontainers.ext.ScriptUtils;
import org.testcontainers.jdbc.JdbcDatabaseDelegate;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ThemeFragmentRepositoryImplTest extends AbstractContainerBaseTest {

    @BeforeAll
    static void startAbstractContainer() {
        postgreSQLContainer.start();
        JdbcDatabaseDelegate containerDelegate = new JdbcDatabaseDelegate(postgreSQLContainer, "");
        ScriptUtils.runInitScript(containerDelegate, "sql/schema.sql");
        ScriptUtils.runInitScript(containerDelegate, "sql/data.sql");
    }

    @AfterEach
    public void closeEMF() {
        this.emf.close();
    }

    @Autowired
    private EntityManagerFactory emf;

    private static final Logger logger = LoggerFactory.getLogger(ThemeFragmentRepositoryImplTest.class);

    @Autowired
    private ThemeRepository repository;

    private final Long tId = 13L;


    @Test
    @Order(1)
    void whenSaveTheme_ThenReturnExpectedTheme() {
        final String tName = "Cthulhu";
        Theme t = new Theme();
        t.setName(tName);
        t = repository.saveTheme(t);

        assertThat(t.getId()).isEqualTo(tId);
        assertThat(t.getName()).isEqualTo(tName);
        assertThat(t.getLowerCaseName()).isEqualTo(tName.toLowerCase());
    }

    @Test
    @Order(2)
    void whenDeleteTheme_ThenThemeCountDecreaseBy1() {
        final long preDeleteCount = repository.count();
        repository.remove(tId);
        final long postDeleteCount = repository.count();

        assertThat(postDeleteCount).isEqualTo(preDeleteCount - 1L);
    }

    @Test
    @Ignore
    @Order(3)
    void persistLotsOfThemes() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        for (int i = 0; i < 51; i++) {
            Theme t = new Theme();
            t.setName("theme" + i);
            em.persist(t);
        }

        logger.info("Commit transaction");
        em.getTransaction().commit();

        em.getTransaction().begin();

        TypedQuery<Theme> tq = em.createQuery("SELECT t FROM Theme AS t", Theme.class);
        em.getTransaction().commit();
        List<Theme> themes = tq.getResultList();


        for (Theme t : themes) {
            logger.debug("theme:" + t.getName() + " of id=" + t.getId());
        }
    }

    @Test
    @Order(4)
    void whenSaveAlreadyExistingTheme_ThenThrowADataIntegrityViolationException() {
        EntityManager em = emf.createEntityManager();
        Theme t = new Theme();
        t.setName(em.find(Theme.class, 1L).getName());
        Exception exception = assertThrows(DataIntegrityViolationException.class, () -> repository.saveTheme(t));

        assertThat(exception.getClass()).isEqualTo(DataIntegrityViolationException.class);
    }
}