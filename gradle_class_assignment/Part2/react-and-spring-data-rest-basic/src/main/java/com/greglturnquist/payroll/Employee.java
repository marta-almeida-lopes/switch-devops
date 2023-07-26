/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.greglturnquist.payroll;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.*;

/**
 * @author Greg Turnquist
 */
// tag::code[]
@Entity // <1>
public class Employee {

	private @Id @GeneratedValue Long id; // <2>
	@NotBlank (message = "First name can't be empty")
	private String firstName;
	@NotBlank (message = "Last name can't be empty")
	private String lastName;
	@NotBlank (message = "Description can't be empty")
	private String description;

	/**
	 * @NotBlank is a Java Bean validation. It is used validate that the firstName, lastName and description
	 * is not empty/null
	 * @Min is used to validate that the jobYears attribute has a value no smaller than 1
	 * @Max is used to validate that the jobYears attribute has a value no bigger than 80
	 * @NotNull is used to validate that the jobYears and e-mail attributes are not null
	 * @Email is used to validate that the annotated property is a valid email address
	 */

	@NotNull (message = "Job Years can't be empty") @Min(1) @Max(80)
	private int jobYears;
	@Email @NotNull (message = "This field must not be empty. The e-mail must be valid")
	private String email;

	private Employee() {}


	public Employee(String firstName, String lastName, String description, int jobYears, String email) {
		if (firstName == null || firstName.trim().isEmpty()) {
			throw new IllegalArgumentException("The first name can't be empty");
		}
		if (lastName == null || lastName.trim().isEmpty()) {
			throw new IllegalArgumentException("The last name can't be empty");
		}
		if (description == null || description.trim().isEmpty()) {
			throw new IllegalArgumentException("The description can't be empty");
		}
		if (jobYears <= 0 || jobYears > 80) {
			throw new IllegalArgumentException("Job Years can't be zero nor negative");
		}
		if (email == null || email.trim().isEmpty() || !email.contains("@")) {
			throw new IllegalArgumentException("Email can't be empty");
		}
		this.firstName = firstName;
		this.lastName = lastName;
		this.description = description;
		this.jobYears = jobYears;
		this.email = email;
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Employee employee = (Employee) o;
		return Objects.equals(id, employee.id) &&
			Objects.equals(firstName, employee.firstName) &&
			Objects.equals(lastName, employee.lastName) &&
			Objects.equals(description, employee.description) &&
			Objects.equals(jobYears, employee.jobYears) &&
			Objects.equals(email, employee.email);
	}

	@Override
	public int hashCode() {

		return Objects.hash(id, firstName, lastName, description, jobYears, email);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getJobYears() { return jobYears; }

	public void setJobYears(int jobYears) { this.jobYears = jobYears; }

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "Employee{" +
			"id=" + id +
			", firstName='" + firstName + '\'' +
			", lastName='" + lastName + '\'' +
			", description='" + description + '\'' +
			", email='" + email + '\'' +
			'}';
	}
}
// end::code[]
