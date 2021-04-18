package info.kgeorgiy.ja.asmirko.concurrent;

import info.kgeorgiy.java.advanced.concurrent.ScalarIP;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author Anton Asmirko
 */
public class IterativeParallelism implements ScalarIP {

    private <T> Stream<List<T>> partition(List<T> source, int length) {
        int chunkLen = source.size() / length;
        if (chunkLen == 0) {
            return source.stream().map(List::of);
        }
        return IntStream.range(0, length)
                .mapToObj(n -> source.subList(n * chunkLen, n == length - 1 ? source.size() : (n + 1) * chunkLen))
                .filter(l -> !l.isEmpty());
    }

    private <T, U> U common(int threads, List<? extends T> values, Function<List<? extends T>, U> op,
                            Function<List<U>, U> resOp) throws InterruptedException {
        if (values.isEmpty()) {
            throw new NoSuchElementException();
        }
        List<MyPair<ChunkRunnable<T, U>, Thread>> workers = partition(values, threads)
                .map(chunk -> new ChunkRunnable<>(op, chunk))
                .map(chunkRunnable -> new MyPair<>(chunkRunnable, new Thread(chunkRunnable)))
                .peek(p -> p.s.start())
                .collect(Collectors.toList());
        List<U> results = new ArrayList<>();
        for (final MyPair<ChunkRunnable<T, U>, Thread> worker : workers) {
            worker.s.join();
            results.add(worker.f.result);
        }
        return resOp.apply(results);
    }

    @Override
    public <T> T maximum(int threads, List<? extends T> values, Comparator<? super T> comparator) throws InterruptedException {
        return common(threads, values, (list) -> list.stream().max(comparator).orElseThrow(), (list) -> list.stream().max(comparator).orElseThrow());

    }

    @Override
    public <T> T minimum(int threads, List<? extends T> values, Comparator<? super T> comparator) throws InterruptedException {
        return maximum(threads, values, comparator.reversed());
    }

    @Override
    public <T> boolean all(int threads, List<? extends T> values, Predicate<? super T> predicate) throws InterruptedException {
        return !any(threads, values, predicate.negate());
    }

    @Override
    public <T> boolean any(int threads, List<? extends T> values, Predicate<? super T> predicate) throws InterruptedException {
        return common(threads, values, (list) -> list.stream().anyMatch(predicate), (list) -> list.stream().anyMatch(item -> item));
    }

    private static class ChunkRunnable<T, U> implements Runnable {

        private U result;
        private final Function<List<? extends T>, U> fn;
        private final List<? extends T> chunk;

        public ChunkRunnable(Function<List<? extends T>, U> fn, List<? extends T> chunk) {
            this.fn = fn;
            this.chunk = chunk;
        }

        @Override
        public void run() {
            this.result = fn.apply(chunk);
        }
    }

    private static class MyPair<F, S> {
        public final F f;
        public final S s;

        public MyPair(F f, S s) {
            this.f = f;
            this.s = s;
        }
    }
}