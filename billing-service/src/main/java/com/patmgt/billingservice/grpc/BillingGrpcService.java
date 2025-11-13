package com.patmgt.billingservice.grpc;

import billing.BillingServiceGrpc.BillingServiceImplBase;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GrpcService
public class BillingGrpcService extends BillingServiceImplBase {

    private static final Logger log = LoggerFactory.getLogger(BillingGrpcService.class);

    public void createBillingAccount(
            billing.BillingRequest billingRequest,
            StreamObserver<billing.BillingResponse> responseObserver
    ){
        log.info("createBillingAccount received request {}", billingRequest.toString());

        billing.BillingResponse response = billing.BillingResponse
                .newBuilder()
                .setAccountId("1234567")
                .setStatus("active")
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
