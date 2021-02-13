package com.spring.controller;

import com.spring.dto.TaskDTO;
import com.spring.enums.Status;
import com.spring.service.ProjectService;
import com.spring.service.TaskService;
import com.spring.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/task")
public class TaskController {

    TaskService taskService;
    ProjectService projectService;
    UserService userService;

    public TaskController(TaskService taskService, ProjectService projectService, UserService userService) {
        this.taskService = taskService;
        this.projectService = projectService;
        this.userService = userService;
    }

    //

    @GetMapping("/create")
    public String createTask(Model model) {

        model.addAttribute("task", new TaskDTO());
        model.addAttribute("projects", projectService.listAllNonCompletedProjects());
        model.addAttribute("employees", userService.listAllByRole("employee"));
        model.addAttribute("tasks", taskService.listAllTasks());

        return "task/create";
    }

    @PostMapping("/create")
    public String insertTask(Model model, TaskDTO task) {

        taskService.save(task);

        return "redirect:/task/create";
    }

    @GetMapping("/delete/{id}")
    public String deleteTask(@PathVariable("id") Long id) {
        taskService.delete(id);
        return "redirect:/task/create";
    }

    @GetMapping("/update/{id}")
    public String editTask(@PathVariable("id") Long id, Model model) {

        model.addAttribute("task", taskService.findById(id));
        model.addAttribute("projects", projectService.listAllProjects());
        model.addAttribute("employees", userService.listAllByRole("employee"));
        model.addAttribute("tasks", taskService.listAllTasks());

        return "task/update";
    }

    @PostMapping("/update/{id}")
    public String updateTask(TaskDTO task) {

        taskService.update(task);

        return "redirect:/task/create";
    }

    @GetMapping("/employee")
    public String edit(Model model) {

        List<TaskDTO> tasks = taskService.listAllTasksByStatusIsNot(Status.COMPLETE);
        model.addAttribute("tasks", tasks);

        return "task/employee-tasks";

    }

    @GetMapping("/employee/edit/{id}")
    public String employee_update(@PathVariable("id") Long id, Model model) {

        TaskDTO task = taskService.findById(id);
        List<TaskDTO> tasks = taskService.listAllTasksByStatusIsNot(Status.COMPLETE);


        model.addAttribute("task", task);
        model.addAttribute("users", userService.listAllByRole("employee"));
        model.addAttribute("projects", projectService.listAllNonCompletedProjects());
        model.addAttribute("tasks", tasks);
        model.addAttribute("statuses", Status.values());

        return "task/employee-update";

    }

    @PostMapping("/employee/update/{id}")
    public String employee_update(@PathVariable("id") Long id, TaskDTO taskDTO){
        taskService.updateStatus(taskDTO);
        return "redirect:/task/employee";
    }

    @GetMapping("/employee/archive")
    public String employee_archive(Model model){

        List<TaskDTO> tasks = taskService.listAllTasksByStatus(Status.COMPLETE);
        model.addAttribute("tasks", tasks);
        return "task/employee-archive";
    }
}
