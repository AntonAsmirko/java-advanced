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

public class StudentsDB implements StudentQuery {

    private static final BinaryOperator<String> MAP_SELECTOR = (student1, student2)
            -> student1.compareTo(student2) < 0 ? student1 : student2;
    private static final Comparator<Student> FIRST_NAME_CMP = Comparator.comparing(Student::getFirstName).reversed();
    private static final Comparator<Student> LAST_NAME_CMP = Comparator.comparing(Student::getLastName).reversed();

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
        return mapAndCollect(students, student -> student.getFirstName() + " " + student.getLastName());
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
                .sorted(Comparator.comparing(Student::getId))
                .sorted(FIRST_NAME_CMP)
                .sorted(LAST_NAME_CMP)
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
        return streamAndFilter(students, func(name, Student::getLastName)).collect(Collectors.toList());
    }

    @Override
    public List<Student> findStudentsByGroup(Collection<Student> students, GroupName group) {
        return streamAndFilter(students, func(group, Student::getGroup))
                .sorted(FIRST_NAME_CMP)
                .sorted(LAST_NAME_CMP)
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, String> findStudentNamesByGroup(Collection<Student> students, GroupName group) {
        return streamAndFilter(students, func(group, Student::getGroup))
                .collect(Collectors.toMap(Student::getLastName, Student::getFirstName, MAP_SELECTOR));
    }
}
