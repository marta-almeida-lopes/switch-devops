# README #
This README is a simple technical report about the developed work for the Class Assignment 3 (part 1). <br>

This part of the Class Assignment was about <strong>Virtualization with Vagrant</strong>. <br>

The goal of the first part of CA3 was to practice with VirtualBox, using the same projects from the previous assignments, but now inside a VirtualBox VM with Ubuntu. <br>

This report will cover everything that was done for this work, step by step. It will also cover the used approach with brief explanations and all the used commands.


## CA3 - Part 1 ##

### Creating a VM ###
Linux is an industry-standard operating system and Ubuntu is a popular distribution of Linux. In order to run Ubuntu as the guest operating system in a virtual machine on my host machine (which is a Windows laptop), I firstly installed VirtualBox and then installed Ubuntu within VirtualBox. Then, when I started VirtualBox, I selected Ubuntu (as a guest operating system) to run in the virtual machine managed by VirtualBox. <br>
These were the steps you should follow:<br>
* Download the latest version of [Ubuntu](https://ubuntu.com/download/desktop) - version Ubuntu 22.04.2 LTS <br>
* By dowloading the latest version of <strong>Ubuntu</strong>, you'll get a .iso file which is an image of the installation optical disk (just like a CD ROM) <br>
* Download the latest version of [VirtualBox](https://www.virtualbox.org/) - version 7.0 <br>
* Install and run VirtualBox <br>
* See that there isn't any other guest operating systems already installed <br>
* Click the New button and fill in the Create Virtual Machine form <br>
* Choose a machine folder on your laptop that will contain the virtual machine image. The type should be Linux, and the version should be Ubuntu (64 bit). <br>
* Set a memory size for the virtual machine, but don’t give it more than half of the memory of your host laptop. <br>
* Select the Create a virtual hard disk now radio button and click the Create button. <br>
* On the Create Virtual Hard Disk, specify the maximum size to which Ubuntu’s virtual hard disk can  grow. Select the VDI (VirtualBox Disk  Image) and the Dynamically allocated  radio buttons. <br>
* For an installation of Ubuntu, specify at least 64 GB for the size of the virtual hard disk. <br>
* Select the name of the newly created virtual machine and click the Settings icon at the top. <br>
* In the System Settings form, select the Motherboard tab. Specify the amount of base memory you want to devote the virtual machine, but not more than half of the physical memory in your host machine. <br>
* Select the Processor tab and specify the number of CPUs you want to devote to the virtual machine, but not more than half the number of CPUs in your host machine. <br>
* For an installation of Ubuntu, specify at least 2 CPUs and 4 GB of memory. <br>
* Click Storage in the left panel. The Storage Settings form (Figure 6) shows the virtual CD ROM drive, which is initially empty. You want to install Ubuntu on the virtual machine, so you must “insert” the .iso installation disk image file that you downloaded earlier into the virtual CD ROM drive.<br>
* Now you are ready to start the virtual machine in order to install Ubuntu. To start the Ubuntu virtual machine, highlight it and click the Start button at the top. <br>
* The virtual machine will boot off the installation disk and start the installation process. Because the installation process downloads files from the Internet, you will need a good connection. <br>
* Choose a language and click Install Ubuntu to start the installation process. At various times, you will be asked to make a choice. Except otherwise directed, you should accept the default choices. <br>
* On the Installation type form, accept the choice Erase disk and install Ubuntu. The disk that it will erase is the virtual disk, not your host laptop’s physical disk! <br>
* The installation process will take a while, especially if you have a slow Internet connection. You can watch files download and install. When it’s finally done, it will ask you to restart. Press the Restart Now button. <br>
* Ubuntu will restart and ask you to remove the installation disk from the virtual CD ROM drive. Right-click on the image of the CD ROM at the bottom of the screen and select Remove disk from virtual drive. You may need to press the right control key on your keyboard if the virtual machine has “captured” your mouse. After removing the disk, click in the Ubuntu window and press the enter key <br>
* After the VirtualBox splash screen and some system startup messages, you will be asked to log in. Click on your name and enter your password.<br>
* You have successfully installed Ubuntu as a virtual machine! <br>

### Clone my individual repository inside the VM ###
Now that I had the VirtualBox and Ubuntu installed, I needed to clone my individual repository inside the VM. To do so, I tried to access my VM via SSH on my machine.
* To do this you should run the command on my machine's terminal: <br>
&#36; ssh marta@192.168.56.5 (switch marta to the name of your VM and check your VM's IP to put after the @) <br>
* To know what is your VM's IP, run the command:<br>
&#36; ifconfig <br>
* To clone a repository inside a VM, you should use a SSH key. To do so, you should firstly run the following command on your VM's terminal: <br>
&#36; ssh-keygen <br>
&#36; cat .ssh/id_rsa.pub <br>
* By running the previous command, you'll be able to see the generated key that you'll need to copy to your remote repository. <br>
* Copy the key and access the remote repository settings <br>
* Go to Access Keys and click on Add Key<br>
* There, paste the previously copied key<br>
* Don't forget to paste cat ~/.ssh/id_rsa.pub | pbcopy where it says "Copy and paste your key here with"<br>
* Now you can clone your remote repository into your VM via SSH <br>
* On your VM's terminal, run the command: <br>
&#36; git clone [remote repository](git@bitbucket.org:marta_lopes/devops-22-23-maal-1221850.git)<br>
* If you run the <strong>ls</strong> you'll now be able to see your repository.<br>

### Build and execute the spring boot tutorial basic project and the gradle basic demo project ###
To build and execute the projects, I had to install its dependencies. To do so, you should run the commands:<br>
&#36; sudo apt install maven <br>
&#36; sudo apt install gradle <br>
&#36; sudo apt install default-jdk <br>

### Accessing web applications from the browser in my host machine ###

#### Run the Spring Boot tutorial basic project ####
To run the Maven project and access the web application from the browser, I ran the command:<br>
&#36; ./mvnw spring-boot:run <br>
This command generated an error stating permission denied. If you encounter the same problem, run:<br>
&#36; ls -l <br>
And now you can check your permissions on mvnw. I could see that I only had permissions to read and write, but not to execute. So I changed the permissions using the command:<br>
&#36; chmod +x mvnw <br>
After doing so, run again the command on your VM's terminal:<br>
&#36; ./mvnw spring-boot:run <br>
If the build were successful, access the browser and enter your VM's IP followed by the port number. For me, that translated into accessing:<br>
 http://192.168.56.5:8080 <br>
* I've done this step on two different ways. Being on my machine accessing the VM via SSH and running the application but also tried directly on the VM and accessing the browser on my machine. It worked both ways. <br>

#### Run the Gradle basic demo project ####

To build the Gradle project, I ran the command:<br>
&#36; ./gradlew build <br>
This command generated an error stating permission denied.f you encounter the same problem, run:<br>
&#36; ls -l <br>
And now you can check your permissions on gradlew. I could see that I only had permissions to read and write, but not to execute. So I changed the permissions using the command:<br>
&#36; chmod +x gradlew <br>

* However, that didn't solve the problem. I kept running <$ ./gradlew build or $ ./gradlew runServer and nothing happened. I figured that my gradle version was not compatible with my VM. So, if you have the same problem and can't run this project, you should update your gradle version: <br>
&#36; sudo add-apt-repository ppa:cwchien/gradle <br>
&#36; sudo apt-get update <br>
&#36; sudo apt upgrade gradle <br>
Then, run the command:<br>
&#36; gradle build <br>

* The previous command will generate a new JAR file that will be important for the application. This generated JAR file contains the compiled bytecode of your application along with any required dependencies. <br>
Now, you should be at the root level. Try running:<br>
&#36; ./gradlew tasks <br>
&#36; ./gradlew runServer <br>
* You'll get the message "The chat server is running..."<br>

Now, you should open your machine's terminal and connect to the server via SSH:<br>
&#36; ssh marta@192.168.56.5 <br>
&#36; gradle build
&#36; ./gradlew runClient <br>
* You'll probably will have the same problem as I did: "runClient FAILED. Exception in thread "main" java.awt.HeadlessException: No X11 DISPLAY variable was set, but this program performed an operation which requires it."<br>
* This error message indicates that the client application is trying to perform an operation that requires an X11 display, but the display is not available. This error commonly occurs when running Java applications with a graphical user interface (GUI) on a system without an active X11 display. <br>

So, you should try running the following:<br>
&#36; exit <br>
&#36; ssh -Y marta@192.168.56.5 <br>
&#36; echo $DISPLAY <br>
&#36; export DISPLAY=:0.0 <br>
&#36; gradle build <br>
&#36; ./gradlew runClient <br>

* Now, you'll be able to see the runClient task running. If you access the VM and if you have GUI, you'll be able to see that a client joined the chat.

### Executing the server inside the VM and the client in my host machine ###
One reason to execute the server inside the VM and the clients in your host machine is to simulate a distributed environment.
In a distributed system, different components run on different machines and communicate with each other over a network.
By running the server in a VM, you can create a separate virtual machine that emulates a remote server, which allows you to test your client's ability to communicate with a remote server.
Another reason is to provide a sandboxed environment for the server.
Running the server inside a virtual machine isolates it from the host system and protects it from potential security threats.
This is particularly important if you are working on a production system or dealing with sensitive data.
Finally, running the clients on the host machine allows you to easily interact with the application using a web browser or a native application.
This is more convenient than having to access the application through the virtual machine's console or remote desktop connection.
Additionally, it allows you to leverage the host machine's resources, such as the display and input devices, to provide a better user experience.
