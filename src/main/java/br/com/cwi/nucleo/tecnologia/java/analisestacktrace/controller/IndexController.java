/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cwi.nucleo.tecnologia.java.analisestacktrace.controller;

import br.com.cwi.nucleo.tecnologia.java.analisestacktrace.model.FilterModel;
import br.com.cwi.nucleo.tecnologia.java.analisestacktrace.model.ThreadBeanDataModel;
import br.com.cwi.nucleo.tecnologia.java.analisestacktrace.service.SerializerService;
import br.com.cwi.nucleo.tecnologia.java.analisestacktrace.service.ThreadParseService;
import br.com.cwi.nucleo.tecnologia.java.analisestacktrace.threads.JstackFileInfoBean;
import br.com.cwi.nucleo.tecnologia.java.analisestacktrace.threads.ThreadBean;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import org.apache.commons.lang.StringUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author kuntzer
 */
@ManagedBean
@ViewScoped
public class IndexController implements Serializable {
    
    private static final Logger _logger = LoggerFactory.getLogger(IndexController.class);
    
    @Inject
    private ThreadParseService threadParseService;
    
    @Inject
    private SerializerService jsonSerializerService;
    
    private JstackFileInfoBean jstackFileInfo;
    private ThreadBeanDataModel threads = null;
    private ThreadBean selected = null;
    private List<String> selectedCategories;
    private Set<String> usedCategories = new HashSet<String>();
    
    private FilterModel filterModel = new FilterModel();
    
    private StreamedContent fileContent;
    private StreamedContent fileContentDump;
    
    public void handleFileUpload(FileUploadEvent event) throws IOException {  
        UploadedFile fileUpload = event.getFile();
        InputStream stream = fileUpload.getInputstream();
        
        BufferedReader reader = null;
        try{
            reader = new BufferedReader(new InputStreamReader(stream));
            
            this.jstackFileInfo = new JstackFileInfoBean();
            this.jstackFileInfo.setFileName(fileUpload.getFileName());
			ThreadBean thread = null;
            
            int line = 0;
            while(reader.ready()){
                String logEntryLine = reader.readLine();
                
                if("".equals(logEntryLine)) {
                    continue;
                }
                
                if(!logEntryLine.startsWith("\t") && !logEntryLine.startsWith(" ")){
					thread = threadParseService.parseThreadDescription(logEntryLine);
                    if(thread == null){
                       if(line == 0){
                           this.jstackFileInfo.setThreadDumpDate(logEntryLine);
                       }else if(line == 1){
                           this.jstackFileInfo.setThreadDumpInfo(logEntryLine);
                       }else{
                           _logger.warn("Thread description not parsed :: {}",logEntryLine);
                       }
                    }else{
                        thread.setId(Integer.valueOf(line).longValue());
    					this.jstackFileInfo.getLstThreads().add(thread);
                    }
				}else if(thread != null){
                    if(logEntryLine.trim().startsWith("java.lang.Thread.State")){
                        String state = threadParseService.parseThreadState(logEntryLine.trim());
                        thread.setState(state);
                    }else if (logEntryLine.startsWith("\t")) {
                        logEntryLine = logEntryLine.replaceAll("\\t","");
                        Object stackTraceElement = threadParseService.parseThreadDetailLine(logEntryLine);
                        if(stackTraceElement != null){
                            thread.getStackElements().add(stackTraceElement);
                        }else{
                            _logger.warn("StackTraceElement description not parsed :: {}",logEntryLine);
                        }
                    }
                }
                line++;
            }
            this.threads = new ThreadBeanDataModel(this.jstackFileInfo.getLstThreads());
            this.jstackFileInfo.setThreadCount(this.jstackFileInfo.getLstThreads().size());
            this.updateListaStates();
            
        }finally{
            if(reader != null) {
                reader.close();
            }
        }
        
        
        RequestContext rc = RequestContext.getCurrentInstance();
        rc.execute("onUploadCompleted(1);");
        rc.update("row-table");
        rc.update("formInfo");
    } 
    
    public void handleXmlFileUpload(FileUploadEvent event) throws Exception {  
        UploadedFile fileUpload = event.getFile();
        InputStream stream = fileUpload.getInputstream();
        
        try{
            
            this.jstackFileInfo = jsonSerializerService.unmarshallThreadsFromXmlString(stream);
            this.threads = new ThreadBeanDataModel(this.jstackFileInfo.getLstThreads());
            
        }finally{
            stream.close();
        }
        this.updateListaStates();
        
        RequestContext rc = RequestContext.getCurrentInstance();
        rc.execute("onUploadCompleted(2);");
        rc.update("row-table");
        rc.update("formInfo");
    } 
    
    public void onRowSelect(SelectEvent event) { 
        this.selected = (ThreadBean) event.getObject();
        this.selectedCategories = new ArrayList<String>();
        this.selectedCategories.addAll(this.selected.getCategories());
    }
    
    public List<String> completeCategories(String query) {
        List<String> results = new ArrayList<String>();

        boolean equals = false;
        String queryAux = query.toLowerCase();
        for (String item:this.usedCategories) {
            String aux = item.toLowerCase();
            if(aux.startsWith(queryAux)){
                results.add(item);
                if(aux.equals(queryAux)){
                    equals = true;
                }
            }
        }
        if(!equals){
            results.add(query);
        }

        return results;
    }
    
    public void handleCategorySelect(SelectEvent event){
        String category = (String) event.getObject();
        this.usedCategories.add(category);
        this.selectedCategories.add(category);
    }
    
    public void saveThread(){
        this.selected.getCategories().clear();
        if(this.selectedCategories != null){
            this.selected.getCategories().addAll(this.selectedCategories);
        }
        
        RequestContext rc = RequestContext.getCurrentInstance();
        rc.execute("modalDetail.hide();");
        rc.update("row-table");
    }
    
    private void generateJsonFile()throws Exception{
        String json = jsonSerializerService.serializeThreadAsString(this.jstackFileInfo);
        ByteArrayInputStream bais = new ByteArrayInputStream(json.getBytes());
        this.fileContent = new DefaultStreamedContent(bais, "application/json", this.jstackFileInfo.getFileName()+".json");
    }
    
    private void generateXMLFile() throws Exception {
        String xml = jsonSerializerService.serializeThreadsAsXmlString(this.jstackFileInfo);
        ByteArrayInputStream bais = new ByteArrayInputStream(xml.getBytes());
        this.fileContent = new DefaultStreamedContent(bais, "text/xml", this.jstackFileInfo.getFileName() + ".xml");
    }
    
    private void generateDumpFile() throws Exception {
        List<ThreadBean> lista = (List<ThreadBean>) this.threads.getWrappedData();
        
        StringWriter writer = null;
        BufferedWriter bufferWriter = null;
        try{
            writer = new StringWriter();
            bufferWriter = new BufferedWriter(writer);
            for(ThreadBean thread:lista){
                bufferWriter.append(thread.print());
                bufferWriter.newLine();
            }
            ByteArrayInputStream bais = new ByteArrayInputStream(writer.getBuffer().toString().getBytes());
            this.fileContentDump = new DefaultStreamedContent(bais, "text/plan", this.jstackFileInfo.getFileName() + ".txt");
            
        }finally{
            bufferWriter.close();
            writer.close();
        }
    }

    public void filtrar() {
        String name = StringUtils.defaultString(this.filterModel.getName()).toLowerCase();
        boolean hasName = StringUtils.isNotEmpty(name), hasState = StringUtils.isNotEmpty(this.filterModel.getState());
        boolean hasStacktrace = StringUtils.isNotEmpty(this.filterModel.getStacktrace());
        List<ThreadBean> lista = new ArrayList<ThreadBean>();
        for(ThreadBean bean:this.jstackFileInfo.getLstThreads()){
            boolean okName = !hasName || bean.getName().toLowerCase().startsWith(name);
            boolean okState = !hasState || StringUtils.defaultString(bean.getState()).equals(this.filterModel.getState());
            boolean okStacktrace = !hasStacktrace || bean.print().indexOf(this.filterModel.getStacktrace()) != -1;
            if(okName && okState && okStacktrace){
                lista.add(bean);
            }
        }
        this.threads = new ThreadBeanDataModel(lista);
        this.jstackFileInfo.setThreadCount(lista.size());
        this.updateListaStates();
        
        RequestContext rc = RequestContext.getCurrentInstance();
        rc.execute("onUploadCompleted(3);");
        rc.update("formInfo");
    }
    
    private void updateListaStates(){
        List<ThreadBean> lista = (List<ThreadBean>) this.threads.getWrappedData();
        
        Set<String> states = new HashSet<String>();
        for(ThreadBean thread:lista){
            states.add(thread.getState());
        }
        this.filterModel.buildItensState(states);
    }

    public ThreadBeanDataModel getThreads() {
        return threads;
    }

    public void setThreads(ThreadBeanDataModel threads) {
        this.threads = threads;
    }

    public void setThreadParseService(ThreadParseService threadParseService) {
        this.threadParseService = threadParseService;
    }

    public JstackFileInfoBean getJstackFileInfo() {
        return jstackFileInfo;
    }

    public void setJstackFileInfo(JstackFileInfoBean jstackFileInfo) {
        this.jstackFileInfo = jstackFileInfo;
    }

    public ThreadBean getSelected() {
        return selected;
    }

    public void setSelected(ThreadBean selected) {
        this.selected = selected;
    }

    public List<String> getSelectedCategories() {
        return selectedCategories;
    }

    public void setSelectedCategories(List<String> selectedCategories) {
        this.selectedCategories = selectedCategories;
    }

    public StreamedContent getFileContent()throws Exception {
        this.generateXMLFile();
        return fileContent;
    }

    public StreamedContent getFileContentDump()throws Exception {
        this.generateDumpFile();
        return fileContentDump;
    }

    public void setJsonSerializerService(SerializerService jsonSerializerService) {
        this.jsonSerializerService = jsonSerializerService;
    }

    public FilterModel getFilterModel() {
        return filterModel;
    }

    public void setFilterModel(FilterModel filterModel) {
        this.filterModel = filterModel;
    }
    
    
}
