package com.isoo.todo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping({ "/api" })
public class Controller {
	
	@Autowired
	TaskRepository tRepo;
	
	@GetMapping({ "/validateLogin" })
	public User validateLogin() {
		return new User("User successfully authenticated");
		//iam master
	}
	
	@GetMapping("/getall")
	public ResponseEntity<List<Task>> getAll() {
		List<Task> tempFind = new ArrayList<Task>();
		tempFind  = tRepo.findAll();
		
		if(tempFind.isEmpty()) {
			return 	new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return 	new ResponseEntity<>(tempFind,HttpStatus.OK);
	}
	
	@GetMapping("/todo/{id}")
	public ResponseEntity<Task> getTodoById(@PathVariable("id") int id){
		Optional<Task> t=  tRepo.findById(id);
		if(t.isPresent()){
			return new ResponseEntity<>(t.get(),HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	@PostMapping("/todo")
	public ResponseEntity<Task>  createTodo(@RequestBody Task t){
		try {
			Task tasktemp =tRepo.save(new Task(t.getTodo(),t.getStatus()));
			return new ResponseEntity<>(tasktemp,HttpStatus.CREATED);
		}catch(Exception e) {
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}
		
	}
	
	@GetMapping("/delete/{id}")
	public ResponseEntity<Task> deleteById(@PathVariable("id") int id){
		try {
			tRepo.deleteById(id);
			return new ResponseEntity<>(HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}
	}
	
	@PostMapping("/update/{id}")
	public ResponseEntity<Task> updateById(@PathVariable("id") int id, @RequestBody Task t){
		Optional<Task> taskData = tRepo.findById(id);
		if(taskData.isPresent()) {
			Task tempTask = taskData.get();
			tempTask.setStatus(t.getStatus());
			tempTask.setTodo(t.getTodo());
			return new ResponseEntity<Task>(tRepo.save(tempTask),HttpStatus.OK);
		}else {
			return new ResponseEntity<Task>(HttpStatus.NOT_FOUND);
		}
	}
	
	
}
