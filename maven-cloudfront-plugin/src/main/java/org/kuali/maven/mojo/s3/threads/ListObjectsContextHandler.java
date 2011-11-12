package org.kuali.maven.mojo.s3.threads;

import java.util.ArrayList;
import java.util.List;

import org.kuali.maven.mojo.s3.ListObjectsContext;
import org.kuali.maven.mojo.s3.S3BucketContext;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;

public class ListObjectsContextHandler implements ElementHandler<ListObjectsContext> {
    Object mutex = new Object();
    List<ObjectListing> objectListings;

    @Override
    public void handleElement(ListObjectsContext context) {
        S3BucketContext bucketContext = context.getBucketContext();
        AmazonS3Client client = bucketContext.getClient();
        ListObjectsRequest request = context.getRequest();
        ObjectListing listing = client.listObjects(request);
        synchronized (mutex) {
            if (objectListings == null) {
                objectListings = new ArrayList<ObjectListing>();
            }
            objectListings.add(listing);
        }
    }

    public List<ObjectListing> getObjectListings() {
        return objectListings;
    }

    public void setObjectListings(List<ObjectListing> objectListings) {
        this.objectListings = objectListings;
    }

}
