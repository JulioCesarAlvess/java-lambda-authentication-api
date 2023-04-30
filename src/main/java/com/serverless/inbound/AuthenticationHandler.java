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
        String idUsuario = request.getPathParameters().get("idUsuario");
        Gson gson = new Gson();
        log.info("Realizando autenticação do usuário: {}", idUsuario);
        AuthRepository authRepository = new AuthRepository();
        TokenDTO tokenDTO = authRepository.findByUsuario(idUsuario);
        if (tokenDTO == null) {
            log.info("Erro ao realizar a autenticação, faça login novamente");
            return ApiGatewayResponse.builder()
                    .setStatusCode(403)
                    .build();
        }
        return ApiGatewayResponse.builder()
                .setStatusCode(200)
                .setRawBody(gson.toJson(tokenDTO))
                .build();
    }
}
