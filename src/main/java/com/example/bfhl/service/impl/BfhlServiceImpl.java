package com.example.bfhl.service.impl;

import com.example.bfhl.dto.BfhlRequest;
import com.example.bfhl.dto.BfhlResponse;
import com.example.bfhl.service.BfhlService;
import com.example.bfhl.util.BfhlParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class BfhlServiceImpl implements BfhlService {

    @Value("${bfhl.user.name:john_doe}")
    private String userName;

    @Value("${bfhl.user.dob:17091999}")
    private String userDob;

    @Value("${bfhl.user.email:john.doe@example.com}")
    private String userEmail;

    @Value("${bfhl.user.roll-number:ABCD123}")
    private String userRollNumber;

    @Override
    public BfhlResponse processRequest(BfhlRequest request) {
        // Build user_id: full_name_ddmmyyyy (must be lowercase)
        // Replacing spaces with underscores if there are any
        String formattedName = userName.trim().toLowerCase().replaceAll("\\s+", "_");
        String userId = formattedName + "_" + userDob.trim();

        // Parse input data
        BfhlParser.ParseResult parseResult = BfhlParser.parse(request.getData());

        // Construct and return response DTO
        return new BfhlResponse(
            true,
            userId,
            userEmail,
            userRollNumber,
            parseResult.getOddNumbers(),
            parseResult.getEvenNumbers(),
            parseResult.getAlphabets(),
            parseResult.getSpecialCharacters(),
            parseResult.getSum(),
            parseResult.getConcatString()
        );
    }
}
