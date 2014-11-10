package controller;

import decomposer.SignatureDecomposer;
import jm.JMC;
import jm.util.Read;
import model.Signature;
import model.composition.Composition;
import model.composition.CompositionInfo;
import model.viewer.BatchSignatureViewer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Class represents controller in standard MVC pattern
 * @author Pavel Yurkin
 */
public class Controller implements JMC {

    Logger logger = LoggerFactory.getLogger( getClass() );

    /**
     * Analyzes compositions and stores signatures from them
     * @param args
     */
    public static void main( String[] args ) throws IOException {
        //        String dirPath = "D:\\YandexDisk\\Музыка\\Algorithmic music\\Database\\Test\\Bach chorals\\";

        File[] files = new File[]{
                new File("D:\\YandexDisk\\Музыка\\Письмо\\1"),
                new File("D:\\YandexDisk\\Музыка\\Письмо\\2"),
                new File("D:\\YandexDisk\\Музыка\\Письмо\\4"),
                new File("D:\\YandexDisk\\Музыка\\Письмо\\5"),

//                new File("D:\\JAVA\\music\\test midi"),

        };

        List< Composition > compositions = new Controller().getCompositions( files );

        ApplicationContext context = new ClassPathXmlApplicationContext( "spring.configuration.xml" );

        SignatureDecomposer decomposer = context.getBean( SignatureDecomposer.class );
        Map< Signature, Set< Signature > > signatures = decomposer.analyzeSignatures( compositions );

        BatchSignatureViewer batchSignatureViewer = context.getBean( BatchSignatureViewer.class );
        batchSignatureViewer.view( signatures );

//        StoreManager storeManager = context.getBean( StoreManager.class );
//        storeManager.storeSignatures( signatures );
    }

    private List<File> listFilesForFolder(final File folder) {
        List<File> nameArray = new ArrayList<>();
        for (final File fileEntry : folder.listFiles()) {
            if (!fileEntry.isDirectory()) {
                nameArray.add( fileEntry );
            }
        }
        return nameArray;
    }

    public List< Composition > getCompositions( File directory ) {
        List< Composition > compositions = new ArrayList<Composition>(  );
        List<File> listFiles = listFilesForFolder( directory );
        for ( File currentFile : listFiles ) {
            if ( !currentFile.getName().matches( ".*\\.mid" ) || currentFile.getName().matches( ".*drum.*" ) ) continue;
            logger.info( " Reading composition {}", currentFile);
            CompositionInfo compositionInfo = new CompositionInfo();
            compositionInfo.setTitle(currentFile.getName());

            Composition composition = new Composition();
            composition.setCompositionInfo( compositionInfo );

            Read.midi( composition, currentFile.getAbsolutePath() );
            composition.roundAllRhythmValues();
            compositions.add( composition );
        }
        return compositions;
    }

    public List< Composition > getCompositions( File ... directories ) {
        List< Composition > compositions = new ArrayList<Composition>(  );
        for ( File currentDirectory : directories ) {
            compositions.addAll( getCompositions( currentDirectory ) );
        }
        return compositions;
    }

}