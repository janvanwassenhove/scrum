# SCRUM Installation

## Running A Scrum Program
Example code files can be found within [SCRUM Examples](development/examples).

### Running locally via command line
To run SCRUM programs locally, the scrum distributable can be used (make sure to have a recent java version installed).
The project is build upon JAVA 25 (LTS).
This can either be generated running a maven build (distribution will be generated within the /development directory), or directly downloading the scrum.zip distributable.

To run a program from the repository code, navigate to [Development](development) and run following code via CLI:
```
scrum examples/HelloWorld.scrum
```
You can use the [SCRUM Examples](development/examples) for testing purposes.
Keep in mind running this in Powershell you'll need to run it with:
```
.\scrum examples/HelloWorld.scrum
```

##### Local installation
If you want to run the code from anywhere, you can create a SCRUM_HOME variable and add it to the system varaiables.
Set the SCRUM_HOME environment variable pointing to your SCRUM installation and add this variable into your PATH variable adding %SCRUM_HOME%.
