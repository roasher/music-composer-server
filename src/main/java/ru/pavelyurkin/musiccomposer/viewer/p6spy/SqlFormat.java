package ru.pavelyurkin.musiccomposer.viewer.p6spy;

import com.p6spy.engine.spy.appender.MessageFormattingStrategy;
import org.springframework.util.StringUtils;

/**
 * Created by wish on 14.12.2015.
 * Implementation of MessageFormattingStrategy that leaves sql only
 */
public class SqlFormat implements MessageFormattingStrategy {
	@Override
	public String formatMessage( int connectionId, String now, long elapsed, String category, String prepared, String sql ) {
		return ( StringUtils.isEmpty( sql ) ? prepared : sql ) + ";";
	}
}
