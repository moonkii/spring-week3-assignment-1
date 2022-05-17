// 1. Read Collection - GET /tasks
// 2. Read Item - Get /tasks/{id}
// 3. Create - Post /tasks
// 4. Update - PUT/PATCH /tasks/{id}
// 5. Delete - DELETE /tasks/{id}
// => 전체: Service 가 올바를 것!

package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

class TaskControllerTest {
  // 가능한 것
  // 1. Real Object
  // 2. Mock Object => 타입이 필요함
  // 3. Spy -> Proxy 패턴 -> 진짜 오브젝트가 필요함
  TaskController controller;

  TaskService taskService;

  @BeforeEach
  void setup() {
    taskService = mock(TaskService.class);

    List<Task> tasks = new ArrayList<>();
    Task task = new Task();
    task.setTitle("Test1");
    tasks.add(task);

    given(taskService.getTasks()).willReturn(tasks);
    given(taskService.getTask(1L)).willReturn(task);
    given(taskService.getTask(100L))
        .willThrow(new TaskNotFoundException(100L));
    given(taskService.updateTask(eq(100L), any(Task.class)))
        .willThrow(new TaskNotFoundException(100L));
    given(taskService.deleteTask(100L))
        .willThrow(new TaskNotFoundException(100L));

    controller = new TaskController(taskService);
  }

  @Test
  void listWithoutTasks() {
    given(taskService.getTasks()).willReturn(new ArrayList<>());

    assertThat(controller.list()).isEmpty();

    verify(taskService).getTasks();
  }

  @Test
  void listWithSomeTasks() {
    assertThat(controller.list()).isNotEmpty();

    verify(taskService).getTasks();
  }

  @Test
  void detail() {
    Task task = controller.detail(1L);

    assertThat(task).isNotNull();
  }

  @Test
  void detailWithNotExistedID() {
    assertThatThrownBy(() -> controller.detail(100L))
        .isInstanceOf(TaskNotFoundException.class);
  }

  @Test
  void createNewTask() {
    Task task = new Task();
    task.setTitle("Test2");

    controller.create(task);

    verify(taskService).createTask(task);
  }

  @Test
  void updateTask() {
    Task task = new Task();
    task.setTitle("Renamed Task");

    controller.update(1L, task);

    verify(taskService).updateTask(1L, task);
  }

  @Test
  void updateNotExistedTask() {
    Task task = new Task();
    task.setTitle("Renamed Task");

    assertThatThrownBy(() -> controller.update(100L, task))
        .isInstanceOf(TaskNotFoundException.class);
  }

  @Test
  void deleteTask() {
    controller.delete(1L);

    verify(taskService).deleteTask(1L);
  }

  @Test
  void deleteNotExistedTask() {
    assertThatThrownBy(() -> controller.delete(100L))
        .isInstanceOf(TaskNotFoundException.class);
  }
}
