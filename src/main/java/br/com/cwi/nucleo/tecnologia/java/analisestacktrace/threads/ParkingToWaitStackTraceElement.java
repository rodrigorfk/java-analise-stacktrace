/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.cwi.nucleo.tecnologia.java.analisestacktrace.threads;

import java.io.Serializable;

/**
 *
 * @author rodrigokuntzer
 */
public class ParkingToWaitStackTraceElement implements Serializable {

	private String pointer;
	private String object;

	public ParkingToWaitStackTraceElement(String pointer, String object) {
		this.pointer = pointer;
		this.object = object;
	}

	public String getObject() {
		return object;
	}

	public void setObject(String object) {
		this.object = object;
	}

	public String getPointer() {
		return pointer;
	}

	public void setPointer(String pointer) {
		this.pointer = pointer;
	}

	@Override
	public String toString() {
		return "- parking to wait for <" + pointer + "> (a " + object + ")";
	}
}
