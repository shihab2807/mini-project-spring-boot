package com.jsp.studentmanagementsystem.serviceimp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jsp.studentmanagementsystem.dto.MessageData;
import com.jsp.studentmanagementsystem.dto.StudentRequest;
import com.jsp.studentmanagementsystem.dto.StudentResponse;
import com.jsp.studentmanagementsystem.entity.Student;
import com.jsp.studentmanagementsystem.exception.StudentNotFoundByEmailException;
import com.jsp.studentmanagementsystem.exception.StudentNotFoundByIdException;
import com.jsp.studentmanagementsystem.exception.StudentNotFoundByPhnoException;
import com.jsp.studentmanagementsystem.repositary.StudentRepo;
import com.jsp.studentmanagementsystem.service.StudentService;
import com.jsp.studentmanagementsystem.util.ResponseStructure;

@Service
public class StudentServiceImpl implements StudentService {

	@Autowired
	private StudentRepo repo;

	@Autowired
	JavaMailSender javaMailSender;

	@Override
	public ResponseEntity<ResponseStructure<StudentResponse>> saveStudent(Student studentRequest) {

		Student student = new Student();
//		student.setStudentName(studentRequest.getStudentName());
//		student.setStudentEmail(studentRequest.getStudentEmail());
//		student.setStudentGrade(studentRequest.getStudentGrade());
//		student.setStudentPhNo(studentRequest.getStudentPhNo());
//		student.setStudentPassword(studentRequest.getStudentPassword());

		student.setStudentName("Joker");
		student.setStudentEmail("Joker@123");
		student.setStudentGrade("A");
		student.setStudentId(101);
		student.setStudentPassword("123");
		student.setStudentPhNo(9876543456l);

		Student student2 = repo.save(student);

		StudentResponse response = new StudentResponse();
		response.setStudentId(student2.getStudentId());
		response.setStudentName(student2.getStudentName());
		response.setStudentGrade(student2.getStudentGrade());

		ResponseStructure<StudentResponse> structure = new ResponseStructure<StudentResponse>();
		structure.setStatus(HttpStatus.CREATED.value());
		structure.setMessage("Student Data Saved Successfully");
		structure.setData(response);
		return new ResponseEntity<ResponseStructure<StudentResponse>>(structure, HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<ResponseStructure<StudentResponse>> updateStudent(StudentRequest studentRequest,
			int StudentId) {
		Optional<Student> optional = repo.findById(StudentId);
		if (optional.isPresent()) {

			// creating new student entity

			Student student = new Student();
			student.setStudentName(studentRequest.getStudentName());
			student.setStudentEmail(studentRequest.getStudentEmail());
			student.setStudentGrade(studentRequest.getStudentGrade());
			student.setStudentPhNo(studentRequest.getStudentPhNo());

			Student student2 = optional.get();
			student.setStudentId(student2.getStudentId());
			Student student3 = repo.save(student);

			StudentResponse response = new StudentResponse();
			response.setStudentId(student2.getStudentId());
			response.setStudentName(student2.getStudentName());
			response.setStudentGrade(student2.getStudentGrade());

			ResponseStructure<StudentResponse> structure = new ResponseStructure<StudentResponse>();
			structure.setStatus(HttpStatus.CREATED.value());
			structure.setMessage("Student Data is Updated Successfully");
			structure.setData(response);
			return new ResponseEntity<ResponseStructure<StudentResponse>>(structure, HttpStatus.CREATED);
		} else {
			throw new StudentNotFoundByIdException("Student not Found for this Id .....!!!!");
		}
	}

	@Override
	public ResponseEntity<ResponseStructure<Student>> deleteStudent(int studentId) {
		Optional<Student> optional = repo.findById(studentId);
		ResponseStructure<Student> structure = new ResponseStructure<Student>();
		if (optional.isPresent()) {
			Student student2 = optional.get();
			repo.delete(student2);
			structure.setStatus(HttpStatus.OK.value());
			structure.setMessage(studentId + " Student Data deleted succesfully");
			structure.setData(student2);
			return new ResponseEntity<ResponseStructure<Student>>(structure, HttpStatus.OK);
		}
		throw new StudentNotFoundByIdException("Failed to Delete the Student .....!!!!");
	}

	@Override
	public ResponseEntity<ResponseStructure<StudentResponse>> findStudentbyId(int studentId) {
		Optional<Student> optional = repo.findById(studentId);

		if (optional.isPresent()) {
			Student student = optional.get();

			StudentResponse response = new StudentResponse();
			response.setStudentId(student.getStudentId());
			response.setStudentName(student.getStudentName());
			response.setStudentGrade(student.getStudentGrade());

			ResponseStructure<StudentResponse> structure = new ResponseStructure<StudentResponse>();
			structure.setStatus(HttpStatus.FOUND.value());
			structure.setMessage(studentId + " Student deatils is as below");
			structure.setData(response);
			return new ResponseEntity<ResponseStructure<StudentResponse>>(structure, HttpStatus.FOUND);
		}
		throw new StudentNotFoundByIdException("Failed to Find the Student .....!!!!");
	}

	@Override
	public ResponseEntity<ResponseStructure<List<StudentResponse>>> findAllStudent() {
		Optional<List<Student>> optional = Optional.of(repo.findAll());
		ResponseStructure<List<StudentResponse>> structure = new ResponseStructure<List<StudentResponse>>();
		if (optional.isPresent()) {
			List<Student> students = optional.get();
			List<StudentResponse> responseList = new ArrayList<StudentResponse>();

			for (Student stud : students) {
				StudentResponse response = new StudentResponse();
				response.setStudentId(stud.getStudentId());
				response.setStudentName(stud.getStudentName());
				response.setStudentGrade(stud.getStudentGrade());
				responseList.add(response);
			}
			structure.setStatus(HttpStatus.FOUND.value());
			structure.setMessage("All the students details are fetched");
			structure.setData(responseList);
			return new ResponseEntity<ResponseStructure<List<StudentResponse>>>(structure, HttpStatus.FOUND);
		}
		throw new StudentNotFoundByIdException("Failed to find  the Student .....!!!!");
	}

	@Override
	public ResponseEntity<ResponseStructure<StudentResponse>> findStudentbyEmail(String studentEmail) {
		Student student = repo.findByStudentEmail(studentEmail);
		if (student != null) {

			StudentResponse response = new StudentResponse();
			response.setStudentId(student.getStudentId());
			response.setStudentGrade(student.getStudentGrade());
			response.setStudentName(student.getStudentName());

			ResponseStructure<StudentResponse> structure = new ResponseStructure<StudentResponse>();
			structure.setStatus(HttpStatus.FOUND.value());
			structure.setMessage("Student found based  on email");
			structure.setData(response);
			return new ResponseEntity<ResponseStructure<StudentResponse>>(structure, HttpStatus.FOUND);
		}
		throw new StudentNotFoundByEmailException("Failed to find student by Id");
		// return null;
	}

	@Override
	public ResponseEntity<ResponseStructure<StudentResponse>> findStudentByPhNo(long studentPhno) {
		Student student = repo.findByStudentPhNo(studentPhno);

		if (student != null) {
			StudentResponse response = new StudentResponse();
			response.setStudentId(student.getStudentId());
			response.setStudentName(student.getStudentName());
			response.setStudentGrade(student.getStudentGrade());

			ResponseStructure<StudentResponse> structure = new ResponseStructure<StudentResponse>();
			structure.setStatus(HttpStatus.FOUND.value());
			structure.setMessage("Student found By PhoneNumber");
			structure.setData(response);
			return new ResponseEntity<ResponseStructure<StudentResponse>>(structure, HttpStatus.FOUND);
		}
		throw new StudentNotFoundByPhnoException("Student is Not found by this Number");
	}

	@Override
	public ResponseEntity<ResponseStructure<List<String>>> getAllEmailsByGrade(String grade) {

		List<String> student = repo.getAllEmailsByGrade(grade);
		ResponseStructure<List<String>> structure = new ResponseStructure<>();
		structure.setStatus(HttpStatus.FOUND.value());
		structure.setMessage("Student emails are found by Grade");
		structure.setData(student);
		return new ResponseEntity<ResponseStructure<List<String>>>(structure, HttpStatus.FOUND);
	}

	@Override
	public ResponseEntity<String> extractDataFromExcel(MultipartFile file) throws IOException {

		XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
		for (Sheet sheet : workbook) {
			for (Row row : sheet) {
				if (row.getRowNum() > 0) {
					if (row != null) {
						String name = row.getCell(0).getStringCellValue();
						String email = row.getCell(1).getStringCellValue();
						long phoneNumber = (long) row.getCell(2).getNumericCellValue();
						String grade = row.getCell(3).getStringCellValue();
						String password = row.getCell(4).getStringCellValue();

						System.out.println(name + ", " + email + ", " + phoneNumber + ", " + grade + ", " + password);

//						Student student = new Student();
//						student.setStudentEmail(email);
//						student.setStudentGrade(grade);
//						student.setStudentName(name);
//						student.setStudentPassword(password);
//						student.setStudentPhNo(phoneNumber);
			//			repo.save(student);
					}
				}
			}
		}
		workbook.close();
		return new ResponseEntity<String>("Data Printed", HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> sendMail(MessageData messageData) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(messageData.getTo());
		message.setSubject(messageData.getSubject());
		message.setText(messageData.getText() + "/n/nThanks & Regards" + "/n" + messageData.getSenderName() + "/n"
				+ messageData.getSenderAddress());
		message.setSentDate(new Date());

		javaMailSender.send(message);

		return new ResponseEntity<String>("Mail send successfully!", HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> sendMimeMessage(MessageData messageData) throws MessagingException {
	
		MimeMessage mime=javaMailSender.createMimeMessage();
		MimeMessageHelper message=new MimeMessageHelper(mime,true);
		message.setTo(messageData.getTo());
		message.setSubject(messageData.getSubject());
		
		String emailBody=messageData.getText()+"<br><br><h4>Thanks & Regards<br>"+
		"<h4>"+messageData.getSenderName()+"<br>"+"<h4>"+messageData.getSenderAddress()+"</h4> <img src=\"https://jspiders.com/_nuxt/img/logo_jspiders.3b552d0.png\" alt=\"\">";
		
		message.setText(emailBody,true);
		message.setSentDate(new Date());
		
		javaMailSender.send(mime);
		return new ResponseEntity<String>("Mime message send successfully!!!",HttpStatus.OK);
	}
}
