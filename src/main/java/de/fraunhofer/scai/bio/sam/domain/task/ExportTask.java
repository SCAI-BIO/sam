package de.fraunhofer.scai.bio.sam.domain.task;

public class ExportTask {

	private String taskId;
	private String terminologyId;
	private String versionTag;
	private String filename;
	private boolean splitted;
	
	public ExportTask(String taskId, String terminologyId, String versionTag, String filename) {
		this(taskId, terminologyId, versionTag, filename, false);
	}
	
	public ExportTask(String taskId, String terminologyId, String versionTag, String filename, boolean splitted) {
		setTerminologyId(terminologyId);
		setVersionTag(versionTag);
		setFilename(filename);
		setTaskId(taskId);
		setSplitted(splitted);
	}
	
	public String getTerminologyId() {
		return terminologyId;
	}
	public void setTerminologyId(String terminologyId) {
		this.terminologyId = terminologyId;
	}
	public String getVersionTag() {
		return versionTag;
	}
	public void setVersionTag(String versionTag) {
		this.versionTag = versionTag;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public boolean isSplitted() {
		return splitted;
	}

	public void setSplitted(boolean splitted) {
		this.splitted = splitted;
	}
}
