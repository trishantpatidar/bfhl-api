package com.example.bfhl.util;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BfhlParser {

    private static final Pattern TOKEN_PATTERN = Pattern.compile("-?\\d+|[a-zA-Z]+|[^a-zA-Z0-9\\s]+");

    public static ParseResult parse(List<String> data) {
        List<String> oddNumbers = new ArrayList<>();
        List<String> evenNumbers = new ArrayList<>();
        List<String> alphabets = new ArrayList<>();
        List<String> specialCharacters = new ArrayList<>();
        BigInteger sum = BigInteger.ZERO;
        StringBuilder alphabeticChars = new StringBuilder();

        if (data == null) {
            return new ParseResult(oddNumbers, evenNumbers, alphabets, specialCharacters, "0", "");
        }

        for (String item : data) {
            if (item == null) {
                continue;
            }
            String trimmed = item.trim();
            if (trimmed.isEmpty()) {
                continue; // Handle empty values gracefully by skipping
            }

            // If the item is strictly numeric (negative or positive)
            if (trimmed.matches("-?\\d+")) {
                processNumeric(trimmed, oddNumbers, evenNumbers);
                sum = sum.add(new BigInteger(trimmed));
            } 
            // If the item is strictly alphabetic
            else if (trimmed.matches("[a-zA-Z]+")) {
                processAlphabetic(trimmed, alphabets, alphabeticChars);
            } 
            // If the item is strictly special characters
            else if (trimmed.matches("[^a-zA-Z0-9]+")) {
                specialCharacters.add(trimmed);
            } 
            // If the item is mixed (e.g. "a1$", "33a", etc.)
            else {
                Matcher matcher = TOKEN_PATTERN.matcher(trimmed);
                while (matcher.find()) {
                    String token = matcher.group();
                    if (token.matches("-?\\d+")) {
                        processNumeric(token, oddNumbers, evenNumbers);
                        sum = sum.add(new BigInteger(token));
                    } else if (token.matches("[a-zA-Z]+")) {
                        processAlphabetic(token, alphabets, alphabeticChars);
                    } else {
                        specialCharacters.add(token);
                    }
                }
            }
        }

        // Build concat_string: Take all alphabetical characters, reverse them, and convert to alternating caps
        String reversedAlphabetic = alphabeticChars.reverse().toString();
        StringBuilder concatStrBuilder = new StringBuilder();
        for (int i = 0; i < reversedAlphabetic.length(); i++) {
            char c = reversedAlphabetic.charAt(i);
            if (i % 2 == 0) {
                concatStrBuilder.append(Character.toUpperCase(c));
            } else {
                concatStrBuilder.append(Character.toLowerCase(c));
            }
        }

        return new ParseResult(
            oddNumbers,
            evenNumbers,
            alphabets,
            specialCharacters,
            sum.toString(),
            concatStrBuilder.toString()
        );
    }

    private static void processNumeric(String numStr, List<String> odd, List<String> even) {
        try {
            BigInteger val = new BigInteger(numStr);
            // Classify odd/even
            if (val.mod(BigInteger.TWO).equals(BigInteger.ZERO)) {
                even.add(numStr);
            } else {
                odd.add(numStr);
            }
        } catch (NumberFormatException e) {
            // Fallback for unexpected number format (should not happen with match check)
            char lastChar = numStr.charAt(numStr.length() - 1);
            if ("02468".indexOf(lastChar) >= 0) {
                even.add(numStr);
            } else {
                odd.add(numStr);
            }
        }
    }

    private static void processAlphabetic(String letters, List<String> alphabets, StringBuilder alphabeticChars) {
        for (int i = 0; i < letters.length(); i++) {
            char c = letters.charAt(i);
            alphabets.add(String.valueOf(Character.toUpperCase(c)));
            alphabeticChars.append(c);
        }
    }

    public static class ParseResult {
        private final List<String> oddNumbers;
        private final List<String> evenNumbers;
        private final List<String> alphabets;
        private final List<String> specialCharacters;
        private final String sum;
        private final String concatString;

        public ParseResult(List<String> oddNumbers, List<String> evenNumbers, List<String> alphabets,
                           List<String> specialCharacters, String sum, String concatString) {
            this.oddNumbers = oddNumbers;
            this.evenNumbers = evenNumbers;
            this.alphabets = alphabets;
            this.specialCharacters = specialCharacters;
            this.sum = sum;
            this.concatString = concatString;
        }

        public List<String> getOddNumbers() {
            return oddNumbers;
        }

        public List<String> getEvenNumbers() {
            return evenNumbers;
        }

        public List<String> getAlphabets() {
            return alphabets;
        }

        public List<String> getSpecialCharacters() {
            return specialCharacters;
        }

        public String getSum() {
            return sum;
        }

        public String getConcatString() {
            return concatString;
        }
    }
}
