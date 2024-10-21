package qlhtt.Test;

public class TestRegex {
    public static void main(String[] args) {
        String regex = "^(?:\\p{Lu}\\p{Ll}*\\s*)+$";
        String input = "Nguyễn Văn A";
        System.out.println(input.matches(regex));
    }
}
