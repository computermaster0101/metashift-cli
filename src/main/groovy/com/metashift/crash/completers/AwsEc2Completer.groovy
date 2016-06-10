package com.metashift.crash.completers

import com.amazonaws.services.ec2.AmazonEC2Client

/**
 * Created by navid on 3/9/15.
 */
class AwsEc2Completer extends MethodReferenceCompleter {

    AwsEc2Completer() {
        super(AmazonEC2Client.class)
    }
}
