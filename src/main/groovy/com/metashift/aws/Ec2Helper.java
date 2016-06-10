package com.metashift.aws;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.ec2.AmazonEC2Async;
import com.amazonaws.services.ec2.AmazonEC2AsyncClient;
import com.amazonaws.services.ec2.model.*;
import org.springframework.core.io.DefaultResourceLoader;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Ec2Helper{


    public static final String STATE_TERMINATED = "terminated";

    public static final String BEHAVIOR_TERMINATE = "terminate";

    public static final String DEFAULT_REGION = "ec2.us-east-1.amazonaws.com";

    private AmazonEC2Async client;

    public Ec2Helper()
    {
        this(new DefaultAWSCredentialsProviderChain().getCredentials(), DEFAULT_REGION);
    }

    public Ec2Helper(AWSCredentials credentials, String region)
    {
        client = new AmazonEC2AsyncClient(credentials);
        client.setEndpoint(region);
    }

    public String getKeyFingerprint(String keyName)
    {
        String fingerprint = null;
        DescribeKeyPairsResult result = client.describeKeyPairs(new DescribeKeyPairsRequest()
                .withKeyNames(keyName));
        if (!result.getKeyPairs().isEmpty())
        {
            fingerprint = result.getKeyPairs().get(0).getKeyFingerprint();
        }
        return fingerprint;
    }

    public KeyPair createKeyPair(String keyName, boolean overwrite)
    {
        if (overwrite)
        {
            client.deleteKeyPair(new DeleteKeyPairRequest(keyName));
        }
        CreateKeyPairResult result = client.createKeyPair(new CreateKeyPairRequest()
                .withKeyName(keyName));
        return result.getKeyPair();
    }

    public void createSecurityGroup(String name, String description)
    {
        CreateSecurityGroupRequest securityGroupRequest = new CreateSecurityGroupRequest();
        securityGroupRequest.withGroupName(name).withDescription(description);

        try
        {
            client.createSecurityGroup(securityGroupRequest);
        }
        catch (AmazonServiceException e)
        {
            if (!e.getErrorCode().equals("InvalidGroup.Duplicate"))
            {
                throw e;
            }
        }
    }

    public void addIPPermission(String groupId, IpPermission ipPermission)
    {
        AuthorizeSecurityGroupIngressRequest authorizeSecurityGroupIngressRequest = new AuthorizeSecurityGroupIngressRequest();
        authorizeSecurityGroupIngressRequest.withGroupId(groupId).withIpPermissions(
                ipPermission);

        try
        {
            client.authorizeSecurityGroupIngress(authorizeSecurityGroupIngressRequest);
        }
        catch (AmazonServiceException e)
        {
            if (!e.getErrorCode().equals("InvalidPermission.Duplicate"))
            {
                throw e;
            }
        }
    }

    public List<Instance> getInstances()
    {
        LinkedList<Instance> instances = new LinkedList<Instance>();
        DescribeInstancesResult result = client.describeInstances();
        for (Reservation reservations : result.getReservations())
        {
            for (Instance instance : reservations.getInstances())
            {
                if (!instance.getState().getName().equals(STATE_TERMINATED))
                {
                    instances.add(instance);
                }
            }
        }
        return instances;
    }

    public Instance getInstance(String instanceId)
    {
        Instance retval = null;
        DescribeInstancesResult result = client.describeInstances(new DescribeInstancesRequest()
                .withInstanceIds(instanceId));
        for (Reservation reservations : result.getReservations())
        {
            for (Instance instance : reservations.getInstances())
            {
                if (!instance.getState().getName().equals(STATE_TERMINATED))
                {
                    retval = instance;
                }
            }
        }
        return retval;
    }

    public SpotInstanceRequest getSpotInstanceRequest(String requestId)
    {
        DescribeSpotInstanceRequestsRequest describeRequest = new DescribeSpotInstanceRequestsRequest();
        ArrayList<String> spotInstanceRequestIds = new ArrayList<String>();
        spotInstanceRequestIds.add(requestId);
        describeRequest.setSpotInstanceRequestIds(spotInstanceRequestIds);

        SpotInstanceRequest request = null;
        try
        {
            DescribeSpotInstanceRequestsResult describeResult = client
                    .describeSpotInstanceRequests(describeRequest);
            request = describeResult.getSpotInstanceRequests().get(0);
        }
        catch (AmazonServiceException e)
        {
            if (!e.getErrorCode().equals("InvalidSpotInstanceRequestID.NotFound"))
            {
                throw e;
            }
        }
        return request;
    }

    public void terminateInstance(String instanceId)
    {
        client.terminateInstances(new TerminateInstancesRequest().withInstanceIds(instanceId));
    }

    public Instance startInstance(String instanceType, String imageName, String securityGroupId,
            String keyName)
    {
        RunInstancesRequest runInstancesRequest = new RunInstancesRequest();

        runInstancesRequest.withImageId(imageName).withInstanceType(instanceType).withMinCount(1)
                .withMaxCount(1)
                .withSecurityGroups(securityGroupId).withKeyName(keyName)
                .withInstanceInitiatedShutdownBehavior(BEHAVIOR_TERMINATE);

        RunInstancesResult runInstancesResult = client.runInstances(runInstancesRequest);

        return runInstancesResult.getReservation().getInstances().get(0);
    }

    public SpotInstanceRequest startSpotInstance(String price, String instanceType,
            String imageName,
            String securityGroup, String keyName) {

        RequestSpotInstancesRequest request = new RequestSpotInstancesRequest();

        request.setSpotPrice(price);
        request.setInstanceCount(1);

        LaunchSpecification launchSpecification = new LaunchSpecification();
        launchSpecification.setImageId(imageName);
        launchSpecification.setInstanceType(instanceType);
        launchSpecification.setKeyName(keyName);

        ArrayList<String> securityGroups = new ArrayList<String>();
        securityGroups.add(securityGroup);
        launchSpecification.setSecurityGroups(securityGroups);

        request.setLaunchSpecification(launchSpecification);

        RequestSpotInstancesResult result = client.requestSpotInstances(request);

        return result.getSpotInstanceRequests().get(0);
    }

    public void cancelSpotInstanceRequest(String spotInstanceRequestId)
    {
        ArrayList<String> spotInstanceRequestIds = new ArrayList<String>();
        spotInstanceRequestIds.add(spotInstanceRequestId);

        CancelSpotInstanceRequestsRequest cancelRequest = new CancelSpotInstanceRequestsRequest(
                spotInstanceRequestIds);
        try
        {
            client.cancelSpotInstanceRequests(cancelRequest);
        }
        catch (AmazonServiceException e)
        {
            if (!e.getErrorCode().equals("InvalidSpotInstanceRequestID.NotFound"))
            {
                throw e;
            }
        }
    }

    private static AWSCredentials getCredentials(String resource)
    {
        AWSCredentials credentials = null;
        try
        {
            InputStream inputStream = null;
            try
            {
                inputStream = new DefaultResourceLoader().getResource(resource).getInputStream();
                if (inputStream == null)
                {
                    throw new RuntimeException("Resource not found: " + resource);
                }
                credentials = new PropertiesCredentials(inputStream);
            }
            finally
            {
                if (inputStream != null)
                {
                    inputStream.close();
                }
            }
        }
        catch (IOException e)
        {
            throw new RuntimeException("Unable to load credentials from resource: " + resource, e);
        }
        return credentials;
    }
}
