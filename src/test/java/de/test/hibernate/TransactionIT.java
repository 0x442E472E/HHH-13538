package de.test.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.junit.Before;
import org.junit.Test;

import java.util.Properties;

import static org.junit.Assert.assertEquals;

public class TransactionIT {

  private SessionFactory sessionFactory;

  @Before
  public void before() throws Exception {

    final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure() // configures settings from hibernate.cfg.xml
            .build();
    try {
      sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
    }
    catch (Exception e) {
      // The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory
      // so destroy it manually.
      StandardServiceRegistryBuilder.destroy( registry );
      throw e;
    }

  }

  @Test
  public void test() {
    long id;

    // Create a new entity
    try(final Session session = sessionFactory.openSession()) {
      final Transaction tx = session.beginTransaction();
      final MyEntity myEntity = new MyEntity("fieldvalue");
      session.save(myEntity);
      tx.commit();
      id = myEntity.getId();
    }

    // Change a field of the new entity and provoke a RuntimeException that we will catch
    try(final Session session = sessionFactory.openSession()) {
      final Transaction tx = session.beginTransaction();
      final MyEntity myEntity = session.get(MyEntity.class, id);
      myEntity.setMyField("newfieldvalue");
      try {
        session.createQuery("update MyEntity set foo='bar' where id = " + id).executeUpdate();
      } catch (Exception expected) {
        expected.printStackTrace();
      }
      tx.commit();
    }

    // Check whether the previous change has been persisted
    try(final Session session = sessionFactory.openSession()) {
      final Transaction tx = session.beginTransaction();
      final MyEntity myEntity = session.get(MyEntity.class, id);
      //this breaks for Hibernate > 5.4.1
      assertEquals("newfieldvalue", myEntity.getMyField());
      tx.commit();
      id = myEntity.getId();
    }



  }

}
