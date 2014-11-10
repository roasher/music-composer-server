package database;

import model.Signature;

import java.io.*;
import java.util.*;

/**
 * Class saves the results of work
 * Created by Pavel Yurkin on 01.08.14.
 */
public class StoreManager {

    private String signatureStoreDir;

    /**
     * Stores signatures into the signature store directory
     * @param signatures
     * @throws IOException
     */
    public void storeSignatures( Set< Signature > signatures ) throws IOException {
        try ( FileOutputStream fileOutputStream = new FileOutputStream( signatureStoreDir );
              ObjectOutputStream objectOutputStream = new ObjectOutputStream( fileOutputStream ); ) {
            for ( Signature signature : signatures ) {
                objectOutputStream.writeObject( signature );
            }
            objectOutputStream.flush();
        }
    }

    /**
     * Retrieves signatures from signature store directory
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public List<Signature> getStoredSignatures() throws IOException, ClassNotFoundException {
        List< Signature > storedSignatures = new ArrayList<>();
        try ( FileInputStream fileOutputStream = new FileInputStream( signatureStoreDir );
              ObjectInputStream objectOutputStream = new ObjectInputStream( fileOutputStream ); ) {
            Signature signature = null;
            while ( ( signature = ( Signature ) objectOutputStream.readObject() ) != null ) {
                storedSignatures.add( signature );
            }
        }
        return storedSignatures;
    }

    public String getSignatureStoreDir() {
        return signatureStoreDir;
    }

    public void setSignatureStoreDir( String signatureStoreDir ) {
        this.signatureStoreDir = signatureStoreDir;
    }
}
