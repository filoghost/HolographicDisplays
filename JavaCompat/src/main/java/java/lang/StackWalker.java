package java.lang;

import java.util.function.Function;
import java.util.stream.Stream;

public class StackWalker {
	
	public static interface StackFrame {
		StackTraceElement toStackTraceElement();
	}
	
	public static StackWalker getInstance() {
		return null;
	}
	
	public <T> T walk(Function<? super Stream<StackFrame>, ? extends T> function) {
		return null;
	}

}
