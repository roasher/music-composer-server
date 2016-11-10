package ru.pavelyurkin.musiccomposer.persistance.jpa.factory;

import org.springframework.stereotype.Component;
import ru.pavelyurkin.musiccomposer.persistance.jpa.CompositionInfo;

/**
 * Created by Wish on 03.07.2015.
 */
@Component
public class CompositionInfoFactory extends AbstractFactory<CompositionInfo> {

    public CompositionInfo getInstance( String author, String title, double tempo ) {
        CompositionInfo compositionInfo = new CompositionInfo();
        compositionInfo.author = author;
        compositionInfo.title = title;
        compositionInfo.tempo = tempo;
        return getUniqueInstance( compositionInfo );
    }

}
