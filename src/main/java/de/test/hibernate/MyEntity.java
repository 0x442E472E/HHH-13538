package de.test.hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "MyEntity")
public class MyEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private long id;

  @Column(name = "my_field")
  private String myField;

  public MyEntity(final String myField) {
    this.myField = myField;
  }

  public MyEntity() {
  }

  public String getMyField() {
    return myField;
  }

  public void setMyField(final String myField) {
    this.myField = myField;
  }

  public long getId() {
    return id;
  }
}
