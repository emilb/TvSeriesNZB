package com.greyzone.util;

import com.greyzone.domain.tv.Quality;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class QualityConverter implements Converter {

	@Override
	public boolean canConvert(Class type) {
		return type.equals(Quality.class);
	}

	@Override
	public void marshal(Object source, HierarchicalStreamWriter writer,
			MarshallingContext context) {
		Quality q = (Quality) source;
		
		writer.setValue(q == null ? "" : q.toString());
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext context) {
		
		return Quality.fromName(reader.getValue());
	}

}
