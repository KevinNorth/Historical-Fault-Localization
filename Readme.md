#Historical Fault Localization Tool
Implements proximity based weighting fault localization (http://dx.doi.org/10.1109/ASE.2011.6100088) and applies the algorithm to sets of commits in a git repository.

##Table of Contents
 - [Introduction](#introduction)
 - [Organization of the Program](#organization-of-the-program)
 - [How to Compile the Program](#how-to-compile-the-program)
 - [How to Run the Program](#how-to-run-the-program)
 - [How to Configure the Program](#how-to-configure-the-program)
 - [How to Set Up a Target Program](#how-to-set-up-a-target-program)

If you're reading this document in a text editor instead of a Markdown viewer, you can Find i.e. `#How to Compile the Program` in order to jump to a section.

#Introduction

This program performs fault localization analysis of multiple commits of a target program's history. It does so by getting a list of commits from the target program's version control history, then checking out each of those commits one at a time. Each time it checks out a particular commit, it runs the target program's tests and collects information about which lines of code from the target program were executed by the tests.

This information is then fed to a fault localization algorithm, which gives each line of code a suspiciousness score. The higher a line of code's suspiciousness score is, the more likely it is that line of code will need to be changed as part of writing a patch to make a failing test pass again.

Finally, this fault localization information is passed to a visualization tool modeled after the Tarantula visualizer (http://dx.doi.org/10.1145/581339.581397). This visualization creates one image for each commit that was processed in the first step. The image contains vertical bars, one for each source code file the tests obtained coverage information for. Each of these vertical bars are made of smaller horizontal bars stacked on top of each other. Bars at the top of the image represent lines of code at the beginning of the source code files, and bars at the bottom represent lines of code at the end of files. Each line of code is colored red if it is highly suspicious, blue if it is not suspicious, and green to yellow if it is moderately suspicious.

#Organization of the Program

This program is made up of four primary components:

 1. The [Target Program Handler](#target-program-handler) runs Git commands in order to checkout the requested commits in the target program's Git repository.
 2. The [Test Executor](#test-executor) asks the target program to run its tests and send back test coverage data.
 3. The [Suspiciousness Calculator](#suspiciousness-calculator) performs fault localization and calculates the suspiciousness of lines of code.
 4. The [Visualizer](#visualizer) outputs images showing the results of the fault localization.

##Target Program Handler
The Target Program Handler's code is located in the `edu.unl.knorth.historical_fault_localization.target_program_handler` package.

The Target Program Handler performs three tasks. First, it starts a process that runs `git log` in order to obtain a list of commits in the target program's repository that should be analyzed. Then, for each of those commits, the Target Program Handler performs each of the next two tasks. It starts a process running `git checkout` in order to checkout, one at a time, each of the commits obtained earlier. Then, it asks the Test Executor to run the target program's tests in the currently checked-out version of the target program.

##Text Executor
The Test Executor code is located in the `edu.unl.knorth.historical_fault_localization.target_program_handler.test_executor` package.

The Test Executor runs the target program's tests. It does so by running a script, called the test harness script, that instructs the target program on how to run its tests and collect coverage data for them.

The Test Executor does not provide the test harness script! The end user needs to write the test harness script him- or herself. This has the advantage that, when provided with an appropriate test harness script, the Historical Fault Localization Tool is compatible with target programs written in any language and using any test environment. The main disadvantage is that this approach requires more work from the end user.

###Test Harness Script

Every time it is run, the test harness script will receive information about where the tests are and what version of the program under test is being analyzed. The test harness script is expected to run the target program's tests and collect information about which lines of code are run by each test. This information should then be put into a file in a particular location, which the Test Executor will subsequently read in order to record the test coverage data.

The Test Executor assumes that the test harness script adheres to a particular specification. It is up to the end user to follow this specification. See the [How to Set Up a Target Program](#how-to-set-up-a-target-program) section to read the test harness' specification.

##Suspiciousness Calculator

The Suspiciousness Calculator's source code is in the `edu.unl.knorth.historical_fault_localization.suspiciousness_calculation` package.

Once the Target Program Handler, Test Executor, and test harness script have collected test coverage data on all of the requested commits, the Suspiciousness Calculator performs fault localization. For each commit in the target program, it calculates the suspiciousness of each line of code the test cases executed.

The Historical Fault Localization Tool implements the Ochiai and Proximity-Based Weighting fault localization algorithms. Which one is used can be configured before running the tool.

If all of the tests pass for a particular commit, the Suspiciousness Calculator assigns all lines of code a suspiciousness of 0, or not suspicious whatsoever. Likewise, if all tests fail, all lines of code are assigned a suspiciousness of 1, or completely suspicious. This ignores lines of code that were not executed by the test case, so the visualization ultimately output will at least show which lines of code were executed or not for each commit.

##Visualizer

The Visualizer's source code is in the `edu.unl.knorth.historical_fault_localization.visualizer` package.

The Visualizer produces produces one image for each commit that was analyzed. These files are named `[order processed]-[commit SHA1 hash].png`, indicating which commit each image correspondes to. For example, if commit `667ffe5f6167352b028e5af83acd7ce52786a497` is the first commit the Target Program Handler checked out, the Visualizer will name that commit's visualization `1-667ffe5f6167352b028e5af83acd7ce52786a497.png`.

Each individual image is made up of multiple vertical bars. Each of these bars represents a single file in the target program's source code. The name of the file corresponding to each bar is shown at the top of the bar.

Each individual vertical bar in the image is made up of smaller horizontal bars. Each bar is a solid block of color. Horizontal bars at the top of the image represent the lines of source code at the beginning of the source code file, and horizontal bars at the bottom represent lines of code at the end of the file. Each line of code has a color:

 - Reds and oranges represent lines of code with high suspiciousness scores
 - Greens represent middling suspiciousness scores
 - Blues, especially deep, dark blues, represent lines of code with low suspiciousness scores
 - Black represents lines of code that were not executed by any of the tests

The color is chosen by selecting a hue value for a Hue-Saturation-Brightness description. In particular, the selected hue is

    (1 - suspiciousness) * (240°)

where 0° is pure red and 240° is pure blue. See [https://en.wikipedia.org/wiki/HSL_and_HSV#HSV_.28Hue_Saturation_Value.29](https://en.wikipedia.org/wiki/HSL_and_HSV#HSV_.28Hue_Saturation_Value.29) for examples of some hue values.

#How to Compile the Program

From the directory this Readme is located in, run

    ant jar

The compiled program will be placed in `dist/Historical_Fault_Localization.jar`.

#How to Run the Program

To run the program:

 1. Prepare the target program. See [How to Set Up a Target Program](#how-to-set-up-a-target-program) for instructions.
 1. Prepare the configuration file. See [How to Configure the Program](#how-to-configure-the-program) for instructions.
 2. Run

      java -jar [path to compiled program] [path to configuration file]

 where
    - `path to compiled program` is the location of your compiled program's jar.
    - `path to configuration file` is the location of your configuration file.

If you simply wish to see how the program runs and do not want to prepare a target program or configuration, I have packaged a demo target program with this application. The target program is located in the `historical-fault-localization-target-ruby-app` directory. In addition, I have prepared an example configuration file that will target the demo program.

To use the demo target program and example configuration, go to the directory this Readme is located in and run

    java [path to compiled program] "Historical Fault Localization/config/config.example.txt"

#How to Configure the Program

The Historical Fault Localization Tool uses a configuration program to control its behavior. See the `config/config.example.txt` file for an example of a valid configuration file.

The configuration file is a list of key-value pairs specifying what the program's behavior should be. Each key-value pair should be on its own line. The format for a key-value pair is

    key value

where the `key` and the `value` are separated by at least one whitespace character. If the value of `key` doesn't match any of the expected key names, the line is ignored, so you can write arbitrary comments in the configuration file as long as you do not accidentally begin a line with a key name. Likewise, empty lines are ignored.

To avoid accidentally beginning a comment with a key name, it is recommended that you write comments starting with `#`, i.e.

    # This is a comment
    # The next line sets the value of the gitArgument option
    gitArgument --since=2015-01-01

Note that the `value` in a key can contain whitespace. The first contiguous block of whitespace characters in a line of the configuration file are used to separate the key and the value, but all subsequent whitespace characters are part of the value. (Trailing whitespace will be removed, though.)

So, for example, the key-value pair

    gitArgument --since=2015-01-01 --reverse --date-order

will use the key `gitArgument` and the value `--since=2015-01-01 --reverse --date-order`.

## Options

This is a list of key-value pairs that the configuration file will recognize:

 - `gitArguments`: To collect a list of commits to process, the Historical Fault Localization Tool will run `git log --format=%H-%cd`. Without any arguments, this will produce a list of all commits in the project in descending date order, which might be more commits than desired or a different order than desired.
  So, to allow you to decide which commits should be used and in what order they should be used, the string you set with `gitArguments` will be appended to the end of `git log --format=%H-%cD`. For example, to use the commits returned by `git log --format=%h%cD --since=2015-01-01 --reverse --date-order`, set the parameter accordingly:

      gitArgument --since=2015-01-01 --reverse --date-order

  This configuration option is mandatory, so if you don't want to set any options, simply use

      gitArgument --date-order

  which preserves `git log`'s default behavior.

 - `testHarnessPath`: The path to the test harness script.
 - `targetProgramDirectory`: The path to the root of the target program's Git repository.
 - `testTimeout`: The amount of time, in milliseconds, to allow the test harness to run before giving up and killing its process. Once the test harness script finishes running for one commit, the timeout timer resets, giving the full timeout period again when the test harness runs for the next commit.
 - `suspiciousnessAlgorithm`: Set to one of two values:
   1. Set to `ochiai` in order to use the Ochiai fault localization algorithm.
   2. Set to `proximity` in order to use the proximity-based weighting algorithm.
 - `lowerBound` (optional): This configuration option is only required if you set `suspiciousnessAlgorithm` to `proximity`. Determines how the lower bound for unadjusted weightings is determined. Set to one of three values:
   1. Set to `none` to indicate that the lower bound should be ignored.
   2. Set to `quartile` to indicate that the lower bound should be the third quartile.
   3. Set to `tail` to indicate that the lower quartile should be the lower outliers.
 - `upperBound` (optional): This configuration option is only required if you set `suspiciousnessAlgorithm` to `proximity`. Determines how the upper bound for unadjusted weightings is determined. Set to one of three values:
   1. Set to `none` to indicate that the upper bound should be ignored.
   2. Set to `quartile` to indicate that the upper bound should be the first quartile.
   3. Set to `tail` to indicate that the upper quartile should be the upper outliers.
 - `statementHeight`: How many pixels tall each statement's horizontal bar should be in the visualization.
 - `statementWidth`: How many pixels wide each statement's horizontal bar should be in the visualization. This is the same as how wide each file's vertical bar should be.
 - `fileMargin`: The amount of horizontal space, in pixels, appearing in-between each file's vertical bar in the visualization.
 - `fileFontSize`: The font size, in pixels, to use for the file labels above the vertical bars in the visualization.
 - `imageOutputDirectory`: The location of the directory to save the visualization's images to.
 - `testHarnessOutput` (optional): The location that the test harness script should output its output text file to. If left unset, defaults to `temp/test_out.txt`.

All relative file paths will be treated as relative to the location you begin running the Historical Fault Localization Tool from.

All of the options are required except for `testHarnessOutput`. In addition, `lowerBound` and `upperBound` are only required if  `suspiciousnessAlgorithm` is set to `proximity`.

The `statementHeight`, `statementWidth`, `fileMargin`, and `fileFontSize` options must be set to a value that can be parsed by `Integer.parseInt()` in Java. The `testTimeout` option must be set to a value that can be parsed by `Long.parseLong()`.

#How to Set Up a Target Program

Your target program must be version controlled using Git and it must have a test suite. In addition, you must create a test harness script that can run the target program's test and report code coverage data for each test.

##Test Harness Script Specification

The test harness script you create must follow this specification.

###Overview

Every time it is run, the test harness script will receive information about where the tests are and what version of the program under test is being analyzed. The test harness script is expected to run the target program's tests and collect information about which lines of code are run by each test. This information should then be put into a file in a particular location, which the [Test Executor](#Test Executor) will subsequently read in order to record the test coverage data.

###The Script's Input

The test harness script will receive input as though it had been called by the command line terminal. It will receive the following flags with the following arguments:

 - `--work-directory=[workingDirectoryPath]`
    This flag will be passed a string indicating the path to the root of the target program's Git repository. If this is a relative path, it will be relative to the Historical Fault Localization Tool's working directory, not the location of the test harness script.
 - `--commit-hash=[commitHash]`
    This flag will be passed the SHA1 hash corresponding to the commit currently checked-out in the target program's Git repository.
 - `--commit-timestamp=[timestamp]`
    This flag will be passed the timestamp of when the currently checked-out commit was originally committed. It is formatted using the format that `git show --format=%cd` uses.
 - `--output_file=[outputFilePath]`
    This flag will be passed a string indicating the path to the file the script should output its results to. Like the `--work-directory` flag, if this path is relative, it is relative to the Historical Fault Localization Tool's working directory.

Altogether, the test harness script will be run as though

    ./[path to test harness] --work-directory=[workingDirectoryPath] --commit-hash=[commitHash] --commit-timestamp=[timestamp] --output_file=[outputFilePath]

had been run from the command line.

The `--commit-timestamp` and `--commit-hash` flags are passed in case the target program went through significant changes to its testing environment during the commits to check. The testing harness can use the information they provide to configure the testing tools in the appropriate ways depending on the needs of the currently checked-out commit.

###Output

The information that the test harness script collects regarding test coverage should be output to the file indicated by the `--output_file` flag. The contents of the file must adhere to the following format:

Each line of the output file must contain information representing exactly one test case in your test suite.

 1. Empty lines and lines containing only whitespace are ignored.

    1 .On non-empty lines, leading and trailing whitespace is stripped before  processing.
    2. Non-empty lines representing test cases should be whitespace-delimited lists of strings.

 2. Each line must start with either the string `passed` or `failed` (case insensitive).

    1. If the string is `passed`, it indicates that the test case passed.
    2. The string `failed` has the obvious meaning.

 1. After the first string in each line, statements that were executed by the test should be represented by pairs of strings.

    1. The first string represents the file that the executed statement can be found in.
    2. The second string represents the line number within the file of the executed statement.

       1. The second string should match the format for a Java integer, as determined by the `Integer.parseInt()` function.

    3. For example, the pair of strings `app.rb 15` would mean that the test case executed line 15 of the file app.rb.

    4. It is acceptable for a test case line to have the same file-line number pair multiple times, but it is not necessary. If a file-line number pair appears multiple times, it will only be recorded once when the file is   parsed.

This is an example of the contents of a correctly formatted test harness output file:

    passed app.rb 1 app.rb 2 app.rb 4 app.rb 6 utility.rb 23 utility.rb 24 utility.rb 26 app.rb 11
    passed app.rb 1 app.rb 1 app.rb 2 app.rb 3 app.rb 6 utility.rb 23 utility.rb 25 app.rb 11
    failed app.rb 1 app.rb 2 app.rb 4 app.rb 5 utility.rb 23 utility.rb 24 utility.rb 25 app.rb 11

The script can output anything to STDOUT and STDERR. As the Test Executor runs the test harness, it will redirect the test harness' STDOUT and STDERR output to the Historical Fault Localization Tool's own STDOUT, so you can observe these streams' outputs in order to determine how the test harness script behaves when run by the Test Executor.

The specification that the test harness script should save its useful output to a file instead of to STDOUT was made, even though it adds some complexity, because some testing tools output to STDOUT on their own with no easy way to silence them. Hence, if the test harness was expected to write its useful output to STDOUT, it would be difficult in many situations to ensure that STDOUT is not polluted with the output of the testing tools. In addition, STDOUT can now be used to output information the end user can observe to ensure the test harness script runs as expected.

###Timeouts and Failures

If the test harness takes too long to finish, its process will be killed, and no output will be generated for the currently checked-out commit. The Test Program Handler will then checkout the next commit, and the Test Executor will attempt to run the test harness again.

The amount of time to wait before timing out is configurable. See "How to Configure the Program" above.

If the test harness script produces an output file that is of the incorrect format or fails to produce an output file, the Test Executor will not record any information for the currently checked-out commit. If the test harness script crashes without outputting the complete output file, but what it does output is of the correct format, the Test Executor will assume the test harness had completed correctly and read the incorrect test coverage data.
