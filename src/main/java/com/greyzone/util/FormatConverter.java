package com.greyzone.util;

import com.greyzone.domain.tv.Format;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class FormatConverter implements Converter {

	@Override
	public boolean canConvert(Class type) {
		return type.equals(Format.class);
	}

	@Override
	public void marshal(Object source, HierarchicalStreamWriter writer,
			MarshallingContext context) {
		Format f = (Format) source;

		writer.setValue(f == null ? "" : f.toString());
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext context) {

		return Format.fromName(reader.getValue());
	}

}
