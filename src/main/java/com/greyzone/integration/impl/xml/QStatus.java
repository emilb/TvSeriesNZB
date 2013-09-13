package com.greyzone.integration.impl.xml;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.greyzone.util.ToStringStyleNewLine;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("queue")
public class QStatus {

	private String have_warnings;
	private String timeleft;
	private String mb;
	private String noofslots;
	private String paused;
	private String mbleft;
	private String diskspace1;
	private String diskspace2;
	private String kbpersec;
	@XStreamImplicit
	@XStreamAlias("jobs")
	private List<QJob> jobs;

	public String getHave_warnings() {
		return have_warnings;
	}

	public void setHave_warnings(String have_warnings) {
		this.have_warnings = have_warnings;
	}

	public String getTimeleft() {
		return timeleft;
	}

	public void setTimeleft(String timeleft) {
		this.timeleft = timeleft;
	}

	public String getMb() {
		return mb;
	}

	public void setMb(String mb) {
		this.mb = mb;
	}

	public String getNoofslots() {
		return noofslots;
	}

	public void setNoofslots(String noofslots) {
		this.noofslots = noofslots;
	}

	public String getPaused() {
		return paused;
	}

	public void setPaused(String paused) {
		this.paused = paused;
	}

	public String getMbleft() {
		return mbleft;
	}

	public void setMbleft(String mbleft) {
		this.mbleft = mbleft;
	}

	public String getDiskspace1() {
		return diskspace1;
	}

	public void setDiskspace1(String diskspace1) {
		this.diskspace1 = diskspace1;
	}

	public String getDiskspace2() {
		return diskspace2;
	}

	public void setDiskspace2(String diskspace2) {
		this.diskspace2 = diskspace2;
	}

	public String getKbpersec() {
		return kbpersec;
	}

	public void setKbpersec(String kbpersec) {
		this.kbpersec = kbpersec;
	}

	public List<QJob> getJobs() {
		return jobs;
	}

	public void setJobs(List<QJob> jobs) {
		this.jobs = jobs;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				new ToStringStyleNewLine());
	}
}
