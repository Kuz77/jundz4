package org.example;

import javax.persistence.*;
import java.util.List;

public class CourseManager {

    private static EntityManagerFactory entityManagerFactory;

    static {
        entityManagerFactory = Persistence.createEntityManagerFactory("org.example.Course");
    }

    public static void main(String[] args) {
        createTable();

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = null;

        try {
            transaction = entityManager.getTransaction();
            transaction.begin();

            // Создание и сохранение нового курса
            Course course = new Course("Дждикей", "5 занятий");
            saveCourse(entityManager, course);

            // Чтение курса по ID
            Course retrievedCourse = findCourseById(entityManager, course.getId());
            System.out.println("Курс: " + retrievedCourse);

            // Обновление курса
            retrievedCourse.setTitle("Ява джун");
            updateCourse(entityManager, retrievedCourse);

            // Удаление курса
//            deleteCourse(entityManager, retrievedCourse.getId());

            transaction.commit();
        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            ex.printStackTrace();
        } finally {
            entityManager.close();
        }

        entityManagerFactory.close();
    }

    public static void createTable() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        // Define entity class
        Class<?> entityClass = Course.class;

        // Create table
        Table table = entityClass.getAnnotation(Table.class);
        if (table != null) {
            String tableName = table.name();
            String createTableQuery = "CREATE TABLE IF NOT EXISTS " + tableName + " (id INT AUTO_INCREMENT PRIMARY KEY, title VARCHAR(255), duration VARCHAR(255))";
            Query query = entityManager.createNativeQuery(createTableQuery);
            query.executeUpdate();
            System.out.println("Table 'Courses' created successfully.");
        } else {
            System.out.println("Entity does not have a @Table annotation.");
        }

        entityManager.getTransaction().commit();
        entityManager.close();
    }

    public static void saveCourse(EntityManager entityManager, Course course) {
        entityManager.persist(course);
    }

    public static Course findCourseById(EntityManager entityManager, int id) {
        return entityManager.find(Course.class, id);
    }

    public static void updateCourse(EntityManager entityManager, Course course) {
        entityManager.merge(course);
    }

//    public static void deleteCourse(EntityManager entityManager, int id) {
//        Course course = entityManager.find(Course.class, id);
//        if (course != null) {
//            entityManager.remove(course);
//        }
//    }
}


