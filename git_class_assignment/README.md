# README #

This README is a simple technical report about the developed work for the Class Assignment 1. 
This Class Assignment was about <strong>Version Control with Git</strong>.

This report will cover everything that was done for this work, step by step. It will also cover the used approach with brief explanations, all the commands used, and finally, an alternative on how this work could have been done.

## CA1 - Part 1 ##
### Getting a Git Repository ###
To create a git repository I used the following commands: <br>
&#36; mkdir projDevops/ <br>
&#36; cd projDevops/ <br>
&#36; git init CA1 <br>

I also created a remote repository on Bitbucket called <strong> devops-22-23-MAAL-1221850 </strong> to then connect to my previously created local repository.

### Adding the Tutorial React.js and Spring Data REST application to my repository ###
These were the commands I've used to complete the first requirement of this assignment 

"Copy the code of the Tutorial React.js and Spring Data REST Application into a
new folder named CA1": <br>
&#36; mkdir projDevops_clone/ <br>
&#36; cd projDevops_clone/ <br>
&#36; git clone https://github.com/spring-guides/tut-react-and-spring-data-rest.git <br>
&#36; mv basic/ pom.xml ~/Documents/projDevops/CA1/ <br>


### Connecting my remote repository with my local repository ###
To connect both created repositories I've used the following commands: <br>
&#36; git remote add origin [remote repository](https://marta_lopes@bitbucket.org/marta_lopes/devops-22-23-maal-1221850.git) <br>
&#36; git pull origin master --allow-unrelated-histories <br>
&#36; git add CA1/ <br>
&#36; git commit -m "Adding the code of the Tutorial React.js and Spring Data REST Application into a new directory" <br>
&#36; git push origin master <br>
&#36; git tag v1.1.0 <br>

By this time I created issues in Bitbucket to reference while committing every change to the project.

### Developing a new field - jobYears ###
When opening the project on the IDE (IntelliJ), there were inconsistencies on the pom.xml, referencing other projects.
These dependencies were deleted from the pom.xml and then committed the changes.

After committing these changes, I started by adding int jobYears as an attribute to the Employee Class and also adding
it to the constructor of said Class. I also added this field to the equals and hashcode methods, as well as added its getter and setter.
Then I committed and pushed the changes without referencing an issue, so I ran the following commands: <br>
&#36; git commit --amend -m "#1 - adding jobYears as attribute to the Employee Class, adding it to the constructor, to the equals() and hashcode() methods, as well as, adding the getter and setter" <br>
&#36; git push --force origin master <br>

After this, I've added validations to all attributes of the Employee Class. These validations were added to the constructor of the Class.
For the firstName, lastName and description attributes the only added validation was to have an exception thrown if these attributes had a null value.
For the jobYears attribute I've added a validation that would throw an exception if this attribute had a value equals or less than zero.
These validations were changed further on.

The following step was to create a new package called test to test the creation of Employees and the previously added validations.
I've made several tests to all attributes' and I've also tested the validations, having exceptions thrown.
Everytime I changed something to the project, I've committed and pushed those changes always using the command-line.

### Debug the server and the client ###
From the server side, all the tests were passing and all the validations I've added were being respected.
Firstly, I ran the following command being positioned on the basic/ directory <br>

&#36; ./mvnw spring-boot:run <br>

Then, I accessed the http://localhost:8080 and observed that the new added field was not appearing on the browser, which means that the jobYears attribute was not appearing on the client's side.
So, I added this field to the app.js, which means that I've altered the javaScript file to include the new attribute.
Running again the application with the command: <br>
&#36; ./mvnw spring-boot:run <br>

I checked that this change was successfully made as I could then see the jobYears attribute on the browser.

To debug the server, I also tried to save a new Employee on the DatabaseLoader Class with a null firstName, a null lastName, a null description and value of -1 for the jobYears attribute. Running the application, the project seemed to build successfully.
However, when accessing the browser, it stated <em>HTTP Status 500 - Internal Server Error: Description The server encountered an unexpected condition that prevented it from fulfilling the request. </em> <br>
Which means that the validations that were created were being detected, throwing exceptions from the server's side.
When saving a new Employee with valid values on the DatabaseLoader Class, I accessed the browser, and then I saw that the new Employee was successfully saved.
This means that the server was correctly functioning.

However, when I tried to debug the client I've encountered some problems.
I ran the following command: <br>

&#36; curl -X POST localhost:8080/api/employees -d "{\"description\": \"some description\"}" -H "Content-Type:application/json" <br>

The curl command is a client-side tool that allows us to send HTTP requests to the server and receive responses. In the case of this POST request, I was trying to send data to the server, a request to post a new Employee.
I wanted to see if a new Employee, with null attributes, would appear on the browser, to check if the previously added validations were respected when running the curl command.
So, this entry appeared from the client's side, the firstName and lastName appeared as null and the jobYears attribute stated 0. These values are invalid and there was no exception thrown, which meant that the curl command with post request was not respecting the validations.
To solve this, I added the Java Bean validations:

<ul> @NotBlank to the firstName, lastName and description attributes on the Employee Class </ul>
<ul> @NotNull @Min(1) @Max(80) to the jobYears attribute on the said Class </ul>

After this change, I ran the application again and tried to run the curl command with post request, and I was unable to insert invalid inputs, having exceptions thrown at the time I ran the command.

I've also used the <strong> React Developer Tools </strong> extension on Google Chrome to debug the client.
When using the extension to debug we can observe the following:
If I changed, for example, the jobYears to an invalid value, such as -1, the browser shows us this value. However, this doesn't affect the server, this value is not persistent and if we refresh the page, it shows us the previous value.
There were no problems with the client's performance.

Througout this work, I also ran the following commands: <br>
&#36; curl http://localhost:8080/api/employees (to see the persistence entries) <br>
&#36; curl -X DELETE http://localhost:8080/api/employees/2 (to delete an entry, in this case, the number 2 entry) <br>

At the end of this part of the assignment, I ran the following commands: <br>
&#36; git tag ca1-part1 <br>
&#36; git push origin master --tags (to push all the previoulsy created tags to the remote repository)

## CA1 - Part 2 ##
### Creating a new branch - email-field ###
To create a new branch and to work on this branch I ran the following commands: <br>
&#36; git branch email-field <br>
&#36; git checkout email-field <br>

### Developing a new field - email ###
I started by adding String email as an attribute to the Employee Class and also adding it to the constructor of said Class. I also added this field to the equals and hashcode methods, as well as added its getter and setter.
I committed and pushed the changes to the email-field branch. By this time, I switched to the master branch to check if the submitted change affected in any way the master branch and, as expected, it didn't. I've also made a change that I've committed and pushed to the master branch, to purposefully work on two branches at the same time, experiencing the two branches full potential.
Then I switched again to the email-field branch and added a no null validation to the email attribute on the Employee Class' constructor. I also updated the toString method to include the new field.
Then I did unit tests of the email attribute and tested the validations to check if the exceptions were being thrown.

### Debug the server and the client ###
When adding an entry to the DatabaseLoader Class, with a null email we have the following result: <br>
<em> Application run failed. java.lang.IllegalStateException: Failed to execute CommandLineRunner
Caused by: java.lang.IllegalArgumentException: Email can't be empty </em> <br>
When trying to access http://localhost:8080, it's not possible to establish a connection which is also expected.
Which means that from the server side, if I try to save an entry with an invalid e-mail, the application doesn't build properly, having an exception thrown.
So, the program is functioning as expected from the server side.

When I execute the curl command with the post option from the client side to send a request to the server, and if I attempt to include an entry with a null email, the server accepts the data without throwing an exception.
Consequently, upon accessing the browser, the new entry appears with an empty email field.
This implies that the server is not enforcing the email field validations during a post request made by the client.

To solve this, I added the @NotNull Java Bean validation to the email attribute on the Employee Class.
After doing so, when trying to do the same post request with a null e-mail, this is the result: <br>

<em>"status":500,"error":"Internal Server Error","message":"Could not commit JPA transaction; nested exception is javax.persistence.RollbackException: Error while committing the transaction"</em> <br>

Stating this: <br>
<em> messageTemplate='E-mail can't be empty'</em>

Which means that adding the Java Bean validation was a successful way to not allow the client to do a post request with an invalid input.

After successfully debugging the client and the server, I ran the following commands: <br>
&#36; git checkout master <br>
&#36; git merge e-mail-field <br>

And then there was a merge conflict: <br>
&#36; git add . (to mark resolution)<br>
&#36; git commit (to conclude the merge)<br>
&#36; git tag v1.3.0 <br>
&#36; git push origin v.1.3.0 <br>

### Creating a new branch - fix-invalid-email ###
I created a new branch to add a validation that says that the email must contain '@'.
I ran the commands: <br>
&#36; git branch fix-invalid-email <br>
&#36; git checkout fix-invalid-email <br>

### Debug the server and the client ###
I tried to add a new entry on the DatabaseLoader Class, not respecting this new validation.
When running the programm, there is an error: <br>
<em>Caused by: java.lang.IllegalArgumentException: Email can't be empty and must contain '@'</em>

When saving a new Employee with a valid email (having an '@') there is no exception thrown and this new entry appears on the browser.

So, the validation is working properly from the server's side.

By this time, I switched to the master branch and added a unit test to the description attribute where I've setted the description to a whitespace. The test passed so I reopened a previously closed issue to correct this problem.
I added a validation to all attributes (.trim().isEmpty()) and pushed the changes to the master branch.

Finally, I've tried to add a new Employee with a client request, not respecting the validation of the email field, and it allowed me to add an entry with an invalid email that did not contain an '@'.
So, I've added an @Email Java Bean validation to the email attribute.
Then I've tried to run the same curl command to post an invalid entry and there was an error stating: <br>
<em> "status":500,"error":"Internal Server Error","message":"Could not commit JPA transaction; </em> <br>

Which means that adding the Java Bean validation was a successful way to not allow the client to do a post request with an invalid input.

After successfully debugging the client and the server, I ran the following commands: <br>
&#36; git checkout master <br>
&#36; git merge fix-invalid-email <br>

And then there was a merge conflict: <br>
&#36; git add . (to mark resolution)<br>
&#36; git commit (to conclude the merge)<br>
&#36; git tag v1.3.1 <br>
&#36; git push origin v.1.3.1 <br>

## Alternative to the presented solution ##
* Instead of using Git, I could've used a different distributed version control system like Mercurial. Mercurial is similar to Git in many ways: it has a similar command-line interface and workflow, and it also supports branching and merging. It is said that one advantage of Mercurial is that it is easier to use for beginners, however it has lost some popularity in recent years compared to other version control systems like Git. I could've also used SVN (Subversion), which is a centralized version control system that is an older but still widely used alternative to Git. Unlike Git, SVN uses a central repository to store all versions of a project, and developers must check out files from the central repository before making changes. 
  * If I had used Mercurial, I would've run the following commands on the command-line: <br>
    &#36; hg init CA1/ <br>
    &#36; hg clone https://github.com/spring-guides/tut-react-and-spring-data-rest.git CA1/ <br>
    &#36; hg pull [remote repository](https://marta_lopes@bitbucket.org/marta_lopes/devops-22-23-maal-1221850.git) <br>
    &#36; hg update <br>
    &#36; hg add pom.xml <br>
    &#36; hg commit -m "Initial commit" <br>
    &#36; hg push origin master <br>
    &#36; hg status <br>
    &#36; hg branch email-field <br>
    &#36; hg branches <br>
    &#36; hg update origin master <br>
    &#36; hg merge email-field <br> 
* Instead of using Bitbucket, I could've used a different git hosting platform like GitHub. Both alternatives are valid and the choice between them will depend on our needs. Supposedly, GitHub has a larger community than Bitbucket and there are more open-source projects. However, I am already used to work with Bitbucket and so it seemed a perfect fit for this project.<br>
* Instead of creating a local repository and then connecting it to a remote repository, I could've only cloned an existing repository from a remote source. Cloning an existing remote repository is faster and more convenient, however starting with a local repository gives us more flexibility since we can make changes and commit them locally before pushing them to the remote, which is very useful for testing and experimenting different approaches. Creating a local repository gives us also more control and provides better security, since the data is not immediately exposed to potential security risks on the remote repository. <br>
* Instead of using Git on the command-line, I could've used a GUI git client like SourceTree or GitHub Desktop, having a graphical user interface tool that would allow me to interact with the repository visually. These kind of tools are more intuitive and perhaps easier to work with, however using the command line offer certain advantages such as having a more precise control over Git operations, better integration with other tools and provide a better understanding of Git. <br>
* Instead of cloning the Tutorial React.js and Spring Data Rest application to my computer and then using the command-line to move the basic/ directory to my local repository, I could've downloaded the project as a ZIP and then, using my operating system's file manager, I could've moved the basic/ directory to my local repository, manually. This alternative may be easier and faster, however it does not provide the version control features that Git offers. <br>
* Instead of using IntelliJ, I could've used a different IDE such as VSCode. Both tools have many similarities, however IntelliJ is particularly strong when it comes to Java development. Anyway, other IDE would be also valid to do this assignment.
* Instead of using React Developer Tools, I could've used the Browser Developer Tools: most browsers come with a set of built-in developer tools that can be used to inspect and debug client-side code. However, the React Developer Tools extension allows us to inspect and debug React components which is useful.
* When working in a second branch, instead of committing a change and pushing to that branch each time I make some change, I could've only committed the changes and then create a pull request on Bitbucket to merge that second branch with the master. This would trigger a code review process where I could make adjustments before merging. This alternative still involves commiting all the changes before pushing them to the remote, but it would allow me to push all the commits at once and initiate the merge process through a pull request.


## Conclusion ##
This assignment allowed me to understand how to work with Git and why Git is a powerful tool.

I have gained a deeper understanding of how Git works and how it can be used to manage software development projects and other projects that require version control. By learning about key concepts such as local and remote repositories, an existing staging area, branching, merging, and by using the command-line, I have gained practical skills that can be applied to real-world projects.

As I have learned, Git is a distributed version control that provides speed and efficiency, being a powerful tool for managing code and collaborating with others. By using Git, I can track changes to my code, collaborate with others, and roll back changes when necessary. Additionally, Git's open-source nature and widespread adoption mean that there is a large community of developers and resources available to help me get started and troubleshoot any issues I may encounter.

Overall, this assignment has helped me to see Git as an effective tool for managing software development projects. Whether I am working on a large-scale software development project or a small personal project, Git can help me to stay organized, collaborate with others, and ensure that my code is always backed up and version-controlled.