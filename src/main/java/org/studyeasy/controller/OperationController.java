package org.studyeasy.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.studyeasy.entity.User;
import org.studyeasy.model.UsersModel;

import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@WebServlet("/operation")
public class OperationController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	@Resource(name="jdbc/project")
	private DataSource dataSource;
	
    public OperationController() {
        
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String page = request.getParameter("page");
		page = page.toLowerCase();
		
		switch(page) {
			case "listusers":
				listUsers(request, response);
				break;
			case "adduser":
				addUserFormLoader(request, response);
				break;
			case "updateuser":
				updateUserFormLoader(request, response);
				break;
			case "deleteuser":
				deleteUser(Integer.parseInt(request.getParameter("usersId")));
				listUsers(request, response);
				break;
			default:
				errorPage(request, response);
		}
	}
	private void deleteUser(int usersId) {
		new UsersModel().deleteUser(dataSource, usersId);
	}

	private void updateUserFormLoader(HttpServletRequest request, HttpServletResponse response) {
		request.setAttribute("title", "Update User");
		try {
			request.getRequestDispatcher("updateUser.jsp").forward(request, response);
		} catch (ServletException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String operation = request.getParameter("form");
		operation = operation.toLowerCase();
		switch(operation) {
			case "adduseroperation":
				User newUser = new User(request.getParameter("username"), request.getParameter("email"));
				addUserOperation(newUser);
				listUsers(request, response);
				break;
			case "updateuseroperation":
				User updatedUser = new User(Integer.parseInt(request.getParameter("usersId")), request.getParameter("username"),
										request.getParameter("email"));
				updateUserOperation(dataSource, updatedUser);
				listUsers(request, response);
				break;
			default:
				errorPage(request, response);
		}
	}
	
	private void updateUserOperation(DataSource dataSource2, User updatedUser) {
		new UsersModel().updateUser(dataSource, updatedUser);
	}

	private void addUserOperation(User newUser) {
		new UsersModel().addUser(dataSource, newUser);
		return;
	}
	
	protected void listUsers(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<User> listUsers = new ArrayList<>();
		listUsers = new UsersModel().listUsers(dataSource);
		request.setAttribute("listUsers", listUsers);
		request.setAttribute("title", "List of users");
		request.getRequestDispatcher("listUser.jsp").forward(request, response);
	}
	
	protected void addUserFormLoader(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("title", "Add User");
		request.getRequestDispatcher("addUser.jsp").forward(request, response);
	}
	
	protected void errorPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("title", "Error page");
		request.getRequestDispatcher("error.jsp").forward(request, response);
	}
	
}
