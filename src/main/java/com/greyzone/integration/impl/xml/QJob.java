package com.greyzone.integration.impl.xml;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.greyzone.util.ToStringStyleNewLine;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("job")
public class QJob {
	private String id;
	private String filename;
	private String mb;
	private String mbleft;
	private String msgid;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getMb() {
		return mb;
	}

	public void setMb(String mb) {
		this.mb = mb;
	}

	public String getMbleft() {
		return mbleft;
	}

	public void setMbleft(String mbleft) {
		this.mbleft = mbleft;
	}

	public String getMsgid() {
		return msgid;
	}

	public void setMsgid(String msgid) {
		this.msgid = msgid;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				new ToStringStyleNewLine());
	}
}
