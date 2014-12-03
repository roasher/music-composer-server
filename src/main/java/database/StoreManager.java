package database;

import model.melody.Melody;

import java.io.*;
import java.util.*;

/**
 * Class saves the results of work
 * Created by Pavel Yurkin on 01.08.14.
 */
public class StoreManager {

    private String signatureStoreDir;

    /**
     * Stores melodies into the melody store directory
     * @param melodies
     * @throws IOException
     */
    public void storeSignatures( Set<Melody> melodies ) throws IOException {
        try ( FileOutputStream fileOutputStream = new FileOutputStream( signatureStoreDir );
              ObjectOutputStream objectOutputStream = new ObjectOutputStream( fileOutputStream ); ) {
            for ( Melody melody : melodies ) {
                objectOutputStream.writeObject( melody );
            }
            objectOutputStream.flush();
        }
    }

    /**
     * Retrieves melodies from melody store directory
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public List<Melody> getStoredSignatures() throws IOException, ClassNotFoundException {
        List<Melody> storedMelodies = new ArrayList<>();
        try ( FileInputStream fileOutputStream = new FileInputStream( signatureStoreDir );
              ObjectInputStream objectOutputStream = new ObjectInputStream( fileOutputStream ); ) {
            Melody melody = null;
            while ( ( melody = ( Melody ) objectOutputStream.readObject() ) != null ) {
                storedMelodies.add( melody );
            }
        }
        return storedMelodies;
    }

    public String getSignatureStoreDir() {
        return signatureStoreDir;
    }

    public void setSignatureStoreDir( String signatureStoreDir ) {
        this.signatureStoreDir = signatureStoreDir;
    }
}
