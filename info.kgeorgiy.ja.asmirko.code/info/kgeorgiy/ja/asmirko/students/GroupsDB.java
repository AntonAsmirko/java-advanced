package info.kgeorgiy.ja.asmirko.students;

import info.kgeorgiy.java.advanced.student.Group;
import info.kgeorgiy.java.advanced.student.GroupName;
import info.kgeorgiy.java.advanced.student.GroupQuery;
import info.kgeorgiy.java.advanced.student.Student;

import java.util.*;

public class GroupsDB implements GroupQuery {
    @Override
    public List<Group> getGroupsByName(Collection<Student> students) {
//        return students.stream().reduce(new LinkedList<Group>(), (result, student) -> {
//            boolean groupFound = false;
//            for(var group : result){
//                for (var s : group.getStudents()){
//                    if (s.getGroup().equals(student.getGroup())){
//                        group.getStudents().add(student);
//                        groupFound = true;
//                        break;
//                    }
//                }
//            }
//            if (!groupFound){
//
//            }
//        }, (group) -> {
//
//        });
        return null;
    }

    @Override
    public List<Group> getGroupsById(Collection<Student> students) {
        return null;
    }

    @Override
    public GroupName getLargestGroup(Collection<Student> students) {
        return null;
    }

    @Override
    public GroupName getLargestGroupFirstName(Collection<Student> students) {
        return null;
    }

    @Override
    public List<String> getFirstNames(List<Student> students) {
        return null;
    }

    @Override
    public List<String> getLastNames(List<Student> students) {
        return null;
    }

    @Override
    public List<GroupName> getGroups(List<Student> students) {
        return null;
    }

    @Override
    public List<String> getFullNames(List<Student> students) {
        return null;
    }

    @Override
    public Set<String> getDistinctFirstNames(List<Student> students) {
        return null;
    }

    @Override
    public String getMaxStudentFirstName(List<Student> students) {
        return null;
    }

    @Override
    public List<Student> sortStudentsById(Collection<Student> students) {
        return null;
    }

    @Override
    public List<Student> sortStudentsByName(Collection<Student> students) {
        return null;
    }

    @Override
    public List<Student> findStudentsByFirstName(Collection<Student> students, String name) {
        return null;
    }

    @Override
    public List<Student> findStudentsByLastName(Collection<Student> students, String name) {
        return null;
    }

    @Override
    public List<Student> findStudentsByGroup(Collection<Student> students, GroupName group) {
        return null;
    }

    @Override
    public Map<String, String> findStudentNamesByGroup(Collection<Student> students, GroupName group) {
        return null;
    }
}
