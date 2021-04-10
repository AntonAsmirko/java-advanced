package info.kgeorgiy.ja.asmirko.concurrent;

import info.kgeorgiy.java.advanced.concurrent.ScalarIP;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author Anton Asmirko
 */
public class IterativeParallelism implements ScalarIP {

    @Override
    public <T> T maximum(int threads, List<? extends T> values, Comparator<? super T> comparator) throws InterruptedException {
        if (values.size() == 0) {
            throw new NoSuchElementException("No values are given");
        }
        final VarReference<T> currentMaximum = new VarReference<>(values.get(0));
        final VarReference<Integer> numThreads = new VarReference<>(0);

        List<Thread> workers = values.stream().map(item -> {
            Thread thread = new Thread(() -> {
                synchronized (currentMaximum) {
                    System.out.println(currentMaximum.getVar() + " vs " + item);
                    if (comparator.compare(currentMaximum.getVar(), item) < 0) {
                        currentMaximum.setVar(item);
                    }
                }
            });
            while (numThreads.getVar() < threads) {
                synchronized (numThreads) {
                    if (numThreads.getVar() < threads) {
                        System.out.println(thread.getName() + " started");
                        numThreads.setVar(numThreads.getVar() + 1);
                        thread.start();
                        break;
                    }
                }
            }
            return thread;
        }).collect(Collectors.toList());
        for (final Thread worker : workers) {
            worker.join();
        }
        return currentMaximum.getVar();
    }

    @Override
    public <T> T minimum(int threads, List<? extends T> values, Comparator<? super T> comparator) throws InterruptedException {
        return null;
    }

    @Override
    public <T> boolean all(int threads, List<? extends T> values, Predicate<? super T> predicate) throws InterruptedException {
        return false;
    }

    @Override
    public <T> boolean any(int threads, List<? extends T> values, Predicate<? super T> predicate) throws InterruptedException {
        return false;
    }

    public static void main(String[] args) throws InterruptedException {
        IterativeParallelism iterativeParallelism = new IterativeParallelism();
        System.out.println(iterativeParallelism.maximum(2, List.of(1, 2, 1000, 3, 4, 5, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1), Comparator.naturalOrder()));
    }
}
