package ru.pavelyurkin.musiccomposer.core.utils;

import jm.music.data.Part;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.pavelyurkin.musiccomposer.core.model.InstrumentPart;

@Component
public class InstrumentPartToPartConverter implements Converter<InstrumentPart, Part> {
	@Override
	public Part convert( InstrumentPart instrumentPart ) {
		// todo implement
		return null;
	}

	public InstrumentPart convertTo( Part part ) {
		// todo implement
		return null;
	}
}
