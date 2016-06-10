package com.metashift.crash.completers

import com.amazonaws.resources.ec2.internal.EC2Impl

/**
 * Created by navid on 3/9/15.
 */
class AwsResourceCompleter extends MethodReferenceCompleter {

    AwsResourceCompleter() {
        super(EC2Impl.class)
    }
}
