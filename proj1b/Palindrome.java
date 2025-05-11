import java.util.LinkedList;

public class Palindrome {
    public Deque<Character> wordToDeque(String word){
        Deque<Character> deque = new LinkedListDeque<>();
        for(int i = 0; i < word.length(); i++){
            deque.addLast(word.charAt(i));
        }
        return deque;
    }
    public boolean isPalindrome(String word){
        LinkedListDeque<Character> deque = (LinkedListDeque<Character>) wordToDeque(word);
        for (int i = 0; i < word.length()/2; i++){
            if (deque.get(i) != deque.get(word.length()-i-1)){
                return false;
            }
        }
        return true;
    }
    public boolean isPalindrome(String word, CharacterComparator cc){
        LinkedListDeque<Character> deque = (LinkedListDeque<Character>) wordToDeque(word);
        for (int i = 0; i < word.length()/2; i++){
            if (!cc.equalChars(deque.get(i) , deque.get(word.length()-i-1))){
                return false;
            }
        }
        return true;
    }
}
