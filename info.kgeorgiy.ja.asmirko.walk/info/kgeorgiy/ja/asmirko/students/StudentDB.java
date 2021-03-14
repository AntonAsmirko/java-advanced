package info.kgeorgiy.ja.asmirko.students;

import info.kgeorgiy.java.advanced.student.GroupName;
import info.kgeorgiy.java.advanced.student.Student;
import info.kgeorgiy.java.advanced.student.StudentQuery;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class StudentDB implements StudentQuery {

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {

        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    private static <F,S> List<S> mapAndCollect(List<F> firstForm, Function<F,S> mapping){
        return firstForm
                .stream()
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
                (new StringBuilder(student.getFirstName()))
                .append(" ")
                .append(student.getLastName()).toString());
    }

    @Override
    public Set<String> getDistinctFirstNames(List<Student> students) {
        return students
                .stream()
                .filter(distinctByKey(Student::getFirstName))
                .map(Student::getFirstName)
                .sorted(String::compareTo)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public String getMaxStudentFirstName(List<Student> students) {
        return students
                .stream()
                .max(Comparator.comparingInt(Student::getId))
                .orElseGet(() -> new Student(228, "его", "отчислили", GroupName.M3234))
                .getFirstName();
    }

    @Override
    public List<Student> sortStudentsById(Collection<Student> students) {
        return students
                .stream()
                .sorted(Comparator.comparingInt(Student::getId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Student> sortStudentsByName(Collection<Student> students) {
        return students
                .stream()
                .sorted((student1, student2) -> {
                    int tmpRes = student1.getFirstName().compareTo(student2.getFirstName());
                    if (tmpRes != 0) {
                        return -tmpRes;
                    } else {
                        int tmpRes2 = -student1.getLastName().compareTo(student2.getLastName());
                        if (tmpRes2 != 0)
                            return tmpRes2;
                        else if (student1.getId() > student2.getId()){
                            return 1;
                        } else {
                            return -1;
                        }
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Student> findStudentsByFirstName(Collection<Student> students, String name) {
        return students
                .stream()
                .filter(student -> student.getFirstName().equals(name))
                .sorted(Student::compareTo)
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
                .collect(Collectors.toMap(Student::getLastName, Student::getFirstName, (student1, student2) -> {
                    if (student1.compareTo(student2) > 0)
                        return student1;
                    else
                        return student2;
                }));
    }
}
