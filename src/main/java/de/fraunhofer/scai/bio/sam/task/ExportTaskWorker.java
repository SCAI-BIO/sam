package de.fraunhofer.scai.bio.sam.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;

import de.fraunhofer.scai.bio.sam.service.TerminologyExportService;
import de.fraunhofer.scai.bio.sam.service.impl.ProminerExportService;

/**
 * implementing a constant lookup for long running tasks which are queued
 * 
 * cf. https://www.baeldung.com/spring-scheduled-tasks
 * 
 * @author Marc Jacobs
 *
 */
@EnableAsync
public class ExportTaskWorker {

	Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	ProminerExportService pes;
	
	@Async
	@Scheduled(fixedRate=10000, initialDelay = 10000) // every 10 seconds, first time after 10 seconds
	public void work() {
		export(pes);
	}

	private void export(TerminologyExportService exportService) {
		log.debug("tasks scheduled: {}", exportService.getTasks().size());

		exportService.getTasks().forEach(task -> log.debug("{}: {} - {}", exportService.getClass().getSimpleName(), task.getTaskId(), task.getFilename()));
		
		if(!exportService.getFiles().isEmpty()) {
			exportService.getFiles().forEach((id, file) -> 
			log.debug("id {}: {}",id, file)); 			
		}

		// process first task in queue
		if(!exportService.getTasks().isEmpty()) {
			try {
				log.debug("created file of size {}", 
						exportService.export(
								exportService.getTasks().remove(0) 
								)
						);
				
			} catch (Exception e) {
				log.info(e.getLocalizedMessage());
				log.debug(e.getLocalizedMessage(), e);
			}
		}
	}
}