package info.kgeorgiy.ja.asmirko.concurrent;

import info.kgeorgiy.java.advanced.concurrent.ScalarIP;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author Anton Asmirko
 */
public class IterativeParallelism implements ScalarIP {

    private <T> Stream<List<T>> partition(List<T> source, int length) {
        int chunkLen = source.size() / length;
        return IntStream.range(0, length + 1)
                .mapToObj(n -> source.subList(n * chunkLen, n == length ? source.size() : (n + 1) * chunkLen));
    }

    private <T, U> U common(int threads, List<? extends T> values, BiConsumer<T, VarReference<U>> action,
                            VarReference<U> currentMaximum) throws InterruptedException {
        if (values.size() == 0) {
            throw new NoSuchElementException("No values are given");
        }
        List<Thread> workers = partition(values, threads).map(chunk -> new Thread(() -> chunk.forEach(item -> {
            synchronized (currentMaximum) {
                action.accept(item, currentMaximum);
            }
        }))).peek(Thread::start).collect(Collectors.toList());
        for (final Thread worker : workers) {
            worker.join();
        }
        return currentMaximum.getVar();
    }

    @Override
    public <T> T maximum(int threads, List<? extends T> values, Comparator<? super T> comparator)
            throws InterruptedException {
        if (values.size() == 0) {
            throw new NoSuchElementException("No values are given");
        }
        return this.<T, T>common(threads, values, (f, s) -> {
            if (comparator.compare(s.getVar(), f) < 0) {
                s.setVar(f);
            }
        }, new VarReference<>(values.get(0)));
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
        return this.<T, Boolean>common(threads, values, (f, s) -> s.setVar(s.getVar() || predicate.test(f)), new VarReference<>(false));
    }
}