package edu.cnm.deepdive.many_to_many.controller;


import edu.cnm.deepdive.many_to_many.model.dao.ProjectRepository;
import edu.cnm.deepdive.many_to_many.model.dao.StudentRepository;
import edu.cnm.deepdive.many_to_many.model.entity.Project;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@ExposesResourceFor(Project.class)
@RestController
@RequestMapping("/projects")
public class ProjectController{

  private ProjectRepository projectRepository;
  private StudentRepository studentRepository;

  public ProjectController(
      ProjectRepository projectRepository,
      StudentRepository studentRepository) {
    this.projectRepository = projectRepository;
    this.studentRepository = studentRepository;
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public List<Project> list(){
    return projectRepository.findAllByOrderByNameAsc();
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Project> post(@RequestBody Project project){
    projectRepository.save(project);
    return ResponseEntity.created(project.getHref()).body(project);
  }

  @GetMapping(value = "{projectId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Project get(@PathVariable("projectId") long projectId){
    return projectRepository.findById(projectId).get();
  }


  @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Resource not found")
  @ExceptionHandler(NoSuchElementException.class)
  public void noFound(){
    //
  }
}
