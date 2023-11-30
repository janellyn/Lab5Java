import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
import java.util.ArrayList;

public class App {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.print("Enter text: ");
        String input = in.nextLine();

        Text inputText = new Text(input);
        System.out.println("\nYour entered text:");
        System.out.println(inputText);

        Set<String> uniqueWords = new TreeSet<>();
        for (Sentence sentence : inputText.getSentences()) {
            for (Word word : sentence.getWords()) {
                if(!word.toString().isEmpty()) uniqueWords.add(word.toString());
            }
        }
        System.out.print("In alphabet order:");
        System.out.println(uniqueWords);

        System.out.println("\nModified mode (spaces are not punctuation)");
        TextModified inputTextModified = new TextModified(input);

        System.out.println("\nYour entered text:");
        System.out.println(inputTextModified);

        Set<String> uniqueWordsModified = new TreeSet<>();
        for (SentenceModified sentence : inputTextModified.getSentences()) {
            for (Object object : sentence.getObjects()) {
                if(object instanceof Word)
                    uniqueWordsModified.add(object.toString());
            }
        }
        System.out.print("In alphabet order:");
        System.out.println(uniqueWordsModified);
    }
}

class Letter {
    private final char character;
    public Letter(char character) {
        this.character = character;
    }

    @Override
    public String toString() {
        return Character.toString(character);
    }
}

class Punctuation {
    private final String punctuation;
    public Punctuation(String punctuation) {
        this.punctuation = punctuation;
    }

    @Override
    public String toString() {
        return punctuation;
    }
}

class Sentence {
    private final Word[] words;
    private final Punctuation[] punctuations;
    private boolean startsWithWord;

    public Sentence(String sentence) {
        String[] onlyWords = sentence.split("\\W+");
        this.words = new Word[onlyWords.length];
        for (int i = 0; i < onlyWords.length; i++) {
            words[i] = new Word(onlyWords[i]);
        }
        if (onlyWords.length>0 && !onlyWords[0].isEmpty())
            this.startsWithWord = true;
        String[] onlyPunctuation = sentence.split("\\w+");
        this.punctuations = new Punctuation[onlyPunctuation.length];
        for (int i = 0; i < onlyPunctuation.length; i++) {
            punctuations[i] = new Punctuation(onlyPunctuation[i]);
        }
    }

    public Word[] getWords() {
        return words;
    }

    @Override
    public String toString() {
        String sentence = "";
        if (punctuations.length == 0) return words[0].toString();
        if (words.length == 0) return punctuations[0].toString();
        if (startsWithWord && punctuations.length == words.length) {
            for (int i = 0; i < words.length; i++) {
                sentence += punctuations[i];
                sentence += words[i];
            }
            return sentence;
        }
        if (startsWithWord) {
            for (int i = 1; i < punctuations.length; i++) {
                sentence += words[i - 1];
                sentence += punctuations[i];
            }
            return sentence;
        }
        for (int i = 1; i < words.length; i++) {
            sentence += punctuations[i - 1];
            sentence += words[i];
        }
        return sentence;
    }
}

class SentenceModified {
    private final ArrayList<Object> objects;

    public SentenceModified(String sentence) {
        objects = new ArrayList<>();
        String word = "";
        for (int i = 0; i < sentence.length(); i++) {
            if (Character.isLetterOrDigit(sentence.charAt(i))) word += sentence.charAt(i);
            else {
                if (!word.isEmpty()) objects.add(new Word(word));
                if (!Character.isSpaceChar(sentence.charAt(i)))
                    objects.add(new Punctuation(Character.toString(sentence.charAt(i))));
                word = "";
            }
        }
        if (!word.isEmpty()) objects.add(new Word(word));
    }

    public ArrayList<Object> getObjects(){
        return objects;
    }

    @Override
    public String toString() {
        String sentence = "";
        for (Object s : objects) {
            sentence+=s+" ";
        }
        return sentence;
    }
}

class Text {
    private final Sentence[] sentences;
    public Text(String input) {
        input = input.replaceAll("[\\s\\t]+", " ");
        String[] inputText = input.split("[.!?]+");
        this.sentences = new Sentence[inputText.length];
        for(int i = 0; i< inputText.length; i++) {
            sentences[i] = new Sentence(inputText[i].trim());
        }
    }

    public Sentence[] getSentences() {
        return sentences;
    }

    @Override
    public String toString() {
        String text = "";
        for (Sentence sentence : sentences) {
            text +=sentence+"\n";
        }
        return text;
    }
}

class TextModified {
    private final SentenceModified[] sentences;
    public TextModified(String input) {
        input = input.replaceAll("[\\s\\t]+", " ");
        String[] inputText = input.split("[.!?]+");
        this.sentences = new SentenceModified[inputText.length];
        for(int i = 0; i< inputText.length; i++) {
            sentences[i] = new SentenceModified(inputText[i].trim());
        }
    }
    public SentenceModified[] getSentences() {
        return sentences;
    }

    @Override
    public String toString() {
        String text = "";
        for (SentenceModified sentence : sentences) {
            text +=sentence+"\n";
        }
        return text;
    }
}

class Word {
    private final Letter[] letters;
    public Word(String word) {
        this.letters = new Letter[word.length()];
        for (int i = 0; i < word.length(); i++) {
            this.letters[i] = new Letter(word.charAt(i));
        }
    }

    @Override
    public String toString() {
        String word = "";
        for (Letter l : letters) {
            word +=l;
        }
        return word;
    }
}