package com.metashift.aws.s3

import com.amazonaws.services.s3.model.Owner
import com.amazonaws.services.s3.model.S3ObjectSummary

/**
 * Created by nicholaspadilla on 6/6/16.
 */
public class S3ObjectHolder {

    private S3ObjectSummary summary

    public S3ObjectHolder(S3ObjectSummary summary) {
        this.summary = summary
    }

    public String getBucketName() {
        return summary.getBucketName();
    }

    public String getKey() {
        return summary.getKey();
    }

    public String getETag() {
        return summary.getETag();
    }

    public long getSize() {
        return summary.getSize();
    }

    public Date getLastModified() {
        return summary.getLastModified();
    }

    public Owner getOwner() {
        return summary.getOwner();
    }

    public String getStorageClass() {
        return summary.getStorageClass();
    }

    @Override
    public String toString() {
        return "S3ObjectHolder{"+'bucket='+summary.getBucketName()+', key='+summary.getKey()+', size='+summary.getSize()+' bytes, lastModified='+summary.getLastModified().toString()+'}\n';
    }
}
