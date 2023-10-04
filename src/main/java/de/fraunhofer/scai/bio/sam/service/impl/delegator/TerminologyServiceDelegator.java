package de.fraunhofer.scai.bio.sam.service.impl.delegator;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ws.rs.NotFoundException;

import org.apache.commons.lang3.Range;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import de.fraunhofer.scai.bio.sam.domain.DAO.Terminology;
import de.fraunhofer.scai.bio.sam.domain.DAO.VersionTag;
import de.fraunhofer.scai.bio.sam.service.CurieService;
import de.fraunhofer.scai.bio.sam.service.ServiceRegistry;
import de.fraunhofer.scai.bio.sam.service.TerminologyService;

/**
 * TerminologyServiceDelegator
 * <p>
 * collects information from all registered terminology services therefore is annotated as 'primary'
 *
 * @author Johannes Darms <johannes.darms@scai.fraunhofer.de>
 * @author Marc Jacobs
 **/
@Primary
@Order(1)
@Service("TerminologyService")
@DependsOn({"itsProfile", "olsProfile", "loincProfile", "jpmProfile"})
@ComponentScan("de.fraunhofer.scai.bio.sam.config")
public class TerminologyServiceDelegator implements TerminologyService {

    Logger log = LoggerFactory.getLogger(getClass());

    @Autowired 
    ServiceRegistry serviceRegistry;
    
    @Autowired 
    CurieService curieService;

    @Override
    public void save(Terminology t) {
        if(t != null && serviceRegistry.getTerminologyServiceList() != null && serviceRegistry.getTerminologyServiceList().size() > 0) {
            serviceRegistry.getTerminologyServiceList().get(0).save(t);
        }
    }

    @Override
    public Terminology getTerminology(String id) {
        if(serviceRegistry.getTerminologyServiceList() != null) {
            TerminologyService service = serviceRegistry.getTerminologyServiceList()
                    .parallelStream()
                    .filter(s -> s.existsByID(id))
                    .findFirst()
                    .orElseThrow(() -> new NotFoundException("id"));
            return service.getTerminology(id);
        }

        return null;
    }

    @Override
    public Page<Terminology> getAllTerminologies(Pageable request) {
        Page<Terminology> terminologies = Page.empty();

        if(serviceRegistry.getTerminologyServiceList() != null && serviceRegistry.getTerminologyServiceList().size() > 0) {
            log.debug(" >> delegator {}", serviceRegistry.getTerminologyServiceList().size());
            // filter for services which at least contain 1 terminology
            List<TerminologyService> nonEmptyServicesList =
                    serviceRegistry.getTerminologyServiceList().stream().filter(s -> s.getCountOfTerminologies()>0).collect(Collectors.toList());

            // get local offsets..
            List<Long> sum1 =
                    nonEmptyServicesList.stream().map(s -> s.getCountOfTerminologies()).collect(Collectors.toList());
            // [0,n1][n1 +1,(n1 +1) + n2],.....

            log.debug(" >>> sum {}, page {} ", sum1, request);

            int index=-1;
            int yetToFill = request.getPageSize();
            List<Terminology> terminologies1 = new ArrayList<>();

            // requested more than in total available
            if(request.getPageNumber() == 0) {
                for (TerminologyService ts : nonEmptyServicesList) {
                    ++index;
                    Page<Terminology> a = ts.getAllTerminologies(PageRequest.of(0, Math.min(sum1.get(index).intValue(), yetToFill)));                    
                    terminologies1.addAll(a.getContent());    
                    yetToFill = request.getPageSize()-terminologies1.size();
                    if(yetToFill <= 0) break;
                }

                log.debug(" >>> got {}", terminologies1.size());
                return terminologies = new PageImpl<Terminology>(terminologies1,request,terminologies1.size());

                // TODO other page numbers
            } else {
                // global offsets
                LinkedHashMap<Range<Long>,TerminologyService> ranges = new LinkedHashMap<>();

                Range<Long> prevRange = Range.between(0l,sum1.get(0)-1);
                ranges.put(prevRange, nonEmptyServicesList.get(0));

                for(int i=1;i<sum1.size();i++){
                    Range<Long> newRange =Range.between(
                            prevRange.getMaximum()+1,
                            prevRange.getMaximum()+sum1.get(i));
                    ranges.put(newRange,nonEmptyServicesList.get(i));
                    prevRange=newRange;

                }
                long begin =request.getOffset();
                long end = begin+request.getPageSize();
                @SuppressWarnings("unchecked")
                Map.Entry<Range<Long>,TerminologyService>[] array =new Map.Entry[sum1.size()];
                ranges.entrySet().toArray(array);

                for(Map.Entry<Range<Long>,TerminologyService> range : array) {
                    ++index;
                    log.debug(" >>> index: {}", index); // covers all
                    if(range.getKey().contains(begin) && range.getKey().contains(end)) {
                        log.debug(" >>> case 1 [{},{}]", begin, end);
                        long localOffsetStart = begin - range.getKey().getMinimum() ;
                        //long localOffsetEnd = end - range.getKey().getMinimum() ;
                        int page= (int) (localOffsetStart/request.getPageSize());
                        Page<Terminology> a =range.getValue().getAllTerminologies(PageRequest.of(page,request.getPageSize()));                    
                        terminologies1.addAll(a.getContent());
                        break;

                    } else if(range.getKey().contains(begin)) {
                        // only begin included, means end must be located in next partition.
                        // if there is no further partition, we can only serve what's left...
                        if(index+1 > ranges.size()-1) {
                            log.debug(" >>> case 2a {}>{}", index+1, ranges.size()-1);
                            long localOffsetStart = begin - range.getKey().getMinimum() ;
                            // long localOffsetEnd =end -range.getKey().getMaximum() ;
                            int page= (int) (localOffsetStart/request.getPageSize());
                            Page<Terminology> a = range.getValue().getAllTerminologies(PageRequest.of(page,request.getPageSize()));
                            terminologies1.addAll(a.getContent());

                        } else {
                            log.debug(" >>> case 2b");
                            long localOffsetStart1= begin -range.getKey().getMinimum() ;
                            // long localOffsetEnd1 =range.getKey().getMaximum() ;
                            int page= (int) (localOffsetStart1/request.getPageSize());
                            if(range.getValue() != null
                                    && range.getValue().getAllTerminologies(PageRequest.of(page,
                                            request.getPageSize())) != null) {
                                terminologies1.addAll(range.getValue().getAllTerminologies(PageRequest.of(page,
                                        request.getPageSize())).getContent());
                            }

                            long localOffsetStart2 = array[index+1].getKey().getMinimum() ;
                            //long localOffsetEnd2 =end - array[index+1].getKey().getMaximum() ;

                            page= (int) (localOffsetStart2/request.getPageSize());

                            Page<Terminology> tpage = array[index+1].getValue().getAllTerminologies(PageRequest.of(page,
                                    request.getPageSize()));

                            if(tpage != null) {
                                terminologies1.addAll(tpage.getContent());
                            }
                        }


                    }
                }

                return terminologies = new PageImpl<Terminology>(terminologies1,request,getCountOfTerminologies());
            }
        }

        return terminologies;
    }

    @Override
    public long getCountOfTerminologies() {
        return serviceRegistry.getTerminologyServiceList()
                .parallelStream()
                .mapToLong(s -> s.getCountOfTerminologies())
                .sum();
    }

    @Override
    public boolean existsByID(String id) {
        TerminologyService service = serviceRegistry.getTerminologyServiceList()
                .parallelStream()
                .filter(s -> s.existsByID(id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(""));

        if(service != null) {
            log.debug("{}", service.getTerminology(id));
        }

        return true;
    }

    @Override
    public VersionTag getLastVersionTag(String terminologyId) {
        TerminologyService service = serviceRegistry.getTerminologyServiceList()
                .parallelStream()
                .filter(s -> s.existsByID(terminologyId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(""));
        return service.getLastVersionTag(terminologyId);
    }
    
    /**
     * tries to identify the correct terminology id (maps from JPM annotations to ontology)
     * @param query - some terminology name 
     * @param lang - language version
     * @return mapped terminology id
     */
    public String searchTerminology(String query, String lang) {
        int score = -1;
        String terminologyId = null;

        log.debug(" >>> searchTerminology: {}, {}", query, lang);

        log.trace("language: {}", lang);

        // search for language specific ontology, having suffix 'lang_xxx'
        if(!lang.isEmpty() && !query.contains("_lang_")) {
            query = query + "_lang_" + lang;
        }

        log.trace(" >>> terminology query {}", query);

        if(curieService.getTerminologyIDs().containsKey(query)) {
            return curieService.getTerminologyIDs().get(query);
        }

        Page<Terminology> terminologies = getAllTerminologies(PageRequest.of(0, 500));

        //  TODO remove workaround  
        if(query.toLowerCase().contains("homo_sapiens")) {
            terminologyId = "HGNC";

        } else {

            log.trace(" >>> #terminologies: {}", terminologies.getContent().size());
            if(!terminologies.getContent().isEmpty()) {
                log.trace(" >>> 1st terminology: {}", terminologies.getContent().get(0).getOlsID());
            }

            for (Terminology terminology : terminologies.getContent()) {

                String testMe = terminology.getShortName().toLowerCase();
                log.trace(" >>> testing short name {} - {}", testMe, query.toLowerCase());

                if(query.toLowerCase().contains(testMe)
                        && testMe.length() > score) {   // chose longest match
                    terminologyId = terminology.getOlsID();
                    score = testMe.length();                    
                    log.trace(" >>> successs !");
                }

                testMe = terminology.getOlsID().toLowerCase();
                log.trace(" >>> testing olsid {} - {}", testMe, query.toLowerCase());

                if(query.toLowerCase().contains(testMe)
                        && testMe.length() > score) {   // chose longest match
                    terminologyId = terminology.getOlsID();
                    score = testMe.length();
                    log.trace(" >>> successs !");
                }

                String iri = terminology.getIri();
                if(iri != null && ! iri.isEmpty()) {
                    iri = iri.substring(0, iri.length()-1);

                    testMe = iri.substring(iri.lastIndexOf("/")+1, iri.length()).toLowerCase();
                    log.trace(" >>> testing iri {} - {}", testMe, query.toLowerCase());
                    if(query.toLowerCase().contains(testMe)
                            && testMe.length() > score) {   // chose longest match
                        terminologyId = terminology.getOlsID();
                        score = testMe.length();
                        log.trace(" >>> successs !");
                    }
                }
            }
        }

        log.trace(" >>> terminologyId: {} ", terminologyId);
        if(terminologyId == null) { terminologyId = query; }

        log.debug(" >> Passed '{}' - mapped to '{}'", query, terminologyId);
        curieService.getTerminologyIDs().put(query, terminologyId);

        return terminologyId;
    }


}
