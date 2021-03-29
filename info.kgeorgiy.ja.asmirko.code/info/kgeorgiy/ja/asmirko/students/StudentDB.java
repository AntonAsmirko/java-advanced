package info.kgeorgiy.ja.asmirko.students;

import info.kgeorgiy.java.advanced.student.GroupName;
import info.kgeorgiy.java.advanced.student.Student;
import info.kgeorgiy.java.advanced.student.StudentQuery;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Easy-version interface
 * for <a href="https://www.kgeorgiy.info/courses/java-advanced/homeworks.html#homework-student">Student</a> homework
 * of <a href="https://www.kgeorgiy.info/courses/java-advanced/">Java Advanced</a> course.
 * <p>
 * <em>Students ordered by name</em>:
 * students ordered by {@link Student#getLastName() last name} in descending order
 * students with equal last names are ordered by {@link Student#getFirstName() first name} in descending order,
 * students having equal both last and first names are ordered by {@link Student#getId() id} in ascending order.
 *
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */

public class StudentDB implements StudentQuery {

    private static final BinaryOperator<String> MAP_SELECTOR = (student1, student2)
            -> student1.compareTo(student2) < 0 ? student1 : student2;
    private static final Comparator<Student> FIRST_NAME_CMP = Comparator.comparing(Student::getFirstName).reversed();
    private static final Comparator<Student> LAST_NAME_CMP = Comparator.comparing(Student::getLastName).reversed();
    private static final Comparator<Student> FULL_CMP = LAST_NAME_CMP
            .thenComparing(FIRST_NAME_CMP)
            .thenComparing(Comparator.naturalOrder());

    private static <F, S> List<S> mapAndCollect(List<F> firstForm, Function<F, S> mapping) {
        return firstForm.stream()
                .map(mapping)
                .collect(Collectors.toList());
    }

    private static <T> Stream<T> streamAndFilter(Collection<T> list, Predicate<T> predicate) {
        return list.stream().filter(predicate);
    }

    private static <T, P> Predicate<P> func(T s1, Function<P, T> sup) {
        return student -> sup.apply(student).equals(s1);
    }

    @Override
    public List<String> getFirstNames(List<Student> students) {
        return mapAndCollect(students, Student::getFirstName);
    }

    @Override
    public List<String> getLastNames(List<Student> students) {
        return mapAndCollect(students, Student::getLastName);
    }

    @Override
    public List<GroupName> getGroups(List<Student> students) {
        return mapAndCollect(students, Student::getGroup);
    }

    @Override
    public List<String> getFullNames(List<Student> students) {
        return mapAndCollect(students, student -> String.format("%s %s", student.getFirstName(), student.getLastName()));
    }

    @Override
    public Set<String> getDistinctFirstNames(List<Student> students) {
        return students.stream()
                .map(Student::getFirstName)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    @Override
    public String getMaxStudentFirstName(List<Student> students) {
        return students.stream()
                .max(Comparator.naturalOrder())
                .map(Student::getFirstName)
                .orElse("");
    }

    @Override
    public List<Student> sortStudentsById(Collection<Student> students) {
        return students.stream()
                .sorted(Comparator.naturalOrder())
                .collect(Collectors.toList());
    }

    @Override
    public List<Student> sortStudentsByName(Collection<Student> students) {
        return students.stream()
                .sorted(FULL_CMP)
                .collect(Collectors.toList());
    }

    @Override
    public List<Student> findStudentsByFirstName(Collection<Student> students, String name) {
        return streamAndFilter(students, func(name, Student::getFirstName))
                .sorted(LAST_NAME_CMP)
                .collect(Collectors.toList());
    }

    @Override
    public List<Student> findStudentsByLastName(Collection<Student> students, String name) {
        return streamAndFilter(students, func(name, Student::getLastName))
                .sorted(FULL_CMP)
                .collect(Collectors.toList());
    }

    @Override
    public List<Student> findStudentsByGroup(Collection<Student> students, GroupName group) {
        return streamAndFilter(students, func(group, Student::getGroup))
                .sorted(FULL_CMP)
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, String> findStudentNamesByGroup(Collection<Student> students, GroupName group) {
        return streamAndFilter(students, func(group, Student::getGroup))
                .collect(Collectors.toMap(Student::getLastName, Student::getFirstName, MAP_SELECTOR));
    }
}
