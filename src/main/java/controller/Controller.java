package controller;

import decomposer.SignatureDecomposer;
import jm.JMC;
import jm.util.Read;
import model.Melody;
import model.composition.Composition;
import model.composition.CompositionInfo;
import utils.CompositionLoader;
import viewer.BatchMelodyViewer;
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

        List< Composition > compositions = new CompositionLoader().getCompositions( files );

        ApplicationContext context = new ClassPathXmlApplicationContext( "spring.configuration.xml" );

        SignatureDecomposer decomposer = context.getBean( SignatureDecomposer.class );
        Map<Melody, Set<Melody> > signatures = decomposer.analyzeSignatures( compositions );

        BatchMelodyViewer batchSignatureViewer = context.getBean( BatchMelodyViewer.class );
        batchSignatureViewer.view( signatures );

//        StoreManager storeManager = context.getBean( StoreManager.class );
//        storeManager.storeSignatures( signatures );
    }


}