package com.metashift.crash.completers

import com.amazonaws.services.ec2.AmazonEC2AsyncClient
/**
 * Created by navid on 3/9/15.
 */
class AwsEc2AsyncCompleter extends MethodReferenceCompleter {

    AwsEc2AsyncCompleter() {
        super(AmazonEC2AsyncClient.class)
    }
}
