package crash.commands.s3

import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.auth.AWSCredentialsProviderChain
import com.amazonaws.auth.profile.ProfileCredentialsProvider
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.*
import com.metashift.aws.s3.S3ObjectHolder
import org.crsh.cli.Argument
import org.crsh.cli.Command
import org.crsh.cli.Required
import org.crsh.cli.Usage
import org.crsh.command.BaseCommand
import org.crsh.command.Pipe

/**
 * Created by nicholaspadilla on 6/6/16.
 */

@Usage("S3 manager.")
class S3 extends BaseCommand {

    private AmazonS3Client getClient(String awsProfile){
        AWSCredentialsProvider credsProvider =  new AWSCredentialsProviderChain(new ProfileCredentialsProvider(awsProfile))
        AWSCredentials awsCredentials = credsProvider.getCredentials()
        AmazonS3Client s3Client = new AmazonS3Client(awsCredentials)
        return s3Client;
    }


    @Usage('List all buckets the current user has access to. You must provide the ~/.aws/credentials profile to use for authentication against aws S3 services. ')
    @Command
    public List<Bucket> listBuckets(
            @Usage('The ~/.aws/credentials profile to use for authentication against aws S3 services. ')
            @Argument
            @Required String awsProfile) {
        return getClient(awsProfile).listBuckets()
    }

    @Usage('List the data for the given bucket/prefix. Requires awsProfile, bucketName, prefix(optional) can be null.')
    @Command
    public ArrayList<S3ObjectHolder> listDataInBucket(
            @Usage('The ~/.aws/credentials profile to use for authentication against aws S3 services. ')
            @Argument
            @Required String awsProfile,
            @Usage('The bucket name to view data for. ')
            @Argument
            @Required String bucketName,
            @Usage('The prefix of the data we are trying to view data for. ')
            @Argument String prefix,
            @Usage('The size of the data set returned by AWS. 1000 by default. ')
            @Argument Integer maxSize) {

        if(prefix != null && prefix.equalsIgnoreCase("null")){
            prefix = null;
        }

        AmazonS3Client client = getClient(awsProfile);
        ListObjectsRequest req = new ListObjectsRequest().withBucketName(bucketName).withPrefix(prefix).withMaxKeys(maxSize)
        ObjectListing listing = client.listObjects( req )
        ArrayList<S3ObjectHolder> summaries = new ArrayList<>()

        for (S3ObjectSummary summary : listing.getObjectSummaries()){
            summaries.push(new S3ObjectHolder(summary))
        }

        while (listing.isTruncated()) {
            listing = client.listNextBatchOfObjects(listing);
            for (S3ObjectSummary summary : listing.getObjectSummaries()){
                summaries.push(new S3ObjectHolder(summary))
            }
        }
        return summaries
    }

    @Usage('List the data for the given bucket/prefix but allows you to scroll via terminal. Requires awsProfile, bucketName. Optional, prefix can be null, maxSize defaults to 1000.')
    @Command
    public void listDataInBucketContinuous(
            @Usage('The ~/.aws/credentials profile to use for authentication against aws S3 services. ')
            @Argument
            @Required String awsProfile,
            @Usage('The bucket name to view data for. ')
            @Argument
            @Required String bucketName,
            @Usage('The prefix of the data we are trying to view data for. Can be null, which gives all data in bucket.')
            @Argument String prefix,
            @Usage('The size of the data set returned by AWS. 1000 by default. ')
            @Argument Integer maxSize)  {

        out << 'Contents of Bucket => ' << red << bucketName + ' ' + prefix << reset << '\n'
        out.flush()

        if(prefix != null && prefix.equalsIgnoreCase("null")){
            prefix = null;
        }

        AmazonS3Client client = getClient(awsProfile);
        ListObjectsRequest req = new ListObjectsRequest().withBucketName(bucketName).withPrefix(prefix).withMaxKeys(maxSize)
        ObjectListing listing = client.listObjects( req )
        ArrayList<S3ObjectSummary> summaries = new ArrayList<>()
        summaries.addAll(listing.getObjectSummaries())

        for (S3ObjectSummary summary : summaries){
            out << '    BucketObject => ' << blue << '[ bucket='+summary.getBucketName()+', key='+summary.getKey()+', size='+summary.getSize()+' bytes, lastModified='+summary.getLastModified().toString()+' ]' << reset << '\n'
            out.flush()
        }

        while (listing.isTruncated()) {

            String line = context.readLine("Press Enter to Continue (Press 'Q' or 'q' 2 Quit then Enter)", false);
            if(line.equalsIgnoreCase('q')){
                break;
            }

            listing = client.listNextBatchOfObjects(listing);
            summaries.clear()
            summaries.addAll(listing.getObjectSummaries())
            for (S3ObjectSummary summary : summaries){
                out << '    BucketObject => ' << blue << '[ bucket='+summary.getBucketName()+', key='+summary.getKey()+', size='+summary.getSize()+' bytes, lastModified='+summary.getLastModified().toString()+' ]' << reset << '\n'
                out.flush()
            }
        }
    }

    @Usage('Filters the dataset from listDataInBucket command. Requires start/end times for filtering. Use format shown in output for lastModified field.')
    @Command
    public Pipe<ArrayList, ArrayList> s3ObjectFilter(
            @Usage('The start time to view data for. Use format shown in output for lastModified field.')
            @Argument
            @Required String startTime,
            @Usage('The end time to view data for. Use format shown in output for lastModified field.')
            @Argument
            @Required String endTime) {
        return new Pipe<ArrayList, ArrayList>() {
            public void provide(ArrayList holders) {
                ArrayList list = new ArrayList()
                long start = new Date(startTime).getTime()
                long end = new Date(endTime).getTime()
                for(Object obj : holders){
                    if(obj instanceof S3ObjectHolder){
                        long objTime = ((S3ObjectHolder)obj).getLastModified().getTime()
                        if(objTime >= start && objTime <= end){
                            list.push(obj)
                        }
                    }
                }
                context.provide(list);
            }
        };
    }

    @Usage('Downloads the dataset from listDataInBucket and s3ObjectFilter commands. Requires awsProfile and the fully qualified path to the directory the files should be downloaded to. ')
    @Command
    public Pipe<ArrayList, ArrayList> download(
            @Usage('The ~/.aws/credentials profile to use for authentication against aws S3 services. ')
            @Argument
            @Required String awsProfile,
            @Usage('The fully qualified path to the directory the files should be downloaded to. ')
            @Argument
            @Required String path) {
        return new Pipe<ArrayList, ArrayList>() {
            public void provide(ArrayList holders) {
                if(!path.endsWith("/")){
                    path += "/"
                }
                AmazonS3Client client = getClient(awsProfile);
                for(Object obj : holders){
                    if(obj instanceof S3ObjectHolder){
                        S3ObjectHolder holder = (S3ObjectHolder)obj
                        File localFile = new File(path+holder.getKey().replace("/","-"))
                        client.getObject(new GetObjectRequest(holder.getBucketName(), holder.getKey()), localFile)
                    }
                }
                context.provide(holders);
            }
        };
    }

}
