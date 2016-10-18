package com.microsoft.azure.documentdb.sample.dao;

import com.microsoft.azure.documentdb.ConnectionPolicy;
import com.microsoft.azure.documentdb.ConsistencyLevel;
import com.microsoft.azure.documentdb.DocumentClient;

public class DocumentClientFactory {
    private static final String HOST = "https://valdadocdb.documents.azure.com:443/";
    private static final String MASTER_KEY = "CzKUGwpq4EAflPKxGw6iNHPyxtR8qyFOf3NOFOI3Qg3KjMpVsFMRDunYSmQsFc4f4Pg2C8CGLk6QZ0TtpRVJsQ==";

    private static DocumentClient documentClient;

    public static DocumentClient getDocumentClient() {
        if (documentClient == null) {
            documentClient = new DocumentClient(HOST, MASTER_KEY,
                    ConnectionPolicy.GetDefault(), ConsistencyLevel.Session);
        }

        return documentClient;
    }

}
