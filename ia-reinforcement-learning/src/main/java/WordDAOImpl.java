import javax.persistence.*;
import java.util.List;

/**
 * Created by foleac on 6/11/2015.
 */
public class WordDAOImpl implements WordDAO {

    private static final String PERSISTENCE_UNIT_NAME = "test";
    private static EntityManagerFactory factory;

    @Override
    public void insertWord(Word word) {
        factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        EntityManager em = factory.createEntityManager();
        em.getTransaction().begin();
        em.persist(word);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public void updateWord(Word word) {
        factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        EntityManager em = factory.createEntityManager();
        em.getTransaction().begin();
        Word wordFromDB = getWord(word.getWord());
        if(wordFromDB == null) {
            insertWord(word);
        } else {
            wordFromDB.setAppearance(wordFromDB.getAppearance() + 1);
            Query query = em.createNamedQuery("Word.updateWord");
            query.setParameter("wd", wordFromDB.getWord());
            query.setParameter("ap", wordFromDB.getAppearance());
            query.executeUpdate();
        }
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public Word getWord(String name) {
        factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        EntityManager em = factory.createEntityManager();
        em.getTransaction().begin();
        TypedQuery<Word> q = em.createNamedQuery("Word.getWordByName", Word.class);
        q.setParameter("wd", name);
        return q.getSingleResult();
    }

    public List<Word> getWords(int length) {
        factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        EntityManager em = factory.createEntityManager();
        em.getTransaction().begin();
        TypedQuery<Word> q = em.createNamedQuery("Word.getWordsByLength", Word.class);
        q.setParameter("l", length);
        return q.getResultList();
    }
}
