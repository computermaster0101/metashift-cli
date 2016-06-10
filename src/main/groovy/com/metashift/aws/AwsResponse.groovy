package com.metashift.aws

import com.amazonaws.AmazonWebServiceRequest
import groovy.transform.ToString

/**
 * Holds the response from an aws async call or the exception if an exception occurred
 * Created by navid on 3/9/15.
 */
@ToString
class AwsResponse {

    Exception exception

    AmazonWebServiceRequest request

    Object result

    boolean isError(){
        exception != null
    }

}
