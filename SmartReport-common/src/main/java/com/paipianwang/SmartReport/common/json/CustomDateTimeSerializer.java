package com.paipianwang.SmartReport.common.json;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

public class CustomDateTimeSerializer extends JsonSerializer<Date> {

	@Override
	public void serialize(final Date date, final JsonGenerator generator, final SerializerProvider provider)
			throws IOException, JsonProcessingException {
		final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		final String formattedDate = formatter.format(date);
		generator.writeString(formattedDate);
	}

}
