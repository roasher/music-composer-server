package persistance.model.compositionInfo;

import org.springframework.stereotype.Component;
import persistance.factory.AbstractFactory;

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
