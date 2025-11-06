package com.pm.billingservice.grpc;
import billing.BillingResponse;
import billing.BillingServiceGrpc;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

@Slf4j
@GrpcService
public class BillingGrpcService extends BillingServiceGrpc.BillingServiceImplBase // comes from billing package
{

    @Override
    public void createBillingAccount(billing.BillingRequest billingRequest,
//   using stram observer for back & forth communication once connection is established
             StreamObserver<billing.BillingResponse> responseObserver){

        log.info("createBillingRequestAccount request received {}", billingRequest.toString());

//                Buisness logic


        BillingResponse response = BillingResponse.newBuilder()
                .setAccountId("12345")
                .setStatus("ACTIVE")
                .build();

        responseObserver.onNext(response); // used to send response back to the client
        responseObserver.onCompleted(); // response is completed and we are ready to end the cycle (ending data stream)
    }
}
