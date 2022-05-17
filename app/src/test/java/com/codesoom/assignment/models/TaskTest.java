package com.codesoom.assignment.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TaskTest {
  Task task;

  @BeforeEach
  void setUp() {
    task = new Task();
  }

  @Test
  void getId() {
    Long id = 1L;
    task.setId(id);

    assertThat(task.getId()).isEqualTo(id);
  }

  @Test
  void setId() {
    Long id = 1L;
    task.setId(id);
  }
}
