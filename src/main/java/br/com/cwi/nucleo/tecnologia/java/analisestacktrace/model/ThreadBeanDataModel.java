/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cwi.nucleo.tecnologia.java.analisestacktrace.model;

import br.com.cwi.nucleo.tecnologia.java.analisestacktrace.threads.ThreadBean;
import java.util.List;
import javax.faces.model.ListDataModel;
import org.primefaces.model.SelectableDataModel;

/**
 *
 * @author kuntzer
 */
public class ThreadBeanDataModel extends ListDataModel<ThreadBean> implements SelectableDataModel<ThreadBean> {

    public ThreadBeanDataModel() {
    }

    public ThreadBeanDataModel(List<ThreadBean> list) {
        super(list);
    }

    @Override
    public Object getRowKey(ThreadBean object) {
        return object.getId();
    }

    @Override
    public ThreadBean getRowData(String rowKey) {
        List<ThreadBean> list = (List<ThreadBean>) this.getWrappedData();
        Long id = Long.valueOf(rowKey);
        for(ThreadBean thread:list){
            if(thread.getId().equals(id)){
                return thread;
            }
        }
        return null;
    }
    
}
