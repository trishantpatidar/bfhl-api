package com.example.bfhl.service;

import com.example.bfhl.dto.BfhlRequest;
import com.example.bfhl.dto.BfhlResponse;
import com.example.bfhl.service.impl.BfhlServiceImpl;
import com.example.bfhl.util.BfhlParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class BfhlServiceTest {

    private BfhlServiceImpl bfhlService;

    @BeforeEach
    void setUp() {
        bfhlService = new BfhlServiceImpl();
        // Set reflection properties simulating @Value injection
        ReflectionTestUtils.setField(bfhlService, "userName", "john_doe");
        ReflectionTestUtils.setField(bfhlService, "userDob", "17091999");
        ReflectionTestUtils.setField(bfhlService, "userEmail", "john.doe@example.com");
        ReflectionTestUtils.setField(bfhlService, "userRollNumber", "ABCD123");
    }

    @Test
    void testProcessRequest_SuccessSampleData() {
        BfhlRequest request = new BfhlRequest(Arrays.asList("a", "1", "334", "4", "R", "$"));
        BfhlResponse response = bfhlService.processRequest(request);

        assertTrue(response.isSuccess());
        assertEquals("john_doe_17091999", response.getUserId());
        assertEquals("john.doe@example.com", response.getEmail());
        assertEquals("ABCD123", response.getRollNumber());

        // Validate odd/even classification
        assertEquals(Arrays.asList("1"), response.getOddNumbers());
        assertEquals(Arrays.asList("334", "4"), response.getEvenNumbers());

        // Validate alphabets and casing (must be uppercase in array)
        assertEquals(Arrays.asList("A", "R"), response.getAlphabets());

        // Validate special characters
        assertEquals(Arrays.asList("$"), response.getSpecialCharacters());

        // Validate sum: 1 + 334 + 4 = 339
        assertEquals("339", response.getSum());

        // Validate concat_string: alphabets are "a", "R" -> reversed "Ra" -> alternating caps: "Ra"
        assertEquals("Ra", response.getConcatString());
    }

    @Test
    void testProcessRequest_MixedStrings() {
        BfhlRequest request = new BfhlRequest(Arrays.asList("a1b", "$"));
        BfhlResponse response = bfhlService.processRequest(request);

        // a1b contains: "a" (alphabet), "1" (odd number), "b" (alphabet)
        // "$" is special character
        assertEquals(Arrays.asList("1"), response.getOddNumbers());
        assertEquals(Arrays.asList("A", "B"), response.getAlphabets());
        assertEquals(Arrays.asList("$"), response.getSpecialCharacters());
        assertEquals("1", response.getSum());
        
        // alphabetic chars: "a", "b" -> reversed "ba" -> alternating caps: "Ba"
        assertEquals("Ba", response.getConcatString());
    }

    @Test
    void testProcessRequest_EmptyAndNullValues() {
        BfhlRequest request = new BfhlRequest(Arrays.asList("", null, "   "));
        BfhlResponse response = bfhlService.processRequest(request);

        assertTrue(response.isSuccess());
        assertTrue(response.getOddNumbers().isEmpty());
        assertTrue(response.getEvenNumbers().isEmpty());
        assertTrue(response.getAlphabets().isEmpty());
        assertTrue(response.getSpecialCharacters().isEmpty());
        assertEquals("0", response.getSum());
        assertEquals("", response.getConcatString());
    }

    @Test
    void testParser_AlternatingCapsLongWord() {
        // alphabetic chars: "a", "b", "c", "d" -> reversed: "dcba" -> DcBa
        BfhlParser.ParseResult result = BfhlParser.parse(Arrays.asList("a", "b", "c", "d"));
        assertEquals("DcBa", result.getConcatString());
    }

    @Test
    void testParser_LargeAndNegativeNumbers() {
        // 10000000000000000001 is odd, -2 is even
        BfhlParser.ParseResult result = BfhlParser.parse(Arrays.asList("10000000000000000001", "-2"));
        assertEquals(Collections.singletonList("10000000000000000001"), result.getOddNumbers());
        assertEquals(Collections.singletonList("-2"), result.getEvenNumbers());
        assertEquals("9999999999999999999", result.getSum());
    }
}
