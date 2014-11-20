/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.cwi.nucleo.tecnologia.java.analisestacktrace.threads;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author rodrigokuntzer
 */
public class ThreadBean implements Serializable {

    private Long id;
	private String descricao;
	private String state;

	private String name;
	private String group;
	private String priority;
	private String tid;
	private String nid;
	private String info;
	private String pointer;
	private List<Object> stackElements = new ArrayList<Object>();
    private List<String> categories = new ArrayList<String>();

	public ThreadBean(String descricao) {
		this.descricao = descricao;
	}

	public ThreadBean() {
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNid() {
		return nid;
	}

	public void setNid(String nid) {
		this.nid = nid;
	}

	public String getPointer() {
		return pointer;
	}

	public void setPointer(String pointer) {
		this.pointer = pointer;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public List<Object> getStackElements() {
		return stackElements;
	}

	public void setStackElements(List<Object> stackElements) {
		this.stackElements = stackElements;
	}

	public boolean isValid(){
		return descricao != null && name != null && !"".equals(name);
	}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

	@Override
	public String toString() {
		return "ThreadBean{" + "name=" + name + ", state=" + state + '}';
	}

	public String print(){
		StringBuilder builder = new StringBuilder();
		builder.append(this.descricao).append("\n")
			   .append("  java.lang.Thread.State: ").append(this.state).append("\n");
		for(Object stackElement:this.stackElements){
			builder.append("\t").append(stackElement.toString()).append("\n");
		}
		builder.append("\n\n");

		return builder.toString();
	}
	
    public String getTxtCategories(){
        return StringUtils.join(this.categories, ",");
    }
}
