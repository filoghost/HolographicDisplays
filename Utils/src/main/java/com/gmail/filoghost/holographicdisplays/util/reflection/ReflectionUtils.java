/*
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package com.gmail.filoghost.holographicdisplays.util.reflection;

import java.lang.StackWalker.StackFrame;
import java.util.logging.Level;
import java.util.stream.Stream;

import com.gmail.filoghost.holographicdisplays.util.ConsoleLogger;
import com.gmail.filoghost.holographicdisplays.util.Utils;

public class ReflectionUtils {
	
	private static final boolean HAS_JAVA9_STACKWALKER = Utils.classExists("java.lang.StackWalker");
	
	private static final ReflectMethod<Integer> GET_STACKTRACE_DEPTH_METHOD = new ReflectMethod<>(Throwable.class, "getStackTraceDepth");
	private static final ReflectMethod<StackTraceElement> GET_STACKTRACE_ELEMENT_METHOD = new ReflectMethod<>(Throwable.class, "getStackTraceElement", int.class);
	
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
					StackFrame result = StackWalker.getInstance().walk((Stream<StackFrame> stream) -> {
						return stream.skip(index).limit(1).findFirst().orElse(null);
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
				ConsoleLogger.logDebug(Level.WARNING, "Unable to get a stack trace element. This error will only show once and a fallback method will be used.", t);
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
