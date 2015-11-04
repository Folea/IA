/**
 * Created by foleac on 6/5/2015.
 */

import javax.persistence.*;


@Entity
@NamedQueries({
        @NamedQuery(name = "Word.getWordByName", query = "select w from Word as w Where w.word = :wd"),
        @NamedQuery(name = "Word.getWordsByLength", query = "select w from Word as w where length(w.word) = :l order by w.appearance desc"),
        @NamedQuery(name = "Word.updateWord", query = "update Word as w set w.appearance = :ap where w.word = :wd")
})
public class Word {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Basic
    @Column(unique = true)
    private String word;

    @Basic
    private Integer appearance;

    public Word(){
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word){
        this.word = word;
    }

    public int getAppearance() {
        return appearance;
    }

    public void setAppearance(int appearence) {
        this.appearance = appearence;
    }

}
