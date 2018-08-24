package com.gmail.filoghost.holographicdisplays.util.reflection;

import java.lang.StackWalker.StackFrame;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.stream.Stream;

import com.gmail.filoghost.holographicdisplays.util.ConsoleLogger;
import com.gmail.filoghost.holographicdisplays.util.Utils;

public class ReflectionUtils {
	
	private static final boolean HAS_JAVA9_STACKWALKER = Utils.classExists("java.lang.StackWalker");
	
	private static final ReflectMethod<Integer> GET_STACKTRACE_DEPTH_METHOD = new ReflectMethod<Integer>(Throwable.class, "getStackTraceDepth");
	private static final ReflectMethod<StackTraceElement> GET_STACKTRACE_ELEMENT_METHOD = new ReflectMethod<StackTraceElement>(Throwable.class, "getStackTraceElement", int.class);
	
	private static boolean stackTraceError;
	
	
	/**
	 * If you only need one stack trace element this is faster than Throwable.getStackTrace()[element],
	 * it doesn't generate the full stack trace (except as fallback).
	 */
	public static StackTraceElement getStackTraceElement(final int index) {
		// Use the faster methods only if there hasn't been any error while executing them previously
		if (!stackTraceError) {
			try {
				if (HAS_JAVA9_STACKWALKER) {
					// Use the Java 9 StackWalker API
					StackFrame result = StackWalker.getInstance().walk(new Function<Stream<StackFrame>, StackFrame>() {
	
						@Override
						public StackFrame apply(Stream<StackFrame> stream) {
							return stream.skip(index).limit(1).findFirst().orElse(null);
						}
						
					});
					return result != null ? result.toStackTraceElement() : null;
					
				} else {
					// Use reflection to avoid generating the full stack trace
					Throwable dummyThrowable = new Throwable();
					int depth = GET_STACKTRACE_DEPTH_METHOD.invoke(dummyThrowable);
		
					if (index < depth) {
						return GET_STACKTRACE_ELEMENT_METHOD.invoke(dummyThrowable, index);
					} else {
						return null;
					}
				}
			} catch (Throwable t) {
				ConsoleLogger.log(Level.WARNING, "Unable to get a stack trace element, please inform the developer. You will only see this error once and a fallback method will be used.", t);
				stackTraceError = true;
			}
		}
		
		// Fallback slower method, generates the full stack trace (it should never be called if everything works as expected)
		StackTraceElement[] fullStackTrace = new Throwable().getStackTrace();
		if (index < fullStackTrace.length) {
			return fullStackTrace[index];
		} else {
			return null;
		}
		
	}
}
