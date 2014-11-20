/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cwi.nucleo.tecnologia.java.analisestacktrace.service;

import br.com.cwi.nucleo.tecnologia.java.analisestacktrace.threads.LockedStackTraceElement;
import br.com.cwi.nucleo.tecnologia.java.analisestacktrace.threads.ParkingToWaitStackTraceElement;
import br.com.cwi.nucleo.tecnologia.java.analisestacktrace.threads.ThreadBean;
import br.com.cwi.nucleo.tecnologia.java.analisestacktrace.threads.WaitOnStackTraceElement;
import br.com.cwi.nucleo.tecnologia.java.analisestacktrace.threads.WaitToLockStackTraceElement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.enterprise.context.ApplicationScoped;

/**
 *
 * @author kuntzer
 */
@ApplicationScoped
public class ThreadParseService {
    
    private static final String descricaoThreadPattern  = "^\"(.+?)\" (\\S+) (\\S+) (\\S+) (\\S+) (.+?) \\[(\\S+)\\]";
	private static final String descricaoThreadPattern2 = "^\"(.+?)\" (\\S+) (\\S+) (\\S+) (\\S+) (.+?)\\[(\\S+)\\]";
	private static final String descricaoThreadPattern3 = "^\"(.+?)\" (\\S+) (\\S+) (\\S+) (.+?) \\[(\\S+)\\]";
	private static final String descricaoThreadPattern4 = "^\"(.+?)\" (\\S+) (\\S+) (\\S+) (.+?)\\[(\\S+)\\]";
    private static final String descricaoThreadPattern5 = "^\"(.+?)\" (\\S+) (\\S+) (\\S+) (.+?)";
    private static final String descricaoThreadPattern6 = "^\"(.+?)\" prio=(\\S+) tid=(\\S+) nid=(\\S+) (.+?) \\[(\\S+)\\]";
	private static final String statePattern1 = "^(\\S+): (.+?)";
	private static final String stackElementPattern  = "^at (\\S+)\\((\\S+):(\\S+)\\)";
	private static final String stackElementPattern2 = "^at (\\S+)\\((.+?)\\)";
	private static final String stackElementPattern3 = "^- locked <(\\S+)> \\(a (\\S+)\\)";
	private static final String stackElementPattern4 = "^- waiting to lock <(\\S+)> \\(a (\\S+)\\)";
	private static final String stackElementPattern5 = "^- waiting on <(\\S+)> \\(a (\\S+)\\)";
    private static final String stackElementPattern6 = "^- parking to wait for\\s+<(\\S+)> \\(a (\\S+)\\)";
	private static final Pattern p  = Pattern.compile(descricaoThreadPattern);
	private static final Pattern p2 = Pattern.compile(descricaoThreadPattern2);
	private static final Pattern p3 = Pattern.compile(descricaoThreadPattern3);
	private static final Pattern p4 = Pattern.compile(descricaoThreadPattern4);
	private static final Pattern p5 = Pattern.compile(statePattern1);
	private static final Pattern p6  = Pattern.compile(stackElementPattern);
	private static final Pattern p7 = Pattern.compile(stackElementPattern2);
	private static final Pattern p8 = Pattern.compile(stackElementPattern3);
	private static final Pattern p9 = Pattern.compile(stackElementPattern4);
	private static final Pattern p10 = Pattern.compile(stackElementPattern5);
    private static final Pattern p11 = Pattern.compile(descricaoThreadPattern5);
    private static final Pattern p12 = Pattern.compile(stackElementPattern6);
    private static final Pattern p13 = Pattern.compile(descricaoThreadPattern6);
    
    public ThreadBean parseThreadDescription(String description){
        ThreadBean thread = new ThreadBean(description);
        
        Matcher matcher = p13.matcher(description);
        if(matcher.matches()){
            thread.setName(matcher.group(1));
            thread.setPriority("prio="+matcher.group(2));
            thread.setTid("tid="+matcher.group(3));
            thread.setNid("nid="+matcher.group(4));
            thread.setInfo(matcher.group(5));
            thread.setPointer(matcher.group(6));
        }else{
            matcher = p.matcher(description);
            if (matcher.matches()) {
                thread.setName(matcher.group(1));
                thread.setGroup(matcher.group(2));
                thread.setPriority(matcher.group(3));
                thread.setTid(matcher.group(4));
                thread.setNid(matcher.group(5));
                thread.setInfo(matcher.group(6));
                thread.setPointer(matcher.group(7));
            }else{
                matcher = p2.matcher(description);
                if (matcher.matches()) {
                    thread.setName(matcher.group(1));
                    thread.setGroup(matcher.group(2));
                    thread.setPriority(matcher.group(3));
                    thread.setTid(matcher.group(4));
                    thread.setNid(matcher.group(5));
                    thread.setInfo(matcher.group(6));
                    thread.setPointer(matcher.group(7));
                }else{
                    matcher = p3.matcher(description);
                    if (matcher.matches()) {
                        thread.setName(matcher.group(1));
                        thread.setPriority(matcher.group(2));
                        thread.setTid(matcher.group(3));
                        thread.setNid(matcher.group(4));
                        thread.setInfo(matcher.group(5));
                        thread.setPointer(matcher.group(6));
                    }else{
                        matcher = p4.matcher(description);
                        if (matcher.matches()) {
                            thread.setName(matcher.group(1));
                            thread.setPriority(matcher.group(2));
                            thread.setTid(matcher.group(3));
                            thread.setNid(matcher.group(4));
                            thread.setInfo(matcher.group(5));
                            thread.setPointer(matcher.group(6));
                        }else{
                            matcher = p11.matcher(description);
                            if(matcher.matches()){
                                thread.setName(matcher.group(1));
                                thread.setPriority(matcher.group(2));
                                thread.setTid(matcher.group(3));
                                thread.setNid(matcher.group(4));
                                thread.setInfo(matcher.group(5));
                            }else{
                                return null;
                            }
                        }
                    }
                }
            }
        }
        
        return thread;
    }
    
    public Object parseThreadDetailLine(String description){
        Matcher matcher = p6.matcher(description);
		if (matcher.matches()) {
			String classe = matcher.group(1);
			String method = classe.substring(classe.lastIndexOf(".")+1);
			classe = classe.substring(0,classe.lastIndexOf("."));
			return new StackTraceElement(classe, method, matcher.group(2), Integer.parseInt(matcher.group(3)));
		}else{
			matcher = p7.matcher(description);
			if (matcher.matches()) {
				String classe = matcher.group(1);
				String method = classe.substring(classe.lastIndexOf(".")+1);
				classe = classe.substring(0,classe.lastIndexOf("."));
				return new StackTraceElement(classe, method, matcher.group(2), -1);
			}else{
				matcher = p8.matcher(description);
				if (matcher.matches()) {
					return new LockedStackTraceElement(matcher.group(1), matcher.group(2));
				}else{
					matcher = p9.matcher(description);
					if (matcher.matches()) {
						return new WaitToLockStackTraceElement(matcher.group(1), matcher.group(2));
					}else{
						matcher = p10.matcher(description);
						if (matcher.matches()) {
							return new WaitOnStackTraceElement(matcher.group(1), matcher.group(2));
						}else{
                            matcher = p12.matcher(description);
                            if (matcher.matches()) {
                                return new ParkingToWaitStackTraceElement(matcher.group(1), matcher.group(2));
                            }
                        }
					}
				}
			}
		}
        return null;
    }
    
    public String parseThreadState(String state){
        Matcher matcher = p5.matcher(state);
		if (matcher.matches()) {
			return matcher.group(2);
		}else{
			return state;
		}
    }
 
}
