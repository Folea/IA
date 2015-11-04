import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Game {
    Hangman hangman;
    String word;

    public Game(){
        readWord();
        hangman = new Hangman(word);
        hangman.createWord();
        hangman.play();
    }

    public void readWord(){
        try{
            System.out.print("Introduce the word for hangman: ");
            BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
            word = bufferRead.readLine();
        } 	catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        Game game = new Game();
    }
}
