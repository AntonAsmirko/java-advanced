package info.kgeorgiy.ja.asmirko.students;

import info.kgeorgiy.java.advanced.student.GroupName;
import info.kgeorgiy.java.advanced.student.Student;
import info.kgeorgiy.java.advanced.student.StudentQuery;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StudentDB implements StudentQuery {

    private static <F, S> List<S> mapAndCollect(List<F> firstForm, Function<F, S> mapping) {
        return firstForm.stream()
                .map(mapping)
                .collect(Collectors.toList());
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
        return mapAndCollect(students, student ->
                student.getFirstName() +
                        " " +
                        student.getLastName());
    }

    @Override
    public Set<String> getDistinctFirstNames(List<Student> students) {
        Function<Predicate<? super Student>, Stream<Student>> tmp = students.stream()::filter;

        return students
                .stream()
                .map(Student::getFirstName)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    @Override
    public String getMaxStudentFirstName(List<Student> students) {
        return students
                .stream()
                .max(Comparator.naturalOrder())
                .orElseThrow()
                .getFirstName();
    }

    @Override
    public List<Student> sortStudentsById(Collection<Student> students) {
        return students
                .stream()
                .sorted(Comparator.naturalOrder())
                .collect(Collectors.toList());
    }

    @Override
    public List<Student> sortStudentsByName(Collection<Student> students) {
        return students
                .stream()
                .sorted(Comparator.comparing(Student::getId))
                .sorted(Comparator.comparing(Student::getLastName))
                .sorted(Comparator.comparing(Student::getFirstName).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<Student> findStudentsByFirstName(Collection<Student> students, String name) {
        return students
                .stream()
                .filter(student -> student.getFirstName().equals(name))
                .sorted(Comparator.naturalOrder())
                .collect(Collectors.toList());
    }

    @Override
    public List<Student> findStudentsByLastName(Collection<Student> students, String name) {
        return students
                .stream()
                .filter(student -> student.getLastName().equals(name))
                .collect(Collectors.toList());
    }

    @Override
    public List<Student> findStudentsByGroup(Collection<Student> students, GroupName group) {
        return students
                .stream()
                .filter(student -> student.getGroup().equals(group))
                .sorted(Comparator.comparingInt(Student::getId))
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, String> findStudentNamesByGroup(Collection<Student> students, GroupName group) {
        return students
                .stream()
                .filter(student -> student.getGroup().equals(group))
                .collect(Collectors.toMap(Student::getLastName, Student::getFirstName, (student1, student2) ->
                        student1.compareTo(student2) > 0 ? student1 : student2));
    }
}
