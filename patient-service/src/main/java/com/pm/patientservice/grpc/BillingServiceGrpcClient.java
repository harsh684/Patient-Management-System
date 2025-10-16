package com.pm.patientservice.grpc;

import billing.BillingRequest;
import billing.BillingResponse;
import billing.BillingServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BillingServiceGrpcClient {
    //    variable to hold our client
    private final BillingServiceGrpc.BillingServiceBlockingStub blockingStub;

    //    adding env variables for server variables to docker container
//    these env variable get injected by spring (seperate for address and port so easily configurable)
// after hosting aws.grpc:123123/BillingService/CreatePatientAccount
    public BillingServiceGrpcClient(@Value("${billing.service.address:localhost}") String serverAddress,
                                    @Value("${billing.service.grpc.port:9001}") int serverPort) {
        log.info("Connecting to Billing Service GRPC at {}:{}", serverAddress, serverPort);

//        creates a managed channel from server channel and port
//        usePlaintext() disables encryption
        ManagedChannel channel = ManagedChannelBuilder.forAddress(serverAddress, serverPort)
                .usePlaintext().build();

        blockingStub = BillingServiceGrpc.newBlockingStub(channel);
    }

    public BillingResponse createBillingAccount(String patiientId, String name, String email) {
        BillingRequest request = BillingRequest.newBuilder().setPatientId(patiientId).setName(name).setEmail(email).build();

        BillingResponse response = blockingStub.createBillingAccount(request);
        log.info("Received response from Billing Service GRPC at {}:{}", response);

        return response;
    }
}
