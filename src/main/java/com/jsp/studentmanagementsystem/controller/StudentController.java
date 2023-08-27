package com.jsp.studentmanagementsystem.controller;

import java.io.IOException;
import java.util.List;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jsp.studentmanagementsystem.dto.MessageData;
import com.jsp.studentmanagementsystem.dto.StudentRequest;
import com.jsp.studentmanagementsystem.dto.StudentResponse;
import com.jsp.studentmanagementsystem.entity.Student;
import com.jsp.studentmanagementsystem.service.StudentService;
import com.jsp.studentmanagementsystem.util.ResponseStructure;

//import jakarta.validation.Valid;

@RestController
@RequestMapping("/students")       // it will be the endpoint for all the methods until
public class StudentController {   // we dont have same Mapping methods if any methods are same then we go with 
								   //  pariticular params

	@Autowired
	private StudentService service;
	
	
	@PostMapping
	public ResponseEntity<ResponseStructure<StudentResponse>> saveStudent(@RequestBody Student studentRequest) {
		return service.saveStudent(studentRequest);
	}

	
	
	@PutMapping("/{studentId}")
	public ResponseEntity<ResponseStructure<StudentResponse>> updateStudent(@RequestBody StudentRequest studentRequest,
			@PathVariable int studentId) {
		return service.updateStudent(studentRequest, studentId);
	}

	
//  @DeleteMapping("/student")
//	public ResponseEntity<Student> deleteStudent(@RequestParam int studentId){

	@DeleteMapping("/{studentId}")
	public ResponseEntity<ResponseStructure<Student>> deleteStudent(@PathVariable int studentId) {
		return service.deleteStudent(studentId);
	}

	
	@GetMapping("/{studentId}")
	public ResponseEntity<ResponseStructure<StudentResponse>> getStudent(@PathVariable int studentId) {
		return service.findStudentbyId(studentId);
	}
	
	
	@CrossOrigin
	@GetMapping
	public ResponseEntity<ResponseStructure<List<StudentResponse>>> getStudents() {
		return service.findAllStudent();
	}
	
	
	@GetMapping(params = "studentEmail")
	public ResponseEntity<ResponseStructure<StudentResponse>> getStudentbyEmail(@RequestParam String studentEmail) {
		return service.findStudentbyEmail(studentEmail);
	}
	
	
	@GetMapping(params ="studentPhno")
	public ResponseEntity<ResponseStructure<StudentResponse>> getStudentByPhNo(@RequestParam long studentPhno){
		return service.findStudentByPhNo(studentPhno);
	}
	
	
	
	@GetMapping(params ="grade")
	public ResponseEntity<ResponseStructure<List<String>>> getAllEmailsByGrade( @RequestParam String grade){
		return service.getAllEmailsByGrade(grade);
	}
	
	@PostMapping("/extract")
	public ResponseEntity<String> extractDataFromExcel(MultipartFile file) throws IOException
	{
		return service.extractDataFromExcel(file);
	}
	
	@PostMapping("/mail")
	public ResponseEntity<String> sendMail(MessageData messageData){
		
		return service.sendMail(messageData);
	}
	
	@PostMapping("/mime-message")
	public ResponseEntity<String> sendMimeMessage(@RequestBody MessageData messageData) throws MessagingException{
		
		return service.sendMimeMessage(messageData);
	}
}
