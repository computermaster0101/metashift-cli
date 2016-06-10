package com.metashift.aws;


import com.amazonaws.handlers.AsyncHandler;
import com.amazonaws.regions.*;
import com.amazonaws.resources.ServiceBuilder;
import com.amazonaws.resources.ec2.EC2;
import com.amazonaws.services.ec2.AmazonEC2AsyncClient;
import com.amazonaws.services.ec2.model.*;
import rx.Observable;

/**
 * Created by navid on 3/1/15.
 */
public class AwsHelper {

    private final AmazonEC2AsyncClient client = new AmazonEC2AsyncClient();

    private final EC2 ec2Client = ServiceBuilder.forService(EC2.class)
            .withRegion(com.amazonaws.regions.Region.getRegion(Regions.US_EAST_1))
            .build();

    public Observable<Instance> listInstances(){
        return listInstances(null);
    }

    public Observable<Instance> listInstances(String instanceId) {
        Observable.OnSubscribe<Instance> subscribeFunction = subscriber -> {

            DescribeInstancesRequest request = new DescribeInstancesRequest();
            if(instanceId != null) request.withInstanceIds(instanceId);

            client.describeInstancesAsync(request, new AsyncHandler<DescribeInstancesRequest, DescribeInstancesResult>() {
                @Override
                public void onError(Exception ex) {
                    if (!subscriber.isUnsubscribed()) {
                        subscriber.onError(ex);
                    }
                }
                @Override
                public void onSuccess(DescribeInstancesRequest request, DescribeInstancesResult result) {
                    for (final Reservation reservation : result.getReservations()) {
                        for(Instance instance: reservation.getInstances()){
                            if (!subscriber.isUnsubscribed()) {
                                subscriber.onNext(instance);
                            }else{
                                break;
                            }
                        }
                    }

                    if (!subscriber.isUnsubscribed()) {
                        subscriber.onCompleted();
                    }
                }
            });
        };

        return rx.Observable.create(subscribeFunction);
    }

    public void startInstance(String instanceId) {
        final DescribeInstancesRequest request = new DescribeInstancesRequest().withInstanceIds(instanceId);
        final DescribeInstancesResult describeInstanceResult = client.describeInstances(request);
        final Instance instance = describeInstanceResult.getReservations().get(0).getInstances().get(0);
        if(instance.getState().getName().equalsIgnoreCase("stopped")) {
            final StartInstancesRequest startRequest = new StartInstancesRequest().withInstanceIds(instanceId);
            client.startInstances(startRequest);
        }
    }

    public void stopInstance(String instanceId) {
        final DescribeInstancesRequest request = new DescribeInstancesRequest().withInstanceIds(instanceId);
        final DescribeInstancesResult describeInstanceResult = client.describeInstances(request);
        final Instance instance = describeInstanceResult.getReservations().get(0).getInstances().get(0);
        if(instance.getState().getName().equalsIgnoreCase("running")) {
            final StopInstancesRequest stopRequest = new StopInstancesRequest().withInstanceIds(instanceId);
            client.stopInstances(stopRequest);
        }
    }

//    @Override
//    public Future<Map<String, String>> getImages() {
//        final DescribeImagesRequest request = new DescribeImagesRequest().withOwners(owner);
//        final Promise<Map<String, String>> promise = Futures.promise();
//        client.describeImagesAsync(request, new AsyncHandler<DescribeImagesRequest, DescribeImagesResult>() {
//            @Override
//            public void onError(Exception ex) {
//                promise.failure(ex);
//            }
//
//            @Override
//            public void onSuccess(DescribeImagesRequest request, DescribeImagesResult result) {
//                final Map<String, String> map = new HashMap<String, String>();
//                for(final Image image : result.getImages()) {
//                    map.put(image.getImageId(), image.getName());
//                }
//                promise.success(map);
//            }
//        });
//        return promise.future();
//    }
//
//    @Override
//    public Future<Map<String, String>> getKeysMap() {
//        final Promise<Map<String, String>> promise = Futures.promise();
//        client.describeKeyPairsAsync(new DescribeKeyPairsRequest(), new AsyncHandler<DescribeKeyPairsRequest, DescribeKeyPairsResult>() {
//            @Override
//            public void onError(Exception ex) {
//                promise.failure(ex);
//            }
//
//            @Override
//            public void onSuccess(DescribeKeyPairsRequest request, DescribeKeyPairsResult result) {
//                final Map<String, String> map = new HashMap<String, String>();
//                for(final KeyPairInfo info : result.getKeyPairs()) {
//                    map.put(info.getKeyName(), info.getKeyName());
//                }
//                promise.success(map);
//            }
//        });
//        return promise.future();
//    }
//
//    @Override
//    public Future<Map<String, String>> getSecurityGroupsMap() {
//        final Promise<Map<String, String>> promise = Futures.promise();
//        client.describeSecurityGroupsAsync(new DescribeSecurityGroupsRequest(), new AsyncHandler<DescribeSecurityGroupsRequest, DescribeSecurityGroupsResult>() {
//            @Override
//            public void onError(Exception ex) {
//                promise.failure(ex);
//            }
//
//            @Override
//            public void onSuccess(DescribeSecurityGroupsRequest request, DescribeSecurityGroupsResult result) {
//                final Map<String, String> map = new HashMap<String, String>();
//                for(final SecurityGroup group : result.getSecurityGroups()) {
//                    map.put(group.getGroupId(), group.getGroupName());
//                }
//                promise.success(map);
//            }
//
//        });
//        return promise.future();
//    }



}
