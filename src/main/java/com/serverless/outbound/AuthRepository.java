package com.serverless.outbound;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;
import com.serverless.inbound.dto.TokenDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.HashMap;
import java.util.Map;

public class AuthRepository {
    private static final Logger LOG = LogManager.getLogger(AuthRepository.class);
    private AmazonDynamoDB dynamoDB;

    public AuthRepository() {
        this.dynamoDB = AmazonDynamoDBClientBuilder.standard()
                .withRegion(Regions.US_EAST_1)
                .build();
    }

    public TokenDTO findByToken(String token) {
        Map<String, String> attributesNames = new HashMap<>();
        attributesNames.put("#token", "token");
        Map<String, AttributeValue> attributeValues = new HashMap<>();
        attributeValues.put(":token", new AttributeValue().withS(token));
        QueryRequest query = new QueryRequest("AuthV5")
                .withIndexName("token-index")
                .withKeyConditionExpression("#token = :token")
                .withExpressionAttributeNames(attributesNames)
                .withExpressionAttributeValues(attributeValues);
        QueryResult result = this.dynamoDB.query(query);
        LOG.info("resultado encontrado: {}", result);
        if (result.getCount() > 0) {
            return new TokenDTO(result.getItems().get(0).get("token").getS());
        }
        return null;
    }
}
