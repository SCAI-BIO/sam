package de.fraunhofer.scai.bio.sam.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fraunhofer.scai.bio.sam.domain.task.ExportTask;
import de.fraunhofer.scai.bio.sam.service.exceptions.NotFoundException;

/**
 * TerminologyExportService
 * <p>
 * TODO: Implementing a "Long Running Operation with Polling" pattern
 * cf. http://restalk-patterns.org/long-running-operation-polling.html
 *
 * @author Johannes Darms <johannes.darms@scai.fraunhofer.de>
 * @author Marc Jacobs
 **/
public abstract class TerminologyExportService {
	
	Logger log = LoggerFactory.getLogger(getClass());

	List<ExportTask> tasks = new ArrayList<ExportTask>();

	ConcurrentMap<String, File> files = new ConcurrentHashMap<String, File>(); 
	
	public abstract CompletableFuture<Long> export(ExportTask task) throws IOException, NotFoundException;
	
	public String schedule(String terminologyId, String version, String filename) throws IllegalArgumentException {
		return schedule(terminologyId, version, filename, false);
	}
	
	public String schedule(String terminologyId, String version, String filename, boolean split) {

		log.debug(" >>> schedule {}", filename);
		
		if(terminologyId==null || terminologyId.trim().isEmpty()){
			throw new IllegalArgumentException("passed terminologyId cannot be null nor empty!");
		}
		
		if(filename==null || filename.trim().isEmpty()){
			throw new IllegalArgumentException("passed filename cannot be null nor empty!");
		}

		if(version==null || version.trim().isEmpty()){
			throw new IllegalArgumentException("passed versionTag cannot be null nor empty!");
		}
		
		String taskId = getClass().getSimpleName()+ "_" +UUID.randomUUID().toString();
		ExportTask task = new ExportTask(taskId, terminologyId, version, filename);
		task.setSplitted(split);
		
		tasks.add(task);
		return taskId;
	}

	/**
	 * getter for ExportTaks
	 * @return List of ExportTask
	 */
	public List<ExportTask> getTasks() {
		return tasks;
	}
	
	/**
	 * @param id of the task, returns file if processed else throws an Exception
	 * @return <code>File</code>
	 * @throws NotFoundException
	 */
	public File fetch(String id) throws NotFoundException {
		log.info("fetch {}", id);
		
		if(files.containsKey(id)) {
			return files.remove(id);
		} else {	
			throw new NotFoundException("not yet processed");
		}
	}
	
	/**
	 * getter for Map of taskId to file
	 * @return <code>Map</code>
	 */
	public Map<String, File> getFiles() {
		return files;
	}
	

}
