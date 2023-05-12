package com.serverless.inbound;

import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import com.serverless.entity.ApiGatewayResponse;
import com.serverless.inbound.dto.TokenDTO;
import com.serverless.outbound.AuthRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AuthenticationHandler implements RequestHandler<AwsProxyRequest, ApiGatewayResponse> {

    private static final Logger log = LogManager.getLogger(AuthenticationHandler.class);

    @Override
    public ApiGatewayResponse handleRequest(AwsProxyRequest request, Context context) {
        String token = request.getPathParameters().get("token");
        log.info("Realizando autenticação com o token: {}", token);
        AuthRepository authRepository = new AuthRepository();
        TokenDTO tokenDTO = authRepository.findByToken(token);
        if (tokenDTO == null) {
            log.info("Erro ao realizar a autenticação, faça login novamente");
            return ApiGatewayResponse.builder()
                    .setStatusCode(403)
                    .build();
        }
        log.info("Autenticação realizada com sucesso");
        return ApiGatewayResponse.builder()
                .setStatusCode(200)
                .build();
    }
}
