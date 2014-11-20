/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cwi.nucleo.tecnologia.java.analisestacktrace.service;

import br.com.cwi.nucleo.tecnologia.java.analisestacktrace.threads.JstackFileInfoBean;
import com.thoughtworks.xstream.XStream;
import java.io.InputStream;
import javax.enterprise.context.ApplicationScoped;
import org.codehaus.jackson.map.ObjectMapper;

/**
 *
 * @author kuntzer
 */
@ApplicationScoped
public class SerializerService {

    private ObjectMapper mapper = new ObjectMapper();
    private XStream xstream = new XStream();
    
    public String serializeThreadAsString(JstackFileInfoBean threads)throws Exception{
        return mapper.writeValueAsString(threads);
    }
 
    public String serializeThreadsAsXmlString(JstackFileInfoBean threads)throws Exception{
        return xstream.toXML(threads);
    }
    
    public JstackFileInfoBean unmarshallThreadsFromXmlString(InputStream xml)throws Exception{
        return (JstackFileInfoBean) xstream.fromXML(xml);
    }
}
