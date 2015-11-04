import javax.persistence.RollbackException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Hangman {
    private String word;
    private LinkedList<Character> charAlreadyUsed;
    private int lives;
    private String wordWithSpaces;
    private int numberOfCharacters;
    private WordDAO wordDao;
    private StringBuilder alphabet;
    private List<Word> listOfWordsFromDB;

    public Hangman(String word){
        this.word = word;
        lives = 5;
        wordWithSpaces = "";
        charAlreadyUsed = new LinkedList<Character>();
        numberOfCharacters = 0;
        wordDao = new WordDAOImpl();
        alphabet = new StringBuilder("abcdefghijklmnopqrstuvwxyz");
    }

    /**
     * Create Word creates the word replacing the characters with _.
     */

    public void createWord(){
        char firstChar = word.charAt(0);
        char lastChar = word.charAt(word.length()-1);
        charAlreadyUsed.add(firstChar);
        charAlreadyUsed.add(lastChar);
        alphabet.deleteCharAt(alphabet.indexOf(String.valueOf(firstChar)));
        alphabet.deleteCharAt(alphabet.indexOf(String.valueOf(lastChar)));
        for (char character : word.toCharArray()){
            if(character == firstChar || character == lastChar) {
                wordWithSpaces += character;
            }
            else if (character == ' '){
                wordWithSpaces += ' ';
            }
            else {
                wordWithSpaces += '_';
                numberOfCharacters++;
            }
        }
    }

    /**
     * Starting the game
     */

    public void play() {
        boolean isFromDB = false;
        Word wordFromDB = null;
        listOfWordsFromDB = getWordsFromDB();
        while (lives > 0 && numberOfCharacters > 0){
            int index = 1;
            char character;
            /*
            Try to take a list o words with starts and finish with the same character as the word and have the same length.
            The list is sorted by appearance. If the first char that is taken from the word is not a correct char the word
            is deleted from the list and will try with the next word. If the list is empty will try random characters.
             */
            if(!listOfWordsFromDB.isEmpty()){
                wordFromDB = listOfWordsFromDB.get(0);
                while(charAlreadyUsed.contains(wordFromDB.getWord().charAt(index)) || wordFromDB.getWord().charAt(index) == ' '){
                    index++;
                }
                character = wordFromDB.getWord().charAt(index);
                System.out.println("The selected char is: " + character);
                isFromDB = true;
                alphabet.deleteCharAt(alphabet.indexOf(String.valueOf(character)));
            } else {
                character = getRandomChar();
                System.out.println("The selected char is: " + character);
                isFromDB = false;
            }

            if(!charAlreadyUsed.contains(character)){
                charAlreadyUsed.add(character);
                boolean existChar = false;
                char[] charArray = wordWithSpaces.toCharArray();
                for(int i = 0; i < word.length()-1; i++){
                    if(word.charAt(i) == character){
                        charArray[i] = character;
                        existChar = true;
                        numberOfCharacters--;
                    }
                }
                wordWithSpaces = String.valueOf(charArray);
                if(!existChar){
                    lives--;
                    System.out.println("The word doesn't contains " + character);
                    System.out.println("You have " + lives + " lives");
                    if (isFromDB){
                        listOfWordsFromDB.remove(0);
                    }
                }
            }
            System.out.println("The word is : " + wordWithSpaces);
        }
        if(lives == 0){
            System.out.println("Lose");
            Word wd = new Word();
            wd.setWord(word);
            wd.setAppearance(1);
            addWordToDB(wd);
        }
        else if (numberOfCharacters == 0){
            Word wd = new Word();
            wd.setWord(word);
            wd.setAppearance(1);
            updateWord(wd);
            System.out.println("Win");
        }
    }

    /**
     * Get words which have the same length as the word to find.
     * @return List of words.
     */
    private List<Word> getWordsFromDB(){
        List<Word> listOfWords = wordDao.getWords(wordWithSpaces.length());

        for(Word w : listOfWords){
            if((w.getWord().charAt(0) != word.charAt(0)) || (w.getWord().charAt(w.getWord().length() - 1) != word.charAt(word.length() - 1)) ){
                listOfWords.remove(w);
            }
        }
        return listOfWords;
    }

    /**
     * Generates a random char from the list of available characters.
     * @return Character
     */
    private char getRandomChar(){
        Random rand = new Random();
        int randomNum = rand.nextInt((alphabet.length()-1) + 1) + 0;
        char character = alphabet.charAt(randomNum);
        alphabet.deleteCharAt(randomNum);
        return character;
    }

    /**
     * Add a word to the database.
     * @param word The word to add.
     */
    private void addWordToDB(Word word){
        try{
            wordDao.insertWord(word);
        } catch (RollbackException e) {
            System.out.println("Error while trying to persist the word " + word.getWord());
        }
    }

    /**
     * Update the stat of a word in the database.
     * @param word The word to update.
     */
    private void updateWord(Word word) {
        wordDao.updateWord(word);
    }
}
