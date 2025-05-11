
public class Palindrome {
    public Deque<Character> wordToDeque(String word) {
        Deque<Character> deque = new LinkedListDeque<>();
        for (int i = 0; i < word.length(); i++) {
            deque.addLast(word.charAt(i));
        }
        return deque;
    }
    public boolean isPalindrome(String word) {
        LinkedListDeque<Character> deque = (LinkedListDeque<Character>) wordToDeque(word);
        while (deque.size() > 1) {
            char first = deque.removeFirst();
            char last = deque.removeLast();
            if (first != last) {
                return false;
            }
        }
        return true;
    }
    public boolean isPalindrome(String word, CharacterComparator cc) {
        LinkedListDeque<Character> deque = (LinkedListDeque<Character>) wordToDeque(word);
        while (deque.size() > 1) {
            char first = deque.removeFirst();
            char last = deque.removeLast();
            if (!cc.equalChars(first, last)) {
                return false;
            }
        }
        return true;
    }
}
