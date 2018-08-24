package java.util.stream;

import java.util.Optional;

public interface Stream<T> {

	public Stream<T> skip(long n);

	public Stream<T> limit(long maxSize);

	public Optional<T> findFirst();

}