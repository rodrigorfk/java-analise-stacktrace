/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cwi.nucleo.tecnologia.java.analisestacktrace.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.faces.model.SelectItem;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author kuntzer
 */
public class FilterModel implements Serializable {
    
    private String name;
    private String state;
    private String stacktrace;
    
    private List<SelectItem> itensState = new ArrayList<SelectItem>();
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<SelectItem> getItensState() {
        return itensState;
    }

    public void setItensState(List<SelectItem> itensState) {
        this.itensState = itensState;
    }

    public String getStacktrace() {
        return stacktrace;
    }

    public void setStacktrace(String stacktrace) {
        this.stacktrace = stacktrace;
    }
    
    public void buildItensState(Set<String> states){
        this.itensState = new ArrayList<SelectItem>();
        for(String stateName:states){
            if(StringUtils.isNotEmpty(stateName)){
                this.itensState.add(new SelectItem(stateName, stateName));
            }
        }
    }
}
