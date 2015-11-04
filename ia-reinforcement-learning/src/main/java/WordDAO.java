import java.util.List;

/**
 * Created by foleac on 6/11/2015.
 */

public interface WordDAO {

    public void insertWord(Word word);

    public void updateWord(Word word);

    public Word getWord(String name);

    public List<Word> getWords(int length);
}
