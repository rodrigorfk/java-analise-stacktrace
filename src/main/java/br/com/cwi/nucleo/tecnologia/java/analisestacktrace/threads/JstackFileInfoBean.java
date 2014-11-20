/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cwi.nucleo.tecnologia.java.analisestacktrace.threads;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kuntzer
 */
public class JstackFileInfoBean implements Serializable {
    
    private String fileName = null;
    private String threadDumpInfo = null;
    private String threadDumpDate = null;
    private Integer threadCount = null;
    private List<ThreadBean> lstThreads = new ArrayList<ThreadBean>();

    public String getThreadDumpInfo() {
        return threadDumpInfo;
    }

    public void setThreadDumpInfo(String threadDumpInfo) {
        this.threadDumpInfo = threadDumpInfo;
    }

    public String getThreadDumpDate() {
        return threadDumpDate;
    }

    public void setThreadDumpDate(String threadDumpDate) {
        this.threadDumpDate = threadDumpDate;
    }

    public Integer getThreadCount() {
        return threadCount;
    }

    public void setThreadCount(Integer threadCount) {
        this.threadCount = threadCount;
    }

    public List<ThreadBean> getLstThreads() {
        return lstThreads;
    }

    public void setLstThreads(List<ThreadBean> lstThreads) {
        this.lstThreads = lstThreads;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    
}
